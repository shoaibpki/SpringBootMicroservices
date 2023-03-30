package com.dailycodebuffer.ProductService.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {
    private String errorMessage;
    private String errorCode;
}
