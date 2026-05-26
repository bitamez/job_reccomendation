package com.mesi.jobai.ui;

import com.mesi.jobai.controller.ApplicationController;
import com.mesi.jobai.model.Application;
import com.mesi.jobai.model.User;
import com.mesi.jobai.service.ReportGenerationService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class EmployerApplicationsUI {
    private JPanel view;
    private User currentUser;
    private ApplicationController applicationController;
    private ReportGenerationService reportService;
    private JTable table;
    private DefaultTableModel tableModel;

    public EmployerApplicationsUI(User currentUser) {
        this.currentUser = currentUser;
        this.applicationController = new ApplicationController();
        this.reportService = new ReportGenerationService();
        
        view = new JPanel();
        view.setLayout(new BoxLayout(view, BoxLayout.Y_AXIS));
        view.setBackground(SystemColors.BACKGROUND);
        view.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JLabel sectionTitle = new JLabel("Review Job Applicants");
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
        String[] columnNames = {"Applicant Name", "Applied For", "Update Status", "Applied On"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2; // Only status column is editable
            }
        };

        // Create table
        table = new JTable(tableModel);
        table.setBackground(SystemColors.BACKGROUND);
        table.setForeground(SystemColors.TEXT_PRIMARY);
        table.setGridColor(SystemColors.BORDER);
        table.setSelectionBackground(SystemColors.PRIMARY);
        table.setSelectionForeground(Color.WHITE);
        table.setRowHeight(40);
        table.getTableHeader().setBackground(SystemColors.SURFACE);
        table.getTableHeader().setForeground(SystemColors.TEXT_PRIMARY);
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, SystemColors.BORDER));

        // Set column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(200);
        table.getColumnModel().getColumn(1).setPreferredWidth(250);
        table.getColumnModel().getColumn(2).setPreferredWidth(160);
        table.getColumnModel().getColumn(3).setPreferredWidth(200);

        // Custom renderer and editor for status column
        table.getColumnModel().getColumn(2).setCellRenderer(new StatusComboBoxRenderer());
        table.getColumnModel().getColumn(2).setCellEditor(new StatusComboBoxEditor());

        // Load data
        List<Application> dbApps = applicationController.getApplicationsForEmployer(currentUser.getId());
        for (Application app : dbApps) {
            Object[] rowData = {
                app.getApplicantName(),
                app.getJobTitle(),
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

    // Custom renderer for status combobox
    class StatusComboBoxRenderer extends JComboBox<String> implements TableCellRenderer {
        public StatusComboBoxRenderer() {
            super(new String[]{"PENDING", "REVIEWING", "INTERVIEW", "REJECTED", "HIRED"});
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setSelectedItem(value);
            setBackground(isSelected ? SystemColors.PRIMARY : SystemColors.BACKGROUND);
            setForeground(isSelected ? Color.WHITE : SystemColors.TEXT_PRIMARY);
            return this;
        }
    }

    // Custom editor for status combobox
    class StatusComboBoxEditor extends DefaultCellEditor {
        private JComboBox<String> comboBox;

        public StatusComboBoxEditor() {
            super(new JComboBox<>(new String[]{"PENDING", "REVIEWING", "INTERVIEW", "REJECTED", "HIRED"}));
            comboBox = (JComboBox<String>) getComponent();
            comboBox.setBackground(SystemColors.BACKGROUND);
            comboBox.setForeground(SystemColors.TEXT_PRIMARY);
            
            comboBox.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int row = table.getSelectedRow();
                    if (row >= 0) {
                        String newStatus = (String) comboBox.getSelectedItem();
                        String currentStatus = (String) tableModel.getValueAt(row, 2);
                        
                        if (newStatus != null && !newStatus.equals(currentStatus)) {
                            List<Application> dbApps = applicationController.getApplicationsForEmployer(currentUser.getId());
                            if (row < dbApps.size()) {
                                Application app = dbApps.get(row);
                                if (applicationController.updateApplicationStatus(app.getId(), newStatus)) {
                                    tableModel.setValueAt(newStatus, row, 2);
                                    
                                    // Automatically generate report for status change
                                    String reportFile = reportService.generateStatusChangeReport(
                                        app.getId(), 
                                        currentStatus, 
                                        newStatus, 
                                        currentUser.getId()
                                    );
                                    
                                    if (reportFile != null) {
                                        JOptionPane.showMessageDialog(view,
                                            "Status updated successfully!\n\n" +
                                            "Automatic report generated:\n" + reportFile,
                                            "Status Updated", 
                                            JOptionPane.INFORMATION_MESSAGE);
                                    } else {
                                        JOptionPane.showMessageDialog(view,
                                            "Status updated successfully!",
                                            "Status Updated", 
                                            JOptionPane.INFORMATION_MESSAGE);
                                    }
                                }
                            }
                        }
                    }
                    fireEditingStopped();
                }
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            comboBox.setSelectedItem(value);
            return comboBox;
        }

        public Object getCellEditorValue() {
            return comboBox.getSelectedItem();
        }
    }

    public JPanel getView() {
        return view;
    }
}
