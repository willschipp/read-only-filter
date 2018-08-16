package com.example.cloud.service.jwt;

import com.example.cloud.service.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * JWT based implementation, reusing JWTokenUtil
 */
public class JWTAuthorizationService implements AuthorizationService {

    @Autowired
    private JWTokenUtil tokenUtil;

    @Override
    public boolean isAuthorized(String token) {
        return tokenUtil.isValid(token);
    }
}
