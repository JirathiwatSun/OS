import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.OutputStream;
import java.io.PrintStream;

public class ModernBankGUI extends JFrame {

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

    // Brand Colors
    private final Color PRIMARY_BLUE = new Color(41, 128, 185);
    private final Color BACKGROUND_GRAY = new Color(244, 246, 249);
    private final Color CONSOLE_BG = new Color(30, 30, 30);
    private final Color CONSOLE_TEXT = new Color(166, 226, 46); // Hacker Green

    public ModernBankGUI() {
        // 1. Initialize Domain Objects
        currentCustomer = new Customer("Alice", "C001");
        currentAccount = new SavingsAccount(currentCustomer, 1000.0);
        government = new CountryFinancial(100000);
        scheduler = new BankScheduler();

        // 2. Setup Main Window
        setTitle("Modern Banking System");
        setSize(700, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(BACKGROUND_GRAY);

        // 3. Header Panel (Sleek Blue Banner)
        JPanel headerPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        headerPanel.setBackground(PRIMARY_BLUE);
        headerPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        lblName = new JLabel("Welcome back, " + currentCustomer.getName());
        lblName.setForeground(Color.WHITE);
        lblName.setFont(new Font("Segoe UI", Font.PLAIN, 18));

        lblBalance = new JLabel("$" + String.format("%.2f", currentAccount.getBalance()));
        lblBalance.setForeground(Color.WHITE);
        lblBalance.setFont(new Font("Segoe UI", Font.BOLD, 36));

        headerPanel.add(lblName);
        headerPanel.add(lblBalance);
        add(headerPanel, BorderLayout.NORTH);

        // 4. Center Panel (Controls & Inputs)
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(BACKGROUND_GRAY);
        centerPanel.setBorder(new EmptyBorder(10, 30, 10, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Amount Input Row
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel lblPrompt = new JLabel("Enter Transaction Amount:");
        lblPrompt.setFont(new Font("Segoe UI", Font.BOLD, 14));
        centerPanel.add(lblPrompt, gbc);

        gbc.gridy = 1; gbc.gridwidth = 2;
        txtAmount = new JTextField();
        txtAmount.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        txtAmount.setHorizontalAlignment(JTextField.CENTER);
        txtAmount.setPreferredSize(new Dimension(200, 40));
        centerPanel.add(txtAmount, gbc);

        // Primary Action Buttons Row
        gbc.gridy = 2; gbc.gridwidth = 1; gbc.weightx = 0.5;
        JButton btnDeposit = createStyledButton("Deposit", new Color(39, 174, 96));
        centerPanel.add(btnDeposit, gbc);

        gbc.gridx = 1;
        JButton btnWithdraw = createStyledButton("Withdraw", new Color(192, 57, 43));
        centerPanel.add(btnWithdraw, gbc);

        // Secondary Action Buttons Row
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1;
        JButton btnTax = createStyledButton("Collect Tax", new Color(142, 68, 173));
        centerPanel.add(btnTax, gbc);

        gbc.gridx = 1;
        JButton btnInterest = createStyledButton("Auto-Interest (3s)", new Color(243, 156, 18));
        centerPanel.add(btnInterest, gbc);

        // NEW: Preview Projections Button Row
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; 
        JButton btnPreview = createStyledButton("Preview Tax & Interest", new Color(52, 73, 94));
        centerPanel.add(btnPreview, gbc);

        add(centerPanel, BorderLayout.CENTER);

        // 5. Bottom Panel (Dark Mode Console)
        txtConsole = new JTextArea(12, 50);
        txtConsole.setEditable(false);
        txtConsole.setBackground(CONSOLE_BG);
        txtConsole.setForeground(CONSOLE_TEXT);
        txtConsole.setFont(new Font("Consolas", Font.PLAIN, 13));
        txtConsole.setMargin(new Insets(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(txtConsole);
        TitledBorder consoleBorder = BorderFactory.createTitledBorder(" Live System Console & Memory Trace ");
        consoleBorder.setTitleColor(Color.GRAY);
        consoleBorder.setTitleFont(new Font("Segoe UI", Font.BOLD, 12));
        scrollPane.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(0, 20, 20, 20), consoleBorder));
        
        add(scrollPane, BorderLayout.SOUTH);

        // Redirect System.out to our slick console
        redirectSystemStreams();

        // 6. Action Listeners
        btnDeposit.addActionListener(e -> processTransaction(true));
        btnWithdraw.addActionListener(e -> processTransaction(false));

        btnTax.addActionListener(e -> {
            System.out.println("\n[SYSTEM] Government tax collection initiated...");
            government.collectTax(currentAccount);
            updateBalanceLabel();
        });

        btnInterest.addActionListener(e -> {
            System.out.println("\n[SYSTEM] Starting background scheduler...");
            System.out.println("[SYSTEM] You can continue making transactions. Interest will apply in 3 seconds.");
            scheduler.startInterestTimer(currentAccount);
            
            // Wait 3.5 seconds, then update GUI balance to reflect the background thread's work
            new Timer(3500, evt -> {
                updateBalanceLabel();
                ((Timer)evt.getSource()).stop(); // stop timer after one run
            }).start();
        });

        // NEW Action Listener for Preview feature
        btnPreview.addActionListener(e -> {
            showCalculationsPreview();
        });

        System.out.println("====== SYSTEM INITIALIZED ======");
        System.out.println("UI Loaded successfully.");
        System.out.println("Ready for transactions.");
    }

    // --- Helper Methods for Clean Code ---

    private void processTransaction(boolean isDeposit) {
        try {
            double amount = Double.parseDouble(txtAmount.getText());
            if (isDeposit) {
                currentAccount.deposit(amount);
            } else {
                currentAccount.withdraw(amount);
            }
            updateBalanceLabel();
            txtAmount.setText(""); // Clear input box
        } catch (NumberFormatException ex) {
            System.out.println("\n[ERROR] Invalid input! Please enter a numeric value.");
        }
    }

    private void updateBalanceLabel() {
        lblBalance.setText("$" + String.format("%.2f", currentAccount.getBalance()));
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(150, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void redirectSystemStreams() {
        OutputStream out = new OutputStream() {
            @Override
            public void write(int b) { updateTextArea(String.valueOf((char) b)); }
            @Override
            public void write(byte[] b, int off, int len) { updateTextArea(new String(b, off, len)); }
        };
        System.setOut(new PrintStream(out, true));
    }

    private void updateTextArea(final String text) {
        SwingUtilities.invokeLater(() -> {
            txtConsole.append(text);
            txtConsole.setCaretPosition(txtConsole.getDocument().getLength());
        });
    }

    // NEW Method: Preview Tax and Interest without modifying the account
    private void showCalculationsPreview() {
        double currentBalance = currentAccount.getBalance();
        
        // 1. Function to calculate tax (2% based on CountryFinancial)
        double expectedTax = currentBalance * CountryFinancial.TAX_RATE;
        
        // 2. Function to calculate interest (5% based on SavingsAccount)
        double expectedInterest = currentBalance * 0.05;
        
        // 3. Calculate what the balance would be after both
        double projectedBalance = (currentBalance + expectedInterest) - expectedTax;

        // Format the message nicely
        String message = String.format(
            "Based on your current balance of $%.2f:\n\n" +
            "Expected Tax Deduction (2%%):  -$%.2f\n" +
            "Expected Interest Earned (5%%):  +$%.2f\n" +
            "----------------------------------------\n" +
            "Projected Future Balance:       $%.2f",
            currentBalance, expectedTax, expectedInterest, projectedBalance
        );

        // Show a pop-up dialog with the calculations
        JOptionPane.showMessageDialog(
            this, 
            message, 
            "Financial Projections Preview", 
            JOptionPane.INFORMATION_MESSAGE
        );
        
        System.out.println("\n[SYSTEM] Previewed calculations for Tax and Interest.");
    }

    public static void main(String[] args) {
        // Activate "Nimbus" Look and Feel for a modern UI
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    
                    // Tweak some default Nimbus properties for better colors
                    UIManager.put("control", new Color(244, 246, 249));
                    UIManager.put("info", new Color(244, 246, 249));
                    UIManager.put("nimbusBlueGrey", new Color(41, 128, 185));
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Failed to initialize modern Look and Feel.");
        }

        SwingUtilities.invokeLater(() -> {
            ModernBankGUI gui = new ModernBankGUI();
            gui.setVisible(true);
        });
    }
}