package com.cabinet.radiology.exam.dto;

import com.cabinet.radiology.exam.domain.ExamStatus;
import jakarta.validation.constraints.NotNull;

public record ExamStatusUpdateRequest(@NotNull ExamStatus status) {
}
