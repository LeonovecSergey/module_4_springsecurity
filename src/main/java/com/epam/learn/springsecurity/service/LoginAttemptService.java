package com.epam.learn.springsecurity.service;

import com.epam.learn.springsecurity.model.CachedValue;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class LoginAttemptService {

    public static final int MAX_ATTEMPT = 3;
    public static final int BLOCK_DURATION_MIN = 5;
    private final LoadingCache<String, CachedValue> attemptsCache;

    public LoginAttemptService() {
        this.attemptsCache = CacheBuilder.newBuilder()
                .expireAfterWrite(BLOCK_DURATION_MIN, TimeUnit.MINUTES)
                .build(new CacheLoader<>() {
                    @Override
                    public CachedValue load(final String key) throws Exception {
                        return new CachedValue(0, LocalDateTime.now());
                    }
                });
    }

    public void loginFailed(final String key) {
        var cashedValue = new CachedValue();
        try {
            cashedValue = attemptsCache.get(key);
            cashedValue.setAttempts(cashedValue.getAttempts() + 1);
        } catch (final ExecutionException e) {
            cashedValue.setAttempts(0);
        }
        if (isBlocked(key) && cashedValue.getBlockedTimestamp() == null) {
            cashedValue.setBlockedTimestamp(LocalDateTime.now());
        }

        attemptsCache.put(key, cashedValue);
    }

    public void loginSuccess(final String key) {
        var cashedValue = new CachedValue(0, null);
        attemptsCache.put(key, cashedValue);
    }

    public CachedValue getCachedValue(final String key) {
        return attemptsCache.getUnchecked(key);
    }

    public boolean isBlocked(final String key) {
        try {
            return attemptsCache.get(key).getAttempts() >= MAX_ATTEMPT;
        } catch (ExecutionException e) {
            return false;
        }
    }
}
