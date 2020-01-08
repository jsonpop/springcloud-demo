package momik.spring.cloud.lock;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = "momik.spring.cloud.lock")
public class SpringcloudLockApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringcloudLockApplication.class, args);
    }


    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://qadb-int.xiaozipu.cn:6379")
                .setDatabase(2)
                .setPassword("PC8hXD76Vn3vJNye");
        RedissonClient redisson = Redisson.create(config);
        return redisson;
    }
}
