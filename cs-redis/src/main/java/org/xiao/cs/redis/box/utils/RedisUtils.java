package org.xiao.cs.redis.box.utils;

import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtils {

    private static final Logger log = LoggerFactory.getLogger(RedisUtils.class);

    @Resource
    RedisTemplate<String, Object> redisTemplate;
    private static final double size = Math.pow(2, 32);

    public boolean setBit(String key, long offset, boolean value) {
        try {
            ValueOperations<String, Object> operations = this.redisTemplate.opsForValue();
            return Boolean.TRUE.equals(operations.setBit(key, offset, value));
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    public boolean getBit(String key, long offset) {
        try {
            ValueOperations<String, Object> operations = this.redisTemplate.opsForValue();
            return Boolean.TRUE.equals(operations.getBit(key, offset));
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    public void set(final String key, Object value) {
        try {
            ValueOperations<String, Object> operations = this.redisTemplate.opsForValue();
            operations.set(key, value);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public boolean set(final String key, Object value, Long expireTime) {
        try {
            ValueOperations<String, Object> operations = this.redisTemplate.opsForValue();
            operations.set(key, value);
            return Boolean.TRUE.equals(this.redisTemplate.expire(key, expireTime, TimeUnit.MILLISECONDS));
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    public void remove(final String... keys) {
        for (String key : keys) {
            remove(key);
        }
    }

    public void remove(final String key) {
        if (hasKey(key)) {
            this.redisTemplate.delete(key);
        }
    }

    public boolean hasKey(final String key) {
        return Boolean.TRUE.equals(this.redisTemplate.hasKey(key));
    }

    public Object get(final String key) {
        ValueOperations<String, Object> operations = this.redisTemplate.opsForValue();
        return operations.get(key);
    }

    public void put(String key, Object hashKey, Object value) {
        HashOperations<String, Object, Object> hash = this.redisTemplate.opsForHash();
        hash.put(key, hashKey, value);
    }

    public Object get(String key, Object hashKey) {
        HashOperations<String, Object, Object> hash = this.redisTemplate.opsForHash();
        return hash.get(key, hashKey);
    }

    public void rightPush(String k, Object v) {
        ListOperations<String, Object> list = this.redisTemplate.opsForList();
        list.rightPush(k, v);
    }

    public List<Object> range(String k, long start, long end) {
        ListOperations<String, Object> list = this.redisTemplate.opsForList();
        return list.range(k, start, end);
    }

    public Long add(String key, Object value) {
        SetOperations<String, Object> set = this.redisTemplate.opsForSet();
        return set.add(key, value);
    }

    public Set<Object> members(String key) {
        SetOperations<String, Object> zSet = this.redisTemplate.opsForSet();
        return zSet.members(key);
    }

    public void add(String key, Object value, double score) {
        ZSetOperations<String, Object> zSet = this.redisTemplate.opsForZSet();
        zSet.add(key, value, score);
    }

    public Set<Object> rangeByScore(String key, double scoreSource, double scoreTarget) {
        ZSetOperations<String, Object> zSet = this.redisTemplate.opsForZSet();
        this.redisTemplate.opsForValue();
        return zSet.rangeByScore(key, scoreSource, scoreTarget);
    }

    public boolean setBit(String name) {
        double index = Math.abs(name.hashCode() % size);
        long indexLong = Double.doubleToLongBits(index);
        return setBit("availableUsers", indexLong, true);
    }

    public boolean getBit(String name) {
        double index = Math.abs(name.hashCode() % size);
        long indexLong = Double.doubleToLongBits(index);
        return getBit("availableUsers", indexLong);
    }

    public Long rank(String key, Object value) {
        ZSetOperations<String, Object> zSet = this.redisTemplate.opsForZSet();
        return zSet.rank(key, value);
    }

    public Set<ZSetOperations.TypedTuple<Object>> rangeWithScores(String key, long start, long end) {
        ZSetOperations<String, Object> zSet = this.redisTemplate.opsForZSet();
        return zSet.rangeWithScores(key, start, end);
    }

    public Double score(String key, Object value) {
        ZSetOperations<String, Object> zSet = this.redisTemplate.opsForZSet();
        return zSet.score(key, value);
    }

    public void incrementScore(String key, Object value, double score) {
        ZSetOperations<String, Object> zSet = this.redisTemplate.opsForZSet();
        zSet.incrementScore(key, value, score);
    }

    public Set<ZSetOperations.TypedTuple<Object>> reverseRangeByScoreWithScores(String key, long start, long end) {
        ZSetOperations<String, Object> zSet = this.redisTemplate.opsForZSet();
        return zSet.reverseRangeByScoreWithScores(key, start, end);
    }

    public Set<ZSetOperations.TypedTuple<Object>> reverseRangeWithScores(String key, long start, long end) {
        ZSetOperations<String, Object> zSet = this.redisTemplate.opsForZSet();
        return zSet.reverseRangeWithScores(key, start, end);
    }
}
