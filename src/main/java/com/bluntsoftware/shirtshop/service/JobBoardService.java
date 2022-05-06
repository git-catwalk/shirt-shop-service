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
        return JobCard.builder()
                .description(li.getDescription())
                .name(li.getStyleNumber() + "-" + li.getBrand() + " - " + li.getGarmentColor().getName())
                .invoiceId(ord.getId())
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

    public JobBoard get(){
        List<JobCard> orderGarments = new ArrayList<>();
        List<JobCard> prepress = new ArrayList<>();
        List<JobCard> production = new ArrayList<>();
        List<JobCard> delivery = new ArrayList<>();
        this.repo.findAll().forEach(ord-> ord.getItems().forEach(li->{
           if(li.getReceivedDate() == null){
               orderGarments.add(getJobCard(ord,li,null));
           }

           if(li.getReceivedDate() != null && productionComplete(li)){
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
