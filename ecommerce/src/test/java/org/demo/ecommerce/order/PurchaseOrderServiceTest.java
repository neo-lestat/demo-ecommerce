package org.demo.ecommerce.order;

import org.demo.ecommerce.model.PaymentStatus;
import org.demo.ecommerce.model.Product;
import org.demo.ecommerce.model.ProductIdAndQuantity;
import org.demo.ecommerce.model.PurchaseOrder;
import org.demo.ecommerce.model.PurchaseOrderItem;
import org.demo.ecommerce.model.PurchaseOrderStatus;
import org.demo.ecommerce.order.exception.ProductWithoutEnoughStockException;
import org.demo.ecommerce.order.exception.PurchaseOrderNotFoundException;
import org.demo.ecommerce.order.exception.UpdateStockException;
import org.demo.ecommerce.product.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PurchaseOrderServiceTest {

    @Mock
    private PurchaseOrderRepository purchaseOrderRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private PurchaseOrderService purchaseOrderService;

    @Test
    void testFindByIdReturnsPurchaseOrder() {
        Optional<PurchaseOrder> purchaseOrderOptional = Optional.of(new PurchaseOrder());
        when(purchaseOrderRepository.findById(anyLong())).thenReturn(purchaseOrderOptional);
        PurchaseOrder purchaseOrder = purchaseOrderService.findById(1L);
        assertEquals(purchaseOrderOptional.get(), purchaseOrder);
    }

    @Test
    void testFindByIdThrowsPurchaseOrderNotFound() {
        when(purchaseOrderRepository.findById(anyLong())).thenThrow(new PurchaseOrderNotFoundException("PurchaseOrder not found with 1"));
        PurchaseOrderNotFoundException thrown = Assertions.assertThrows(PurchaseOrderNotFoundException.class, () -> {
            purchaseOrderService.findById(1L);
        });
        Assertions.assertEquals("PurchaseOrder not found with 1", thrown.getMessage());
    }
    @Test
    void testCreatePurchaseOrder() {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        when(purchaseOrderRepository.save(any(PurchaseOrder.class))).thenReturn(purchaseOrder);
        PurchaseOrder purchaseOrderSaved = purchaseOrderService.create(purchaseOrder);
        assertEquals(purchaseOrder, purchaseOrderSaved);
        assertEquals(PurchaseOrderStatus.OPEN, purchaseOrderSaved.getPurchaseOrderStatus());
    }

    @Test
    void testUpdatePurchaseOrder() {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        Product productOne = new Product();
        productOne.setId(1L);
        productOne.setStock(10);
        productOne.setPrice(2.0);
        Product productTwo = new Product();
        productTwo.setId(2L);
        productTwo.setStock(10);
        productTwo.setPrice(5.0);
        when(purchaseOrderRepository.findById(eq(1L))).thenReturn(Optional.of(purchaseOrder));
        when(productRepository.findById(eq(1L))).thenReturn(Optional.of(productOne));
        when(productRepository.findById(eq(2L))).thenReturn(Optional.of(productTwo));
        Set<ProductIdAndQuantity> products = new HashSet<>();
        products.add(new ProductIdAndQuantity(1, 1));
        products.add(new ProductIdAndQuantity(2, 3));
        PurchaseOrder purchaseOrderUpdated = purchaseOrderService.update(1L, "test@test.com", products);
        assertEquals(2, purchaseOrderUpdated.getPurchaseOrderItems().size());
        assertEquals("test@test.com", purchaseOrderUpdated.getEmail());
        assertEquals(productOne, purchaseOrderUpdated.getPurchaseOrderItems().get(0).getProduct());
        assertEquals(1, purchaseOrderUpdated.getPurchaseOrderItems().get(0).getQuantity());
        assertEquals(2.0, purchaseOrderUpdated.getPurchaseOrderItems().get(0).getAmount());
        assertEquals(productTwo, purchaseOrderUpdated.getPurchaseOrderItems().get(1).getProduct());
        assertEquals(3, purchaseOrderUpdated.getPurchaseOrderItems().get(1).getQuantity());
        assertEquals(5.0, purchaseOrderUpdated.getPurchaseOrderItems().get(1).getAmount());
        assertEquals(17.0, purchaseOrderUpdated.getTotalPrice());
    }

    @Test
    void testUpdatePurchaseOrderThrowsNotEnoughStock() {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        Product productOne = new Product();
        productOne.setId(1L);
        productOne.setStock(4);
        productOne.setPrice(2.0);
        when(purchaseOrderRepository.findById(eq(1L))).thenReturn(Optional.of(purchaseOrder));
        when(productRepository.findById(eq(1L))).thenReturn(Optional.of(productOne));
        Set<ProductIdAndQuantity> products = new HashSet<>();
        products.add(new ProductIdAndQuantity(1, 5));
        ProductWithoutEnoughStockException thrown = Assertions.assertThrows(ProductWithoutEnoughStockException.class, () -> {
            purchaseOrderService.update(1L, "test@test.com", products);
        });
        Assertions.assertEquals("Product (1) with stock 4 is no enough  for quantity 5", thrown.getMessage());
    }

    @Test
    void testCancelPurchaseOrder() {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        Optional<PurchaseOrder> purchaseOrderOptional = Optional.of(purchaseOrder);
        when(purchaseOrderRepository.findById(eq(1L))).thenReturn(purchaseOrderOptional);
        PurchaseOrder purchaseOrderCancel = purchaseOrderService.cancel(1L);
        assertEquals(purchaseOrder, purchaseOrderCancel);
        assertEquals(PurchaseOrderStatus.DROPPED, purchaseOrderCancel.getPurchaseOrderStatus());
    }

    @Test
    void testFinishPurchaseOrder() {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setId(1L);
        purchaseOrder.setPaymentGateway("payment-gateway");
        purchaseOrder.setPaymentCardToken("card-token");
        purchaseOrder.setPaymentStatus(PaymentStatus.DONE);
        purchaseOrder.setPaymentDate(ZonedDateTime.now());
        PurchaseOrderItem purchaseOrderItem = new PurchaseOrderItem();
        purchaseOrderItem.setId(1L);
        Product product = new Product();
        product.setId(1L);
        product.setStock(10);
        purchaseOrderItem.setProduct(product);
        purchaseOrderItem.setQuantity(2);
        purchaseOrderItem.setAmount(1);
        purchaseOrder.setPurchaseOrderItems(Collections.singletonList(purchaseOrderItem));
        when(purchaseOrderRepository.findById(eq(1L))).thenReturn(Optional.of(purchaseOrder));
        PurchaseOrder purchaseOrderFinished = purchaseOrderService.finish(purchaseOrder);
        assertEquals(PurchaseOrderStatus.FINISHED, purchaseOrderFinished.getPurchaseOrderStatus());
        assertEquals(purchaseOrder.getId(), purchaseOrderFinished.getId());
        assertEquals(purchaseOrder.getPaymentGateway(), purchaseOrderFinished.getPaymentGateway());
        assertEquals(purchaseOrder.getPaymentCardToken(), purchaseOrderFinished.getPaymentCardToken());
        assertEquals(purchaseOrder.getPaymentStatus(), purchaseOrderFinished.getPaymentStatus());
        assertEquals(purchaseOrder.getPaymentDate(), purchaseOrderFinished.getPaymentDate());
        assertEquals(8, purchaseOrder.getPurchaseOrderItems().get(0).getProduct().getStock());
    }

    @Test
    void testFinishPurchaseOrderThrowsUpdateStockException() {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setId(1L);
        purchaseOrder.setPaymentGateway("payment-gateway");
        purchaseOrder.setPaymentCardToken("card-token");
        purchaseOrder.setPaymentStatus(PaymentStatus.DONE);
        purchaseOrder.setPaymentDate(ZonedDateTime.now());
        PurchaseOrderItem purchaseOrderItem = new PurchaseOrderItem();
        purchaseOrderItem.setId(1L);
        Product product = new Product();
        product.setId(1L);
        product.setStock(4);
        purchaseOrderItem.setProduct(product);
        purchaseOrderItem.setQuantity(5);
        purchaseOrderItem.setAmount(1);
        purchaseOrder.setPurchaseOrderItems(Collections.singletonList(purchaseOrderItem));
        when(purchaseOrderRepository.findById(eq(1L))).thenReturn(Optional.of(purchaseOrder));
        UpdateStockException thrown = Assertions.assertThrows(UpdateStockException.class, () -> {
            purchaseOrderService.finish(purchaseOrder);
        });
        Assertions.assertEquals("Product (1) with stock 4 is no enough  for quantity 5", thrown.getMessage());
    }
}
