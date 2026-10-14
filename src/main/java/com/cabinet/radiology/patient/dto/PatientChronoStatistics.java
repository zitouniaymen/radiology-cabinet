package com.cabinet.radiology.patient.dto;

import java.time.Instant;

/**
 * Nombre de patients distincts pris en charge sur une période donnée (jour, mois ou année).
 */
public record PatientChronoStatistics(
        String periodeType,
        Instant periode,
        long total
) {
}