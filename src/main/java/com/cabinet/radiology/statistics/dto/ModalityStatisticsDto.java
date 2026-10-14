package com.cabinet.radiology.statistics.dto;

import com.cabinet.radiology.exam.domain.Modality;

import java.util.Map;

/**
 * Répartition du nombre d'examens par type d'examen (modalité).
 */
public record ModalityStatisticsDto(
        long totalExams,
        Map<Modality, Long> countByModality
) {
}