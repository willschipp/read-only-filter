package com.example.cloud.service.jwt;


import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.UUID;

/**
 * very VERY simple JWT Token service
 */
public class JWTokenUtil {

    private SignatureAlgorithm algorithm = SignatureAlgorithm.HS256;

    private String secret;

    private String issuer;

    public JWTokenUtil(String secret, String issuer) {
        this.secret = secret;
        this.issuer = issuer;
    }

    public String getToken() {

        byte[] signature = DatatypeConverter.parseBase64Binary(secret);
        Key signingKey = new SecretKeySpec(signature,algorithm.getJcaName());

        JwtBuilder builder = Jwts.builder().setId(UUID.randomUUID().toString())
                .setIssuedAt(new Date())
                .setIssuer(issuer)
                .signWith(signingKey,algorithm);

        return builder.compact();
    }


    public boolean isValid(String token) {
        return Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(secret)).isSigned(token);
    }
}
