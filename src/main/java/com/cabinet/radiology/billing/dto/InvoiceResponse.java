package com.cabinet.radiology.billing.dto;

import com.cabinet.radiology.billing.domain.InvoiceStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record InvoiceResponse(
        UUID id,
        String invoiceNumber,
        BigDecimal amount,
        InvoiceStatus status,
        LocalDateTime createdAt,
        LocalDateTime paidAt

) {
}
