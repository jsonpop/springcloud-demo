package momik.spring.cloud.transaction.branka;

import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BrankAServiceImpl implements BrankAService {
    private BrankAMapper brankAMapper;
    private BrankBClient brankBClient;

    @Autowired
    public BrankAServiceImpl(BrankAMapper brankAMapper, BrankBClient brankBClient) {
        this.brankAMapper = brankAMapper;
        this.brankBClient = brankBClient;
    }

    @LcnTransaction
    @Transactional
    @Override
    public String transferAccounts(int money, String aid) {
        brankBClient.subMoney(money, aid);
        int count = brankAMapper.addMoney(money, aid);
        return count > 0 ? "success" : "error";
    }
}
