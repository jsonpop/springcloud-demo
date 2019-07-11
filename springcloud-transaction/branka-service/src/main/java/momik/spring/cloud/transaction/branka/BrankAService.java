package momik.spring.cloud.transaction.branka;

public interface BrankAService {
    String transferAccounts(int money, String aid);
}
