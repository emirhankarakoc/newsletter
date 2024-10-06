package com.karakoc.enewsletter.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // İstek bilgilerini alalım
        String method = request.getMethod();
        String endpoint = request.getRequestURI();
        LocalDateTime requestTime = LocalDateTime.now();

        // İstek bilgilerini loglayalım
        log.info("Request Time: {},HTTP Method: {}, Endpoint: {}, ",requestTime, method, endpoint);

        // İstek zincirine devam edelim
        filterChain.doFilter(request, response);
    }
}
