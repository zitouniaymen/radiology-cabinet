package com.cabinet.radiology.statistics.dto;

import com.cabinet.radiology.exam.domain.Modality;

import java.util.Map;

/**
 * Séries temporelles détaillées par type d'examen (modalité) pour une période donnée.
 *
 * @param period          libellé de la période ({@code YYYY-MM-DD}, {@code YYYY-MM} ou {@code YYYY})
 * @param totalExams      nombre total d'examens sur la période
 * @param countByModality nombre d'examens par type d'examen sur la période
 */
public record ChronoStatisticsDto(
        String period,
        long totalExams,
        Map<Modality, Long> countByModality
) {
}