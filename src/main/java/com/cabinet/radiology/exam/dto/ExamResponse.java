package com.cabinet.radiology.exam.dto;

import com.cabinet.radiology.exam.domain.Exam;
import com.cabinet.radiology.exam.domain.ExamStatus;
import com.cabinet.radiology.exam.domain.Modality;

import java.time.OffsetDateTime;
import java.util.UUID;

public record ExamResponse(
        UUID id,
        String reference,
        String patientReference,
        Modality modality,
        String bodyRegion,
        ExamStatus status,
        OffsetDateTime scheduledAt,
        int durationMinutes,
        String room
) {

    public static ExamResponse from(Exam exam) {
        return new ExamResponse(
                exam.getId(),
                exam.getReference(),
                exam.getPatientReference(),
                exam.getModality(),
                exam.getBodyRegion(),
                exam.getStatus(),
                exam.getScheduledAt(),
                exam.getDurationMinutes(),
                exam.getRoom());
    }
}
