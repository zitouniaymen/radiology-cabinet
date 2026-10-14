package com.cabinet.radiology.statistics.dto;

public record DashboardStatistics(

        long totalExams,
        long completedExams,
        long pendingExams,
        long cancelledExams,
        double completionRate
) {
}
