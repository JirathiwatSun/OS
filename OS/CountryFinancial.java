public class CountryFinancial {

    private double countryBalance;        // National reserve
    public static final double TAX_RATE = 0.02;

    public CountryFinancial(double countryBalance) {
        this.countryBalance = countryBalance;
    }

    public double getSumBalance() {
        return countryBalance;
    }

    public void collectTax(BankAccount account) {

        double tax = account.getBalance() * TAX_RATE;

        if (tax > 0) {
            account.withdraw(tax);
            countryBalance += tax;

            System.out.println("Tax Collected: " + tax);
            System.out.println("Updated National Reserve: " + countryBalance);
        }
    }
}