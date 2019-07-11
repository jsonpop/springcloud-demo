package momik.spring.cloud.transaction.branka;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "brankb-service")
public interface BrankBClient {

    @GetMapping("/subMoney")
    String subMoney(@RequestParam(name = "money") int money, @RequestParam(name = "aid") String aid);
}
