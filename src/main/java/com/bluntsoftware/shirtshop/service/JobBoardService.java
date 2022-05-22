package com.bluntsoftware.shirtshop.service;

import com.bluntsoftware.shirtshop.model.*;
import com.bluntsoftware.shirtshop.repository.InvoiceRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Service
public class JobBoardService {

    private final InvoiceRepo repo;

    public JobBoardService(InvoiceRepo repo) {
        this.repo = repo;
    }

    JobCard getJobCard(Invoice ord,LineItem li,PrintLocation pl){
        String style = li.getGarmentStyle() != null ? li.getGarmentStyle().getTitle() : "Style missing";
        String color = li.getGarmentColor() != null ? li.getGarmentColor().getName() : "Color missing";

        return JobCard.builder()
                .customer(ord.getCustomer())
                .description(li.getDescription())
                .name(style + " - " + color)
                .invoiceId(ord.getId())
                .tags(ord.getTags())
                .lineItem(li)
                .printLocation(pl)
                .dueDate(ord.getDateDue())
                .orderNumber(ord.getInvoiceNumber()).build();
    }

    boolean productionComplete(LineItem li){
        AtomicBoolean ret = new AtomicBoolean(true);
        li.getPrintLocations().forEach(pl->{
            if(pl.getProductionCompleteDate() == null){
                ret.set(false);
            }
        });
        return ret.get();
    }

    public boolean isInteger( String input ) {
        try {
            Integer.parseInt( input );
            return true;
        }
        catch( NumberFormatException e ) {
            return false;
        }
    }
    public JobBoard search(String searchTerm){
        Integer orderNumber = isInteger(searchTerm) ? Integer.parseInt( searchTerm ) : null;
        List<JobCard> orderGarments = new ArrayList<>();
        List<JobCard> prepress = new ArrayList<>();
        List<JobCard> production = new ArrayList<>();
        List<JobCard> delivery = new ArrayList<>();
        this.repo.findAllByCustomer_NameIgnoreCaseContainingOrStatusIgnoreCaseContainingOrInvoiceNumber(searchTerm,searchTerm,orderNumber).forEach(ord-> ord.getItems().forEach(li->{
           if(li.getReceivedDate() == null){
               orderGarments.add(getJobCard(ord,li,null));
           }

           if(li.getReceivedDate() != null && productionComplete(li) && li.getDeliveryReceivedDate() == null){
               delivery.add( getJobCard(ord,li,null));
           }

           li.getPrintLocations().forEach(pl->{
               if(pl.getPrePressCompleteDate() == null || li.getReceivedDate() == null ){
                   prepress.add(getJobCard(ord,li,pl) );
               }else if(pl.getProductionCompleteDate() == null && li.getReceivedDate() != null){
                   production.add(getJobCard(ord,li,pl) );
               }
           });
       }));
        return  JobBoard.builder()
                .delivery(delivery)
                .garmentOrders(orderGarments)
                .prepress(prepress)
                .production(production).build();
    }

}
