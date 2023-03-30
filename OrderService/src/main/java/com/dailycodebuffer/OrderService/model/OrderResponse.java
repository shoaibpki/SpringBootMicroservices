package com.dailycodebuffer.OrderService.model;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {
    private long orderId;
    private String orderStatus;
    private Instant orderDate;
    private long amount;
    private long quantity;
    private ProductDetails productDetails;
    private PaymentDetails paymentDetails;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ProductDetails {
        private long productId;
        private String productName;
        private long price;
        private long quantity;
    }
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PaymentDetails {

        private long paymentId;
        private String status;
        private PaymentMode paymentMode;
        private Instant paymentDate;
    }
}
