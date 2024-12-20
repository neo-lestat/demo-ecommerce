package org.demo.ecommerce.order;


import org.demo.ecommerce.model.Product;
import org.demo.ecommerce.model.ProductIdAndQuantity;
import org.demo.ecommerce.model.PurchaseOrder;
import org.demo.ecommerce.model.PurchaseOrderItem;
import org.demo.ecommerce.model.PurchaseOrderStatus;
import org.demo.ecommerce.order.exception.ProductWithoutEnoughStockException;
import org.demo.ecommerce.order.exception.UpdateStockException;
import org.demo.ecommerce.order.exception.PurchaseOrderNotFoundException;
import org.demo.ecommerce.product.ProductNotFoundException;
import org.demo.ecommerce.product.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class PurchaseOrderService {

    private PurchaseOrderRepository purchaseOrderRepository;
    private ProductRepository productRepository;

    @Autowired
    public PurchaseOrderService(PurchaseOrderRepository purchaseOrderRepository,
                                ProductRepository productRepository) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public PurchaseOrder findById(Long id) {
        return purchaseOrderRepository.findById(id)
                .orElseThrow(()-> new PurchaseOrderNotFoundException("Order not found with %s".formatted(id)));
    }

    public PurchaseOrder create(PurchaseOrder purchaseOrder) {
        purchaseOrder.setPurchaseOrderStatus(PurchaseOrderStatus.OPEN);
        PurchaseOrder saved = purchaseOrderRepository.save(purchaseOrder);
        return saved;
    }

    public PurchaseOrder update(Long purchaseOrderId, String email, Set<ProductIdAndQuantity> productIds) {

        PurchaseOrder purchaseOrder = findById(purchaseOrderId);
        //todo validate order status
        //PurchaseOrderStatus.OPEN.equals(purchaseOrder.getPurchaseOrderStatus())
        if (email != null && !email.isEmpty()) {
            purchaseOrder.setEmail(email);
        }

        if (purchaseOrder.getPurchaseOrderItems() == null) { //todo is this needed?
            purchaseOrder.setPurchaseOrderItems(new ArrayList<>());
        }

        productIds.forEach(productQuantity -> {
            Product product = findProductById(productQuantity.id());
            if (isThereEnoughStock(product, productQuantity.quantity())) {
                PurchaseOrderItem purchaseOrderItem = new PurchaseOrderItem();
                purchaseOrderItem.setProduct(product);
                purchaseOrderItem.setQuantity(productQuantity.quantity());
                purchaseOrderItem.setAmount(product.getPrice());
                purchaseOrder.getPurchaseOrderItems().add(purchaseOrderItem);
            } else {
                throw new ProductWithoutEnoughStockException("Product (%s) with stock %s is no enough  for quantity %s"
                        .formatted(product.getId(), product.getStock(), productQuantity.quantity()));
            }
        });

        double totalPrice = getTotalPrice(purchaseOrder);
        purchaseOrder.setTotalPrice(totalPrice);
        return purchaseOrder;
    }

    private boolean isThereEnoughStock(Product product, int quantityToTake) {
        return product.getStock() - quantityToTake > 0;
    }

    private Product findProductById(Long id) {
        Optional<Product> productOptional = productRepository.findById(id);
        return productOptional.orElseThrow(()-> new ProductNotFoundException("Product not found with id %s".formatted(id)));
    }

    private double getTotalPrice(PurchaseOrder purchaseOrder) {
        return purchaseOrder.getPurchaseOrderItems()
                .stream()
                .mapToDouble(purchaseOrderItems -> purchaseOrderItems.getAmount() * purchaseOrderItems.getQuantity())
                .sum();
    }

    public PurchaseOrder cancel(Long purchaseOrderId) {
        PurchaseOrder purchaseOrder = findById(purchaseOrderId);
        purchaseOrder.setPurchaseOrderStatus(PurchaseOrderStatus.DROPPED);
        return purchaseOrder;
    }

    public PurchaseOrder finish(PurchaseOrder purchaseOrderFinishData) {
        PurchaseOrder purchaseOrder = findById(purchaseOrderFinishData.getId());
        purchaseOrder.setPaymentCardToken(purchaseOrderFinishData.getPaymentCardToken());
        purchaseOrder.setPaymentDate(purchaseOrderFinishData.getPaymentDate());
        purchaseOrder.setPaymentGateway(purchaseOrderFinishData.getPaymentGateway());
        purchaseOrder.setPaymentStatus(purchaseOrderFinishData.getPaymentStatus());
        purchaseOrder.setPurchaseOrderStatus(PurchaseOrderStatus.FINISHED);
        purchaseOrder.getPurchaseOrderItems()
                .forEach(poi -> {
                    Product product = poi.getProduct();
                    if (product.getStock() - poi.getQuantity() > 0) {
                        product.setStock(product.getStock() - poi.getQuantity());
                    } else {
                        throw new UpdateStockException("Product (%s) with stock %s is no enough  for quantity %s"
                                .formatted(product.getId(), product.getStock(), poi.getQuantity()));
                    }
                });
        return purchaseOrder;
    }
}
