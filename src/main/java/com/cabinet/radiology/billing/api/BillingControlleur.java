package com.cabinet.radiology.billing.api;


import com.cabinet.radiology.billing.dto.InvoiceRequest;
import com.cabinet.radiology.billing.dto.InvoiceResponse;
import com.cabinet.radiology.billing.service.InvoiceService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/invoices")
public class BillingControlleur {

    public final InvoiceService invoiceService;

    public BillingControlleur(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @PostMapping("/create")
    public InvoiceResponse create(@Valid @RequestBody InvoiceRequest invoiceRequest) {

        return invoiceService.create(invoiceRequest);
    }
}
