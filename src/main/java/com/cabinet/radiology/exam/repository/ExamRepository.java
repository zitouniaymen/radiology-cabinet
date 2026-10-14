package com.cabinet.radiology.exam.repository;

import com.cabinet.radiology.exam.domain.Exam;
import com.cabinet.radiology.exam.domain.ExamStatus;
import com.cabinet.radiology.exam.domain.Modality;
import com.cabinet.radiology.exam.dto.ChronoStatisticsExam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ExamRepository extends JpaRepository<Exam, UUID> {

    Optional<Exam> findByReference(String reference);

    boolean existsByReference(String reference);

    long countByStatus(ExamStatus status);

    long countByModality(Modality modality);

    @Query(value = """
            SELECT
                :periodType AS periodType,
                DATE_TRUNC(CAST(:periodType AS text), scheduled_at) AS period,
                COUNT(*) AS total
            FROM exam
            WHERE scheduled_at IS NOT NULL
            GROUP BY 2
            ORDER BY 2
            """, nativeQuery = true)
    List<ChronoStatisticsExam> chronostatisticsExamlist(
            @Param("periodType") String periodType);


    @Query(value = """
            SELECT
                UPPER(p.period_type) AS periodType,
                DATE_TRUNC(p.period_type, e.scheduled_at) AS period,
                COUNT(*) AS total
            FROM exam e
            CROSS JOIN (
                VALUES ('day'), ('month'), ('year')
            ) AS p(period_type)
            WHERE e.scheduled_at IS NOT NULL
            GROUP BY
                p.period_type,
                DATE_TRUNC(p.period_type, e.scheduled_at)
            ORDER BY
                p.period_type,
                period
            """, nativeQuery = true)
    List<ChronoStatisticsExam> findAllChronoStatistics();


    @Query("""
            select e from Exam e
            where (:status is null or e.status = :status)
              and (:modality is null or e.modality = :modality)
            """)
    Page<Exam> search(@Param("status") ExamStatus status,
                      @Param("modality") Modality modality,
                      Pageable pageable);
}
