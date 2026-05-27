package com.mesi.jobai.ui;

import com.mesi.jobai.controller.ApplicationController;
import com.mesi.jobai.model.Application;
import com.mesi.jobai.model.User;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ApplicationsUI {
    private JPanel view;
    private User currentUser;
    private ApplicationController applicationController;

    public ApplicationsUI(User currentUser) {
        this.currentUser = currentUser;
        this.applicationController = new ApplicationController();
        
        view = new JPanel();
        view.setLayout(new BoxLayout(view, BoxLayout.Y_AXIS));
        view.setBackground(SystemColors.BACKGROUND);
        view.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JLabel sectionTitle = new JLabel("My Applications");
        sectionTitle.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 28));
        sectionTitle.setForeground(SystemColors.TEXT_PRIMARY);
        sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel cardPanel = new JPanel(new BorderLayout());
        cardPanel.setBackground(SystemColors.SURFACE);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SystemColors.BORDER, 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        cardPanel.setMaximumSize(new Dimension(850, Integer.MAX_VALUE));
        cardPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Create table model
        String[] columnNames = {"Job Title", "Company", "Status", "Applied On"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };

        // Create table
        JTable table = new JTable(tableModel);
        table.setBackground(SystemColors.BACKGROUND);
        table.setForeground(SystemColors.TEXT_PRIMARY);
        table.setGridColor(SystemColors.BORDER);
        table.setSelectionBackground(SystemColors.PRIMARY);
        table.setSelectionForeground(Color.WHITE);
        table.setRowHeight(30);
        table.getTableHeader().setBackground(SystemColors.SURFACE);
        table.getTableHeader().setForeground(SystemColors.TEXT_PRIMARY);
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, SystemColors.BORDER));

        // Set column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(250);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);
        table.getColumnModel().getColumn(2).setPreferredWidth(150);
        table.getColumnModel().getColumn(3).setPreferredWidth(200);

        // Load data
        List<Application> dbApps = applicationController.getApplicationsForUser(currentUser.getId());
        for (Application app : dbApps) {
            Object[] rowData = {
                app.getJobTitle(),
                app.getCompanyName(),
                app.getStatus(),
                app.getAppliedAt()
            };
            tableModel.addRow(rowData);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(800, 400));
        scrollPane.setBorder(BorderFactory.createLineBorder(SystemColors.BORDER, 1));
        scrollPane.getViewport().setBackground(SystemColors.BACKGROUND);

        cardPanel.add(scrollPane, BorderLayout.CENTER);

        view.add(sectionTitle);
        view.add(Box.createVerticalStrut(25));
        view.add(cardPanel);
        view.add(Box.createVerticalGlue());
    }

    public JPanel getView() {
        return view;
    }
}
