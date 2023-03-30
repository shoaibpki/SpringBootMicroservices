package com.dailycodebuffer.OrderService.service;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;

@TestConfiguration
public class OrderServiceConfig {

    
    ServiceInstanceListSupplier supplier(){
        return new TestServiceInstanceListSupplier();
    }
}
