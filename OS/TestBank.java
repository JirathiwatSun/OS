public class TestBank {
    public static void main(String[] args) {
        System.out.println("=== PART 1: STACK POINTER ANALYSIS ===");
        // [Pg 4] Requirement: "Create multiple BankAccount objects"
        BankAccount acc1 = new BankAccount("John", 1000.0);
        
        // [Pg 3] Requirement: "Trace call stack" (deposit -> validate -> updateBalance)
        acc1.deposit(500.0);

        // [Pg 3] Requirement: "Cause StackOverflowError"
        // Uncomment the line below to test the crash (Required for discussion)
        // acc1.causeStackOverflow(); 

        System.out.println("\n=== PART 2: HEAP ALLOCATOR & GC ===");
        // [Pg 4] Requirement: "Create multiple objects... Show allocation"
        BankAccount acc2 = new BankAccount("Alice", 2000.0);
        BankAccount acc3 = new BankAccount("Bob", 500.0);

        // [Pg 4] Requirement: "Demonstrate what happens when references are reassigned"
        System.out.println("Before reassignment: acc2 is Alice");
        System.out.println("Alice's balance: " + acc2.getBalance());
        acc2 = acc3; 
        System.out.println("After reassignment (acc2 = acc3): acc2 is now Bob");

        // [Pg 4] Requirement: "Trigger garbage collection... by nullifying references"
        acc3 = null; 
        // Now the original "Alice" object has no reference pointing to it -> Eligible for GC.
        // The "Bob" object is still pointed to by acc2.
        System.gc(); // Suggest GC to run (demonstration purposes)

        System.out.println("\n=== PART 3: REFERENCE VS PRIMITIVE EXPERT ===");
        double primitiveBalance = 500.0;
        System.out.println("Primitive before: " + primitiveBalance);
        modifyPrimitive(primitiveBalance);
        System.out.println("Primitive after: " + primitiveBalance + " (No Change)");

        System.out.println("Object Balance before: " + acc1.getBalance());
        modifyReference(acc1);
        System.out.println("Object Balance after: " + acc1.getBalance() + " (Changed!)");

        System.out.println("\n=== PART 4: STRING IMMUTABILITY ===");
        // [Pg 6] Requirement: Show String immutability
        String s1 = "Customer";
        String s2 = s1; 
        s1 = s1 + " (VIP)"; // Creates NEW object in Heap, s2 still points to old "Customer"
        System.out.println("s1: " + s1); // Customer (VIP)
        System.out.println("s2: " + s2); // Customer

        // [Pg 6] Optimization requirement
        acc1.printStatementEfficiently();

        System.out.println("\n=== PART 5: SCHEDULING CONCEPTS ===");
        // [Pg 8] Requirement: "Implement some scheduling concepts"
        acc1.scheduleInterestTask();
        
        // Wait for scheduled task to complete before program exits
        try {
            Thread.sleep(2000); // Wait 2 seconds for the scheduled task
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // [Pg 5] Helper for Primitive vs Reference
    public static void modifyPrimitive(double val) {
        val = val + 1000.0; // Only modifies the copy on the Stack
    }

    // [Pg 5] Helper for Primitive vs Reference
    public static void modifyReference(BankAccount acc) {
        acc.withdraw(100.0); // Modifies the actual Object on the Heap
    }
}