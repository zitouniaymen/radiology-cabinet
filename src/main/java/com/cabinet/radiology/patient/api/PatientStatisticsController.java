package com.cabinet.radiology.patient.api;

import com.cabinet.radiology.exam.Enum.PeriodType;
import com.cabinet.radiology.patient.dto.PatientChronoStatistics;
import com.cabinet.radiology.patient.dto.PatientExamCount;
import com.cabinet.radiology.patient.dto.PatientModalityStatistics;
import com.cabinet.radiology.patient.dto.PatientStatistics;
import com.cabinet.radiology.patient.service.PatientStatisticsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * API de calcul des indicateurs patients.
 */
@RestController
@RequestMapping("/api/v1/patients/statistics")
public class PatientStatisticsController {

    private final PatientStatisticsService patientStatisticsService;

    public PatientStatisticsController(final PatientStatisticsService patientStatisticsService) {
        this.patientStatisticsService = patientStatisticsService;
    }

    @GetMapping
    public PatientStatistics globalStatistics() {
        return patientStatisticsService.globalStatistics();
    }

    @GetMapping("/examCounts")
    public List<PatientExamCount> examCountsByPatient() {
        return patientStatisticsService.examCountsByPatient();
    }

    @GetMapping("/modalityStatistics")
    public PatientModalityStatistics modalityStatistics() {
        return patientStatisticsService.modalityStatistics();
    }

    @GetMapping("/calculChronostatisticsPatient")
    public List<PatientChronoStatistics> calculChronostatisticsPatient(
            @RequestParam final PeriodType periodeType) {
        return patientStatisticsService.chronoStatistics(periodeType.name());
    }
}