package com.cabinet.radiology.statistics.dto;

/**
 * Projection brute : nombre d'éléments pour une période donnée (jour, mois ou année).
 */
public record PeriodCount(String period, long total) {
}