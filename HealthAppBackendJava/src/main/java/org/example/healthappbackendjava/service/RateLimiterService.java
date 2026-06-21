package org.example.healthappbackendjava.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RateLimiterService {
    private final StringRedisTemplate redisTemplate;

    public RateLimiterService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean allowRequest(String key,int maxRequests, Duration window){
        Long count =redisTemplate.opsForValue().increment(key);

        if(count==null){
            return false;
        }
        if(count == 1){
            redisTemplate.expire(key,window);
        }
        return count<=maxRequests;
    }

}
