package org.demo.ecommerce.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Autowired
    private ObjectMapper objectMapper;

    @Bean
    public FilterRegistrationBean<ApiTokenFilter> apiTokenFilter() {
        FilterRegistrationBean<ApiTokenFilter> registrationBean
                    = new FilterRegistrationBean<>();
        registrationBean.setFilter(new ApiTokenFilter(objectMapper));
        registrationBean.addUrlPatterns("/api/*");
        registrationBean.setOrder(2);

        return registrationBean;
    }
}