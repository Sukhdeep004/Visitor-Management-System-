package utils;

import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class UIUtils {
    // Color palette
    public static final Color PRIMARY_COLOR = new Color(41, 128, 185); // Blue
    public static final Color SECONDARY_COLOR = new Color(52, 152, 219); // Lighter blue
    public static final Color ACCENT_COLOR = new Color(231, 76, 60); // Red
    public static final Color SUCCESS_COLOR = new Color(46, 204, 113); // Green
    public static final Color WARNING_COLOR = new Color(241, 196, 15); // Yellow
    public static final Color BACKGROUND_COLOR = new Color(236, 240, 241); // Light grey
    public static final Color PANEL_BACKGROUND = new Color(255, 255, 255); // White
    public static final Color TEXT_COLOR = new Color(44, 62, 80); // Dark blue/grey
    public static final Color LIGHT_TEXT_COLOR = new Color(149, 165, 166); // Light grey
    
    // Fonts
    public static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 24);
    public static final Font SUBTITLE_FONT = new Font("Arial", Font.BOLD, 18);
    public static final Font REGULAR_FONT = new Font("Arial", Font.PLAIN, 14);
    public static final Font SMALL_FONT = new Font("Arial", Font.PLAIN, 12);
    
    // Borders
    public static Border createRoundedBorder(Color color, int thickness, int radius) {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, thickness),
            BorderFactory.createEmptyBorder(radius, radius, radius, radius)
        );
    }
    
    // Button styling
    public static void styleButton(JButton button, Color background, Color foreground) {
        button.setBackground(background);
        button.setForeground(foreground);
        button.setFocusPainted(true);
        button.setBorderPainted(true);
        button.setFont(REGULAR_FONT);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(background.darker(), 1),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        button.setOpaque(true);
    }
    
    // Panel styling
    public static void stylePanel(JPanel panel, Color background) {
        panel.setBackground(background);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(background.darker(), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
    }
    
    // Create a container panel with title
    public static JPanel createTitledPanel(String title) {
        JPanel panel = new JPanel(new java.awt.BorderLayout(10, 10));
        panel.setBackground(PANEL_BACKGROUND);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
                title,
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                SUBTITLE_FONT,
                PRIMARY_COLOR
            ),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        return panel;
    }
}
