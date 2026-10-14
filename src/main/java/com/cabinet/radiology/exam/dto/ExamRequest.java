package com.cabinet.radiology.exam.dto;

import com.cabinet.radiology.exam.domain.ExamStatus;
import com.cabinet.radiology.exam.domain.Modality;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.OffsetDateTime;

/**
 * Payload accepted when creating or replacing an exam.
 *
 * <p>patientReference must be a pseudonymized identifier, never a patient name.</p>
 */
public record ExamRequest(
        @NotBlank @Size(max = 32) @Pattern(regexp = "^[A-Z0-9-]+$", message = "must be an uppercase alphanumeric code")
        String patientReference,

        @NotNull Modality modality,

        @NotBlank @Size(max = 64) String bodyRegion,

        @NotNull ExamStatus status,

        @NotNull OffsetDateTime scheduledAt,

        @Min(5) @Max(480) int durationMinutes,

        @NotBlank @Size(max = 32) String room
) {
}
