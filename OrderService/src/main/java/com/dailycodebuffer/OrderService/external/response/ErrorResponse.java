package com.dailycodebuffer.OrderService.external.response;

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
