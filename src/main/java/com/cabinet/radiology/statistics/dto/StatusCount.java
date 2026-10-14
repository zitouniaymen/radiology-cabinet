package com.cabinet.radiology.statistics.dto;

/**
 * Projection brute : nombre d'examens pour un statut donné.
 */
public record StatusCount(String status, long total) {
}