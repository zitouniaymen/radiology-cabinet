package com.cabinet.radiology.exam;

import com.cabinet.radiology.common.error.ResourceNotFoundException;
import com.cabinet.radiology.exam.api.ExamController;
import com.cabinet.radiology.exam.domain.ExamStatus;
import com.cabinet.radiology.exam.domain.Modality;
import com.cabinet.radiology.exam.dto.ExamResponse;
import com.cabinet.radiology.exam.dto.PageResponse;
import com.cabinet.radiology.exam.service.ExamService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ExamController.class)
class ExamControllerTest {

    private static final OffsetDateTime SLOT = OffsetDateTime.of(2026, 9, 1, 8, 30, 0, 0, ZoneOffset.UTC);

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ExamService examService;

    @Test
    void returnsPagedExams() throws Exception {
        ExamResponse exam = new ExamResponse(UUID.randomUUID(), "EXM-ABCD2345", "PAT-001",
                Modality.MRI, "Knee", ExamStatus.SCHEDULED, SLOT, 30, "IRM-1");
        when(examService.search(eq(null), eq(null), any()))
                .thenReturn(new PageResponse<>(List.of(exam), 0, 20, 1, 1));

        mockMvc.perform(get("/api/v1/exams"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].reference").value("EXM-ABCD2345"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void returnsNotFoundPayloadForUnknownExam() throws Exception {
        UUID id = UUID.randomUUID();
        when(examService.getById(id)).thenThrow(new ResourceNotFoundException("Exam %s not found".formatted(id)));

        mockMvc.perform(get("/api/v1/exams/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.path").value("/api/v1/exams/" + id));
    }

    @Test
    void rejectsInvalidPayloadWithFieldViolations() throws Exception {
        String body = """
                {
                  "patientReference": "",
                  "modality": "MRI",
                  "bodyRegion": "Knee",
                  "status": "SCHEDULED",
                  "scheduledAt": "2026-09-01T08:30:00Z",
                  "durationMinutes": 1,
                  "room": "IRM-1"
                }
                """;

        mockMvc.perform(post("/api/v1/exams").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.violations").isArray())
                .andExpect(jsonPath("$.message").value("Request validation failed"));
    }
}
