package com.cabinet.radiology.statistics.dto;

/**
 * Projection brute : patients distincts et examens comptés sur une période donnée.
 */
public record PatientPeriodCount(String period, long totalPatients, long totalExams) {
}