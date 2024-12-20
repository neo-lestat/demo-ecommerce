package org.demo.ecommerce.product;

import org.demo.ecommerce.model.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void testFindByIdReturnsProduct() {
        Optional<Product> productOptional = Optional.of(new Product());
        when(productRepository.findById(anyLong())).thenReturn(productOptional);
        Product product = productService.findById(1L);
        assertEquals(productOptional.get(), product);
    }

    @Test
    void testFindByIdThrowsProductNotFound() {
        when(productRepository.findById(anyLong())).thenThrow(new ProductNotFoundException("Product not found with 1"));
        ProductNotFoundException thrown = Assertions.assertThrows(ProductNotFoundException.class, () -> {
            productService.findById(1L);
        });
        Assertions.assertEquals("Product not found with 1", thrown.getMessage());
    }

    @Test
    void testGetProducts() {
        Product product = new Product();
        Page<Product> productPage = new PageImpl<>(Collections.singletonList(product));
        when(productRepository.findAll(any(PageRequest.class))).thenReturn(productPage);
        Pageable paging = PageRequest.of(0, 1);
        Page<Product> result = productService.getProducts(paging);
        assertEquals(productPage, result);
    }

    @Test
    void testCreateProduct() {
        Product product = new Product();
        when(productRepository.save(any(Product.class))).thenReturn(product);
        Product productSaved = productService.create(product);
        assertEquals(product, productSaved);
    }

    @Test
    void testUpdateProduct() {
        Product product = new Product();
        product.setId(1L);
        Optional<Product> productOptional = Optional.of(product);
        when(productRepository.findById(anyLong())).thenReturn(productOptional);
        Product productUpdated = productService.update(product);
        assertEquals(product, productUpdated);
    }

}
