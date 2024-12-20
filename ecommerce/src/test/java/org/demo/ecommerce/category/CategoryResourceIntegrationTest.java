package org.demo.ecommerce.category;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.demo.ecommerce.commons.GlobalExceptionHandler;
import org.demo.ecommerce.model.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;


import java.net.URISyntaxException;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CategoryResourceIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private HttpHeaders headers;
    private String urlBase;

    @BeforeEach
    void setUp() throws URISyntaxException {
        headers = new HttpHeaders();
        headers.add("api-token", "test-token");
        headers.setContentType(MediaType.APPLICATION_JSON);
        urlBase = "http://localhost:%s/api/categories".formatted(port);
    }

    @Test
    @Order(1)
    void testCreate() {
        //add 2 categories, first one will be the parent category
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("food");
        HttpEntity<CategoryDto> request = new HttpEntity<>(categoryDto, headers);
        ResponseEntity<Category> response = restTemplate.postForEntity(urlBase, request, Category.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        Long categoryId = response.getBody().getId();
        assertNotNull(categoryId);
        assertNull(response.getBody().getParentId());
        assertEquals(categoryDto.getName(), response.getBody().getName());

        CategoryDto categoryDtoTwo = new CategoryDto();
        categoryDtoTwo.setName("pizza");
        categoryDtoTwo.setParentId(response.getBody().getId());
        request = new HttpEntity<>(categoryDtoTwo, headers);
        response = restTemplate.postForEntity(urlBase, request, Category.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());
        assertEquals(categoryId, response.getBody().getParentId());
        assertEquals(categoryDtoTwo.getName(), response.getBody().getName());

    }

    @Test
    @Order(2)
    void testGetById()  {
        //get category with id 2 from test create
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Category> response = restTemplate.exchange(urlBase + "/2",
                HttpMethod.GET, entity, Category.class);
        assertThat(response.getStatusCode().value(), is(200));
        assertNotNull(response.getBody());
        Category category = response.getBody();
        assertThat(category.getParentId(), is(1L));
        assertThat(category.getId(), is(2L));
        assertThat(category.getName(), is("pizza"));
    }

    @Test
    @Order(3)
    void testGetAll() throws Exception {
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(urlBase + "/all?page=0&size=5",
                HttpMethod.GET, entity, String.class);
        assertThat(response.getStatusCode().value(), is(200));
        Map<String, Object> objectMap = objectMapper.readValue(response.getBody(), Map.class);
        assertThat(objectMap.get("pageNumber"), is(0));
        assertThat(objectMap.get("pageSize"), is(5));
        assertThat(objectMap.get("totalPages"), is(1));
        assertThat(objectMap.get("totalPages"), is(1));
        assertThat(objectMap.get("content"), notNullValue());
    }

    @Test
    @Order(4)
    void testGetByIdReturnsForbidden()  {
        headers.set("api-token", "");
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<GlobalExceptionHandler.ErrorMessage> response = restTemplate.exchange(urlBase + "/2",
                HttpMethod.GET, entity, GlobalExceptionHandler.ErrorMessage.class);
        assertThat(response.getStatusCode().value(), is(403));
        assertNotNull(response.getBody());
        GlobalExceptionHandler.ErrorMessage errorMessage = response.getBody();

        assertThat(errorMessage.statusCode(), is(403));
        assertThat(errorMessage.message(), is("Invalid token"));
    }


}
