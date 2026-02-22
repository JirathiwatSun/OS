public class TestBank {

    public static void main(String[] args) {

        Customer c1 = new Customer("Alice", "C001");
        SavingsAccount acc1 = new SavingsAccount(c1, 1000);

        acc1.deposit(500);
        acc1.withdraw(200);

        System.out.println("Balance Inquiry: " + acc1.getBalance());

        BankBranch branch = new BankBranch("Main Branch", "Bangkok");
        branch.addAccount(acc1);
        branch.branchReport();

        Employee emp = new Employee(101, "John", "Manager");
        emp.setAccBalance(acc1, 2000);

        CountryFinancial government = new CountryFinancial(100000);
        government.collectTax(acc1);

        BankScheduler scheduler = new BankScheduler();
        scheduler.startInterestTimer(acc1);
    }

    public static void primitiveVsReferenceTest(double val, BankAccount ref) {

        val += 1000;      // Primitive → only local STACK copy changes
        ref.deposit(1000); // Reference → modifies HEAP object
    }

    public static void testStackOverflow() {
        testStackOverflow(); // Infinite recursion
    }
}