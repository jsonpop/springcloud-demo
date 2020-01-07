package momik.spring.cloud.gateway.controller;

import momik.spring.cloud.gateway.service.GatewayService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GatewayController {

    private GatewayService gatewayService;

    public GatewayController(GatewayService gatewayService) {
        this.gatewayService = gatewayService;
    }

    @PostMapping("/get")
    public String test() {
        return gatewayService.testSelect();
    }
}
