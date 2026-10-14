package com.cabinet.radiology.exam.dto;


import com.cabinet.radiology.exam.domain.Modality;

import java.util.Map;

public record ExamStatisticsChrono(long total, Map<Modality, Long> countByModality) {
}
