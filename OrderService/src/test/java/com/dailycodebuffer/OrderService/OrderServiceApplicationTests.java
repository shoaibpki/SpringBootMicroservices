package com.dailycodebuffer.OrderService;

import static com.dailycodebuffer.OrderService.model.PaymentMode.CASH;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Optional;

import com.dailycodebuffer.OrderService.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import com.dailycodebuffer.OrderService.entity.Order;
import com.dailycodebuffer.OrderService.exception.CustomException;
import com.dailycodebuffer.OrderService.external.client.PaymentService;
import com.dailycodebuffer.OrderService.external.client.ProductService;
import com.dailycodebuffer.OrderService.external.request.PaymentRequest;
import com.dailycodebuffer.OrderService.repository.OrderRepository;
import com.dailycodebuffer.OrderService.service.OrderService;
import com.dailycodebuffer.OrderService.service.OrderServiceImpl;

@SpringBootTest
class OrderServiceApplicationTests {

	@Mock
	OrderRepository orderRepository;
	@Mock
	ProductService productService;

	@Mock
	PaymentService paymentService;

	@Mock
	RestTemplate restTemplate;

	@Value("${microservices.product}")
	private String productServiceUrl;

	@Value("${microservices.payment}")
	private String paymentServiceUrl;

	@InjectMocks
	OrderService orderService = new OrderServiceImpl();

	@BeforeEach
	public void setup(){
		ReflectionTestUtils
				.setField(orderService, "productServiceUrl", productServiceUrl);
		ReflectionTestUtils
				.setField(orderService, "paymentServiceUrl", paymentServiceUrl);
	}
	@DisplayName("Get order - Success Scenario")
	@Test
	void test_when_order_Success() {

//		Mocking
		Order order = getMockOrder();
		when(orderRepository.findById(anyLong()))
				.thenReturn(Optional.of(order));
		when(restTemplate.getForObject(
				productServiceUrl + order.getProductId(),
				ProductResponse.class
		)).thenReturn(getMockProductResponse());
		when(restTemplate.getForObject(
				paymentServiceUrl + "order/" + order.getId(),
				PaymentResponse.class
		)).thenReturn(getMockPaymentResponse());

//		Acual
		OrderResponse orderResponse = orderService.getOrderDetails(1);

//		Verify
		verify(orderRepository,times(1)).findById(anyLong());
		verify(restTemplate, times(1)).getForObject(
				productServiceUrl + order.getProductId(),
				ProductResponse.class
		);
		verify(restTemplate, times(1)).getForObject(
				paymentServiceUrl + "order/" + order.getId(),
				PaymentResponse.class
		);

//		Assert
		assertNotNull(orderResponse);
		assertEquals(order.getId(), orderResponse.getOrderId());
	}

	private PaymentResponse getMockPaymentResponse() {
		return PaymentResponse.builder()
				.paymentId(1)
				.paymentDate(Instant.now())
				.orderId(1)
				.paymentMode(PaymentMode.CASH)
				.status("ACCEPTED")
				.amount(100)
				.build();
	}

	@DisplayName("Get Orders - Failure Scenario")
	@Test
	void when_order_not_found(){
		when(orderRepository.findById(anyLong()))
				.thenReturn(Optional.ofNullable(null));
		CustomException exception =
				assertThrows(CustomException.class,
						() -> orderService.getOrderDetails(1));
		assertEquals("NOT_FOUND", exception.getErrorCode());
		assertEquals(404, exception.getStatus());

		verify(orderRepository, times(1))
				.findById(anyLong());
	}
	private ProductResponse getMockProductResponse() {
		return ProductResponse.builder()
				.productId(2)
				.productName("MacBook")
				.price(100)
				.quantity(200)
				.build();
	}
	private Order getMockOrder() {
		Order order = Order.builder()
				.id(1)
				.orderDate(Instant.now())
				.orderStatus("PLACED")
				.quantity(1)
				.amount(100)
				.productId(2)
				.build();
		return order;
	}

	@DisplayName("Place Order - Success Scenario")
	@Test
	void when_orderPlace_success(){
		Order order = getMockOrder();
		OrderRequest orderRequest = getMockOrderRequest();

		when(orderRepository.save(any(Order.class)))
				.thenReturn(order);
		when(productService.reduceQuantity(anyLong(),anyLong()))
				.thenReturn(new ResponseEntity<Void>(HttpStatus.OK));
		when(paymentService.doPayment(any(PaymentRequest.class)))
				.thenReturn(new ResponseEntity<Long>(1L, HttpStatus.OK));
		long orderId = orderService.placeOrder(orderRequest);

		verify(orderRepository, times(2))
				.save(any());
		verify(paymentService, times(1))
				.doPayment(any(PaymentRequest.class));
		verify(productService, times(1))
				.reduceQuantity(anyLong(), anyLong());
		assertEquals(orderId, order.getId());
	}

	private OrderRequest getMockOrderRequest() {

		return OrderRequest.builder()
				.productId(1)
				.quantity(10)
				.totalAmount(100)
				.paymentMode(CASH)
				.build();
	}
}
