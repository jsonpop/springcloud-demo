package momik.spring.cloud.gateway.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface GatewayMapper {

    @Select("select nick from brank_account")
    String testSelect();
}
