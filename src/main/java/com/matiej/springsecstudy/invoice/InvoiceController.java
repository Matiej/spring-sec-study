package com.matiej.springsecstudy.invoice;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/inv")
@RequiredArgsConstructor
public class InvoiceController {

    @GetMapping
    public String getInvoices() {
        return "/invoice/invoicePage";
    }

}
