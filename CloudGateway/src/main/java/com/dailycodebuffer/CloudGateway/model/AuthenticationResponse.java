package com.dailycodebuffer.CloudGateway.model;

import lombok.*;

import java.util.Collection;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticationResponse {

    private String userId;
    private String accessToken;
    private String refreshToken;
    private long expiresAt;
    private Collection<String> authorityList;
}
