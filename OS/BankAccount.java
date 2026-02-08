import java.util.Timer;
import java.util.TimerTask;

public class BankAccount {
    // [Pg 2, 4] Fields for state
    private String name; 
    private double balance;
    private int recursionDepth = 0; // Added for controlled recursion

    // Constructor
    public BankAccount(String name, double initialBalance) {
        this.name = name;
        this.balance = initialBalance;
    }

    // [Pg 2] Basic Method: getBalance
    public double getBalance() {
        return balance;
    }

    // [Pg 2] Basic Method: withdraw
    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            System.out.println(name + " withdrew: " + amount);
        }
    }

    // [Pg 2, 3] Basic Method + Nested Call Requirement
    // Requirement: "Create multiple nested method calls (e.g. deposit -> validate -> updateBalance)"
    public void deposit(double amount) {
        if (amount > 0) {
            System.out.println("1. Method deposit() called.");
            balance += amount;
            validate(); // Nested Call 1
        }
    }

    // [Pg 3] Nested Method 1
    private void validate() {
        System.out.println("2. Method validate() called (Stack Frame created).");
        updateBalance(); // Nested Call 2
    }

    // [Pg 3] Nested Method 2
    private void updateBalance() {
        System.out.println("3. Method updateBalance() called (Stack Frame created).");
        System.out.println("   (Stack Unwinding begins after this returns...)");
    }

    // [Pg 3] Requirement: "Cause a StackOverflowError by creating excessive recursive calls"
    public void causeStackOverflow() {
        recursionDepth++;
        System.out.println("Pushing stack frame... (Depth: " + recursionDepth + ")");
        if (recursionDepth < 1000) {
            causeStackOverflow(); // Controlled recursion
        } else {
            System.out.println("Stack depth limit reached. Stopping recursion.");
            recursionDepth = 0; // Reset
        }
    }

    // [Pg 8] Requirement: "MUST implement some scheduling concepts"
    // We demonstrate a scheduled task (e.g., adding interest after 1 second)
    public void scheduleInterestTask() {
        Timer timer = new Timer();
        System.out.println(">> Scheduler: Interest calculation scheduled for 1 second later.");
        
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                balance += 10.0; // Add interest
                System.out.println(">> Scheduler: Interest added. New Balance: " + balance);
                timer.cancel(); // Stop the timer thread
            }
        }, 1000); // 1000ms delay
    }

    // [Pg 6] Requirement: "Optimize memory efficiency" (StringBuilder)
    public void printStatementEfficiently() {
        // Bad way: String s = "A" + "B" + "C"; (Creates multiple objects in Heap)
        // Good way (Required):
        StringBuilder sb = new StringBuilder();
        sb.append("Account: ").append(name);
        sb.append(" | Balance: ").append(balance);
        sb.append(" | Status: Active");
        System.out.println(sb.toString());
    }
}