public class TestBank {

    public static void main(String[] args) {

        System.out.println("=================================");
        System.out.println("TASK 1: HEAP ALLOCATION DEMO");
        System.out.println("Creating Customer and SavingsAccount objects.");
        System.out.println("Objects will be stored in HEAP, references in STACK.");
        System.out.println("=================================\n");

        Customer c1 = new Customer("Alice", "C001");
        SavingsAccount acc1 = new SavingsAccount(c1, 1000);


        System.out.println("=================================");
        System.out.println("TASK 2: DEPOSIT (Nested Stack Demo)");
        System.out.println("Calling deposit() → validate() → updateBalance()");
        System.out.println("This will create multiple STACK frames.");
        System.out.println("=================================\n");

        acc1.deposit(500);


        System.out.println("=================================");
        System.out.println("TASK 3: WITHDRAW (Nested Stack Demo)");
        System.out.println("Calling withdraw() → validate() → updateBalance()");
        System.out.println("Stack frames will be created and unwound.");
        System.out.println("=================================\n");

        acc1.withdraw(200);


        System.out.println("=================================");
        System.out.println("TASK 4: BALANCE INQUIRY");
        System.out.println("Calling getBalance().");
        System.out.println("Primitive double value returned via STACK.");
        System.out.println("=================================\n");

        System.out.println("Balance Inquiry Result: " + acc1.getBalance());


        System.out.println("=================================");
        System.out.println("TASK 5: BANK BRANCH REPORT");
        System.out.println("Demonstrating object aggregation and total calculation.");
        System.out.println("=================================\n");

        BankBranch branch = new BankBranch("Main Branch", "Bangkok");
        branch.addAccount(acc1);
        branch.branchReport();


        System.out.println("=================================");
        System.out.println("TASK 6: EMPLOYEE AUTHORIZATION");
        System.out.println("Manager adjusts account balance.");
        System.out.println("Demonstrates role-based access control.");
        System.out.println("=================================\n");

        Employee emp = new Employee(101, "John", "Manager");
        emp.setAccBalance(acc1, 2000);


        System.out.println("=================================");
        System.out.println("TASK 7: GOVERNMENT TAX COLLECTION");
        System.out.println("Applying TAX_RATE and updating national reserve.");
        System.out.println("=================================\n");

        CountryFinancial government = new CountryFinancial(100000);
        government.collectTax(acc1);


        System.out.println("=================================");
        System.out.println("TASK 8: SCHEDULER (Background Thread)");
        System.out.println("Interest will be applied after 3 seconds.");
        System.out.println("Demonstrates scheduling and multithreading.");
        System.out.println("=================================\n");

        BankScheduler scheduler = new BankScheduler();
        scheduler.startInterestTimer(acc1);


        // ================= HEAP REFERENCE REASSIGNMENT =================
        System.out.println("=================================");
        System.out.println("TASK 9: HEAP REFERENCE REASSIGNMENT");
        System.out.println("Reassigning acc1 to a new SavingsAccount object.");
        System.out.println("Old object becomes unreachable and eligible for GC.");
        System.out.println("=================================\n");

        SavingsAccount acc2 = new SavingsAccount(c1, 3000);
        acc1 = acc2; // old SavingsAccount object now eligible for GC
        System.gc();

        System.out.println("Old object now eligible for Garbage Collection.");


        System.out.println("=================================");
        System.out.println("TASK 10: PRIMITIVE VS REFERENCE DEMO");
        System.out.println("Primitive will NOT change original value.");
        System.out.println("Object reference WILL modify HEAP object.");
        System.out.println("=================================\n");

        double val = 100;
        primitiveVsReferenceTest(val, acc1);

        System.out.println("Primitive value after method call: " + val);
        System.out.println("Account balance after reference modification: " + acc1.getBalance());


        System.out.println("=================================");
        System.out.println("TASK 11: STACK OVERFLOW (NOT EXECUTED)");
        System.out.println("If testStackOverflow() is called,");
        System.out.println("it will cause infinite recursion and StackOverflowError.");
        System.out.println("=================================\n");

        // testStackOverflow(); // DO NOT RUN
    }


    // ================= PRIMITIVE VS REFERENCE =================
    public static void primitiveVsReferenceTest(double val, BankAccount ref) {

        val += 1000;       // Primitive → STACK copy only
        ref.deposit(1000); // Reference → modifies HEAP object
    }


    // ================= STACK OVERFLOW DEMO =================
    public static void testStackOverflow() {
        testStackOverflow(); // Infinite recursion
    }
}