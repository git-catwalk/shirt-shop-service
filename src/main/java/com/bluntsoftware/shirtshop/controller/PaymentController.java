package com.bluntsoftware.shirtshop.controller;

import com.bluntsoftware.shirtshop.service.OrderService;
import com.bluntsoftware.shirtshop.tenant.TenantUser;
import com.bluntsoftware.shirtshop.tenant.TenantUserService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    private final OrderService orderService;

    public PaymentController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/deposit-url")
    Map<String,Object> getDepositDetailsUrl(){
        Map<String,Object> details = new HashMap<>();
        Optional<TenantUser> optionalTenantUser = TenantUserService.getUser();
        TenantUser tenantUser = optionalTenantUser.orElseThrow(()->new RuntimeException("Tenant user not found"));
        return details;
    }

    @GetMapping("/payment-url")
    Map<String,Object> getPaymentDetailsUrl(){
        Map<String,Object> details = new HashMap<>();
        Optional<TenantUser> optionalTenantUser = TenantUserService.getUser();
        TenantUser tenantUser = optionalTenantUser.orElseThrow(()->new RuntimeException("Tenant user not found"));

        return details;
    }

    @GetMapping("order/deposit/{tenantId}/{orderId}")
    void orderMinimumDeposit(@PathVariable("tenantId") String tenantId,@PathVariable("orderId") String orderId ){
        System.out.println("deposit made");
    }

    @GetMapping("order/pay/{tenantId}/{orderId}")
    void orderPay(@PathVariable("tenantId") String tenantId,@PathVariable("orderId") String orderId ){


        System.out.println("payment made");
    }

    @GetMapping("estimate/deposit/{tenantId}/{orderId}")
    void estimateMinimumDeposit(@PathVariable("tenantId") String tenantId,@PathVariable("orderId") String orderId ){
        System.out.println("deposit made");
    }

    @GetMapping("estimate/pay/{tenantId}/{orderId}")
    void estimatePay(@PathVariable("tenantId") String tenantId,@PathVariable("orderId") String orderId ){
        System.out.println("payment made");
    }

}
