import java.util.Timer;
import java.util.TimerTask;

public class BankAccount {
    // [Requirement: Heap Allocator] Fields stored in Heap
    private String name; 
    private double balance;

    // Constructor
    public BankAccount(String name, double initialBalance) {
        this.name = name;
        this.balance = initialBalance;
    }

    // [Requirement: Basic Functionality]
    public double getBalance() {
        return balance;
    }

    // [Requirement: Basic Functionality]
    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            System.out.println(name + " withdrew: " + amount);
        } else {
            System.out.println("Insufficient funds or invalid amount.");
        }
    }

    // [Requirement: Stack Pointer Analysis] 
    // "Create multiple nested method calls (deposit -> validate -> updateBalance)"
    public void deposit(double amount) {
        if (amount > 0) {
            System.out.println("\n--- Stack Trace Start ---");
            System.out.println("1. Method deposit() called [Stack Frame 1]");
            balance += amount;
            validate(); // Nested Call
        }
    }

    // Nested Method 1
    private void validate() {
        System.out.println("2. Method validate() called [Stack Frame 2]");
        updateBalance(); // Nested Call
    }

    // Nested Method 2
    private void updateBalance() {
        System.out.println("3. Method updateBalance() called [Stack Frame 3]");
        System.out.println("   (Data updated. Stack unwinding begins now...)");
        System.out.println("--- Stack Trace End ---\n");
    }

    // [Requirement: Stack Overflow]
    // "Cause a StackOverflowError by creating excessive recursive calls"
    public void causeStackOverflow() {
        // Recursive call without an exit condition
        causeStackOverflow(); 
    }

    // [Requirement: Scheduling Concepts]
    // "MUST implement some scheduling concepts for a particular purpose"
    public void scheduleInterestTask() {
        Timer timer = new Timer();
        System.out.println(">> [Scheduler] Background task started. Waiting 3 seconds to add interest...");

        // Schedule a task to run after 3000ms (3 seconds)
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                double interest = balance * 0.05; // 5% interest
                balance += interest;
                System.out.println("\n>> [Scheduler ALERT] 3 Seconds passed!");
                System.out.println(">> Interest of " + interest + " added.");
                System.out.println(">> New Balance for " + name + ": " + balance);
                timer.cancel(); // Stop the timer thread
            }
        }, 3000);
    }

    // [Requirement: String Optimization]
    // "Use StringBuilder instead of + concatenation for better memory efficiency"
    public void printStatementEfficiently() {
        StringBuilder sb = new StringBuilder();
        // Efficient memory usage (One object modified, rather than creating new Strings)
        sb.append("Account Report: ").append(name)
          .append(" | Balance: $").append(balance)
          .append(" | Status: Active");
        
        System.out.println(sb.toString());
    }
}