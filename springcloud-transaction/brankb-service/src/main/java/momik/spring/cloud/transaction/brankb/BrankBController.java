package momik.spring.cloud.transaction.brankb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BrankBController {
    private BrankBService brankBService;

    @Autowired
    public BrankBController(BrankBService brankBService) {
        this.brankBService = brankBService;
    }

    @GetMapping("/subMoney")
    public String subMoney(@RequestParam(name = "money") int money, @RequestParam(name = "aid") String aid) {
        return brankBService.subMoney(money, aid);
    }
}
