package org.demo.ecommerce.order;

import org.demo.ecommerce.model.PaymentStatus;
import org.demo.ecommerce.model.PurchaseOrder;
import org.demo.ecommerce.order.dto.PurchaseOrderCreateDto;
import org.demo.ecommerce.order.dto.PurchaseOrderFinishDto;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PurchaseOrderMapperTest {

    private PurchaseOrderMapper purchaseOrderMapper = new PurchaseOrderMapper();

    @Test
    void fromCreateDto() {
        PurchaseOrderCreateDto purchaseOrderCreateDto = new PurchaseOrderCreateDto();
        purchaseOrderCreateDto.setEmail("test@test.com");
        purchaseOrderCreateDto.setSeatNumber((short) 1);
        purchaseOrderCreateDto.setSeatLetter("A");
        PurchaseOrder purchaseOrder = purchaseOrderMapper.fromCreateDto(purchaseOrderCreateDto);
        assertThat(purchaseOrder)
                .hasFieldOrPropertyWithValue("email", "test@test.com")
                .hasFieldOrPropertyWithValue("seatNumber", (short) 1)
                .hasFieldOrPropertyWithValue("seatLetter", "A");
    }

    @Test
    void fromFinishDto() {
        PurchaseOrderFinishDto purchaseOrderFinishDto = new PurchaseOrderFinishDto();
        purchaseOrderFinishDto.setId(1L);
        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        purchaseOrderFinishDto.setPaymentDate(zonedDateTime);
        purchaseOrderFinishDto.setPaymentStatus(PaymentStatus.DONE.name());
        purchaseOrderFinishDto.setPaymentCardToken("card-token");
        purchaseOrderFinishDto.setPaymentGateway("payment-gateway");
        PurchaseOrder purchaseOrder = purchaseOrderMapper.fromFinishDto(purchaseOrderFinishDto);
        assertThat(purchaseOrder)
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("paymentDate", zonedDateTime)
                .hasFieldOrPropertyWithValue("paymentStatus", PaymentStatus.DONE)
                .hasFieldOrPropertyWithValue("paymentCardToken", "card-token")
                .hasFieldOrPropertyWithValue("paymentGateway", "payment-gateway");
    }
}
