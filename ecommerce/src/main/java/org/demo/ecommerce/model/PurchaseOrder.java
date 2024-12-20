package org.demo.ecommerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "purchase_order")
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private PurchaseOrderStatus purchaseOrderStatus;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    //@JoinColumn(name = "purchase_order_item_id")
    private List<PurchaseOrderItem> purchaseOrderItems;

    //Buyer details
    //constraint : seatLetter-seatNumber?
    @NotBlank
    private String email;
    @NotBlank
    private String seatLetter;
    @NotNull
    private short seatNumber;

    //Payment details
    private double totalPrice;
    private String paymentCardToken;
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
    private ZonedDateTime paymentDate;
    private String paymentGateway;

}
