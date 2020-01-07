package momik.spring.cloud.lock.redis;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.util.StringUtils;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * Redis分布式锁
 */
@Slf4j
@Builder
public class RedisLock implements Lock {
    private static final String UNLOCK_LUA;

    static {
        StringBuilder sb = new StringBuilder();
        sb.append("if redis.call(\"get\",KEYS[1]) == ARGV[1] ");
        sb.append("then ");
        sb.append("    return redis.call(\"del\",KEYS[1]) ");
        sb.append("else ");
        sb.append("    return 0 ");
        sb.append("end ");
        UNLOCK_LUA = sb.toString();
    }

    private static final String LOCKKEY_PREFIX = "RedisLock:";
    private RedisTemplate<String, String> redisTemplate;
    /**
     * 锁主键
     */
    private String lockKey;
    /**
     * 不同client获取锁唯一标识
     */
    private String requestId;
    /**
     * 锁失效时间/毫秒
     */
    private long expire = 1000;
    /**
     * 重试获取锁最大时间/毫秒
     */
    private long retryTimes = 800;
    /**
     * 获取锁线程等待时间/毫秒
     */
    private long threadDelay = 150;
    /**
     * 是否需要线程等待
     */
    private boolean needThreadDelay = true;
    /**
     * 是否已上锁
     */
    private boolean lock = false;

    @Override
    public void lock() {
        lock = redisTemplate.execute((RedisConnection redisConnection) -> {
            long endTime = System.currentTimeMillis() + retryTimes;
            while (endTime > System.currentTimeMillis()) {
                log.info("{}尝试获取锁",requestId);
                boolean flag = redisConnection.set(lockKey.getBytes(), requestId.getBytes(), Expiration.milliseconds(expire), RedisStringCommands.SetOption.SET_IF_ABSENT);
                if (flag) {
                    log.info("{}获取锁成功",requestId);
                    return true;
                }
                if (needThreadDelay) {
                    try {
                        Thread.sleep(threadDelay);
                    } catch (InterruptedException e) {
                        log.error("", e);
                        Thread.currentThread().interrupt();
                    }
                }
            }
            return false;
        });
    }

    @Override
    public void lockInterruptibly() {

    }

    @Override
    public boolean tryLock() {
        return lock;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) {
        return lock;
    }

    @Override
    public void unlock() {
        redisTemplate.execute((RedisConnection redisConnection) -> redisConnection.scriptingCommands().eval(UNLOCK_LUA.getBytes(), ReturnType.BOOLEAN, 1, lockKey.getBytes(), requestId.getBytes()));
        lock = false;
    }

    @Override
    public Condition newCondition() {
        return null;
    }


    public static String getLockkeyPrefix() {
        return LOCKKEY_PREFIX;
    }

    public String getLockKey() {
        return lockKey;
    }

    public void setLockKey(String lockKey) {
        if (StringUtils.isEmpty(lockKey)) {
            throw new NullPointerException("设置Redis锁key不能为空");
        }
        this.lockKey = LOCKKEY_PREFIX + lockKey;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public long getExpire() {
        return expire;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }

    public long getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(long retryTimes) {
        this.retryTimes = retryTimes;
    }

    public long getThreadDelay() {
        return threadDelay;
    }

    public void setThreadDelay(long threadDelay) {
        this.threadDelay = threadDelay;
    }

    public boolean isNeedThreadDelay() {
        return needThreadDelay;
    }

    public void setNeedThreadDelay(boolean needThreadDelay) {
        this.needThreadDelay = needThreadDelay;
    }
}
