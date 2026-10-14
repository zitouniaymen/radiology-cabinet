package com.cabinet.radiology.patient.service;

import com.cabinet.radiology.exam.domain.ExamStatus;
import com.cabinet.radiology.exam.domain.Modality;
import com.cabinet.radiology.patient.dto.PatientChronoStatistics;
import com.cabinet.radiology.patient.dto.PatientExamCount;
import com.cabinet.radiology.patient.dto.PatientModalityStatistics;
import com.cabinet.radiology.patient.dto.PatientStatistics;
import com.cabinet.radiology.patient.repository.PatientStatisticsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Calcul des indicateurs patients à partir des examens planifiés au cabinet.
 *
 * <p>Les patients sont comptés via leur référence opaque ; aucune donnée nominative
 * n'est lue ni exposée.</p>
 */
@Service
@Transactional(readOnly = true)
@Slf4j
public class PatientStatisticsService {

    private static final String STATUS_COMPLETED = ExamStatus.COMPLETED.name();

    private final PatientStatisticsRepository patientStatisticsRepository;

    public PatientStatisticsService(final PatientStatisticsRepository patientStatisticsRepository) {
        this.patientStatisticsRepository = patientStatisticsRepository;
    }

    public PatientStatistics globalStatistics() {
        final long totalPatients = patientStatisticsRepository.countDistinctPatients();
        final long totalExams = patientStatisticsRepository.count();
        final long completedExams = patientStatisticsRepository.countByStatus(ExamStatus.COMPLETED);
        final long patientsWithCompletedExams =
                patientStatisticsRepository.countDistinctPatientsByStatus(STATUS_COMPLETED);

        final List<PatientExamCount> countsByPatient = patientStatisticsRepository.findPatientExamCounts();
        final long maxExamsPerPatient = countsByPatient.stream()
                .mapToLong(PatientExamCount::totalExams)
                .max()
                .orElse(0L);

        final double averageExamsPerPatient = totalPatients == 0
                ? 0.0
                : round((double) totalExams / totalPatients);
        final double completionRate = totalExams == 0
                ? 0.0
                : round((double) completedExams / totalExams);

        log.info("Statistiques patients : {} patients, {} examens", totalPatients, totalExams);

        return new PatientStatistics(
                totalPatients,
                totalExams,
                averageExamsPerPatient,
                maxExamsPerPatient,
                patientsWithCompletedExams,
                completionRate);
    }

    public List<PatientExamCount> examCountsByPatient() {
        return patientStatisticsRepository.findPatientExamCounts();
    }

    public PatientModalityStatistics modalityStatistics() {
        final Map<Modality, Long> countByModality = new EnumMap<>(Modality.class);
        for (final Modality modality : Modality.values()) {
            countByModality.put(modality,
                    patientStatisticsRepository.countDistinctPatientsByModality(modality.name()));
        }
        return new PatientModalityStatistics(patientStatisticsRepository.countDistinctPatients(), countByModality);
    }

    public List<PatientChronoStatistics> chronoStatistics(final String periodType) {
        final List<PatientChronoStatistics> statistics =
                patientStatisticsRepository.chronoPatientStatistics(periodType);
        if (statistics.isEmpty()) {
            log.info("Aucune donnée chronologique patient pour la période {}", periodType);
        }
        return statistics;
    }

    private double round(final double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}