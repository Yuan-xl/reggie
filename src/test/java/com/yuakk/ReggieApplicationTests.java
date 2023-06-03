package com.yuakk;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest
class ReggieApplicationTests {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Test
    void testRedis() {
        /*stringRedisTemplate.opsForValue().set("name", "xiaoming");
        String name = stringRedisTemplate.opsForValue().get("name");
        System.out.println(name);*/
        Boolean name = stringRedisTemplate.delete("name");
        System.out.println(name);
    }




}
