package momik.spring.cloud.lock.business.controller;

import momik.spring.cloud.lock.business.service.SeckillService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SeckillController {
    private SeckillService seckillService;

    public SeckillController(SeckillService seckillService) {
        this.seckillService = seckillService;
    }

    @RequestMapping("/buy")
    public boolean buy() {
        return seckillService.buy();
    }

    @RequestMapping("/buy2")
    public boolean buy2() {
        return true;
        /*return seckillService.buy2();*/
    }
}
