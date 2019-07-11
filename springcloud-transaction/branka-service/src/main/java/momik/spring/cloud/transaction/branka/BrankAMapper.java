package momik.spring.cloud.transaction.branka;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface BrankAMapper {
    @Update("update brank_account set money = money+#{money} where aid=#{aid}")
    int addMoney(int money, String aid);
}
