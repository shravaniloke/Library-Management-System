import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

class ViewBooks extends JFrame {
    Container c;
    JTable table;
    JScrollPane scroll;
    DefaultTableModel model;
    JLabel labTitle, labCount;
    JTextField txtSearch;
    JButton btnSearch, btnRefresh;

    ViewBooks() {
        // Frame setup
        setTitle("View Books");
        setSize(850, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        c = getContentPane();
        c.setLayout(new BorderLayout());
        c.setBackground(new Color(240, 248, 255)); // Light blue

        // Title
        labTitle = new JLabel("📚 Available Books", JLabel.CENTER);
        labTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        labTitle.setForeground(new Color(25, 25, 112));
        labTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        c.add(labTitle, BorderLayout.NORTH);

        // Top panel with search and refresh
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
        topPanel.setBackground(new Color(230, 240, 250));

        txtSearch = new JTextField(25);
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        btnSearch = new JButton("Search");
        btnSearch.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        btnRefresh = new JButton("Refresh");
        btnRefresh.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        topPanel.add(new JLabel("Search (Title/Author):"));
        topPanel.add(txtSearch);
        topPanel.add(btnSearch);
        topPanel.add(btnRefresh);

        c.add(topPanel, BorderLayout.BEFORE_FIRST_LINE);

        // Table
        model = new DefaultTableModel();
        table = new JTable(model);
        scroll = new JScrollPane(table);
        c.add(scroll, BorderLayout.CENTER);

        model.addColumn("Book ID");
        model.addColumn("Title");
        model.addColumn("Author");
        model.addColumn("Available");

        // Count label at the bottom
        labCount = new JLabel("Total books: 0");
        labCount.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        labCount.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 0));
        c.add(labCount, BorderLayout.SOUTH);

        // Load all books initially
        fetchBooks("");

        // Action: Search
        btnSearch.addActionListener(e -> {
            String keyword = txtSearch.getText().trim();
            fetchBooks(keyword);
        });

        // Action: Refresh
        btnRefresh.addActionListener(e -> {
            txtSearch.setText("");
            fetchBooks("");
        });

        setVisible(true);
    }

    void fetchBooks(String keyword) {
        try {
            String url = "jdbc:mysql://localhost:3306/library_javadb";
            String user = "root";
            String password = "Kitten@0203"; // your MySQL password

            Connection con = DriverManager.getConnection(url, user, password);
            String sql;

            if (keyword == null || keyword.equals("")) {
                sql = "SELECT * FROM books";
            } else {
                sql = "SELECT * FROM books WHERE title LIKE ? OR author LIKE ?";
            }

            PreparedStatement pst = con.prepareStatement(sql);

            if (keyword != null && !keyword.equals("")) {
                pst.setString(1, "%" + keyword + "%");
                pst.setString(2, "%" + keyword + "%");
            }

            ResultSet rs = pst.executeQuery();

            model.setRowCount(0); // Clear table
            int count = 0;

            while (rs.next()) {
                String id = rs.getString("bookid");
                String title = rs.getString("title");
                String author = rs.getString("author");
                boolean avail = rs.getBoolean("available");

                model.addRow(new Object[]{id, title, author, avail ? "Yes" : "No"});
                count++;
            }

            labCount.setText("Total books listed: " + count);
            con.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(c, "DB Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new ViewBooks();
    }
}
