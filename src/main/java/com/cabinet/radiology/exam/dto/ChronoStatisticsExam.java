package com.cabinet.radiology.exam.dto;

import java.time.Instant;

public record ChronoStatisticsExam(

        String periodeType,
        Instant periode,
        long total
) {
}
