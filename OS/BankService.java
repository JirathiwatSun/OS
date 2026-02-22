// Interface = stored in Method Area (JVM Metadata)
public interface BankService {

    double getBalance();     // Balance inquiry
    void deposit(double amount);
    void withdraw(double amount);
}