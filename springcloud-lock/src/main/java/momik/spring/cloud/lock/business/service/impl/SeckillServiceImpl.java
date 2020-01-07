package momik.spring.cloud.lock.business.service.impl;

import momik.spring.cloud.lock.business.service.SeckillService;
import momik.spring.cloud.lock.redis.RedisLock;
import momik.spring.cloud.lock.redis.RedisLockFactory;
import momik.spring.cloud.lock.redis.RedisUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class SeckillServiceImpl implements SeckillService {
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
}
