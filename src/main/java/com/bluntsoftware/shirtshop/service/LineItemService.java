package com.bluntsoftware.shirtshop.service;

import com.bluntsoftware.shirtshop.model.Invoice;
import com.bluntsoftware.shirtshop.model.LineItem;
import com.bluntsoftware.shirtshop.model.PrintLocation;
import com.bluntsoftware.shirtshop.repository.InvoiceRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class LineItemService {

    private final InvoiceRepo invoiceRepo;
    private final AuditTrailService auditTrailService;

    public LineItemService(InvoiceRepo invoiceRepo, AuditTrailService auditTrailService) {
        this.invoiceRepo = invoiceRepo;
        this.auditTrailService = auditTrailService;
    }

    public Invoice save(String orderId, LineItem item) {
        Invoice ord = this.invoiceRepo.findById(orderId).orElse(null);
        if(ord != null){
            List<LineItem> lineItems = ord.getItems();
            Optional<LineItem> li = lineItems.stream().filter(i-> i.getId().equalsIgnoreCase(item.getId())).findFirst();
            String orderStatus = orderStatus(ord);
            li.ifPresent(lineItem ->{
                    String lineItemStatus = lineItemStatus(lineItem);
                    lineItems.set(lineItems.indexOf(lineItem), item);
                    String newLineItemStatus = orderStatus(ord);
                    if(!lineItemStatus.equalsIgnoreCase(newLineItemStatus)){
                        auditTrailService.audit("line item status changed from " + lineItemStatus + " to " + newLineItemStatus,orderId,lineItem.getId());
                    }
                }
            );

            String newOrderStatus = orderStatus(ord);
            if(!orderStatus.equalsIgnoreCase(newOrderStatus)){
                auditTrailService.audit("order status changed from " + orderStatus + " to " + newOrderStatus,orderId);
            }
            ord.setStatus(newOrderStatus);
            return this.invoiceRepo.save(ord);
        }
        return null;
    }

    public static String orderStatus(Invoice order){
        Calendar now = Calendar.getInstance();
        Calendar due = null;
        if(order.getDateDue() != null){
            due =  Calendar.getInstance();
            due.setTime(order.getDateDue());
        }
        String status = orderItemsStatus(order);
        status = due != null && now.after(due) ? status + " (Order is late)" : status;
        return status;
    }

    public static String orderItemsStatus(Invoice order){
        List<String> statuses = new ArrayList<>();

        for(LineItem li : order.getItems()){
            statuses.add(lineItemStatus(li));
        }

        if(statuses.contains("Material Not Ordered")){
            return "Material Not Ordered";
        }else  if(statuses.contains("Material Late")) {
            return "Material Late";
        }else if(statuses.contains("Material Ordered")) {
            return "Waiting on Material";
        }else if(statuses.contains("Material Received")){
            return "Material Received";
        }else if(statuses.contains("Pre Production Started")){
            return "In Pre Production";
        }else if(statuses.contains("Pre Production Completed")){
            return "In Production";
        }else if(statuses.contains("Production Started")){
            return "In Production";
        }else if(statuses.contains("Production Completed")){
            return "Ready for Delivery";
        }else if(statuses.contains("Delivered")){
            return "Out for Delivery";
        }else if(statuses.contains("Complete")){
            return "Order Complete";
        }else{
            return "Status Unknown";
        }
    }

    public static String lineItemStatus(LineItem li){
        String status = materialStatus(li);
        for(PrintLocation pl:li.getPrintLocations()){
            status = !preProductionStatus(pl).contains("Not") ? preProductionStatus(pl) : status ;
            status = !productionStatus(pl).contains("Not") ? productionStatus(pl) : status ;
        }
        status = !deliveryStatus(li).contains("Not") ? deliveryStatus(li) : status ;
        return status;
    }

    public static String materialStatus(LineItem li){
        Calendar cal = Calendar.getInstance();
        Calendar estDelivery = null;
        if(li.getEstimatedDeliveryDate() != null){
            estDelivery = Calendar.getInstance();
            estDelivery.setTime(li.getEstimatedDeliveryDate());
        }
        String status = "Material Not Ordered";
        status = li.getOrderedDate() != null ? "Material Ordered" : status;
        status = estDelivery != null && cal.after(estDelivery) ? "Material Late" : status;
        status = li.getReceivedDate() != null ? "Material Received" : status;
        return status;
    }

    public static String preProductionStatus(PrintLocation pl){
        String status = "Pre Production Not Started";
        status = pl.getPrePressStartDate() != null ? "Pre Production Started" : status;
        status = pl.getPrePressCompleteDate() != null ? "Pre Production Completed" : status;
        return status;
    }

    public static String productionStatus(PrintLocation pl){
        String status = "Production Not Started";
        status = pl.getProductionStartDate() != null ? "Production Started" : status;
        status = pl.getProductionCompleteDate() != null ? "Production Completed" : status;
        return status;
    }

    public static String deliveryStatus(LineItem li){
        String status = "Not Delivered";
        status = li.getDeliveryDate() != null ? "Delivered" : status;
        status = li.getDeliveryReceivedDate() != null ? "Complete" : status;
        return status;
    }
}
