package com.example.cloud.service;

import com.example.cloud.service.jwt.JWTokenUtil;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class JWTokenUtilTest {

    @Test
    public void testGenerateAToken() throws Exception {
        JWTokenUtil tokenUtil = new JWTokenUtil(UUID.randomUUID().toString() + UUID.randomUUID().toString(),"blah");
        assertNotNull(tokenUtil.getToken());
    }

    @Test
    public void testValidToken() throws Exception {
        JWTokenUtil tokenUtil = new JWTokenUtil(UUID.randomUUID().toString() + UUID.randomUUID().toString(),"blah");
        String token = tokenUtil.getToken();
        assertTrue(tokenUtil.isValid(token));
    }
}
