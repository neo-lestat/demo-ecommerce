package org.demo.ecommerce.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

//This is just a dummy security filter , just ask for a not empty api-token header
public class ApiTokenFilter implements Filter {

    private ObjectMapper objectMapper;

    public ApiTokenFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws ServletException, IOException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String apiToken = req.getHeader("api-token");
        if (StringUtils.hasLength(apiToken)) {
            chain.doFilter(request, response);
        } else {
            Map<String, Object> errorDetails = new HashMap<>();
            errorDetails.put("statusCode", HttpStatus.FORBIDDEN.value());
            errorDetails.put("message", "Invalid token");
            res.setStatus(HttpStatus.FORBIDDEN.value());
            res.setContentType(MediaType.APPLICATION_JSON_VALUE);
            objectMapper.writeValue(res.getWriter(), errorDetails);
        }

    }

}
