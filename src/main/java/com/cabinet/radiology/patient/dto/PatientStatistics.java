package com.cabinet.radiology.patient.dto;

/**
 * Statistiques globales calculées sur l'ensemble des patients référencés par les examens.
 *
 * <p>Aucune donnée directement identifiante n'est manipulée : les patients sont comptés à partir
 * de leur référence opaque issue du système de gestion du cabinet.</p>
 */
public record PatientStatistics(
        long totalPatients,
        long totalExams,
        double averageExamsPerPatient,
        long maxExamsPerPatient,
        long patientsWithCompletedExams,
        double completionRate
) {
}