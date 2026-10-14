package com.cabinet.radiology.exam;

import com.cabinet.radiology.common.error.BusinessRuleException;
import com.cabinet.radiology.common.error.ResourceNotFoundException;
import com.cabinet.radiology.exam.domain.Exam;
import com.cabinet.radiology.exam.domain.ExamStatus;
import com.cabinet.radiology.exam.domain.Modality;
import com.cabinet.radiology.exam.dto.ExamRequest;
import com.cabinet.radiology.exam.dto.ExamResponse;
import com.cabinet.radiology.exam.repository.ExamRepository;
import com.cabinet.radiology.exam.service.ExamReferenceGenerator;
import com.cabinet.radiology.exam.service.ExamService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExamServiceTest {

    private static final OffsetDateTime SLOT = OffsetDateTime.of(2026, 9, 1, 8, 30, 0, 0, ZoneOffset.UTC);

    @Mock
    private ExamRepository examRepository;

    @Mock
    private ExamReferenceGenerator referenceGenerator;

    @InjectMocks
    private ExamService examService;

    @Test
    @DisplayName("creates an exam with a generated reference")
    void createsExam() {
        when(referenceGenerator.next()).thenReturn("EXM-ABCD2345");
        when(examRepository.save(any(Exam.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ExamResponse response = examService.create(request(ExamStatus.SCHEDULED));

        assertThat(response.reference()).isEqualTo("EXM-ABCD2345");
        assertThat(response.modality()).isEqualTo(Modality.MRI);
        assertThat(response.patientReference()).isEqualTo("PAT-001");
    }

    @Test
    @DisplayName("fails when the exam does not exist")
    void failsWhenMissing() {
        UUID id = UUID.randomUUID();
        when(examRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> examService.getById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(id.toString());
    }

    @Test
    @DisplayName("rejects updates on a completed exam")
    void rejectsUpdateOnCompletedExam() {
        UUID id = UUID.randomUUID();
        when(examRepository.findById(id)).thenReturn(Optional.of(exam(ExamStatus.COMPLETED)));

        ExamRequest update = request(ExamStatus.SCHEDULED);
        assertThatThrownBy(() -> examService.update(id, update))
                .isInstanceOf(BusinessRuleException.class);
    }

    @Test
    @DisplayName("updates the status of a scheduled exam")
    void updatesStatus() {
        UUID id = UUID.randomUUID();
        when(examRepository.findById(id)).thenReturn(Optional.of(exam(ExamStatus.SCHEDULED)));

        ExamResponse response = examService.updateStatus(id, ExamStatus.IN_PROGRESS);

        assertThat(response.status()).isEqualTo(ExamStatus.IN_PROGRESS);
    }

    private static ExamRequest request(ExamStatus status) {
        return new ExamRequest("PAT-001", Modality.MRI, "Knee", status, SLOT, 30, "IRM-1");
    }

    private static Exam exam(ExamStatus status) {
        return new Exam("EXM-ABCD2345", "PAT-001", Modality.CT, "Thorax", status, SLOT, 20, "CT-1");
    }
}
