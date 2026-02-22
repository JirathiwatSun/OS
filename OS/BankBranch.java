import java.util.ArrayList;
import java.util.List;

public class BankBranch {

    // ================= HEAP =================
    private String branchName;
    private String location;
    private List<BankAccount> accounts;

    public BankBranch(String branchName, String location) {
        this.branchName = branchName;
        this.location = location;
        this.accounts = new ArrayList<>();
    }

    public void addAccount(BankAccount account) {
        accounts.add(account);
    }

    public void branchReport() {

        System.out.println("===== Branch Report =====");
        System.out.println("Branch Name: " + branchName);
        System.out.println("Location: " + location);
        System.out.println("Total Accounts: " + accounts.size());

        double total = 0;

        for (BankAccount acc : accounts) {
            total += acc.getBalance();
        }

        System.out.println("Total Branch Balance: " + total);
        System.out.println("=========================");
    }
}