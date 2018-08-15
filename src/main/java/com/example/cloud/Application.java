package com.example.cloud;

import com.example.cloud.filter.ReadOnlyFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

    public static void main(String... args) throws Exception {
        SpringApplication.run(Application.class,args);
    }

    @Bean
    public FilterRegistrationBean requestFilter() throws Exception {
        final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new ReadOnlyFilter());
        registrationBean.addUrlPatterns("/**");
        return registrationBean;
    }
}
