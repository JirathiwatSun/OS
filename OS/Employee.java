public class Employee {

    private int employeeId;
    private String name;
    private String role;

    public Employee(int employeeId, String name, String role) {
        this.employeeId = employeeId;
        this.name = name;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setAccBalance(BankAccount account, double newBalance) {

        if (!role.equalsIgnoreCase("Manager")) {
            System.out.println("Employee ID " + employeeId +
                    " (" + name + ") is NOT authorized to change balance.");
            return;
        }

        double current = account.getBalance();

        if (newBalance > current) {
            account.deposit(newBalance - current);
        } else {
            account.withdraw(current - newBalance);
        }

        System.out.println("Balance adjusted by Manager " + name);
    }

    public void setAccName(BankAccount account, String newName) {

        System.out.println("Employee ID: " + employeeId);
        System.out.println("Employee Name: " + name);
        System.out.println("Role: " + role);
        System.out.println("Requested account name change to: " + newName);
    }
}