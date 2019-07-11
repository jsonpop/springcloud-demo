package momik.spring.cloud.transaction.brankb;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface BrankBMapper {
    @Update("update brank_account set money = money-#{money} where aid=#{aid}")
    int subMoney(int money, String aid);
}
