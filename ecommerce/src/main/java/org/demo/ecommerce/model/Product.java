package org.demo.ecommerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
//todo add version for optimistic locking or synchronize the method purchaseOrderService.update??
@Data
@Entity
@Table(name = "product",
        uniqueConstraints = { @UniqueConstraint(columnNames = {"name"})})
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotBlank(message = "Name is mandatory")
    private String name;
    private Double price;
    private String imageUrl;
    private int stock;
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false, updatable = false)
    private Category category;


}
