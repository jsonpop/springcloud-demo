package momik.spring.cloud.transaction.brankb;

import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BrankBServiceImpl implements BrankBService {
    private BrankBMapper brankBMapper;

    @Autowired
    public BrankBServiceImpl(BrankBMapper brankBMapper) {
        this.brankBMapper = brankBMapper;
    }

    @LcnTransaction
    @Transactional
    @Override
    public String subMoney(int money, String aid) {
        int count = brankBMapper.subMoney(money, aid);
        return count > 0 ? "success" : "error";
    }
}
