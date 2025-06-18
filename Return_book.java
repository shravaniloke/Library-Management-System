import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.*;

    class ReturnBook extends JFrame {
    Container c;
    JLabel labTitle, labBookID, labStuID, labFine;
    JTextField txtBookID, txtStuID;
    JButton btnReturn, btnBack;

    public ReturnBook() {
        setTitle("Return Book");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        c = getContentPane();
        c.setLayout(null);
        c.setBackground(new Color(250, 245, 230));

        Font f = new Font("Segoe UI", Font.BOLD, 20);

        labTitle = new JLabel("Return a Book");
        labTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        labTitle.setBounds(180, 30, 300, 30);
        c.add(labTitle);

        JLabel labBookID = new JLabel("Book ID:");
        labBookID.setFont(f);
        labBookID.setBounds(100, 100, 150, 30);
        c.add(labBookID);

        txtBookID = new JTextField();
        txtBookID.setFont(f);
        txtBookID.setBounds(250, 100, 200, 30);
        c.add(txtBookID);

        JLabel labStuID = new JLabel("Student ID:");
        labStuID.setFont(f);
        labStuID.setBounds(100, 160, 150, 30);
        c.add(labStuID);

        txtStuID = new JTextField();
        txtStuID.setFont(f);
        txtStuID.setBounds(250, 160, 200, 30);
        c.add(txtStuID);

        labFine = new JLabel("Fine: ₹0");
        labFine.setFont(f);
        labFine.setBounds(250, 200, 300, 30);
        labFine.setForeground(Color.RED);
        c.add(labFine);

        btnReturn = new JButton("Return Book");
        btnReturn.setFont(f);
        btnReturn.setBounds(150, 260, 150, 40);
        c.add(btnReturn);

        btnBack = new JButton("Back");
        btnBack.setFont(f);
        btnBack.setBounds(320, 260, 120, 40);
        c.add(btnBack);

        btnReturn.addActionListener(e -> returnBook());

        btnBack.addActionListener(e -> dispose());

        setVisible(true);
    }

    void returnBook() {
        String bookid = txtBookID.getText().trim();
        String stuid = txtStuID.getText().trim();

        if (bookid.isEmpty() || stuid.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!");
            return;
        }

        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/library_javadb", "root", "Kitten@0203");

            // Check if book was issued
            String sql = "SELECT * FROM issue_books WHERE bookid = ? AND stuid = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, bookid);
            pst.setString(2, stuid);
            ResultSet rs = pst.executeQuery();

            if (!rs.next()) {
                JOptionPane.showMessageDialog(this, "No such issued book record.");
                con.close();
                return;
            }

            Date issueDateSQL = rs.getDate("issue_date");
            LocalDate issueDate = issueDateSQL.toLocalDate();
            LocalDate returnDate = LocalDate.now();

            long days = Duration.between(issueDate.atStartOfDay(), returnDate.atStartOfDay()).toDays();
            int fine = (int) (days > 14 ? (days - 14) * 5 : 0);
//Fine Logic
// First 14 days → Free
// After 14 days → ₹5 per extra day
            // Display fine
            labFine.setText("Fine: ₹" + fine);

            // Optional: Save to return_books table
            String insertReturn = "INSERT INTO return_books (bookid, stuid, issue_date, return_date, fine) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement insertPst = con.prepareStatement(insertReturn);
            insertPst.setString(1, bookid);
            insertPst.setString(2, stuid);
            insertPst.setDate(3, Date.valueOf(issueDate));
            insertPst.setDate(4, Date.valueOf(returnDate));
            insertPst.setInt(5, fine);
            insertPst.executeUpdate();

            // Delete from issue_books
            PreparedStatement deleteIssue = con.prepareStatement("DELETE FROM issue_books WHERE bookid = ? AND stuid = ?");
            deleteIssue.setString(1, bookid);
            deleteIssue.setString(2, stuid);
            deleteIssue.executeUpdate();

            // Mark book available
            PreparedStatement updateBook = con.prepareStatement("UPDATE books SET available = true WHERE bookid = ?");
            updateBook.setString(1, bookid);
            updateBook.executeUpdate();

            JOptionPane.showMessageDialog(this, "Book returned successfully!\nFine: ₹" + fine);

            txtBookID.setText("");
            txtStuID.setText("");
            labFine.setText("Fine: ₹0");




            con.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "DB Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new ReturnBook();
    }
}
