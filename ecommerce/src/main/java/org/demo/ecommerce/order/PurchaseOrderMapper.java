package org.demo.ecommerce.order;

import org.demo.ecommerce.model.PaymentStatus;
import org.demo.ecommerce.model.ProductIdAndQuantity;
import org.demo.ecommerce.model.PurchaseOrder;
import org.demo.ecommerce.order.dto.ProductDto;
import org.demo.ecommerce.order.dto.PurchaseOrderFinishDto;
import org.demo.ecommerce.order.dto.PurchaseOrderCreateDto;
import org.springframework.stereotype.Component;


@Component
public class PurchaseOrderMapper {

    public PurchaseOrder fromCreateDto(PurchaseOrderCreateDto purchaseOrderCreateDto) {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setEmail(purchaseOrderCreateDto.getEmail());
        purchaseOrder.setSeatLetter(purchaseOrderCreateDto.getSeatLetter());
        purchaseOrder.setSeatNumber(purchaseOrderCreateDto.getSeatNumber());
        return purchaseOrder;
    }

    public PurchaseOrder fromFinishDto(PurchaseOrderFinishDto purchaseOrderFinishDto) {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setId(purchaseOrderFinishDto.getId());
        purchaseOrder.setPaymentCardToken(purchaseOrderFinishDto.getPaymentCardToken());
        purchaseOrder.setPaymentDate(purchaseOrderFinishDto.getPaymentDate());
        purchaseOrder.setPaymentStatus(PaymentStatus.valueOf(purchaseOrderFinishDto.getPaymentStatus()));
        purchaseOrder.setPaymentGateway(purchaseOrderFinishDto.getPaymentGateway());
        return purchaseOrder;
    }

    public ProductIdAndQuantity fromProductDto(ProductDto productDto) {
        return new ProductIdAndQuantity(productDto.getProductId(), productDto.getQuantity());
    }
}
