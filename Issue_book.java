import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;

    class IssueBook extends JFrame {
    Container c;
    JLabel labTitle, labBookID, labStuID, labStuName;
    JTextField txtBookID, txtStuID;
    JLabel lblNameDisplay;
    JButton btnIssue, btnBack;

    public IssueBook() {
        // Frame setup
        setTitle("Issue Book");
        setSize(600, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        c = getContentPane();
        c.setLayout(null);
        c.setBackground(new Color(230, 240, 250));

        Font f = new Font("Segoe UI", Font.BOLD, 20);

        labTitle = new JLabel("Issue a Book");
        labTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        labTitle.setBounds(180, 30, 300, 30);
        c.add(labTitle);

        labBookID = new JLabel("Book ID:");
        labBookID.setFont(f);
        labBookID.setBounds(100, 100, 150, 30);
        c.add(labBookID);

        txtBookID = new JTextField();
        txtBookID.setFont(f);
        txtBookID.setBounds(250, 100, 200, 30);
        c.add(txtBookID);

        labStuID = new JLabel("Student ID:");
        labStuID.setFont(f);
        labStuID.setBounds(100, 160, 150, 30);
        c.add(labStuID);

        txtStuID = new JTextField();
        txtStuID.setFont(f);
        txtStuID.setBounds(250, 160, 200, 30);
        c.add(txtStuID);

        labStuName = new JLabel("Student Name:");
        labStuName.setFont(f);
        labStuName.setBounds(100, 200, 200, 30);
        c.add(labStuName);

        lblNameDisplay = new JLabel("-");
        lblNameDisplay.setFont(f);
        lblNameDisplay.setBounds(250, 200, 300, 30);
        lblNameDisplay.setForeground(Color.BLUE);
        c.add(lblNameDisplay);

        btnIssue = new JButton("Issue Book");
        btnIssue.setFont(f);
        btnIssue.setBounds(150, 280, 150, 40);
        c.add(btnIssue);

        btnBack = new JButton("Back");
        btnBack.setFont(f);
        btnBack.setBounds(320, 280, 120, 40);
        c.add(btnBack);

        // Load Student Name on ID entry
        txtStuID.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                String stuid = txtStuID.getText().trim();
                if (stuid.isEmpty()) return;
                try {
                    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/library_javadb", "root", "Kitten@0203");
                    String sql = "SELECT name FROM students WHERE stuid = ?";
                    PreparedStatement pst = con.prepareStatement(sql);
                    pst.setString(1, stuid);
                    ResultSet rs = pst.executeQuery();
                    if (rs.next()) {
                        lblNameDisplay.setText(rs.getString("name"));
                    } else {
                        lblNameDisplay.setText("Not found");
                    }
                    con.close();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(c, "DB Error: " + ex.getMessage());
                }
            }
        });

        // Issue Logic
        btnIssue.addActionListener(e -> issueBook());

        // Back
        btnBack.addActionListener(e -> dispose());

        setVisible(true);
    }

    void issueBook() {
        String bookid = txtBookID.getText().trim();
        String stuid = txtStuID.getText().trim();

        if (bookid.isEmpty() || stuid.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!");
            return;
        }

        try {
            String url = "jdbc:mysql://localhost:3306/library_javadb";
            Connection con = DriverManager.getConnection(url, "root", "Kitten@0203");

            // Check book availability
            PreparedStatement checkBook = con.prepareStatement("SELECT * FROM books WHERE bookid = ? AND available = true");
            checkBook.setString(1, bookid);
            ResultSet rsBook = checkBook.executeQuery();

            if (!rsBook.next()) {
                JOptionPane.showMessageDialog(this, "Book not available or doesn't exist.");
                con.close();
                return;
            }

            // Check if student exists
            PreparedStatement checkStudent = con.prepareStatement("SELECT * FROM students WHERE stuid = ?");
            checkStudent.setString(1, stuid);
            ResultSet rsStudent = checkStudent.executeQuery();

            if (!rsStudent.next()) {
                JOptionPane.showMessageDialog(this, "Student ID not found.");
                con.close();
                return;
            }

            // Insert issue record
            PreparedStatement issue = con.prepareStatement("INSERT INTO issue_books (bookid, stuid, issue_date) VALUES (?, ?, ?)");
            issue.setString(1, bookid);
            issue.setString(2, stuid);
            issue.setDate(3, Date.valueOf(LocalDate.now()));
            issue.executeUpdate();

            // Mark book unavailable
            PreparedStatement updateBook = con.prepareStatement("UPDATE books SET available = false WHERE bookid = ?");
            updateBook.setString(1, bookid);
            updateBook.executeUpdate();

            JOptionPane.showMessageDialog(this, "Book issued successfully!");

            txtBookID.setText("");
            txtStuID.setText("");
            lblNameDisplay.setText("-");

            con.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "DB Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new IssueBook();
    }
}
