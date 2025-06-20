import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

class Login extends JFrame {
    Container c;
    JLabel labTitle, labUsername, labPassword;
    JTextField txtUsername;
    JPasswordField txtPassword;
    JButton btnLogin, btnRegister;

    Login() {
        setTitle("Library Login");
        setSize(700, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        c = getContentPane();
        c.setLayout(null);
        c.setBackground(new Color(240, 248, 255)); // light blue

        Font titleFont = new Font("Segoe UI", Font.BOLD, 28);
        Font labelFont = new Font("Segoe UI", Font.PLAIN, 18);
        Font btnFont = new Font("Segoe UI", Font.BOLD, 16);

        labTitle = new JLabel("Library Login", JLabel.CENTER);
        labTitle.setBounds(0, 30, 700, 40);
        labTitle.setFont(titleFont);
        labTitle.setForeground(new Color(25, 25, 112));
        c.add(labTitle);

        labUsername = new JLabel("Username:");
        labUsername.setBounds(150, 110, 120, 30);
        labUsername.setFont(labelFont);
        c.add(labUsername);

        txtUsername = new JTextField();
        txtUsername.setBounds(280, 110, 250, 30);
        txtUsername.setFont(labelFont);
        c.add(txtUsername);

        labPassword = new JLabel("Password:");
        labPassword.setBounds(150, 170, 120, 30);
        labPassword.setFont(labelFont);
        c.add(labPassword);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(280, 170, 250, 30);
        txtPassword.setFont(labelFont);
        c.add(txtPassword);

        btnLogin = new JButton("Login");
        btnLogin.setBounds(200, 250, 120, 40);
        btnLogin.setFont(btnFont);
        c.add(btnLogin);

        btnRegister = new JButton("Register");
        btnRegister.setBounds(360, 250, 120, 40);
        btnRegister.setFont(btnFont);
        c.add(btnRegister);

        btnLogin.addActionListener(e -> {
            try {
                String user = txtUsername.getText();
                String pwd = new String(txtPassword.getPassword());

                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/library_javadb", "root", "Kitten@0203");
                String sql = "SELECT * FROM users WHERE username = ? AND password = md5(?)";
                PreparedStatement pst = con.prepareStatement(sql);
                pst.setString(1, user);
                pst.setString(2, pwd);
                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    JOptionPane.showMessageDialog(c, "Login successful!");
                    new Dashboard();
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(c, "Invalid credentials", "Login Failed", JOptionPane.ERROR_MESSAGE);
                }

                con.close();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(c, "DB Error: " + ex.getMessage());
            }
        });

        btnRegister.addActionListener(e -> {
            new Register();
            dispose();
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new Login();
    }
}
