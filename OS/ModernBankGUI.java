import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.OutputStream;
import java.io.PrintStream;

public class ModernBankGUI extends JFrame {

    private BankAccount currentAccount;
    private JComboBox<String> comboActiveAccount, comboTransferTarget, comboBranches;
    private JLabel lblBalance;
    private JTextField txtAmount, txtTransferAmount, txtNewName, txtNewInitialDeposit;
    private JTextArea txtConsole;

    public ModernBankGUI() {
        comboActiveAccount = new JComboBox<>();
        comboTransferTarget = new JComboBox<>();

        setTitle("Customer Portal - Modern Bank");
        setSize(800, 800); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(244, 246, 249));

        if (!BankSystemLauncher.allAccounts.isEmpty()) {
            currentAccount = BankSystemLauncher.allAccounts.get(0);
        }

        add(createHeaderPanel(), BorderLayout.NORTH);
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 16)); 
        tabbedPane.addTab("🏦 Operations", createOperationsTab());
        tabbedPane.addTab("💸 Transfer Money", createTransferTab());
        tabbedPane.addTab("📝 Open Account", createNewAccountTab());
        add(tabbedPane, BorderLayout.CENTER);
        add(createConsolePanel(), BorderLayout.SOUTH);

        redirectSystemStreams();
        refreshData();
    }

    public void refreshData() {
        if (currentAccount != null) {
            lblBalance.setText("$" + String.format("%.2f", currentAccount.getBalance()));
        }
        
        ActionListener[] listeners = comboActiveAccount.getActionListeners();
        for (ActionListener al : listeners) comboActiveAccount.removeActionListener(al);

        comboActiveAccount.removeAllItems();
        comboTransferTarget.removeAllItems();

        for (BankAccount acc : BankSystemLauncher.allAccounts) {
            String displayName = BankSystemLauncher.accountNames.get(acc) + " - [$" + String.format("%.2f", acc.getBalance()) + "]";
            comboActiveAccount.addItem(displayName);
            comboTransferTarget.addItem(displayName);
        }

        if (currentAccount != null && BankSystemLauncher.allAccounts.contains(currentAccount)) {
            comboActiveAccount.setSelectedIndex(BankSystemLauncher.allAccounts.indexOf(currentAccount));
        }

        for (ActionListener al : listeners) comboActiveAccount.addActionListener(al);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(41, 128, 185));
        panel.setBorder(new EmptyBorder(20, 30, 20, 30));

        JPanel topSwitcher = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topSwitcher.setOpaque(false);
        JLabel lblSwitch = new JLabel("Active Account: ");
        lblSwitch.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblSwitch.setForeground(Color.WHITE);
        topSwitcher.add(lblSwitch);
        
        comboActiveAccount.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        comboActiveAccount.setPreferredSize(new Dimension(300, 40));
        comboActiveAccount.addActionListener(e -> {
            int index = comboActiveAccount.getSelectedIndex();
            if (index >= 0 && index < BankSystemLauncher.allAccounts.size()) {
                currentAccount = BankSystemLauncher.allAccounts.get(index);
                refreshData();
            }
        });
        topSwitcher.add(comboActiveAccount);
        panel.add(topSwitcher, BorderLayout.NORTH);

        lblBalance = new JLabel();
        lblBalance.setForeground(Color.WHITE);
        lblBalance.setFont(new Font("Segoe UI", Font.BOLD, 55)); 
        lblBalance.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(lblBalance, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createOperationsTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(244, 246, 249));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15); 
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblAmount = new JLabel("Transaction Amount:");
        lblAmount.setFont(new Font("Segoe UI", Font.BOLD, 18));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; panel.add(lblAmount, gbc);
        
        gbc.gridy = 1; 
        txtAmount = createStyledTextField(); 
        panel.add(txtAmount, gbc);

        gbc.gridy = 2; gbc.gridwidth = 1; 
        JButton btnDeposit = createStyledButton("Deposit", new Color(39, 174, 96));
        btnDeposit.addActionListener(e -> { 
            if(currentAccount != null && !txtAmount.getText().isEmpty()) { 
                currentAccount.deposit(Double.parseDouble(txtAmount.getText())); 
                BankSystemLauncher.updateAllScreens(); 
                txtAmount.setText(""); 
            }
        }); panel.add(btnDeposit, gbc);

        gbc.gridx = 1; 
        JButton btnWithdraw = createStyledButton("Withdraw", new Color(192, 57, 43));
        btnWithdraw.addActionListener(e -> { 
            if(currentAccount != null && !txtAmount.getText().isEmpty()) { 
                currentAccount.withdraw(Double.parseDouble(txtAmount.getText())); 
                BankSystemLauncher.updateAllScreens(); 
                txtAmount.setText(""); 
            }
        }); panel.add(btnWithdraw, gbc);

        gbc.gridx = 0; gbc.gridy = 3; 
        JButton btnTax = createStyledButton("Collect Tax", new Color(142, 68, 173));
        btnTax.addActionListener(e -> { BankSystemLauncher.government.collectTax(currentAccount); BankSystemLauncher.updateAllScreens(); }); panel.add(btnTax, gbc);

        gbc.gridx = 1; 
        JButton btnInterest = createStyledButton("Auto-Interest (3s)", new Color(243, 156, 18));
        btnInterest.addActionListener(e -> {
            BankSystemLauncher.scheduler.startInterestTimer(currentAccount);
            new Timer(3500, evt -> { BankSystemLauncher.updateAllScreens(); ((Timer)evt.getSource()).stop(); }).start();
        }); panel.add(btnInterest, gbc);

        return panel;
    }

    private JPanel createTransferTab() {
        JPanel panel = new JPanel(new GridBagLayout()); 
        panel.setBackground(new Color(244, 246, 249));
        GridBagConstraints gbc = new GridBagConstraints(); 
        gbc.insets = new Insets(15, 15, 15, 15); gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTarget = new JLabel("Transfer To:"); lblTarget.setFont(new Font("Segoe UI", Font.BOLD, 18));
        gbc.gridx = 0; gbc.gridy = 0; panel.add(lblTarget, gbc);
        
        comboTransferTarget.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        comboTransferTarget.setPreferredSize(new Dimension(300, 45));
        gbc.gridx = 1; panel.add(comboTransferTarget, gbc);
        
        JLabel lblAmt = new JLabel("Amount to Send:"); lblAmt.setFont(new Font("Segoe UI", Font.BOLD, 18));
        gbc.gridx = 0; gbc.gridy = 1; panel.add(lblAmt, gbc);
        
        txtTransferAmount = createStyledTextField(); 
        gbc.gridx = 1; panel.add(txtTransferAmount, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        JButton btnTransfer = createStyledButton("Execute Transfer", new Color(52, 152, 219));
        btnTransfer.addActionListener(e -> {
            int targetIndex = comboTransferTarget.getSelectedIndex();
            if (targetIndex >= 0 && currentAccount != BankSystemLauncher.allAccounts.get(targetIndex)) {
                double amt = Double.parseDouble(txtTransferAmount.getText());
                currentAccount.withdraw(amt);
                BankSystemLauncher.allAccounts.get(targetIndex).deposit(amt);
                BankSystemLauncher.updateAllScreens();
                txtTransferAmount.setText("");
            }
        }); panel.add(btnTransfer, gbc);
        return panel;
    }

    private JPanel createNewAccountTab() {
        JPanel panel = new JPanel(new GridBagLayout()); 
        panel.setBackground(new Color(244, 246, 249));
        GridBagConstraints gbc = new GridBagConstraints(); 
        gbc.insets = new Insets(15, 15, 15, 15); gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblName = new JLabel("Customer Name:"); lblName.setFont(new Font("Segoe UI", Font.BOLD, 18));
        gbc.gridx = 0; gbc.gridy = 0; panel.add(lblName, gbc);
        gbc.gridx = 1; txtNewName = createStyledTextField(); panel.add(txtNewName, gbc);
        
        JLabel lblInit = new JLabel("Initial Deposit:"); lblInit.setFont(new Font("Segoe UI", Font.BOLD, 18));
        gbc.gridx = 0; gbc.gridy = 1; panel.add(lblInit, gbc);
        gbc.gridx = 1; txtNewInitialDeposit = createStyledTextField(); panel.add(txtNewInitialDeposit, gbc);
        
        JLabel lblBranch = new JLabel("Select Branch:"); lblBranch.setFont(new Font("Segoe UI", Font.BOLD, 18));
        gbc.gridx = 0; gbc.gridy = 2; panel.add(lblBranch, gbc);
        
        comboBranches = new JComboBox<>(new String[]{"Main Branch", "Downtown Branch"}); 
        comboBranches.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        comboBranches.setPreferredSize(new Dimension(300, 45));
        gbc.gridx = 1; panel.add(comboBranches, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        JButton btnCreate = createStyledButton("Open New Account", new Color(46, 204, 113));
        btnCreate.addActionListener(e -> {
            BankSystemLauncher.createSharedAccount(txtNewName.getText(), Double.parseDouble(txtNewInitialDeposit.getText()), BankSystemLauncher.branches.get(comboBranches.getSelectedIndex()));
            BankSystemLauncher.updateAllScreens();
            txtNewName.setText(""); txtNewInitialDeposit.setText("");
        }); panel.add(btnCreate, gbc);
        return panel;
    }

    private JScrollPane createConsolePanel() {
        txtConsole = new JTextArea(12, 50); 
        txtConsole.setEditable(false); 
        txtConsole.setBackground(new Color(30,30,30)); 
        txtConsole.setForeground(new Color(166,226,46));
        txtConsole.setFont(new Font("Consolas", Font.PLAIN, 14));
        JScrollPane sp = new JScrollPane(txtConsole); 
        sp.setBorder(BorderFactory.createTitledBorder(new EmptyBorder(10, 10, 10, 10), " System Logs ")); 
        return sp;
    }

    private void redirectSystemStreams() {
        PrintStream previousOut = System.out; 

        OutputStream out = new OutputStream() {
            @Override
            public void write(int b) {
                try { previousOut.write(b); } catch (Exception e) {} 
                SwingUtilities.invokeLater(() -> {
                    txtConsole.append(String.valueOf((char) b));
                    txtConsole.setCaretPosition(txtConsole.getDocument().getLength());
                });
            }

            @Override
            public void write(byte[] b, int off, int len) {
                try { previousOut.write(b, off, len); } catch (Exception e) {} 
                String text = new String(b, off, len);
                SwingUtilities.invokeLater(() -> {
                    txtConsole.append(text);
                    txtConsole.setCaretPosition(txtConsole.getDocument().getLength());
                });
            }
        };

        System.setOut(new PrintStream(out, true)); 
    }

    // --- UI Styling Helpers ---
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 18)); 
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(200, 55)); 
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 20)); 
        field.setHorizontalAlignment(JTextField.CENTER);
        field.setPreferredSize(new Dimension(250, 45)); 
        return field;
    }
}