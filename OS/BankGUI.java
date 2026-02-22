import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.OutputStream;
import java.io.PrintStream;

public class BankGUI extends JFrame {

    // Domain objects
    private Customer currentCustomer;
    private SavingsAccount currentAccount;
    private CountryFinancial government;
    private BankScheduler scheduler;

    // GUI Components
    private JLabel lblName;
    private JLabel lblBalance;
    private JTextField txtAmount;
    private JTextArea txtConsole;

    public BankGUI() {
        // Initialize Domain Objects
        currentCustomer = new Customer("Alice (GUI User)", "C001");
        currentAccount = new SavingsAccount(currentCustomer, 1000.0);
        government = new CountryFinancial(100000);
        scheduler = new BankScheduler();

        // Setup Main Window
        setTitle("Banking System - Unit 5");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Top Panel: Account Info
        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        topPanel.setBorder(BorderFactory.createTitledBorder("Account Information"));
        lblName = new JLabel(" Customer Name: " + currentCustomer.getName());
        lblBalance = new JLabel(" Current Balance: $" + currentAccount.getBalance());
        lblName.setFont(new Font("Arial", Font.BOLD, 14));
        lblBalance.setFont(new Font("Arial", Font.BOLD, 14));
        topPanel.add(lblName);
        topPanel.add(lblBalance);
        add(topPanel, BorderLayout.NORTH);

        // Center Panel: Controls
        JPanel centerPanel = new JPanel();
        centerPanel.setBorder(BorderFactory.createTitledBorder("Transactions"));
        
        centerPanel.add(new JLabel("Amount: $"));
        txtAmount = new JTextField(10);
        centerPanel.add(txtAmount);

        JButton btnDeposit = new JButton("Deposit");
        JButton btnWithdraw = new JButton("Withdraw");
        JButton btnTax = new JButton("Collect Tax");
        JButton btnInterest = new JButton("Start Interest Scheduler");

        centerPanel.add(btnDeposit);
        centerPanel.add(btnWithdraw);
        centerPanel.add(btnTax);
        centerPanel.add(btnInterest);
        add(centerPanel, BorderLayout.CENTER);

        // Bottom Panel: Console Output
        txtConsole = new JTextArea(15, 50);
        txtConsole.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(txtConsole);
        scrollPane.setBorder(BorderFactory.createTitledBorder("System Console & Memory Log"));
        add(scrollPane, BorderLayout.SOUTH);

        // Redirect System.out.println to the JTextArea
        redirectSystemStreams();

        // --- Action Listeners ---
        
        btnDeposit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double amount = Double.parseDouble(txtAmount.getText());
                    currentAccount.deposit(amount);
                    updateBalanceLabel();
                    txtAmount.setText("");
                } catch (NumberFormatException ex) {
                    System.out.println("Error: Please enter a valid number.");
                }
            }
        });

        btnWithdraw.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double amount = Double.parseDouble(txtAmount.getText());
                    currentAccount.withdraw(amount);
                    updateBalanceLabel();
                    txtAmount.setText("");
                } catch (NumberFormatException ex) {
                    System.out.println("Error: Please enter a valid number.");
                }
            }
        });

        btnTax.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                government.collectTax(currentAccount);
                updateBalanceLabel();
            }
        });

        btnInterest.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Starting scheduler... Interest will apply in 3 seconds.");
                scheduler.startInterestTimer(currentAccount);
                
                // Thread to update GUI balance after timer triggers (3.5 seconds to be safe)
                new Timer(3500, evt -> updateBalanceLabel()).start();
            }
        });

        System.out.println("System Initialized. Ready for transactions.");
    }

    private void updateBalanceLabel() {
        lblBalance.setText(" Current Balance: $" + currentAccount.getBalance());
    }

    // Helper method to redirect standard output to the text area
    private void redirectSystemStreams() {
        OutputStream out = new OutputStream() {
            @Override
            public void write(int b) {
                updateTextArea(String.valueOf((char) b));
            }

            @Override
            public void write(byte[] b, int off, int len) {
                updateTextArea(new String(b, off, len));
            }

            @Override
            public void write(byte[] b) {
                write(b, 0, b.length);
            }
        };
        System.setOut(new PrintStream(out, true));
    }

    private void updateTextArea(final String text) {
        SwingUtilities.invokeLater(() -> {
            txtConsole.append(text);
            // Auto-scroll to the bottom
            txtConsole.setCaretPosition(txtConsole.getDocument().getLength());
        });
    }

    public static void main(String[] args) {
        // Run the GUI in the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            BankGUI gui = new BankGUI();
            gui.setVisible(true);
        });
    }
}