package com.cabinet.radiology.statistics.dto;

/**
 * Statistiques d'une période (jour, mois ou année) : examens tous types confondus
 * et patients distincts pris en charge.
 *
 * @param period      libellé de la période ({@code YYYY-MM-DD}, {@code YYYY-MM} ou {@code YYYY})
 * @param totalExams  nombre d'examens sur la période
 * @param totalPatients nombre de patients distincts sur la période
 */
public record PeriodStatisticsDto(
        String period,
        long totalExams,
        long totalPatients
) {
}