public class SavingsAccount extends BankAccount {

    // ================= HEAP =================
    private double interestRate = 0.05;

    public SavingsAccount(Customer owner, double initialBalance) {
        super(owner, initialBalance);
    }

    @Override
    public void withdraw(double amount) {

        if (amount <= getBalance()) {
            super.withdraw(amount);
        } else {
            System.out.println("SavingsAccount: Insufficient funds.");
        }
    }

    public void applyYearlyInterest() {

        double interest = getBalance() * interestRate;

        System.out.println("Applying interest rate: " + interestRate);
        deposit(interest);
    }
}