package com.mesi.jobai.ui;

import com.mesi.jobai.dao.AdminDAO;
import com.mesi.jobai.dao.RecommendationDAO;
import com.mesi.jobai.model.Admin;
import com.mesi.jobai.model.Recommendation;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class RecommendationReportUI extends JFrame {
    private Admin currentAdmin;
    private AdminDAO adminDAO;
    private RecommendationDAO recommendationDAO;
    private JTable recommendationsTable;
    private DefaultTableModel tableModel;
    private JLabel totalRecommendationsLabel;
    private JLabel avgMatchScoreLabel;

    public RecommendationReportUI(Admin admin) {
        this.currentAdmin = admin;
        this.adminDAO = new AdminDAO();
        this.recommendationDAO = new RecommendationDAO();
        initializeComponents();
        loadRecommendations();
    }

    private void initializeComponents() {
        setTitle("AI Recommendation Report - Admin Panel");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(52, 73, 94));
        JLabel titleLabel = new JLabel("AI Recommendation Report", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        headerPanel.add(titleLabel);

        // Statistics Panel
        JPanel statsPanel = createStatisticsPanel();

        // Table Panel
        JPanel tablePanel = createTablePanel();

        // Button Panel
        JPanel buttonPanel = createButtonPanel();

        // Main Content Panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(statsPanel, BorderLayout.NORTH);
        contentPanel.add(tablePanel, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createStatisticsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 20, 0));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        // Total Recommendations Card
        JPanel totalCard = createStatCard("Total Recommendations", "0", new Color(52, 152, 219));
        totalRecommendationsLabel = (JLabel) ((JPanel) totalCard.getComponent(1)).getComponent(0);

        // Average Match Score Card
        JPanel avgCard = createStatCard("Average Match Score", "0%", new Color(46, 204, 113));
        avgMatchScoreLabel = (JLabel) ((JPanel) avgCard.getComponent(1)).getComponent(0);

        panel.add(totalCard);
        panel.add(avgCard);

        return panel;
    }

    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(color);
        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        titleLabel.setForeground(Color.WHITE);

        JPanel valuePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        valuePanel.setBackground(color);
        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 24));
        valueLabel.setForeground(Color.WHITE);
        valuePanel.add(valueLabel);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valuePanel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        String[] columnNames = {"User ID", "User Name", "Job ID", "Job Title", "Match Score (%)", "Recommendation Date"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        recommendationsTable = new JTable(tableModel);
        recommendationsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Set column widths
        recommendationsTable.getColumnModel().getColumn(0).setPreferredWidth(70);
        recommendationsTable.getColumnModel().getColumn(1).setPreferredWidth(120);
        recommendationsTable.getColumnModel().getColumn(2).setPreferredWidth(70);
        recommendationsTable.getColumnModel().getColumn(3).setPreferredWidth(200);
        recommendationsTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        recommendationsTable.getColumnModel().getColumn(5).setPreferredWidth(130);

        JScrollPane scrollPane = new JScrollPane(recommendationsTable);
        scrollPane.setPreferredSize(new Dimension(0, 350));

        // Info Panel
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel infoLabel = new JLabel("AI Job Recommendations - View matching results between users and jobs");
        infoLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        infoLabel.setForeground(new Color(127, 140, 141));
        infoPanel.add(infoLabel);

        panel.add(infoPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        JButton refreshButton = new JButton("Refresh");
        refreshButton.setBackground(new Color(52, 152, 219));
        refreshButton.setForeground(Color.BLACK);
        refreshButton.addActionListener(e -> loadRecommendations());

        JButton exportButton = new JButton("Export Report");
        exportButton.setBackground(new Color(46, 204, 113));
        exportButton.setForeground(Color.BLACK);
        exportButton.addActionListener(e -> exportReport());

        JButton filterButton = new JButton("Filter by Score");
        filterButton.addActionListener(e -> filterByScore());

        JButton detailsButton = new JButton("View Details");
        detailsButton.addActionListener(e -> viewRecommendationDetails());

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());

        panel.add(refreshButton);
        panel.add(exportButton);
        panel.add(filterButton);
        panel.add(detailsButton);
        panel.add(closeButton);

        return panel;
    }

    private void loadRecommendations() {
        tableModel.setRowCount(0);
        
        // Sample data - replace with actual recommendation data from your system
        // This would typically come from a RecommendationDAO or similar service
        Object[][] sampleData = {
            {1, "Mesi Ahmed", 101, "Java Developer", 90, "2024-01-15"},
            {2, "Hana Ali", 102, "Frontend Developer", 85, "2024-01-15"},
            {3, "Ahmed Hassan", 103, "Data Scientist", 78, "2024-01-16"},
            {1, "Mesi Ahmed", 104, "Software Engineer", 82, "2024-01-16"},
            {4, "Sara Mohamed", 105, "UI/UX Designer", 88, "2024-01-17"},
            {2, "Hana Ali", 106, "React Developer", 92, "2024-01-17"},
            {5, "Omar Khalil", 107, "Backend Developer", 75, "2024-01-18"},
            {3, "Ahmed Hassan", 108, "Machine Learning Engineer", 95, "2024-01-18"}
        };

        for (Object[] row : sampleData) {
            tableModel.addRow(row);
        }

        updateStatistics();
    }

    private void updateStatistics() {
        int totalRecommendations = tableModel.getRowCount();
        totalRecommendationsLabel.setText(String.valueOf(totalRecommendations));

        if (totalRecommendations > 0) {
            double totalScore = 0;
            for (int i = 0; i < totalRecommendations; i++) {
                totalScore += (Integer) tableModel.getValueAt(i, 4);
            }
            double avgScore = totalScore / totalRecommendations;
            avgMatchScoreLabel.setText(String.format("%.1f%%", avgScore));
        } else {
            avgMatchScoreLabel.setText("0%");
        }
    }

    private void filterByScore() {
        String input = JOptionPane.showInputDialog(this,
            "Enter minimum match score (0-100):",
            "Filter by Score", JOptionPane.QUESTION_MESSAGE);

        if (input != null && !input.trim().isEmpty()) {
            try {
                int minScore = Integer.parseInt(input.trim());
                if (minScore < 0 || minScore > 100) {
                    JOptionPane.showMessageDialog(this,
                        "Please enter a score between 0 and 100.",
                        "Invalid Input", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Filter table rows
                DefaultTableModel filteredModel = new DefaultTableModel(
                    new String[]{"User ID", "User Name", "Job ID", "Job Title", "Match Score (%)", "Recommendation Date"}, 0) {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };

                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    int score = (Integer) tableModel.getValueAt(i, 4);
                    if (score >= minScore) {
                        Object[] row = new Object[tableModel.getColumnCount()];
                        for (int j = 0; j < tableModel.getColumnCount(); j++) {
                            row[j] = tableModel.getValueAt(i, j);
                        }
                        filteredModel.addRow(row);
                    }
                }

                recommendationsTable.setModel(filteredModel);
                JOptionPane.showMessageDialog(this,
                    "Showing " + filteredModel.getRowCount() + " recommendations with score >= " + minScore + "%",
                    "Filter Applied", JOptionPane.INFORMATION_MESSAGE);

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                    "Please enter a valid number.",
                    "Invalid Input", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void viewRecommendationDetails() {
        int selectedRow = recommendationsTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                "Please select a recommendation to view details.",
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int userId = (Integer) recommendationsTable.getValueAt(selectedRow, 0);
        String userName = (String) recommendationsTable.getValueAt(selectedRow, 1);
        int jobId = (Integer) recommendationsTable.getValueAt(selectedRow, 2);
        String jobTitle = (String) recommendationsTable.getValueAt(selectedRow, 3);
        int matchScore = (Integer) recommendationsTable.getValueAt(selectedRow, 4);
        String date = (String) recommendationsTable.getValueAt(selectedRow, 5);

        String details = String.format(
            "Recommendation Details:\n\n" +
            "User: %s (ID: %d)\n" +
            "Job: %s (ID: %d)\n" +
            "Match Score: %d%%\n" +
            "Recommendation Date: %s\n\n" +
            "This recommendation was generated by the AI system based on:\n" +
            "• User skills and experience\n" +
            "• Job requirements\n" +
            "• Historical application patterns\n" +
            "• Skill gap analysis",
            userName, userId, jobTitle, jobId, matchScore, date
        );

        JOptionPane.showMessageDialog(this, details,
            "Recommendation Details", JOptionPane.INFORMATION_MESSAGE);
    }

    private void exportReport() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Recommendation Report");
        fileChooser.setSelectedFile(new java.io.File("recommendation_report.csv"));

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            try {
                java.io.File fileToSave = fileChooser.getSelectedFile();
                java.io.PrintWriter writer = new java.io.PrintWriter(fileToSave);

                // Write header
                writer.println("User ID,User Name,Job ID,Job Title,Match Score (%),Recommendation Date");

                // Write data
                for (int i = 0; i < recommendationsTable.getRowCount(); i++) {
                    StringBuilder line = new StringBuilder();
                    for (int j = 0; j < recommendationsTable.getColumnCount(); j++) {
                        if (j > 0) line.append(",");
                        line.append(recommendationsTable.getValueAt(i, j));
                    }
                    writer.println(line.toString());
                }

                writer.close();
                JOptionPane.showMessageDialog(this,
                    "Recommendation report exported successfully!",
                    "Export Complete", JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Error exporting file: " + e.getMessage(),
                    "Export Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}