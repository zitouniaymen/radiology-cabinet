package com.cabinet.radiology.statistics.dto;

import com.cabinet.radiology.exam.domain.ExamStatus;
import com.cabinet.radiology.exam.domain.Modality;

import java.util.Map;

/**
 * Vue synthétique et globale de l'activité du cabinet : volumes d'examens,
 * patients distincts et répartitions (types d'examen et statuts).
 */
public record StatisticsOverviewDto(
        long totalExams,
        long totalPatients,
        Map<Modality, Long> countByModality,
        Map<ExamStatus, Long> countByStatus
) {
}