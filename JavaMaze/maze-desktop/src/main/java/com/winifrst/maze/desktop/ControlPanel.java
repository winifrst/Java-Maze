package com.winifrst.maze.desktop;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Панель управления: поля для x/y и кнопки.
 * Не знает ни о MazeService, ни о MazePanel — только хранит компоненты
 * и отдаёт их наружу через геттеры.
 */
public final class ControlPanel extends JPanel {

    private final JTextField xField = new JTextField("10", 4);
    private final JTextField yField = new JTextField("10", 4);
    private final JButton generateButton = new JButton("Сгенерировать");
    private final JButton solveButton = new JButton("Найти путь");
    private final JLabel statusLabel = new JLabel("");

    private final JButton loadButton = new JButton("Загрузить");
    private final JButton saveButton = new JButton("Сохранить");

    public ControlPanel() {
        setLayout(new BorderLayout());

        // Верхний ряд: генерация и решение
        JPanel topRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topRow.add(new JLabel("x:"));
        topRow.add(xField);
        topRow.add(new JLabel("y:"));
        topRow.add(yField);
        topRow.add(generateButton);
        topRow.add(solveButton);
        topRow.add(statusLabel);

        // Нижний ряд: загрузка и сохранение
        JPanel bottomRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomRow.add(loadButton);
        bottomRow.add(saveButton);

        add(topRow, BorderLayout.CENTER);
        add(bottomRow, BorderLayout.SOUTH);
    }

    public JTextField getXField() {
        return xField;
    }

    public JTextField getYField() {
        return yField;
    }

    public JButton getGenerateButton() {
        return generateButton;
    }

    public JButton getLoadButton() {
        return loadButton;
    }

    public JButton getSaveButton() {
        return saveButton;
    }

    public JButton getSolveButton() {
        return solveButton;
    }

    public JLabel getStatusLabel() {
        return statusLabel;
    }
}
