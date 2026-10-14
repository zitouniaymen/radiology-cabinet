package com.cabinet.radiology.exam.dto;

import com.cabinet.radiology.exam.domain.ExamStatus;

import java.util.Map;

public record ExamStatistics(long total, Map<ExamStatus, Long> countByStatus) {
}
