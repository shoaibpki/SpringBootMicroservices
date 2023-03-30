package com.dailycodebuffer.OrderService.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse {
    private long productId;
    private String productName;
    private long price;
    private long quantity;
}
