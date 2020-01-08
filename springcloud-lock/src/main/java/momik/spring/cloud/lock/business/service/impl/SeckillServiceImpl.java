package momik.spring.cloud.lock.business.service.impl;

import momik.spring.cloud.lock.business.service.SeckillService;
import momik.spring.cloud.lock.redis.RedisLock;
import momik.spring.cloud.lock.redis.RedisLockFactory;
import momik.spring.cloud.lock.redis.RedisUtil;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

@Service
public class SeckillServiceImpl implements SeckillService {

    @Autowired
    private RedissonClient redissonClient;

    @Override
    public boolean buy() {
        String lockKey = "seckill_stock_lock";
        RedisLock redisLock = RedisLockFactory.createLockAndUp(lockKey);
        try {
            if (redisLock.tryLock()) {
                String stockStr = RedisUtil.getRedisKeyValue("seckill_stock");
                int stock = StringUtils.isEmpty(stockStr) ? 0 : Integer.valueOf(stockStr);
                if (stock > 0) {
                    stock--;
                    RedisUtil.setRedisKeyValue("seckill_stock", String.valueOf(stock));
                    return true;
                }
            }
            return false;
        } finally {
            redisLock.unlock();
        }
    }

    @Override
    public boolean buy2() {
        String stockStr = RedisUtil.getRedisKeyValue("seckill_stock");
        int stock = StringUtils.isEmpty(stockStr) ? 0 : Integer.valueOf(stockStr);
        if (stock > 0) {
            stock--;
            RedisUtil.setRedisKeyValue("seckill_stock", String.valueOf(stock));
            return true;
        }
        return false;
    }

    @Override
    public boolean buy3() {
        String lockKey = "seckill_stock_lock";
        RLock lock = redissonClient.getLock(lockKey);
        try {
            if (lock.tryLock(500,30000, TimeUnit.MILLISECONDS)) {
                String stockStr = RedisUtil.getRedisKeyValue("seckill_stock");
                int stock = StringUtils.isEmpty(stockStr) ? 0 : Integer.valueOf(stockStr);
                if (stock > 0) {
                    stock--;
                    RedisUtil.setRedisKeyValue("seckill_stock", String.valueOf(stock));
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            lock.unlock();
        }
    }
}
