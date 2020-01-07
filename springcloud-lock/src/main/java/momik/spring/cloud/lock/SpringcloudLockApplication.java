package momik.spring.cloud.lock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "momik.spring.cloud.lock")
public class SpringcloudLockApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringcloudLockApplication.class, args);
    }

}
