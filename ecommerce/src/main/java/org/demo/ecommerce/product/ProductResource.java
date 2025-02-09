package org.demo.ecommerce.product;


import org.demo.ecommerce.commons.PaginatedResponse;
import org.demo.ecommerce.model.Product;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductResource {

    private ProductMapper productMapper;
    private ProductService productService;

    @Autowired
    public ProductResource(ProductMapper productMapper, ProductService productService) {
        this.productMapper = productMapper;
        this.productService = productService;
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public Product getById(@PathVariable Long id) {
        return productService.findById(id);
    }

    @GetMapping(value = "/all", params = {"page", "size"})
    public PaginatedResponse<Product> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Pageable paging = PageRequest.of(page, size);
        Page<Product> products = productService.getProducts(paging);
        PaginatedResponse<Product> response = new PaginatedResponse<>();
        response.setContent(products.getContent());
        response.setPageNumber(products.getNumber());
        response.setPageSize(products.getSize());
        response.setTotalElements(products.getTotalElements());
        response.setTotalPages(products.getTotalPages());
        return response;
    }

    @PostMapping(produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Product create(@Valid @RequestBody ProductDto productDto) {
        Product product = productMapper.fromProductDto(productDto);
        return productService.create(product);
    }

    @PutMapping(produces = "application/json")
    public Product update(@Valid @RequestBody ProductDto productDto) {
        Product product = productMapper.fromProductDto(productDto);
        return productService.update(product);
    }

}
