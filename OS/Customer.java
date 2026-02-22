public class Customer {

    // ================= HEAP =================
    private String name;        // Stored in HEAP
    private String customerID;  // Stored in HEAP

    public Customer(String name, String customerID) {
        this.name = name;
        this.customerID = customerID;
    }

    public String getName() {
        return name;  // Returned to STACK temporarily
    }

    public void processTransaction(double amount, String type) {

        // amount & type are stored in STACK (method parameters)

        System.out.println("Customer ID: " + customerID);
        System.out.println("Customer Name: " + name);
        System.out.println("Transaction Type: " + type);
        System.out.println("Transaction Amount: " + amount);
        System.out.println("Transaction processed successfully.");
        System.out.println("--------------------------------------");
    }
}