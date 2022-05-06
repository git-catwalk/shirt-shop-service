package com.bluntsoftware.shirtshop.service;

import com.bluntsoftware.shirtshop.model.Estimate;
import com.bluntsoftware.shirtshop.model.Invoice;
import com.bluntsoftware.shirtshop.model.LineItem;
import com.bluntsoftware.shirtshop.repository.InvoiceRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class LineItemService {

    private final InvoiceRepo invoiceRepo;

    public LineItemService(InvoiceRepo invoiceRepo) {
        this.invoiceRepo = invoiceRepo;
    }

    public Invoice save(String orderId, LineItem item) {
        Invoice ord = this.invoiceRepo.findById(orderId).orElse(null);
        if(ord != null){
            List<LineItem> lineItems = ord.getItems();
            Optional<LineItem> li = lineItems.stream().filter(i-> i.getId().equalsIgnoreCase(item.getId())).findFirst();
            li.ifPresent(lineItem -> lineItems.set(lineItems.indexOf(lineItem), item));
            return this.invoiceRepo.save(ord);
        }
        return null;
    }

}
