package shop.biday.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisTemplateUtils<T> {

    private final RedisTemplate<String, Object> redisTemplate;

    public void save(String key, T obj) {
        ValueOperations<String, Object> valueOps = redisTemplate.opsForValue();
        valueOps.set(key, obj);
        log.info("saved by Redis: {}", valueOps.get(key));
    }

    public void save(String key, T obj, Long expireMs) {
        ValueOperations<String, Object> valueOps = redisTemplate.opsForValue();
        valueOps.set(key, obj, expireMs, TimeUnit.MILLISECONDS);
        log.info("saved by Redis: {}", valueOps.get(key));
    }

    public T get(String key, Class<T> clazz) {
        if (!isValidKey(key) && !existsKey(key)) {
            return null;
        }

        ValueOperations<String, Object> valueOps = redisTemplate.opsForValue();
        Object value = valueOps.get(key);
        if (!clazz.isInstance(value)) {
            log.warn("해당 키로 저장된 객체가 없습니다 key: {}", key);
            return null;
        }

        return clazz.cast(value);
    }

    public void delete(String key) {
        if (!isValidKey(key)) {
            return;
        }

        if (existsKey(key)) {
            redisTemplate.delete(key);
            log.info("해당 key: {}를 삭제하였습니다.", key);
        }
    }

    public boolean existsKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    private boolean isValidKey(String key) {
        if (!StringUtils.hasText(key)) {
            log.warn("올바르지 않는 키입니다. key: {}", key);
            return false;
        }
        return true;
    }
}
