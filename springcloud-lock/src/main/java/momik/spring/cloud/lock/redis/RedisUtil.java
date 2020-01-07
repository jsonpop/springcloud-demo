package momik.spring.cloud.lock.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * description Redis操作工具类
 * 创建时间 2018/10/29
 *
 * @author 仇兴洲
 */
@Slf4j
@Component
@ConditionalOnClass(RedisTemplate.class)
public class RedisUtil {

    private static RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public RedisUtil(RedisTemplate<String, Object> redisTemplate) {
        RedisUtil.redisTemplate = redisTemplate;
    }


    /**
     * 设置键值及对应内容
     *
     * @param key    主键
     * @param keyVal 值
     */
    public static boolean setRedisKeyValue(String key, String keyVal) {
        boolean retStatus = false;
        try {
            redisTemplate.opsForValue().set(key, keyVal);
            retStatus = true;
        } catch (Exception ex) {
            log.error("setRedisKeyValue_Error:" + ex.getMessage());
        }
        return retStatus;
    }

    /**
     * 获取键值对应数据
     *
     * @param key 主键
     * @return
     */
    public static String getRedisKeyValue(String key) {
        String result = "";
        try {
            result = key == null ? null : String.valueOf(redisTemplate.opsForValue().get(key));
            if ("null".equals(result)) {
                result = "";
            }
        } catch (Exception ex) {
            log.error("getRedisKeyValue_Error:" + ex.getMessage());
        }
        return result;
    }

    /**
     * 哈希map方式，设置键值及对应的数据
     *
     * @param key
     * @param keyMapVal
     */
    public static boolean setRedisHashMap(String key, Map<String, String> keyMapVal) {
        boolean retStatus = false;
        try {
            redisTemplate.opsForHash().putAll(key, keyMapVal);
            retStatus = true;
        } catch (Exception ex) {
            log.error("setRedisHashMap_Error:" + ex.getMessage());
        }
        return retStatus;
    }

    /**
     * 哈希map方式，获取主键对应的内容
     *
     * @param key
     * @return
     */
    public static Map<Object, Object> getRedisHashMap(String key) {
        Map<Object, Object> retMap = new HashMap<Object, Object>();
        try {
            retMap = redisTemplate.opsForHash().entries(key);
            if (retMap.isEmpty()) {
                retMap = null;
            }
        } catch (Exception ex) {
            log.error("getRedisHashMap_Error:" + ex.getMessage());
        }
        return retMap;
    }

    /**
     * 哈希map方式，设置主键，关键字段值
     *
     * @param key
     * @param keyField
     * @param keyFieldVal
     */
    public static boolean setRedisHashMapField(String key, String keyField, String keyFieldVal) {
        boolean retStatus = false;
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put(keyField, keyFieldVal);
            redisTemplate.opsForHash().putAll(key, map);
        } catch (Exception ex) {
            log.error("setRedisHashMapField_Error:" + ex.getMessage());
        }
        return retStatus;
    }

    /**
     * 哈希map方式，获取主键，关键字段对应的数据
     *
     * @param key
     * @param keyField
     * @return
     */
    public static String getRedisHashMapField(String key, String keyField) {
        String result = "";
        try {
            result = String.valueOf(redisTemplate.opsForHash().get(key, keyField));
            if ("null".equals(result)) {
                result = "";
            }
        } catch (Exception ex) {
            log.error("getRedisHashMapField_Error:" + ex.getMessage());
        }
        return result;
    }

    /**
     * 哈希map方式，根据主键，关键字段，删除键值对应的数据
     *
     * @param key
     * @param keyField
     */
    public static boolean delRedisHashMapField(String key, String keyField) {
        boolean retStatus = false;
        try {
            redisTemplate.opsForHash().delete(key, keyField);
        } catch (Exception ex) {
            log.error("delRedisHashMapField_Error:" + ex.getMessage());
        }
        return retStatus;
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public static boolean checkRedisKeyIsExist(String key) {
        boolean retStatus = false;
        try {
            retStatus = redisTemplate.hasKey(key);
        } catch (Exception ex) {
            log.error("checkRedisKeyIsExist_Error:" + ex.getMessage());
        }
        return retStatus;
    }

    /**
     * 删除键值内容
     *
     * @param key
     */
    public static boolean delRedisKey(String key) {
        boolean retStatus = false;
        try {
            redisTemplate.delete(key);
            retStatus = true;
        } catch (Exception ex) {
            log.error("delRedisKey_Error:" + ex.getMessage());
        }
        return retStatus;
    }

    /**
     * 根据传入的键值前缀获取匹配的键值列表
     *
     * @param prex
     * @return
     */
    public Set<String> getKeylistByPrex(String prex) {
        Set<String> keys = new HashSet<String>();
        try {
            if (prex != null && !"".equals(prex)) {
                keys = redisTemplate.keys(prex);
            }
        } catch (Exception ex) {
            log.error("getKeylistByPrex_Error:" + ex.getMessage());
        }
        return keys;
    }


    /**
     * 多服务器集群，使用下面的方法，代替System.currentTimeMillis()，获取redis时间，避免多服务的时间不一致问题！！！
     *
     * @return
     */
    public static long currtTimeForRedis() {
        return redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection redisConnection) throws DataAccessException {
                return redisConnection.time();
            }
        });
    }

    /**
     * 设置主键锁，用于控制并发任务，传入锁的键值和锁自动失效时间
     * 失效时间long参数是秒级的，传入的分钟需要换算为秒传入
     *
     * @param lockKey
     * @param lockTimeOut
     * @return
     */
    public static boolean setRedisLock(final String lockKey, Long lockTimeOut) {
        boolean retStatus = false;
        try {
            final Long lock_timeout = currtTimeForRedis() + lockTimeOut + 1;
            if (redisTemplate.execute(new RedisCallback<Boolean>() {
                @Override
                public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                    StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
                    stringRedisSerializer.serialize(lock_timeout.toString());
                    byte[] value = stringRedisSerializer.serialize(lock_timeout.toString());
                    return connection.setNX(lockKey.getBytes(), value);
                }
            })) {
                //设置超时时间，释放内存
                redisTemplate.expire(lockKey, lockTimeOut, TimeUnit.SECONDS);
                retStatus = true;
            }
        } catch (Exception ex) {
            log.error("setRedisLock_Error:" + ex.getMessage());
        }
        return retStatus;
    }

    /**
     * 删除Redis锁主键
     *
     * @param key
     * @return
     */
    public static boolean delRedisLock(String key) {
        boolean retStatus = false;
        try {
            redisTemplate.delete(key);
            retStatus = true;
        } catch (Exception ex) {
            log.error("delRedisLock_Error:" + ex.getMessage());
        }
        return retStatus;
    }

    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     * @return
     */
    public static boolean setKeyExpire(String key, long time) {
        boolean retStatus = false;
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
                retStatus = true;
            }
        } catch (Exception ex) {
            log.error("setKeyExpire_Error:" + ex.getMessage());
        }
        return retStatus;
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public static long getKeyExpire(String key) {
        long ret = -1;
        try {
            ret = redisTemplate.getExpire(key, TimeUnit.SECONDS);
        } catch (Exception ex) {
            log.error("getKeyExpire_Error:" + ex.getMessage());
        }
        return ret;
    }


}
