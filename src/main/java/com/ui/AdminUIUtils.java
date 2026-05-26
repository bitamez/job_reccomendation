package com.mesi.jobai.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Utility class for consistent admin UI styling
 */
public class AdminUIUtils {
    
    /**
     * Creates a professional styled button with system defaults
     */
    public static JButton createStyledButton(String text, Color backgroundColor, Color textColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(backgroundColor);
        button.setForeground(textColor);
        button.setBorder(new EmptyBorder(10, 20, 10, 20));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
    
    /**
     * Creates a primary action button
     */
    public static JButton createPrimaryButton(String text) {
        return createStyledButton(text, AdminColors.PRIMARY, Color.BLACK);  // Black text
    }
    
    /**
     * Creates a success button
     */
    public static JButton createSuccessButton(String text) {
        return createStyledButton(text, AdminColors.SUCCESS, Color.BLACK);  // Black text
    }
    
    /**
     * Creates a danger button
     */
    public static JButton createDangerButton(String text) {
        return createStyledButton(text, AdminColors.DANGER, Color.BLACK);  // Black text
    }
    
    /**
     * Creates a secondary button with system styling
     */
    public static JButton createSecondaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setBackground(AdminColors.SURFACE);
        button.setForeground(Color.BLACK);  // Black text
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AdminColors.BORDER, 1),
            new EmptyBorder(10, 20, 10, 20)
        ));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
    
    /**
     * Adds hover effect to a button
     */
    public static void addHoverEffect(JButton button, Color normalColor, Color hoverColor) {
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(normalColor);
            }
        });
    }
    
    /**
     * Creates a system default styled text field
     */
    public static JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Dialog", Font.PLAIN, 12)); // System default font
        field.setBorder(BorderFactory.createLoweredBevelBorder()); // System default border
        field.setBackground(AdminColors.SURFACE);
        field.setForeground(AdminColors.TEXT_PRIMARY);
        return field;
    }
    
    /**
     * Creates a system default styled text area
     */
    public static JTextArea createStyledTextArea(int rows, int cols) {
        JTextArea area = new JTextArea(rows, cols);
        area.setFont(new Font("Dialog", Font.PLAIN, 12)); // System default font
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBackground(AdminColors.SURFACE);
        area.setForeground(AdminColors.TEXT_PRIMARY);
        area.setBorder(BorderFactory.createLoweredBevelBorder()); // System default border
        return area;
    }
    
    /**
     * Creates a system default styled table
     */
    public static JTable createStyledTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setFont(new Font("Dialog", Font.PLAIN, 12)); // System default font
        table.setRowHeight(25); // Standard row height
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setBackground(AdminColors.SURFACE);
        table.setForeground(AdminColors.TEXT_PRIMARY);
        table.setSelectionBackground(AdminColors.SELECTED);
        table.setSelectionForeground(AdminColors.TEXT_PRIMARY);
        table.setGridColor(AdminColors.BORDER);
        table.getTableHeader().setFont(new Font("Dialog", Font.BOLD, 12));
        table.getTableHeader().setBackground(AdminColors.BACKGROUND);
        table.getTableHeader().setForeground(AdminColors.TEXT_PRIMARY);
        return table;
    }
    
    /**
     * Creates a system default styled label
     */
    public static JLabel createStyledLabel(String text, Font font, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(font != null ? font : new Font("Dialog", Font.PLAIN, 12));
        label.setForeground(color);
        return label;
    }
    
    /**
     * Creates a header label with system font
     */
    public static JLabel createHeaderLabel(String text) {
        return createStyledLabel(text, new Font("Dialog", Font.BOLD, 14), AdminColors.TEXT_PRIMARY);
    }
    
    /**
     * Creates a form label with system font
     */
    public static JLabel createFormLabel(String text) {
        return createStyledLabel(text, new Font("Dialog", Font.PLAIN, 12), AdminColors.TEXT_PRIMARY);
    }
    
    /**
     * Creates a clean white panel with minimal border
     */
    public static JPanel createStyledPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(AdminColors.SURFACE);
        panel.setBorder(BorderFactory.createLoweredBevelBorder()); // System default border
        return panel;
    }
    
    /**
     * Creates a simple header panel with system colors
     */
    public static JPanel createHeaderPanel(String title) {
        JPanel panel = new JPanel();
        panel.setBackground(AdminColors.PRIMARY_DARK);
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Dialog", Font.BOLD, 16)); // System font
        titleLabel.setForeground(AdminColors.TEXT_WHITE);
        panel.add(titleLabel);
        return panel;
    }
    
    /**
     * Styles a card panel with modern look
     */
    public static void styleCard(JPanel panel) {
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SystemColors.BORDER, 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
    }
    
    /**
     * Styles a text field with placeholder
     */
    public static void styleTextField(JTextField field, String placeholder) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SystemColors.BORDER, 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        field.setBackground(SystemColors.BACKGROUND);
        field.setForeground(SystemColors.TEXT_PRIMARY);
    }
    
    /**
     * Styles a password field with placeholder
     */
    public static void stylePasswordField(JPasswordField field, String placeholder) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SystemColors.BORDER, 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        field.setBackground(SystemColors.BACKGROUND);
        field.setForeground(SystemColors.TEXT_PRIMARY);
    }
    
    /**
     * Styles an accent button
     */
    public static void styleAccentButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 15));
        button.setBackground(SystemColors.PRIMARY);
        button.setForeground(Color.BLACK);  // Black text
        button.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    
    /**
     * Adds rounded border to component
     */
    public static void addRoundedBorder(JComponent component, int radius) {
        // Swing doesn't support rounded borders natively, so we use a simple line border
        component.setBorder(BorderFactory.createLineBorder(SystemColors.BORDER, 1));
    }
    
    /**
     * Adds shadow effect to component (simulated with border)
     */
    public static void addShadow(JComponent component) {
        // Swing doesn't support shadows natively, so we use a compound border
        component.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 0, 0, 30), 2),
            component.getBorder()
        ));
    }
    
    /**
     * Adds light shadow effect to component
     */
    public static void addLightShadow(JComponent component) {
        // Swing doesn't support shadows natively, so we use a compound border
        component.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 0, 0, 15), 1),
            component.getBorder()
        ));
    }
}