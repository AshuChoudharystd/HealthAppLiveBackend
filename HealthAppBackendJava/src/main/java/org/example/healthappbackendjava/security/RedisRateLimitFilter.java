package org.example.healthappbackendjava.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.healthappbackendjava.service.RateLimiterService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;

@Component
public class RedisRateLimitFilter extends OncePerRequestFilter {
    private final RateLimiterService rateLimiterService;
    public RedisRateLimitFilter(RateLimiterService rateLimiterService) {
        this.rateLimiterService = rateLimiterService;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        String path = request.getServletPath();

        if(path.equals("/auth/login")){
            String ip = request.getHeader("X-Forwarded-For");

            if(ip == null || ip.isBlank()){
                ip = request.getRemoteAddr();
            }else{
                ip = ip.split(",")[0].trim();
            }
            String key = "login"+ip;
            boolean allowed = rateLimiterService.allowRequest(key,5, Duration.ofMinutes(1));
            if(!allowed){
                response.setStatus(429);
                response.setContentType("application/json");

                response.getWriter().write("{\"error\":\"Too Many Requests\"}");

                return ;
            }
        }
        filterChain.doFilter(request,response);
    }
}
