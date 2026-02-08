public class BankAccount {
    private double balance; // [cite: 14]
    private String name;    // Added for Heap Allocator task [cite: 54]

    // Constructor
    public BankAccount(String name, double initialBalance) {
        this.name = name;
        this.balance = initialBalance;
    }

    // Method to deposit money [cite: 15]
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            validate(); // Nested call for Stack Analysis [cite: 27]
        }
    }

    // Method to withdraw money [cite: 12]
    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
        }
    }

    // Method to check balance [cite: 13]
    public double getBalance() {
        return balance;
    }

    // Method to get account name
    public String getName() {
        return name;
    }

    // Helper method for Stack Trace demonstration
    private void validate() {
        updateBalance(); // Nested call [cite: 27]
    }

    private void updateBalance() {
        // Logic to update database or logs
        System.out.println("Balance validated and updated.");
    }
    
    // Recursive method to trigger StackOverflowError 
    public void recursiveCheck() {
        recursiveCheck(); 
    }
}

class TestBank {
    public static void main(String[] args) {
        // 1. Basic Operation Test
        BankAccount myAccount = new BankAccount("John", 1000.50); // [cite: 54]
        myAccount.deposit(500.0);
        myAccount.withdraw(200.0);
        System.out.println("Current Balance: " + myAccount.getBalance());
    }
}