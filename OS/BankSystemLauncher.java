import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BankSystemLauncher {

    // --- CENTRAL DATABASE (SHARED HEAP MEMORY) ---
    public static List<BankAccount> allAccounts = new ArrayList<>();
    public static Map<BankAccount, String> accountNames = new HashMap<>();
    public static List<BankBranch> branches = new ArrayList<>();
    public static List<Employee> staffList = new ArrayList<>();
    public static CountryFinancial government = new CountryFinancial(100000);
    public static BankScheduler scheduler = new BankScheduler();

    // GUI References
    public static ModernBankGUI customerGUI;
    public static EmployeePortalGUI employeeGUI;

    public static void main(String[] args) {
        System.out.println("Starting Central Bank Server...");

        // 1. Initialize Shared Branches
        BankBranch mainBranch = new BankBranch("Main Branch", "Bangkok");
        BankBranch downtownBranch = new BankBranch("Downtown Branch", "Chiang Mai");
        branches.add(mainBranch);
        branches.add(downtownBranch);

        // 2. Initialize Shared Employees
        staffList.add(new Employee(101, "Diana (Manager)", "Manager"));
        staffList.add(new Employee(102, "Tom (Teller)", "Teller"));

        // 3. Initialize Shared Customers
        createSharedAccount("Alice (Default)", 1000.0, mainBranch);
        createSharedAccount("Bob (Test Transfer)", 500.0, downtownBranch);

        // 4. Setup Modern Look and Feel
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) { /* Ignore */ }

        // 5. Launch Both GUIs simultaneously
        SwingUtilities.invokeLater(() -> {
            customerGUI = new ModernBankGUI();
            employeeGUI = new EmployeePortalGUI();

            // Position them side by side on your screen
            customerGUI.setLocation(100, 100);
            employeeGUI.setLocation(880, 100);

            customerGUI.setVisible(true);
            employeeGUI.setVisible(true);
        });
    }

    // Helper to generate accounts into the shared database
    public static void createSharedAccount(String name, double balance, BankBranch branch) {
        Customer c = new Customer(name, "C00" + (allAccounts.size() + 1));
        BankAccount acc = new SavingsAccount(c, balance);
        branch.addAccount(acc);
        allAccounts.add(acc);
        accountNames.put(acc, name);
    }

    // --- SYNCHRONIZATION TRIGGER ---
    // When one GUI does something, it calls this to update the other GUI's labels
    public static void updateAllScreens() {
        if (customerGUI != null) customerGUI.refreshData();
        if (employeeGUI != null) employeeGUI.refreshData();
    }
}