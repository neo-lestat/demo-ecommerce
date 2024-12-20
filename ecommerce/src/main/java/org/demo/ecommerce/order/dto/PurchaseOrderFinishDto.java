package org.demo.ecommerce.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class PurchaseOrderFinishDto {

    @NotNull
    private Long id;
    @NotBlank
    private String paymentStatus;
    @NotBlank
    private String paymentCardToken;
    @NotBlank
    private String paymentGateway;
    @NotNull
    private ZonedDateTime paymentDate;

}
