package momik.spring.cloud.transaction.branka;

import com.codingapi.txlcn.tc.config.EnableDistributedTransaction;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@EnableDistributedTransaction
public class BrankaServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BrankaServiceApplication.class, args);
    }

}
