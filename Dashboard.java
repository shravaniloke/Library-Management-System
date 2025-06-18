import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class Dashboard extends JFrame {
    Container c;
    JLabel labWelcome;
    JButton btnAddStudent, btnViewBooks, btnIssueBook, btnReturnBook, btnLogout;

    Dashboard() {
        // Frame setup
        setTitle("Library Dashboard");
        setSize(800, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        c = getContentPane();
        c.setLayout(new BorderLayout());
        c.setBackground(new Color(240, 248, 255)); // Soft light blue

        // Header label
        labWelcome = new JLabel(" Welcome to Library Management Dashboard", JLabel.CENTER);
        labWelcome.setFont(new Font("Segoe UI", Font.BOLD, 26));
        labWelcome.setForeground(new Color(25, 25, 112)); // Midnight Blue
        labWelcome.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));
        c.add(labWelcome, BorderLayout.NORTH);

        // Center Panel
        JPanel panel = new JPanel(new GridLayout(5, 1, 20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 200, 30, 200));
        panel.setBackground(new Color(240, 248, 255)); // Match background

        Font btnFont = new Font("Segoe UI", Font.BOLD, 18);

        btnAddStudent = new JButton("Add Student");
        btnAddStudent.setFont(btnFont);
        panel.add(btnAddStudent);

        btnViewBooks = new JButton(" View Books");
        btnViewBooks.setFont(btnFont);
        panel.add(btnViewBooks);

        btnIssueBook = new JButton("Issue Book");
        btnIssueBook.setFont(btnFont);
        panel.add(btnIssueBook);

        btnReturnBook = new JButton("Return Book");
        btnReturnBook.setFont(btnFont);
        panel.add(btnReturnBook);

        btnLogout = new JButton("Logout");
        btnLogout.setFont(btnFont);
        btnLogout.setForeground(Color.RED);
        panel.add(btnLogout);

        c.add(panel, BorderLayout.CENTER);

        // Button actions
        btnAddStudent.addActionListener(e -> new AddStudent());
        btnViewBooks.addActionListener(e -> new ViewBooks()); // You’ll implement this
        btnIssueBook.addActionListener(e -> new IssueBook()); // Optional expansion
        btnReturnBook.addActionListener(e -> new ReturnBook()); // Optional expansion
        btnLogout.addActionListener(e -> {
            new Login();
            dispose();
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new Dashboard();
    }
}
