package com.cabinet.radiology.patient.dto;

import com.cabinet.radiology.exam.domain.Modality;

import java.util.Map;

/**
 * Répartition du nombre de patients distincts par modalité d'imagerie.
 */
public record PatientModalityStatistics(long totalPatients, Map<Modality, Long> countByModality) {
}