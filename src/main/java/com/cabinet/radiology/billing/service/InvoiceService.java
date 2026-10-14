package com.cabinet.radiology.billing.service;


import com.cabinet.radiology.billing.domain.Invoice;
import com.cabinet.radiology.billing.dto.InvoiceRequest;
import com.cabinet.radiology.billing.dto.InvoiceResponse;
import com.cabinet.radiology.billing.repository.InvoiceRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    public InvoiceService(final InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;


    }

    public InvoiceResponse create(final InvoiceRequest invoiceRequest) {

        final String invoiceNumber = generateInvoiceNumber();
        final Invoice invoice = new Invoice(invoiceNumber, invoiceRequest.amount());

        return toResponse(invoiceRepository.save(invoice));
    }

    private String generateInvoiceNumber() {
        return "FAC-%d-%S".formatted(
                Year.now().getValue(),
                UUID.randomUUID()
                        .toString()
                        .substring(0, 8)
                        .toUpperCase()
        );
    }

    List<InvoiceResponse> findAll() {
        return invoiceRepository
                .findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }


    private InvoiceResponse toResponse(final Invoice invoice) {

        return new InvoiceResponse(invoice.getId(), invoice.getInvoiceNumber(), invoice.getAmount(), invoice.getStatus(), invoice.getCreatedAt(), invoice.getPaidAt());
    }
}
