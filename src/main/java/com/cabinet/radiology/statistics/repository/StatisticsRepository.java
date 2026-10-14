package com.cabinet.radiology.statistics.repository;

import com.cabinet.radiology.exam.domain.Exam;
import com.cabinet.radiology.statistics.dto.ModalityCount;
import com.cabinet.radiology.statistics.dto.ModalityPeriodCount;
import com.cabinet.radiology.statistics.dto.PatientPeriodCount;
import com.cabinet.radiology.statistics.dto.PeriodCount;
import com.cabinet.radiology.statistics.dto.StatusCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Repository dédié aux agrégations statistiques du cabinet.
 *
 * <p>Toutes les requêtes s'appuient sur la table {@code exam}. Les périodes sont calculées
 * par PostgreSQL via {@code DATE_TRUNC} et formatées en libellés lisibles via {@code TO_CHAR}
 * (jour : {@code YYYY-MM-DD}, mois : {@code YYYY-MM}, année : {@code YYYY}).</p>
 *
 * <p>Les bornes {@code from} (inclus) et {@code to} (exclu) sont toujours fournies par la
 * couche service afin d'éviter toute ambiguïté de typage des paramètres côté PostgreSQL.</p>
 */
public interface StatisticsRepository extends JpaRepository<Exam, UUID> {

    // ---------------------------------------------------------------------------------------------
    // Examens par période (jour / mois / année)
    // ---------------------------------------------------------------------------------------------

    @Query(value = """
            SELECT
                TO_CHAR(DATE_TRUNC(CAST(:periodType AS text), scheduled_at), CAST(:periodFormat AS text)) AS period,
                COUNT(*) AS total
            FROM exam
            WHERE scheduled_at >= CAST(:from AS timestamptz)
              AND scheduled_at <  CAST(:to AS timestamptz)
            GROUP BY 1
            ORDER BY 1
            """, nativeQuery = true)
    List<PeriodCount> countExamsByPeriod(@Param("periodType") String periodType,
                                         @Param("periodFormat") String periodFormat,
                                         @Param("from") OffsetDateTime from,
                                         @Param("to") OffsetDateTime to);

    // ---------------------------------------------------------------------------------------------
    // Patients distincts et examens par période
    // ---------------------------------------------------------------------------------------------

    @Query(value = """
            SELECT
                TO_CHAR(DATE_TRUNC(CAST(:periodType AS text), scheduled_at), CAST(:periodFormat AS text)) AS period,
                COUNT(DISTINCT patient_reference) AS "totalPatients",
                COUNT(*) AS "totalExams"
            FROM exam
            WHERE scheduled_at >= CAST(:from AS timestamptz)
              AND scheduled_at <  CAST(:to AS timestamptz)
            GROUP BY 1
            ORDER BY 1
            """, nativeQuery = true)
    List<PatientPeriodCount> countPatientsAndExamsByPeriod(@Param("periodType") String periodType,
                                                           @Param("periodFormat") String periodFormat,
                                                           @Param("from") OffsetDateTime from,
                                                           @Param("to") OffsetDateTime to);

    // ---------------------------------------------------------------------------------------------
    // Examens par type d'examen (modalité)
    // ---------------------------------------------------------------------------------------------

    @Query(value = """
            SELECT modality AS modality, COUNT(*) AS total
            FROM exam
            WHERE scheduled_at >= CAST(:from AS timestamptz)
              AND scheduled_at <  CAST(:to AS timestamptz)
            GROUP BY modality
            ORDER BY total DESC
            """, nativeQuery = true)
    List<ModalityCount> countExamsByModality(@Param("from") OffsetDateTime from,
                                             @Param("to") OffsetDateTime to);

    @Query(value = """
            SELECT modality AS modality, COUNT(*) AS total
            FROM exam
            GROUP BY modality
            ORDER BY total DESC
            """, nativeQuery = true)
    List<ModalityCount> countAllExamsByModality();

    // ---------------------------------------------------------------------------------------------
    // Détail par période et par type d'examen
    // ---------------------------------------------------------------------------------------------

    @Query(value = """
            SELECT
                TO_CHAR(DATE_TRUNC(CAST(:periodType AS text), scheduled_at), CAST(:periodFormat AS text)) AS period,
                modality AS modality,
                COUNT(*) AS total
            FROM exam
            WHERE scheduled_at >= CAST(:from AS timestamptz)
              AND scheduled_at <  CAST(:to AS timestamptz)
            GROUP BY 1, modality
            ORDER BY 1, modality
            """, nativeQuery = true)
    List<ModalityPeriodCount> countExamsByModalityAndPeriod(@Param("periodType") String periodType,
                                                            @Param("periodFormat") String periodFormat,
                                                            @Param("from") OffsetDateTime from,
                                                            @Param("to") OffsetDateTime to);

    // ---------------------------------------------------------------------------------------------
    // Répartition par statut
    // ---------------------------------------------------------------------------------------------

    @Query(value = """
            SELECT status AS status, COUNT(*) AS total
            FROM exam
            GROUP BY status
            ORDER BY total DESC
            """, nativeQuery = true)
    List<StatusCount> countAllExamsByStatus();

    // ---------------------------------------------------------------------------------------------
    // Totaux globaux
    // ---------------------------------------------------------------------------------------------

    @Query(value = "SELECT COUNT(DISTINCT patient_reference) FROM exam", nativeQuery = true)
    long countDistinctPatients();

    @Query(value = """
            SELECT COUNT(DISTINCT patient_reference)
            FROM exam
            WHERE scheduled_at >= CAST(:from AS timestamptz)
              AND scheduled_at <  CAST(:to AS timestamptz)
            """, nativeQuery = true)
    long countDistinctPatientsBetween(@Param("from") OffsetDateTime from,
                                      @Param("to") OffsetDateTime to);

    @Query(value = """
            SELECT COUNT(*)
            FROM exam
            WHERE scheduled_at >= CAST(:from AS timestamptz)
              AND scheduled_at <  CAST(:to AS timestamptz)
            """, nativeQuery = true)
    long countExamsBetween(@Param("from") OffsetDateTime from,
                           @Param("to") OffsetDateTime to);
}