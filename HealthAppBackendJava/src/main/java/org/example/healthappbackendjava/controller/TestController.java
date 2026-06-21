package org.example.healthappbackendjava.controller;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {
    private final StringRedisTemplate stringRedisTemplate;
    public TestController(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @GetMapping("/redis")
    public String testRedis(){
        stringRedisTemplate.opsForValue().set("hello"," redis");
        return stringRedisTemplate.opsForValue().get("hello");
    }
}
