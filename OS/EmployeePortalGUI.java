import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.OutputStream;
import java.io.PrintStream;

public class EmployeePortalGUI extends JFrame {

    private Employee activeEmployee;
    private BankAccount selectedAccount;

    private JComboBox<String> comboActiveEmployee, comboCustomerAccounts;
    private JLabel lblRoleDisplay;
    private JTextField txtNewBalance, txtNewName;
    private JTextArea txtConsole;

    public EmployeePortalGUI() {
        comboActiveEmployee = new JComboBox<>();
        comboCustomerAccounts = new JComboBox<>();

        setTitle("Bank Employee Portal");
        setSize(800, 750); // Increased Window Size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(244, 246, 249));

        if (!BankSystemLauncher.staffList.isEmpty()) activeEmployee = BankSystemLauncher.staffList.get(0);

        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createManagementPanel(), BorderLayout.CENTER);
        add(createConsolePanel(), BorderLayout.SOUTH);

        redirectSystemStreams();
        refreshData();
    }

    public void refreshData() {
        ActionListener[] listeners = comboCustomerAccounts.getActionListeners();
        for (ActionListener al : listeners) comboCustomerAccounts.removeActionListener(al);

        comboActiveEmployee.removeAllItems();
        for (Employee emp : BankSystemLauncher.staffList) comboActiveEmployee.addItem(emp.getName());
        if (activeEmployee != null) comboActiveEmployee.setSelectedIndex(BankSystemLauncher.staffList.indexOf(activeEmployee));

        int prevSelection = comboCustomerAccounts.getSelectedIndex();
        comboCustomerAccounts.removeAllItems();
        for (BankAccount acc : BankSystemLauncher.allAccounts) {
            String displayName = BankSystemLauncher.accountNames.get(acc) + " [$" + String.format("%.2f", acc.getBalance()) + "]";
            comboCustomerAccounts.addItem(displayName);
        }
        if (prevSelection >= 0 && prevSelection < BankSystemLauncher.allAccounts.size()) {
            comboCustomerAccounts.setSelectedIndex(prevSelection);
        }

        for (ActionListener al : listeners) comboCustomerAccounts.addActionListener(al);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(74, 35, 90)); // Admin Purple
        panel.setBorder(new EmptyBorder(20, 30, 20, 30));

        JPanel topSwitcher = new JPanel(new FlowLayout(FlowLayout.LEFT)); 
        topSwitcher.setOpaque(false);
        JLabel lblSwitch = new JLabel("Logged in as: "); 
        lblSwitch.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblSwitch.setForeground(Color.WHITE); 
        topSwitcher.add(lblSwitch);
        
        comboActiveEmployee.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        comboActiveEmployee.setPreferredSize(new Dimension(250, 40));
        comboActiveEmployee.addActionListener(e -> {
            int index = comboActiveEmployee.getSelectedIndex();
            if (index >= 0) {
                activeEmployee = BankSystemLauncher.staffList.get(index);
                lblRoleDisplay.setText(" Clearance Level: " + (activeEmployee.getName().contains("Manager") ? "MANAGER " : "RESTRICTED "));
            }
        }); 
        topSwitcher.add(comboActiveEmployee); 
        panel.add(topSwitcher, BorderLayout.NORTH);

        lblRoleDisplay = new JLabel(" Clearance Level: MANAGER "); 
        lblRoleDisplay.setForeground(Color.WHITE);
        lblRoleDisplay.setFont(new Font("Segoe UI", Font.BOLD, 30)); 
        lblRoleDisplay.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(lblRoleDisplay, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createManagementPanel() {
        JPanel panel = new JPanel(new GridBagLayout()); 
        panel.setBackground(new Color(244, 246, 249));
        panel.setBorder(BorderFactory.createTitledBorder(new EmptyBorder(10, 10, 10, 10), "Customer Management Controls"));
        GridBagConstraints gbc = new GridBagConstraints(); 
        gbc.insets = new Insets(15, 15, 15, 15); // Large Padding
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTarget = new JLabel("Target Account:"); lblTarget.setFont(new Font("Segoe UI", Font.BOLD, 18));
        gbc.gridx = 0; gbc.gridy = 0; panel.add(lblTarget, gbc);
        
        comboCustomerAccounts.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        comboCustomerAccounts.setPreferredSize(new Dimension(300, 45));
        gbc.gridx = 1; panel.add(comboCustomerAccounts, gbc);

        JLabel lblBal = new JLabel("Override Balance To:"); lblBal.setFont(new Font("Segoe UI", Font.BOLD, 18));
        gbc.gridx = 0; gbc.gridy = 1; panel.add(lblBal, gbc);
        
        gbc.gridx = 1; txtNewBalance = createStyledTextField(); panel.add(txtNewBalance, gbc);
        
        gbc.gridx = 2; 
        JButton btnSetBal = createStyledButton("Force Update", new Color(192, 57, 43));
        btnSetBal.addActionListener(e -> {
            int idx = comboCustomerAccounts.getSelectedIndex();
            if (idx >= 0 && !txtNewBalance.getText().isEmpty()) {
                selectedAccount = BankSystemLauncher.allAccounts.get(idx);
                activeEmployee.setAccBalance(selectedAccount, Double.parseDouble(txtNewBalance.getText()));
                BankSystemLauncher.updateAllScreens(); 
                txtNewBalance.setText("");
            }
        }); panel.add(btnSetBal, gbc);

        JLabel lblName = new JLabel("Update Name To:"); lblName.setFont(new Font("Segoe UI", Font.BOLD, 18));
        gbc.gridx = 0; gbc.gridy = 2; panel.add(lblName, gbc);
        
        gbc.gridx = 1; txtNewName = createStyledTextField(); panel.add(txtNewName, gbc);
        
        gbc.gridx = 2; 
        JButton btnSetName = createStyledButton("Update Profile", new Color(41, 128, 185));
        btnSetName.addActionListener(e -> {
            int idx = comboCustomerAccounts.getSelectedIndex();
            if (idx >= 0 && !txtNewName.getText().isEmpty()) {
                selectedAccount = BankSystemLauncher.allAccounts.get(idx);
                activeEmployee.setAccName(selectedAccount, txtNewName.getText());
                BankSystemLauncher.accountNames.put(selectedAccount, txtNewName.getText());
                BankSystemLauncher.updateAllScreens(); 
                txtNewName.setText("");
            }
        }); panel.add(btnSetName, gbc);
        return panel;
    }

    private JScrollPane createConsolePanel() {
        txtConsole = new JTextArea(12, 50); 
        txtConsole.setBackground(new Color(20,20,20)); 
        txtConsole.setForeground(new Color(241,196,15));
        txtConsole.setFont(new Font("Consolas", Font.PLAIN, 14));
        JScrollPane sp = new JScrollPane(txtConsole); 
        sp.setBorder(BorderFactory.createTitledBorder(new EmptyBorder(10, 10, 10, 10), " Action Logs ")); 
        return sp;
    }

    private void redirectSystemStreams() {
        OutputStream out = new OutputStream() {
            public void write(int b) { SwingUtilities.invokeLater(() -> { txtConsole.append(String.valueOf((char) b)); txtConsole.setCaretPosition(txtConsole.getDocument().getLength()); }); }
        }; System.setOut(new PrintStream(out, true));
    }

    // --- UI Styling Helpers ---
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16)); // Big Font
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(180, 50)); // Thick button
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 20)); // Large typing font
        field.setPreferredSize(new Dimension(200, 45)); // Thick input box
        return field;
    }
}