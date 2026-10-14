package com.cabinet.radiology.billing.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record InvoiceRequest(

        String invoiceId,

        @NotNull
        @DecimalMin("0.01")
        BigDecimal amount
) {
}
