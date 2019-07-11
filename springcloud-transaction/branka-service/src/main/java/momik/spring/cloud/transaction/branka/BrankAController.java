package momik.spring.cloud.transaction.branka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BrankAController {
    private BrankAService brankAService;

    @Autowired
    public BrankAController(BrankAService brankAService) {
        this.brankAService = brankAService;
    }

    @GetMapping("/transAccount")
    public String transferAccounts(@RequestParam(name = "money") int money, @RequestParam(name = "aid") String aid) {
        return brankAService.transferAccounts(money, aid);
    }
}
