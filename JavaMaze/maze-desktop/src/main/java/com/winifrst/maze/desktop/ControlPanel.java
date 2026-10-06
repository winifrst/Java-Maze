package com.winifrst.maze.desktop;

import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Панель управления: поля для rows/cols и кнопки.
 * Не знает ни о MazeService, ни о MazePanel — только хранит компоненты
 * и отдаёт их наружу через геттеры.
 */
public final class ControlPanel extends JPanel {

    private final JTextField rowsField = new JTextField("10", 4);
    private final JTextField colsField = new JTextField("10", 4);
    private final JButton generateButton = new JButton("Generate");
    private final JButton solveButton = new JButton("Solve");
    private final JLabel statusLabel = new JLabel("");

    public ControlPanel() {
        setLayout(new FlowLayout(FlowLayout.LEFT));

        add(new JLabel("Rows:"));
        add(rowsField);
        add(new JLabel("Cols:"));
        add(colsField);
        add(generateButton);
        add(solveButton);
        add(statusLabel);
    }

    public JTextField getRowsField() {
        return rowsField;
    }

    public JTextField getColsField() {
        return colsField;
    }

    public JButton getGenerateButton() {
        return generateButton;
    }

    public JButton getSolveButton() {
        return solveButton;
    }

    public JLabel getStatusLabel() {
        return statusLabel;
    }
}