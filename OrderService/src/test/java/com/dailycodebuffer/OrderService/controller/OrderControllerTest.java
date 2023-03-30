package com.dailycodebuffer.OrderService.controller;

import com.dailycodebuffer.OrderService.entity.Order;
import com.dailycodebuffer.OrderService.model.OrderRequest;
import com.dailycodebuffer.OrderService.model.PaymentMode;
import com.dailycodebuffer.OrderService.repository.OrderRepository;
import com.dailycodebuffer.OrderService.service.OrderService;
import com.dailycodebuffer.OrderService.service.OrderServiceConfig;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Optional;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.util.StreamUtils.copyToString;


@SpringBootTest({"server.port=0"})
@EnableConfigurationProperties
@AutoConfigureMockMvc
@ContextConfiguration(classes = {OrderServiceConfig.class})
public class OrderControllerTest {

    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    MockMvc mockMvc;

    @RegisterExtension
    static WireMockExtension wireMockServer =
            WireMockExtension.newInstance()
                    .options(WireMockConfiguration
                            .wireMockConfig()
                            .port(8086))
                    .build();
    private ObjectMapper objectMapper =
            new ObjectMapper()
                    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    @BeforeEach
    void setup() throws IOException {
        getProductDetailsResponse();
        doPayment();
        getPaymentDetails();
        reduceQuantity();
    }

    private void reduceQuantity() {
        wireMockServer.stubFor( put(urlMatching("/product/reduceQuantity/.*"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)));
    }

    private void getPaymentDetails() throws IOException {
        wireMockServer.stubFor(get(urlMatching("/payment/.*"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(
                            copyToString(
                                    OrderController.class
                                            .getClassLoader()
                                            .getResourceAsStream("mock/getPayment.json"),
                                    Charset.defaultCharset()
                            )
                        )));
    }

    private void doPayment() {
        wireMockServer.stubFor(post(urlEqualTo("/payment"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)));
    }

    private void getProductDetailsResponse() throws IOException {
        wireMockServer.stubFor(get("/product/1")
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(copyToString(
                                OrderController.class
                                        .getClassLoader()
                                        .getResourceAsStream("mock/getProduct.json"),
                                Charset.defaultCharset()
                        ))));
    }

    @Disabled
    @Test
    public void test_whenplaceOrder_Dopayment_Success() throws Exception {
        // first place order
        OrderRequest orderRequest = getMockOrderRequest();
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.post("/order/placeOrder")
                        .with(jwt().authorities( new SimpleGrantedAuthority("Customer")))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(orderRequest))
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String orderId = mvcResult.getResponse().getContentAsString();

        // Get order by order id from DB and check
        Optional<Order> order = orderRepository.findById(Long.valueOf(orderId));
        assertTrue(order.isPresent());

        // check output
        Order o = order.get();
        assertEquals(Long.valueOf(orderId), o.getId());
        assertEquals("PLACED", o.getOrderStatus());
        assertEquals(orderRequest.getTotalAmount(), o.getAmount());
        assertEquals(orderRequest.getQuantity(), o.getQuantity());
    }

    private OrderRequest getMockOrderRequest() {
        return OrderRequest.builder()
                .productId(1)
                .paymentMode(PaymentMode.CASH)
                .totalAmount(200)
                .quantity(200)
                .build();
    }
}