package com.cabinet.radiology.statistics.dto;

/**
 * Projection brute : nombre d'éléments pour une modalité (type d'examen) donnée.
 */
public record ModalityCount(String modality, long total) {
}