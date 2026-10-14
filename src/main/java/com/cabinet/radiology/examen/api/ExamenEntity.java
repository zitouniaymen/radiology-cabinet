package com.cabinet.radiology.examen.api;

import com.cabinet.radiology.exam.Enum.ExamPriority;
import com.cabinet.radiology.exam.domain.ExamStatus;
import com.cabinet.radiology.exam.domain.Modality;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "examen")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExamenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Référence métier unique
    @Column(nullable = false, unique = true, updatable = false)
    private String reference;

    // Type d'examen
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Modality modality;

    // Nom précis de l'examen
    @Column(name = "exam_type", nullable = false)
    private String examType;

    // Zone anatomique
    private String bodyPart;

    // Motif / indication
    @Column(length = 1000)
    private String clinicalIndication;

    // Priorité
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExamPriority priority;

    // État actuel
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExamStatus status;

    // Date prévue
    private LocalDateTime scheduledAt;

    // Début réel
    private LocalDateTime startedAt;

    // Fin réelle
    private LocalDateTime completedAt;

    // Commentaire interne
    @Column(length = 2000)
    private String notes;

    // Création
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Dernière modification
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    void onCreate() {
        final var now = LocalDateTime.now();

        createdAt = now;
        updatedAt = now;

        if (status == null) {
            status = ExamStatus.SCHEDULED;
        }

        if (priority == null) {
            priority = ExamPriority.NORMAL;
        }
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
