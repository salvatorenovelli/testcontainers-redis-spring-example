package com.example;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;

@Component
public class ItalianDictionary {

    private static final String KEY = "ItalianDictionary";
    private final HashOperations<String, String, String> hashOperations;

    public ItalianDictionary(RedisTemplate redisTemplate) {
        hashOperations = redisTemplate.opsForHash();
    }

    public String save(String english, String italian) {
        hashOperations.put(KEY, english, italian);
        return italian;
    }

    public Optional<String> findById(String english) {
        return Optional.ofNullable(hashOperations.get(KEY, english));
    }

    public Collection<String> findAll() {
        return hashOperations.values(KEY);
    }
}
