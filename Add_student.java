import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

class AddStudent extends JFrame {
    Container c;
    JLabel labstuid, labName, labCourse;
    JTextField txtstuid, txtName, txtCourse;
    JButton btnSave, btnBack;

    AddStudent() {
        c = getContentPane();
        c.setLayout(null);
        c.setBackground(new Color(224, 255, 255));

        Font f = new Font("Arial", Font.BOLD, 20);

        labstuid = new JLabel("Enter Student ID:");
        labstuid.setBounds(100, 60, 200, 30);
        labstuid.setFont(f);
        c.add(labstuid);

        txtstuid = new JTextField();
        txtstuid.setBounds(300, 60, 200, 30);
        txtstuid.setFont(f);
        c.add(txtstuid);

        labName = new JLabel("Enter Name:");
        labName.setBounds(100, 110, 200, 30);
        labName.setFont(f);
        c.add(labName);

        txtName = new JTextField();
        txtName.setBounds(300, 110, 200, 30);
        txtName.setFont(f);
        c.add(txtName);

        labCourse = new JLabel("Enter Course:");
        labCourse.setBounds(100, 160, 200, 30);
        labCourse.setFont(f);
        c.add(labCourse);

        txtCourse = new JTextField();
        txtCourse.setBounds(300, 160, 200, 30);
        txtCourse.setFont(f);
        c.add(txtCourse);

        btnSave = new JButton("Save");
        btnSave.setBounds(200, 230, 120, 40);
        btnSave.setFont(f);
        c.add(btnSave);

        btnBack = new JButton("Back");
        btnBack.setBounds(340, 230, 120, 40);
        btnBack.setFont(f);
        c.add(btnBack);

        btnSave.addActionListener(e -> {
            try {
                String stuid = txtstuid.getText();
                String name = txtName.getText();
                String course = txtCourse.getText();

                if (stuid.isEmpty() || name.isEmpty() || course.isEmpty()) {
                    JOptionPane.showMessageDialog(c, "All fields are required!");
                    return;
                }

                // ✅ FIX: JDBC URL had typo, fixed '//' to '://'
                Connection con = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/library_javadb", "root", "Kitten@0203");

                String sql = "INSERT INTO students (stuid, name, course) VALUES (?, ?, ?)";
                PreparedStatement pst = con.prepareStatement(sql);
                pst.setString(1, stuid);
                pst.setString(2, name);
                pst.setString(3, course);
                pst.executeUpdate();

                JOptionPane.showMessageDialog(c, "Student added successfully!");
                txtstuid.setText("");
                txtName.setText("");
                txtCourse.setText("");
                con.close();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(c, "DB Error: " + ex.getMessage());
            }
        });

        btnBack.addActionListener(e -> {
            dispose();
        });

        setTitle("Add Student");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        new AddStudent();
    }
}
