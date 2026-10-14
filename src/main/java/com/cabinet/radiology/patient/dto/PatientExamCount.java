package com.cabinet.radiology.patient.dto;

import java.time.OffsetDateTime;

/**
 * Agrégation des examens d'un patient identifié par sa référence opaque.
 */
public record PatientExamCount(
        String patientReference,
        long totalExams,
        long completedExams,
        long scheduledExams,
        long cancelledExams,
        OffsetDateTime lastExamAt
) {
}