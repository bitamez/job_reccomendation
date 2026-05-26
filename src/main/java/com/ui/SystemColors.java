package com.mesi.jobai.ui;

import java.awt.Color;
import javax.swing.UIManager;

/**
 * Universal system default colors for the entire Job AI System
 * Works for Swing components
 */
public class SystemColors {
    
    // Pure System Defaults - White Theme
    public static final Color BACKGROUND = Color.WHITE;                    // Pure White Background
    public static final Color SURFACE = Color.WHITE;                      // Pure White Surface
    public static final Color PANEL = Color.WHITE;                        // Pure White Panels
    
    // Legacy color aliases for compatibility
    public static final Color BG_COLOR = BACKGROUND;
    public static final Color PRIMARY_COLOR = new Color(0, 120, 215);     // System Blue
    public static final Color ACCENT_COLOR = PRIMARY_COLOR;
    public static final Color ACCENT_HOVER = new Color(0, 100, 195);
    public static final Color SECONDARY_COLOR = new Color(108, 117, 125);
    public static final Color BORDER_COLOR = new Color(180, 180, 180);
    public static final Color ERROR_COLOR = new Color(196, 43, 28);
    public static final Color SUCCESS_COLOR = new Color(16, 124, 16);
    public static final Color WARNING_COLOR = new Color(255, 185, 0);
    
    // System Text Colors
    public static final Color TEXT_PRIMARY = new Color(33, 37, 41);       // Very Dark Gray Text (almost black)
    public static final Color TEXT_SECONDARY = new Color(73, 80, 87);     // Dark Gray Text
    public static final Color TEXT_MUTED = new Color(108, 117, 125);      // Medium Gray Text
    public static final Color TEXT_WHITE = Color.WHITE;                   // White Text
    
    // System Borders and Lines
    public static final Color BORDER = new Color(180, 180, 180);          // Light Gray Border
    public static final Color DIVIDER = new Color(220, 220, 220);         // Very Light Gray Divider
    
    // System Interactive Colors
    public static final Color HOVER = new Color(240, 240, 240);           // Light Gray Hover
    public static final Color SELECTED = new Color(220, 235, 255);        // Light Blue Selection
    public static final Color FOCUS = new Color(0, 120, 215, 30);         // Transparent Blue Focus
    
    // System Action Colors (Minimal and Clean)
    public static final Color PRIMARY = new Color(0, 120, 215);           // System Blue
    public static final Color SUCCESS = new Color(16, 124, 16);           // System Green  
    public static final Color WARNING = new Color(255, 185, 0);           // System Orange
    public static final Color DANGER = new Color(196, 43, 28);            // System Red
    
    // Header Colors (Subtle)
    public static final Color HEADER_BACKGROUND = new Color(250, 250, 250); // Very Light Gray
    public static final Color HEADER_TEXT = new Color(60, 60, 60);          // Dark Gray
    
    /**
     * Apply modern theme (legacy method for compatibility)
     */
    public static void applyModernTheme() {
        // No-op for Swing, colors are already defined
    }
    
    /**
     * Get system default button background color
     */
    public static Color getSystemButtonBackground() {
        Color systemColor = UIManager.getColor("Button.background");
        return systemColor != null ? systemColor : new Color(240, 240, 240);
    }
    
    /**
     * Get system default button text color
     */
    public static Color getSystemButtonText() {
        Color systemColor = UIManager.getColor("Button.foreground");
        return systemColor != null ? systemColor : Color.BLACK;
    }
    
    /**
     * Get system default panel background
     */
    public static Color getSystemPanelBackground() {
        Color systemColor = UIManager.getColor("Panel.background");
        return systemColor != null ? systemColor : Color.WHITE;
    }
}