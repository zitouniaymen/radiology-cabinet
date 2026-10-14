package com.cabinet.radiology.statistics.dto;

/**
 * Projection brute : nombre d'examens d'une modalité sur une période donnée.
 */
public record ModalityPeriodCount(String period, String modality, long total) {
}