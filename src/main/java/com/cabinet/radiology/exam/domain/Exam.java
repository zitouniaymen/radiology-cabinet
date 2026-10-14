package com.cabinet.radiology.exam.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Scheduling record for a radiology exam.
 *
 * <p>No medical or directly identifying data is persisted: patients are referenced through an
 * opaque code issued by the practice management system.</p>
 */
@Entity
@Table(name = "exam")
public class Exam {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "reference", nullable = false, unique = true, length = 32)
    private String reference;

    @Column(name = "patient_reference", nullable = false, length = 32)
    private String patientReference;

    @Enumerated(EnumType.STRING)
    @Column(name = "modality", nullable = false, length = 32)
    private Modality modality;

    @Column(name = "body_region", nullable = false, length = 64)
    private String bodyRegion;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 32)
    private ExamStatus status;

    @Column(name = "scheduled_at", nullable = false)
    private OffsetDateTime scheduledAt;

    @Column(name = "duration_minutes", nullable = false)
    private int durationMinutes;

    @Column(name = "room", nullable = false, length = 32)
    private String room;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Version
    @Column(name = "version", nullable = false)
    private long version;

    protected Exam() {
        // required by JPA
    }

    public Exam(String reference,
                String patientReference,
                Modality modality,
                String bodyRegion,
                ExamStatus status,
                OffsetDateTime scheduledAt,
                int durationMinutes,
                String room) {
        this.reference = Objects.requireNonNull(reference);
        this.patientReference = Objects.requireNonNull(patientReference);
        this.modality = Objects.requireNonNull(modality);
        this.bodyRegion = Objects.requireNonNull(bodyRegion);
        this.status = Objects.requireNonNull(status);
        this.scheduledAt = Objects.requireNonNull(scheduledAt);
        this.durationMinutes = durationMinutes;
        this.room = Objects.requireNonNull(room);
    }

    public UUID getId() {
        return id;
    }

    public String getReference() {
        return reference;
    }

    public String getPatientReference() {
        return patientReference;
    }

    public void setPatientReference(String patientReference) {
        this.patientReference = patientReference;
    }

    public Modality getModality() {
        return modality;
    }

    public void setModality(Modality modality) {
        this.modality = modality;
    }

    public String getBodyRegion() {
        return bodyRegion;
    }

    public void setBodyRegion(String bodyRegion) {
        this.bodyRegion = bodyRegion;
    }

    public ExamStatus getStatus() {
        return status;
    }

    public void setStatus(ExamStatus status) {
        this.status = status;
    }

    public OffsetDateTime getScheduledAt() {
        return scheduledAt;
    }

    public void setScheduledAt(OffsetDateTime scheduledAt) {
        this.scheduledAt = scheduledAt;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public long getVersion() {
        return version;
    }
}
