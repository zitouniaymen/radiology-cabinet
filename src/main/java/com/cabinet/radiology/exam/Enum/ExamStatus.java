package com.cabinet.radiology.exam.Enum;

public enum ExamStatus {

    SCHEDULED,       // programmé
    CHECKED_IN,      // patient arrivé
    IN_PROGRESS,     // examen en cours
    COMPLETED,       // examen terminé
    REPORT_PENDING,  // compte rendu en attente
    VALIDATED,       // compte rendu validé
    CANCELLED,       // annulé
    NO_SHOW          // patient absent
}