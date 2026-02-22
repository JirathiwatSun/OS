public class BankAccount implements BankService {

    // ================= HEAP =================
    private Customer owner;       // Reference stored in HEAP
    private double balance;       // Primitive inside HEAP object
    private int recursionDepth = 0;

    public BankAccount(Customer owner, double initialBalance) {
        this.owner = owner;
        this.balance = initialBalance;
    }

    @Override
    public double getBalance() {
        return balance;  // Returned to STACK
    }

    @Override
    public void deposit(double amount) {

        if (amount > 0) {

            System.out.println("\n[STACK] deposit() called");
            balance += amount;

            validate("Deposit", amount);  // Nested call
        }
    }

    @Override
    public void withdraw(double amount) {

        if (amount > 0 && amount <= balance) {

            System.out.println("\n[STACK] withdraw() called");
            balance -= amount;

            validate("Withdraw", amount); // Nested call
        } else {
            System.out.println("Insufficient balance.");
        }
    }

    // Required by UML
    public void validate() {
        System.out.println("[STACK] validate() called");
    }

    private void validate(String type, double amount) {

        recursionDepth++;
        System.out.println("[STACK] validate(type, amount) called");

        if (balance >= 0) {
            updateBalance(type, amount);
        }

        recursionDepth--;  // Simulate stack unwinding
    }

    // Required by UML
    public void updateBalance() {
        System.out.println("[STACK] updateBalance() called");
    }

    private void updateBalance(String type, double amount) {

        System.out.println("[STACK] updateBalance(type, amount) called");

        if (recursionDepth > 10) {
            System.out.println("Warning: Deep stack level detected!");
        }

        StringBuilder sb = new StringBuilder();

        sb.append("Owner: ").append(owner.getName())
          .append("\nTransaction: ").append(type)
          .append("\nAmount: ").append(amount)
          .append("\nNew Balance: ").append(balance)
          .append("\nCurrent Stack Depth: ").append(recursionDepth)
          .append("\n----------------------------");

        System.out.println(sb.toString());

        owner.processTransaction(amount, type);
    }
}