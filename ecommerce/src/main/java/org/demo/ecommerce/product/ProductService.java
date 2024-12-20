package org.demo.ecommerce.product;


import org.demo.ecommerce.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Transactional(readOnly = true)
    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(()-> new ProductNotFoundException("Product not found with %s".formatted(id)));
    }

    @Transactional(readOnly = true)
    public Page<Product> getProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Product create(Product product) {
        Product saved = productRepository.save(product);
        return saved;
    }

    public Product update(Product productDataUpdate) {
        Product product = findById(productDataUpdate.getId());
        product.setName(productDataUpdate.getName());
        product.setCategory(productDataUpdate.getCategory());
        product.setPrice(productDataUpdate.getPrice());
        product.setStock(productDataUpdate.getStock());
        product.setImageUrl(productDataUpdate.getImageUrl());
        return product;
    }

}
