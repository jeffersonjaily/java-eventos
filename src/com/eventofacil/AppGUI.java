package com.eventofacil;

import javax.swing.SwingUtilities;

public class AppGUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TelaLogin().setVisible(true);
        });
    }
}