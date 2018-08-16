package com.example.cloud.service;

/**
 * Abstraction of the token validation
 */
public interface AuthorizationService {

    /**
     * is the token correct/authorized to allow the filter update
     * @param token
     * @return
     */
    boolean isAuthorized(String token);
}
