package org.demo.ecommerce;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class Payment {

    private String cardToken;
    private String status;
    private ZonedDateTime date;
    private String gateway;

}
