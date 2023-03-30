package com.dailycodebuffer.OrderService.entity;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table( name = "ORDER_DETAILS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long productId;
    private long quantity;
    private Instant orderDate;
    private long amount;
    private String orderStatus;
}
