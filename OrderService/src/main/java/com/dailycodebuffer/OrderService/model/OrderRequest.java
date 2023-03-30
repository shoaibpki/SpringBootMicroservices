package com.dailycodebuffer.OrderService.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequest {
    private long productId;
    private PaymentMode paymentMode;
    private long totalAmount;
    private long quantity;
}
