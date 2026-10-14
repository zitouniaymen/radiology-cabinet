package com.cabinet.radiology.patient.repository;

import com.cabinet.radiology.exam.domain.Exam;
import com.cabinet.radiology.exam.domain.ExamStatus;
import com.cabinet.radiology.patient.dto.PatientChronoStatistics;
import com.cabinet.radiology.patient.dto.PatientExamCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

/**
 * Requêtes d'agrégation dédiées au calcul des indicateurs patients.
 *
 * <p>Les patients sont identifiés uniquement par leur référence opaque
 * ({@code patient_reference}), aucune donnée nominative n'est persistée.</p>
 */
public interface PatientStatisticsRepository extends JpaRepository<Exam, UUID> {

    long countByStatus(ExamStatus status);

    @Query(value = "SELECT COUNT(DISTINCT patient_reference) FROM exam", nativeQuery = true)
    long countDistinctPatients();

    @Query(value = """
            SELECT COUNT(DISTINCT patient_reference)
            FROM exam
            WHERE status = :status
            """, nativeQuery = true)
    long countDistinctPatientsByStatus(@Param("status") String status);

    @Query(value = """
            SELECT COUNT(DISTINCT patient_reference)
            FROM exam
            WHERE modality = :modality
            """, nativeQuery = true)
    long countDistinctPatientsByModality(@Param("modality") String modality);

    @Query(value = """
            SELECT
                patient_reference AS patientReference,
                COUNT(*) AS totalExams,
                COUNT(CASE WHEN status = 'COMPLETED' THEN 1 END) AS completedExams,
                COUNT(CASE WHEN status = 'SCHEDULED' THEN 1 END) AS scheduledExams,
                COUNT(CASE WHEN status = 'CANCELLED' THEN 1 END) AS cancelledExams,
                MAX(scheduled_at) AS lastExamAt
            FROM exam
            GROUP BY patient_reference
            ORDER BY COUNT(*) DESC, patient_reference ASC
            """, nativeQuery = true)
    List<PatientExamCount> findPatientExamCounts();

    @Query(value = """
            SELECT
                :periodType AS periodType,
                DATE_TRUNC(CAST(:periodType AS text), scheduled_at) AS period,
                COUNT(DISTINCT patient_reference) AS total
            FROM exam
            WHERE scheduled_at IS NOT NULL
            GROUP BY 2
            ORDER BY 2
            """, nativeQuery = true)
    List<PatientChronoStatistics> chronoPatientStatistics(@Param("periodType") String periodType);
}