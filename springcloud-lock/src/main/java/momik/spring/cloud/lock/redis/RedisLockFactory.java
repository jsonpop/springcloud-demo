package momik.spring.cloud.lock.redis;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@ConditionalOnClass(RedisTemplate.class)
public class RedisLockFactory {

    private static StringRedisTemplate stringRedisTemplate;

    @Autowired
    public void setRedisTemplate(StringRedisTemplate stringRedisTemplate) {
        RedisLockFactory.stringRedisTemplate = stringRedisTemplate;
    }

    public static RedisLock createLock(String key) {
        RedisLock redisLock = RedisLock.builder()
                .stringRedisTemplate(stringRedisTemplate)
                .lockKey(key)
                .requestId(UUID.randomUUID().toString())
                .needThreadDelay(false)
                .threadDelay(RandomUtils.nextLong(50, 100))
                .retryTimes(800)
                .expire(1000)
                .build();
        return redisLock;
    }

    public static RedisLock createLockAndUp(String key) {
        RedisLock redisLock = createLock(key);
        redisLock.lock();
        return redisLock;
    }
}
