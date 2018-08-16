package com.example.cloud;

import com.example.cloud.filter.ReadOnlyFilter;
import com.example.cloud.service.AuthorizationService;
import com.example.cloud.service.jwt.JWTAuthorizationService;
import com.example.cloud.service.jwt.JWTokenUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

    @Value("${filter.adminUrl}")
    private String adminUrl;

    @Value("${filter.secret}")
    private String secret;

    @Value("${filter.issuer}")
    private String issuer;

    public static void main(String... args) throws Exception {
        SpringApplication.run(Application.class,args);
    }

    @Bean
    public FilterRegistrationBean<ReadOnlyFilter> requestFilter() throws Exception {
        ReadOnlyFilter filter = new ReadOnlyFilter();
        filter.setAdminUrl(adminUrl);
        FilterRegistrationBean<ReadOnlyFilter> registrationBean = new FilterRegistrationBean<>(filter);
        return registrationBean;
    }

    @Bean
    public JWTokenUtil tokenUtil() {
        return new JWTokenUtil(secret,issuer);
    }

    @Bean
    public AuthorizationService authorizationService() {
        return new JWTAuthorizationService();
    }
}
