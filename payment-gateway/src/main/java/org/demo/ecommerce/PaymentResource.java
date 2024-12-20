package org.demo.ecommerce;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/payment")
public class PaymentResource {

    @GetMapping(produces = "application/json")
    public Payment doPay() {
        Payment payment = new Payment();
        payment.setDate(ZonedDateTime.now());
        payment.setStatus("ok");
        payment.setGateway("payment-gateway-mock");
        payment.setCardToken(UUID.randomUUID().toString());
        return payment;
    }

}
