package org.demo.ecommerce.order;


import org.demo.ecommerce.category.CategoryDto;
import org.demo.ecommerce.category.CategoryNotFoundException;
import org.demo.ecommerce.model.Category;
import org.demo.ecommerce.model.Product;
import org.demo.ecommerce.model.ProductIdAndQuantity;
import org.demo.ecommerce.model.PurchaseOrder;
import org.demo.ecommerce.model.PurchaseOrderItem;
import org.demo.ecommerce.model.PurchaseOrderStatus;
import org.demo.ecommerce.order.dto.ProductDto;
import org.demo.ecommerce.order.dto.PurchaseOrderCreateDto;
import org.demo.ecommerce.order.dto.PurchaseOrderFinishDto;
import org.demo.ecommerce.order.exception.PurchaseOrderNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PurchaseOrderResource.class)
class PurchaseOrderResourceTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private PurchaseOrderMapper purchaseOrderMapper;

    @MockitoBean
    private PurchaseOrderService purchaseOrderService;

    @Test
    void testGetById() throws Exception {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setId(1L);
        purchaseOrder.setEmail("test@test.com");
        given(purchaseOrderService.findById(eq(1L))).willReturn(purchaseOrder);
        mvc.perform(get("/api/orders/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(purchaseOrder.getId().intValue())))
                .andExpect(jsonPath("$.email", equalTo(purchaseOrder.getEmail())));
    }

    @Test
    void testGetByIdThrowsNotFound() throws Exception {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setId(1L);
        given(purchaseOrderService.findById(eq(1L))).willThrow(new PurchaseOrderNotFoundException("Order not found with 1"));
        mvc.perform(get("/api/orders/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode", equalTo(404)))
                .andExpect(jsonPath("$.message", equalTo("Order not found with 1")));
    }


    @Test
    void testCreate() throws Exception {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setId(1L);
        purchaseOrder.setSeatLetter("A");
        purchaseOrder.setSeatNumber((short) 1);
        purchaseOrder.setEmail("test@test.com");
        given(purchaseOrderMapper.fromCreateDto(any(PurchaseOrderCreateDto.class))).willReturn(purchaseOrder);
        given(purchaseOrderService.create(any(PurchaseOrder.class))).willReturn(purchaseOrder);
        mvc.perform(post("/api/orders").content(purchaseOrderCreateDtoAsJson())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                //.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(purchaseOrder.getId().intValue())))
                .andExpect(jsonPath("$.seatLetter", equalTo(purchaseOrder.getSeatLetter())))
                .andExpect(jsonPath("$.seatNumber", equalTo(Integer.valueOf(purchaseOrder.getSeatNumber()))))
                .andExpect(jsonPath("$.email", equalTo(purchaseOrder.getEmail())));
    }

    @Test
    void testUpdateEmailAndProducts() throws Exception {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setId(1L);
        purchaseOrder.setEmail("test@test.com");
        PurchaseOrderItem purchaseOrderItem = new PurchaseOrderItem();
        purchaseOrderItem.setProduct(new Product());
        purchaseOrderItem.getProduct().setId(1L);
        purchaseOrderItem.setQuantity(1);
        purchaseOrder.setPurchaseOrderItems(Collections.singletonList(purchaseOrderItem));
        given(purchaseOrderMapper.fromProductDto(any(ProductDto.class)))
                .willReturn(new ProductIdAndQuantity(1, 1));
        given(purchaseOrderService.update(anyLong(), anyString(), anySet())).willReturn(purchaseOrder);
        mvc.perform(post("/api/orders/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(purchaseOrderUpdateDtoAsJson()))
                //.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(purchaseOrder.getId().intValue())))
                .andExpect(jsonPath("$.email", equalTo("test@test.com")))
                .andExpect(jsonPath("$.purchaseOrderItems", notNullValue()));
    }

    @Test
    void testCancel() throws Exception {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setId(1L);
        purchaseOrder.setPurchaseOrderStatus(PurchaseOrderStatus.DROPPED);
        given(purchaseOrderService.cancel(eq(1L))).willReturn(purchaseOrder);
        mvc.perform(put("/api/orders/cancel/1").accept(MediaType.APPLICATION_JSON))
                //.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(purchaseOrder.getId().intValue())))
                .andExpect(jsonPath("$.purchaseOrderStatus", equalTo("DROPPED")));
    }

    @Test
    void testFinish() throws Exception {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setId(1L);
        purchaseOrder.setPurchaseOrderStatus(PurchaseOrderStatus.FINISHED);
        given(purchaseOrderMapper.fromFinishDto(any(PurchaseOrderFinishDto.class))).willReturn(purchaseOrder);
        given(purchaseOrderService.finish(any(PurchaseOrder.class))).willReturn(purchaseOrder);
        mvc.perform(put("/api/orders/finish")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(purchaseOrderFinishDtoAsJson()))
                //.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(purchaseOrder.getId().intValue())))
                .andExpect(jsonPath("$.purchaseOrderStatus", equalTo("FINISHED")));
    }

    private String purchaseOrderCreateDtoAsJson() {
        return """ 
               {
                "seatLetter": "A",
                "seatNumber": 20,
                "email": "test@test.com"
               }
               """;
    }

    private String purchaseOrderFinishDtoAsJson() {
        return """ 
               {
                  "id": 1,
                  "paymentStatus": "DONE",
                  "paymentCardToken": "397ede27-5ed3-4a36-800b-8a7fa05c7e4e",
                  "paymentGateway": "payment-gateway-mock",
                  "paymentDate": "2024-12-06T19:12:53Z"
               }
               """;
    }

    private String purchaseOrderUpdateDtoAsJson() {
        return """ 
                {
                  "email": "test@test.com",
                  "id": 1,
                  "products": [
                		{
                			"productId": 1,
                			"quantity": 1
                		}
                	]
                }
               """;
    }
}
