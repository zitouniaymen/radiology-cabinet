package com.cabinet.radiology.exam.service;

import com.cabinet.radiology.common.error.BusinessRuleException;
import com.cabinet.radiology.common.error.ResourceNotFoundException;
import com.cabinet.radiology.exam.Enum.PeriodType;
import com.cabinet.radiology.exam.domain.Exam;
import com.cabinet.radiology.exam.domain.ExamStatus;
import com.cabinet.radiology.exam.domain.Modality;
import com.cabinet.radiology.exam.dto.ChronoStatisticsExam;
import com.cabinet.radiology.exam.dto.ExamRequest;
import com.cabinet.radiology.exam.dto.ExamResponse;
import com.cabinet.radiology.exam.dto.ExamStatistics;
import com.cabinet.radiology.exam.dto.ExamStatisticsChrono;
import com.cabinet.radiology.exam.dto.PageResponse;
import com.cabinet.radiology.exam.repository.ExamRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@Slf4j
public class ExamService {

    private final ExamRepository examRepository;
    private final ExamReferenceGenerator referenceGenerator;

    public ExamService(final ExamRepository examRepository, final ExamReferenceGenerator referenceGenerator) {
        this.examRepository = examRepository;
        this.referenceGenerator = referenceGenerator;
    }

    public PageResponse<ExamResponse> search(final ExamStatus status, final Modality modality, final Pageable pageable) {
        return PageResponse.from(examRepository.search(status, modality, pageable), ExamResponse::from);
    }

    public ExamResponse getById(final UUID id) {
        return ExamResponse.from(requireExam(id));
    }

    public ExamStatistics statistics() {
        final Map<ExamStatus, Long> countByStatus = new EnumMap<>(ExamStatus.class);
        for (final ExamStatus status : ExamStatus.values()) {
            countByStatus.put(status, examRepository.countByStatus(status));
        }
        return new ExamStatistics(examRepository.count(), countByStatus);
    }

    public ExamStatisticsChrono calculParModality() {

        final Map<Modality, Long> countByModality = new EnumMap<>(Modality.class);
        for (final Modality modality : Modality.values()) {

            countByModality.put(modality, examRepository.countByModality(modality));
        }
        return new ExamStatisticsChrono(examRepository.count(), countByModality);


    }

    public List<ChronoStatisticsExam> findAllChronoStatistics() {
        return examRepository.findAllChronoStatistics();
    }

    public List<ChronoStatisticsExam> calculChronostatisticsExamlist(final PeriodType periodeType) {
        final List<ChronoStatisticsExam> chronoStatisticsExamList = examRepository.chronostatisticsExamlist(periodeType.name());
        log.info("Starting chrono statistics processing");

        if (chronoStatisticsExamList.isEmpty() || chronoStatisticsExamList == null) {
            log.info("la liste est vide pas des element pour la liste {}", chronoStatisticsExamList);

        } else {

            log.info("Starting calcul statistics processing to list {}", chronoStatisticsExamList);
            return chronoStatisticsExamList
                    .stream()
                    .map(row -> new ChronoStatisticsExam(
                            row.periodeType(),
                            row.periode(),
                            row.total()

                    )).peek(e -> log.info("periodeType {} periode {} total", e.periodeType(), e.periode(), e.total()))
                    .toList();

        }

        return new ArrayList<>();
    }


    @Transactional
    public ExamResponse create(final ExamRequest request) {
        final Exam exam = new Exam(
                referenceGenerator.next(),
                request.patientReference(),
                request.modality(),
                request.bodyRegion(),
                request.status(),
                request.scheduledAt(),
                request.durationMinutes(),
                request.room());
        return ExamResponse.from(examRepository.save(exam));
    }

    @Transactional
    public ExamResponse update(final UUID id, final ExamRequest request) {
        final Exam exam = requireExam(id);
        ensureMutable(exam);
        exam.setPatientReference(request.patientReference());
        exam.setModality(request.modality());
        exam.setBodyRegion(request.bodyRegion());
        exam.setStatus(request.status());
        exam.setScheduledAt(request.scheduledAt());
        exam.setDurationMinutes(request.durationMinutes());
        exam.setRoom(request.room());
        return ExamResponse.from(exam);
    }

    @Transactional
    public ExamResponse updateStatus(final UUID id, final ExamStatus status) {
        final Exam exam = requireExam(id);
        ensureMutable(exam);
        exam.setStatus(status);
        return ExamResponse.from(exam);
    }

    @Transactional
    public void delete(final UUID id) {
        final Exam exam = requireExam(id);
        examRepository.delete(exam);
    }

    private Exam requireExam(final UUID id) {
        return examRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exam %s not found".formatted(id)));
    }

    private void ensureMutable(final Exam exam) {
        if (exam.getStatus() == ExamStatus.COMPLETED || exam.getStatus() == ExamStatus.CANCELLED) {
            throw new BusinessRuleException(
                    "Exam %s is %s and can no longer be modified".formatted(exam.getReference(), exam.getStatus()));
        }
    }
}
