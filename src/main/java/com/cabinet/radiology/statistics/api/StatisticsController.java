package com.cabinet.radiology.statistics.api;

import com.cabinet.radiology.exam.Enum.PeriodType;
import com.cabinet.radiology.statistics.dto.ModalityStatisticsDto;
import com.cabinet.radiology.statistics.dto.PeriodStatistics;
import com.cabinet.radiology.statistics.dto.PeriodStatisticsDto;
import com.cabinet.radiology.statistics.dto.StatisticsOverviewDto;
import com.cabinet.radiology.statistics.services.StatisticsService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * API REST des statistiques du cabinet.
 *
 * <p>Permet d'interroger les volumes d'examens (tous types confondus ou par type)
 * et de patients, agrégés par jour, par mois ou par année.</p>
 *
 * <p>Exemples :</p>
 * <ul>
 *   <li>{@code GET /api/v1/statistics/overview}</li>
 *   <li>{@code GET /api/v1/statistics/exams?period=DAY}</li>
 *   <li>{@code GET /api/v1/statistics/exams?period=MONTH&from=2026-01-01T00:00:00Z&to=2026-12-31T23:59:59Z}</li>
 *   <li>{@code GET /api/v1/statistics/patients?period=YEAR}</li>
 *   <li>{@code GET /api/v1/statistics/report?period=MONTH}</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/v1/statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;

    public StatisticsController(final StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    /**
     * Vue globale : total examens, patients distincts, répartition par type d'examen et par statut.
     */
    @GetMapping("/overview")
    public StatisticsOverviewDto overview() {
        return statisticsService.overview();
    }

    /**
     * Nombre d'examens (tous types confondus) et de patients distincts par période.
     */
    @GetMapping("/exams")
    public List<PeriodStatisticsDto> examsByPeriod(
            @RequestParam(defaultValue = "MONTH") final PeriodType period,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) final OffsetDateTime from,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) final OffsetDateTime to) {
        return statisticsService.examsByPeriod(period, from, to);
    }

    /**
     * Alias sémantique pour les statistiques patients par période.
     */
    @GetMapping("/patients")
    public List<PeriodStatisticsDto> patientsByPeriod(
            @RequestParam(defaultValue = "MONTH") final PeriodType period,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) final OffsetDateTime from,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) final OffsetDateTime to) {
        return statisticsService.examsByPeriod(period, from, to);
    }

    /**
     * Répartition du nombre d'examens par type d'examen (modalité) sur un intervalle donné.
     */
    @GetMapping("/modalities")
    public ModalityStatisticsDto examsByModality(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) final OffsetDateTime from,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) final OffsetDateTime to) {
        return statisticsService.examsByModality(from, to);
    }

    /**
     * Rapport consolidé : totaux, séries temporelles et détail par type d'examen.
     */
    @GetMapping("/report")
    public PeriodStatistics report(
            @RequestParam(defaultValue = "MONTH") final PeriodType period,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) final OffsetDateTime from,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) final OffsetDateTime to) {
        return statisticsService.statisticsByPeriod(period, from, to);
    }
}
