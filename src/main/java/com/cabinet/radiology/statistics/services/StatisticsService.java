package com.cabinet.radiology.statistics.services;

import com.cabinet.radiology.exam.Enum.PeriodType;
import com.cabinet.radiology.exam.domain.ExamStatus;
import com.cabinet.radiology.exam.domain.Modality;
import com.cabinet.radiology.statistics.dto.ChronoStatisticsDto;
import com.cabinet.radiology.statistics.dto.ModalityCount;
import com.cabinet.radiology.statistics.dto.ModalityPeriodCount;
import com.cabinet.radiology.statistics.dto.ModalityStatisticsDto;
import com.cabinet.radiology.statistics.dto.PatientPeriodCount;
import com.cabinet.radiology.statistics.dto.PeriodCount;
import com.cabinet.radiology.statistics.dto.PeriodStatistics;
import com.cabinet.radiology.statistics.dto.PeriodStatisticsDto;
import com.cabinet.radiology.statistics.dto.StatisticsOverviewDto;
import com.cabinet.radiology.statistics.repository.StatisticsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Service centralisant le calcul des statistiques du cabinet, par jour, par mois et par année,
 * pour l'ensemble des types d'examen (modalités) et pour les patients pris en charge.
 *
 * <p>Les patients sont comptés à partir de leur référence opaque ({@code patient_reference}) :
 * aucune donnée médicale ou nominative n'est manipulée.</p>
 */
@Service
@Transactional(readOnly = true)
@Slf4j
public class StatisticsService {

    /** Borne basse utilisée lorsqu'aucune date de début n'est fournie. */
    private static final OffsetDateTime MIN_DATE = OffsetDateTime.of(1970, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);

    private final StatisticsRepository statisticsRepository;

    public StatisticsService(final StatisticsRepository statisticsRepository) {
        this.statisticsRepository = statisticsRepository;
    }

    // ---------------------------------------------------------------------------------------------
    // Vue globale (toutes périodes confondues)
    // ---------------------------------------------------------------------------------------------

    /**
     * Vue synthétique globale : volumes d'examens, patients distincts et répartitions
     * par type d'examen et par statut.
     */
    public StatisticsOverviewDto overview() {
        final Map<Modality, Long> countByModality = buildModalityMap(statisticsRepository.countAllExamsByModality());
        final Map<ExamStatus, Long> countByStatus = new EnumMap<>(ExamStatus.class);
        statisticsRepository.countAllExamsByStatus()
                .forEach(row -> countByStatus.put(parseStatus(row.status()), row.total()));

        final long totalExams = countByStatus.values().stream().mapToLong(Long::longValue).sum();
        final long totalPatients = statisticsRepository.countDistinctPatients();

        log.info("Statistiques globales : {} examens, {} patients", totalExams, totalPatients);
        return new StatisticsOverviewDto(totalExams, totalPatients, countByModality, countByStatus);
    }

    // ---------------------------------------------------------------------------------------------
    // Examens et patients par période
    // ---------------------------------------------------------------------------------------------

    /**
     * Séries temporelles du nombre d'examens (tous types confondus) et de patients distincts
     * selon la granularité demandée.
     *
     * @param periodType granularité ({@link PeriodType})
     * @param from       début de l'intervalle (inclus), {@code null} pour ne pas borner le début
     * @param to         fin de l'intervalle (exclue), {@code null} pour ne pas borner la fin
     */
    public List<PeriodStatisticsDto> examsByPeriod(final PeriodType periodType,
                                                   final OffsetDateTime from,
                                                   final OffsetDateTime to) {
        final OffsetDateTime start = normalizeFrom(from);
        final OffsetDateTime end = normalizeTo(to);

        final List<PeriodCount> examCounts = statisticsRepository.countExamsByPeriod(
                periodType.key(), periodType.pattern(), start, end);
        final List<PatientPeriodCount> patientCounts = statisticsRepository.countPatientsAndExamsByPeriod(
                periodType.key(), periodType.pattern(), start, end);

        final Map<String, Long> patientsByPeriod = new LinkedHashMap<>();
        patientCounts.forEach(row -> patientsByPeriod.put(row.period(), row.totalPatients()));

        final List<PeriodStatisticsDto> result = new ArrayList<>();
        examCounts.forEach(row -> result.add(new PeriodStatisticsDto(
                row.period(),
                row.total(),
                patientsByPeriod.getOrDefault(row.period(), 0L))));
        return result;
    }

    // ---------------------------------------------------------------------------------------------
    // Répartition par type d'examen
    // ---------------------------------------------------------------------------------------------

