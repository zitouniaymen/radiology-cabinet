package com.cabinet.radiology.exam.api;

import com.cabinet.radiology.exam.Enum.PeriodType;
import com.cabinet.radiology.exam.domain.ExamStatus;
import com.cabinet.radiology.exam.domain.Modality;
import com.cabinet.radiology.exam.dto.ChronoStatisticsExam;
import com.cabinet.radiology.exam.dto.ExamRequest;
import com.cabinet.radiology.exam.dto.ExamResponse;
import com.cabinet.radiology.exam.dto.ExamStatisticsChrono;
import com.cabinet.radiology.exam.dto.ExamStatusUpdateRequest;
import com.cabinet.radiology.exam.dto.PageResponse;
import com.cabinet.radiology.exam.service.ExamService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/exams")
public class ExamController {

    private static final int MAX_PAGE_SIZE = 100;

    private final ExamService examService;

    public ExamController(final ExamService examService) {
        this.examService = examService;
    }

    @GetMapping
    public PageResponse<ExamResponse> list(@RequestParam(required = false) final ExamStatus status,
                                           @RequestParam(required = false) final Modality modality,
                                           @RequestParam(defaultValue = "0") final int page,
                                           @RequestParam(defaultValue = "20") final int size) {
        final int safeSize = Math.clamp(size, 1, MAX_PAGE_SIZE);
        final int safePage = Math.max(page, 0);
        return examService.search(status, modality,
                PageRequest.of(safePage, safeSize, Sort.by(Sort.Direction.ASC, "scheduledAt")));
    }

    //    @GetMapping("/statistics")
//    public ExamStatistics statistics() {
//        return examService.statistics();
//    }
    @GetMapping("/modalityStatistics")
    public ExamStatisticsChrono getModalityStatistics() {
        return examService.calculParModality();
    }


    @GetMapping("/calculChronostatisticsExam")
    public List<ChronoStatisticsExam> getCalculChronostatisticsExamlist(@RequestParam final PeriodType periodeType) {
        return examService.calculChronostatisticsExamlist(periodeType);
    }

    @GetMapping("/calculChronostatisticsExamMonth")
    public List<ChronoStatisticsExam> getCalculChronostatisticsExamMonthlist() {
        return examService.findAllChronoStatistics();
    }
//
//    @GetMapping("/calculChronostatisticsExamYear")
//    public List<ChronoStatisticsExam> getCalculChronostatisticsExamYearlist() {
//        return examService.calculChronostatisticsExamYearlist();
//    }

    @GetMapping("/{id}")
    public ExamResponse getById(@PathVariable final UUID id) {
        return examService.getById(id);
    }

    @PostMapping
    public ResponseEntity<ExamResponse> create(@Valid @RequestBody final ExamRequest request,
                                               final UriComponentsBuilder uriBuilder) {
        final ExamResponse created = examService.create(request);
        return ResponseEntity
                .created(uriBuilder.path("/api/v1/exams/{id}").build(created.id()))
                .body(created);
    }

    @PutMapping("/{id}")
    public ExamResponse update(@PathVariable final UUID id, @Valid @RequestBody final ExamRequest request) {
        return examService.update(id, request);
    }

    @PatchMapping("/{id}/status")
    public ExamResponse updateStatus(@PathVariable final UUID id, @Valid @RequestBody final ExamStatusUpdateRequest request) {
        return examService.updateStatus(id, request.status());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable final UUID id) {
        examService.delete(id);
    }
}
