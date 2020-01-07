package momik.spring.cloud.gateway.service.impl;

import momik.spring.cloud.gateway.mapper.GatewayMapper;
import momik.spring.cloud.gateway.service.GatewayService;
import org.springframework.stereotype.Service;

@Service
public class GatewayServiceImpl implements GatewayService {
    private GatewayMapper gatewayMapper;

    public GatewayServiceImpl(GatewayMapper gatewayMapper) {
        this.gatewayMapper = gatewayMapper;
    }

    @Override
    public String testSelect() {
        return gatewayMapper.testSelect();
    }
}