    /**
     * Répartition du nombre d'examens par type d'examen (modalité) sur un intervalle donné.
     */
    public ModalityStatisticsDto examsByModality(final OffsetDateTime from, final OffsetDateTime to) {
        final List<ModalityCount> counts = statisticsRepository.countExamsByModality(
                normalizeFrom(from), normalizeTo(to));
        final Map<Modality, Long> countByModality = buildModalityMap(counts);
        final long total = counts.stream().mapToLong(ModalityCount::total).sum();
        return new ModalityStatisticsDto(total, countByModality);
    }

    // ---------------------------------------------------------------------------------------------
    // Rapport consolidé par période (examens + patients + détail par type)
    // ---------------------------------------------------------------------------------------------

    /**
     * Rapport consolidé pour la granularité demandée : totaux, séries temporelles examens/patients,
     * répartition par type d'examen et détail chronologique par type d'examen.
     */
    public PeriodStatistics statisticsByPeriod(final PeriodType periodType,
                                               final OffsetDateTime from,
                                               final OffsetDateTime to) {
        final OffsetDateTime start = normalizeFrom(from);
        final OffsetDateTime end = normalizeTo(to);
        final String key = periodType.key();
        final String pattern = periodType.pattern();

        final long totalExams = statisticsRepository.countExamsBetween(start, end);
        final long totalPatients = statisticsRepository.countDistinctPatientsBetween(start, end);

        final List<PeriodCount> examCounts = statisticsRepository.countExamsByPeriod(key, pattern, start, end);
        final List<PatientPeriodCount> patientCounts =
                statisticsRepository.countPatientsAndExamsByPeriod(key, pattern, start, end);

        final Map<String, Long> patientsByPeriod = new LinkedHashMap<>();
        patientCounts.forEach(row -> patientsByPeriod.put(row.period(), row.totalPatients()));

        final List<PeriodStatisticsDto> byPeriod = new ArrayList<>();
        examCounts.forEach(row -> byPeriod.add(new PeriodStatisticsDto(
                row.period(),
                row.total(),
                patientsByPeriod.getOrDefault(row.period(), 0L))));

        final Map<Modality, Long> countByModality = buildModalityMap(
                statisticsRepository.countExamsByModality(start, end));
        final List<ChronoStatisticsDto> chronoByModality =
                buildChronoByModality(statisticsRepository.countExamsByModalityAndPeriod(key, pattern, start, end));

        log.info("Rapport statistique {} : {} examens, {} patients sur la période",
                key, totalExams, totalPatients);

        return new PeriodStatistics(
                key,
                from == null ? null : start,
                to == null ? null : end,
                totalExams,
                totalPatients,
                byPeriod,
                countByModality,
                chronoByModality);
    }

    // ---------------------------------------------------------------------------------------------
    // Helpers
    // ---------------------------------------------------------------------------------------------

    private List<ChronoStatisticsDto> buildChronoByModality(final List<ModalityPeriodCount> rows) {
        final Map<String, Map<Modality, Long>> grouped = new LinkedHashMap<>();
        for (final ModalityPeriodCount row : rows) {
            final Modality modality = parseModality(row.modality());
            if (modality == null) {
                continue;
            }
            grouped.computeIfAbsent(row.period(), ignored -> new EnumMap<>(Modality.class))
                    .merge(modality, row.total(), Long::sum);
        }

        final List<ChronoStatisticsDto> result = new ArrayList<>();
        grouped.forEach((period, modalities) -> {
            final long total = modalities.values().stream().mapToLong(Long::longValue).sum();
            result.add(new ChronoStatisticsDto(period, total, modalities));
        });
        return result;
    }

    private Map<Modality, Long> buildModalityMap(final List<ModalityCount> rows) {
        final Map<Modality, Long> map = new EnumMap<>(Modality.class);
        for (final Modality modality : Modality.values()) {
            map.put(modality, 0L);
        }
        rows.forEach(row -> {
            final Modality modality = parseModality(row.modality());
            if (modality != null) {
                map.merge(modality, row.total(), Long::sum);
            }
        });
        return map;
    }

    private Modality parseModality(final String raw) {
        try {
            return Modality.valueOf(raw.trim().toUpperCase(Locale.ROOT));
        } catch (final IllegalArgumentException | NullPointerException ex) {
            log.warn("Modalité inconnue renvoyée par la base : {}", raw);
            return null;
        }
    }

    private ExamStatus parseStatus(final String raw) {
        try {
            return ExamStatus.valueOf(raw.trim().toUpperCase(Locale.ROOT));
        } catch (final IllegalArgumentException | NullPointerException ex) {
            log.warn("Statut inconnu renvoyé par la base : {}", raw);
            return null;
        }
    }

    private OffsetDateTime normalizeFrom(final OffsetDateTime from) {
        return from == null ? MIN_DATE : from;
    }

    private OffsetDateTime normalizeTo(final OffsetDateTime to) {
        // La borne haute est exclusive : par défaut on inclut jusqu'à demain.
        return to == null ? OffsetDateTime.now(ZoneOffset.UTC).plusDays(1) : to;
    }
}
