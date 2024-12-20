package org.demo.ecommerce.product;

import org.demo.ecommerce.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ProductResource.class)
class ProductResourceTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private ProductMapper productMapper;

    @MockitoBean
    private ProductService productService;


    @Test
    void testGetById() throws Exception {
        Product product = new Product();
        product.setId(1L);
        product.setName("pizza");
        given(productService.findById(eq(1L))).willReturn(product);
        mvc.perform(get("/api/products/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(product.getId().intValue())))
                .andExpect(jsonPath("$.name", equalTo(product.getName())));
    }

    @Test
    void testGetByIdThrowsNotFound() throws Exception {
        Product product = new Product();
        product.setId(1L);
        product.setName("pizza");
        given(productService.findById(eq(1L))).willThrow(new ProductNotFoundException("Product not found with 1"));
        mvc.perform(get("/api/products/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode", equalTo(404)))
                .andExpect(jsonPath("$.message", equalTo("Product not found with 1")));
    }


    @Test
    void testGetAll() throws Exception {
        Product product = new Product();
        product.setId(1L);
        product.setName("pizza");
        Page<Product> productPage = new PageImpl<>(List.of(product), PageRequest.of(0, 1), 1);
        given(productService.getProducts(any(Pageable.class))).willReturn(productPage);
        mvc.perform(get("/api/products/all?page=0&size=1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.totalElements", equalTo(1)))
                .andExpect(jsonPath("$.totalPages", equalTo(1)))
                .andExpect(jsonPath("$.pageSize", equalTo(1)))
                .andExpect(jsonPath("$.pageNumber", equalTo(0)));

    }

    @Test
    void testCreate() throws Exception {
        Product product = new Product();
        product.setId(1L);
        product.setName("pizza");
        given(productMapper.fromProductDto(any(ProductDto.class))).willReturn(product);
        given(productService.create(any(Product.class))).willReturn(product);
        mvc.perform(post("/api/products").content(productDtoAsJson())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                //.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(product.getId().intValue())))
                .andExpect(jsonPath("$.name", equalTo(product.getName())));
    }

    @Test
    void testUpdate() throws Exception {
        Product product = new Product();
        product.setId(1L);
        product.setName("pizza");
        given(productMapper.fromProductDto(any(ProductDto.class))).willReturn(product);
        given(productService.update(any(Product.class))).willReturn(product);
        mvc.perform(put("/api/products").content(productDtoAsJson())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                //.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(product.getId().intValue())))
                .andExpect(jsonPath("$.name", equalTo(product.getName())));
    }

    private String productDtoAsJson() {
        return """
                {
                "id": 1,
                "name":"chease cake",
                "price": 5,
                "stock": 40,
                "imageUrl": "test",
                "categoryId": 2
                }
                """;
    }

}
