package org.demo.ecommerce.order;


import org.demo.ecommerce.model.ProductIdAndQuantity;
import org.demo.ecommerce.model.PurchaseOrder;
import org.demo.ecommerce.order.dto.ProductDto;
import org.demo.ecommerce.order.dto.PurchaseOrderFinishDto;
import org.demo.ecommerce.order.dto.PurchaseOrderUpdateDto;
import org.demo.ecommerce.order.dto.PurchaseOrderCreateDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class PurchaseOrderResource {

    private PurchaseOrderMapper purchaseOrderMapper;
    private PurchaseOrderService purchaseOrderService;

    @Autowired
    public PurchaseOrderResource(PurchaseOrderMapper purchaseOrderMapper, PurchaseOrderService purchaseOrderService) {
        this.purchaseOrderMapper = purchaseOrderMapper;
        this.purchaseOrderService = purchaseOrderService;
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public PurchaseOrder getById(@PathVariable Long id) {
        return purchaseOrderService.findById(id);
    }

    @PostMapping(produces = "application/json")
    public PurchaseOrder create(@Valid @RequestBody PurchaseOrderCreateDto purchaseOrderCreateDto) {
        PurchaseOrder purchaseOrder = purchaseOrderMapper.fromCreateDto(purchaseOrderCreateDto);
        return purchaseOrderService.create(purchaseOrder);
    }

    @PostMapping(path = "/update", produces = "application/json")
    public PurchaseOrder updateEmailAndProducts(@Valid @RequestBody PurchaseOrderUpdateDto purchaseOrderUpdateDto) {
        Set<ProductIdAndQuantity> products = purchaseOrderUpdateDto.getProducts().stream()
                .map(productDto -> purchaseOrderMapper.fromProductDto(productDto))
                .collect(Collectors.toSet());
        return purchaseOrderService.update(purchaseOrderUpdateDto.getId(), purchaseOrderUpdateDto.getEmail(), products);
    }

    @PutMapping(path = "/cancel/{id}", produces = "application/json")
    public PurchaseOrder cancel(@PathVariable Long id) {
        return purchaseOrderService.cancel(id);
    }

    @PutMapping(path = "/finish", produces = "application/json")
    public PurchaseOrder finish(@Valid @RequestBody PurchaseOrderFinishDto purchaseOrderFinishDto) {
        PurchaseOrder purchaseOrder = purchaseOrderMapper.fromFinishDto(purchaseOrderFinishDto);
        return purchaseOrderService.finish(purchaseOrder);
    }

}
