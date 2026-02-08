public class TestBank {
    public static void main(String[] args) {
        System.out.println("====== MIDTERM PROJECT PART 1 ======");

        // --- PART 1: STACK POINTER ANALYSIS ---
        System.out.println("\n[1] Testing Stack Trace (Nested Calls)");
        BankAccount acc1 = new BankAccount("John", 1000.0);
        acc1.deposit(500.0); // This triggers deposit -> validate -> updateBalance

        // UNCOMMENT the line below to prove the StackOverflowError requirement
        // System.out.println("Causing Crash...");
        // acc1.causeStackOverflow(); 

        
        // --- PART 2: HEAP ALLOCATOR & GARBAGE COLLECTION ---
        System.out.println("\n[2] Testing Heap & Garbage Collection");
        // Creating multiple objects in Heap
        BankAccount acc2 = new BankAccount("Alice", 2000.0);
        BankAccount acc3 = new BankAccount("Bob", 500.0);
        
        System.out.println("Before Reassignment:");
        System.out.println("acc2 is Alice, acc3 is Bob");

        // "Demonstrate what happens when references are reassigned"
        acc2 = acc3; 
        System.out.println("After Reassignment (acc2 = acc3):");
        System.out.println("acc2 now points to Bob's data (Balance: " + acc2.getBalance() + ")");
        
        // "Trigger garbage collection by nullifying references"
        acc3 = null; 
        // NOTE: Bob is still safe (acc2 points to him). Alice is lost (no one points to her).
        // Alice's memory is now eligible for Garbage Collection.
        System.gc(); 
        System.out.println("Garbage Collection suggested (Alice's object is unreachable).");


        // --- PART 3: PRIMITIVE VS REFERENCE ---
        System.out.println("\n[3] Testing Primitive vs Reference");
        double primitiveVal = 500.0;
        
        System.out.println("Primitive before method: " + primitiveVal);
        modifyPrimitive(primitiveVal);
        System.out.println("Primitive after method: " + primitiveVal + " (Unchanged - Pass by Value)");

        System.out.println("Object Balance before method: " + acc1.getBalance());
        modifyReference(acc1);
        System.out.println("Object Balance after method: " + acc1.getBalance() + " (Changed - Pass by Reference)");


        // --- PART 4: STRING IMMUTABILITY ---
        System.out.println("\n[4] Testing String Immutability & Optimization");
        String s1 = "Customer";
        String s2 = s1;
        
        // Modifying s1 creates a NEW object. s2 still points to the OLD object.
        s1 = s1 + " (VIP)"; 
        
        System.out.println("s1: " + s1 + " (New Object created)");
        System.out.println("s2: " + s2 + " (Still points to old Object)");
        
        // Efficient way using StringBuilder (from BankAccount class)
        acc1.printStatementEfficiently();


        // --- PART 5: SCHEDULING ---
        System.out.println("\n[5] Testing Scheduling Concepts");
        // This will run in the background while main() finishes
        acc1.scheduleInterestTask();
        
        System.out.println(">> Main Method functionality complete.");
        System.out.println(">> (Notice the program keeps running until the Scheduler finishes...)");
    }

    // Helper method for Primitive testing
    public static void modifyPrimitive(double val) {
        val = val + 1000.0; // Changes copy only
    }

    // Helper method for Reference testing
    public static void modifyReference(BankAccount acc) {
        acc.withdraw(100.0); // Changes actual object
    }
}