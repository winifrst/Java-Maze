package com.winifrst.maze.desktop;

import com.winifrst.maze.MazeService;
import com.winifrst.maze.model.Maze;
import com.winifrst.maze.model.Point;

import java.nio.file.Path;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 * Связывает UI (ControlPanel + MazePanel) с бизнес-логикой (MazeService).
 * Только здесь живут обработчики событий.
 */
public final class MazeController {

    private final MazeService service;
    private final MazePanel mazePanel;
    private final ControlPanel controlPanel;

    public MazeController(MazeService service, MazePanel mazePanel, ControlPanel controlPanel) {
        this.service = service;
        this.mazePanel = mazePanel;
        this.controlPanel = controlPanel;
        wireListeners();
    }

    private void wireListeners() {
        controlPanel.getGenerateButton().addActionListener(e -> onGenerate());
        controlPanel.getLoadButton().addActionListener(e -> onLoad());
        controlPanel.getSaveButton().addActionListener(e -> onSave());
        controlPanel.getSolveButton().addActionListener(e -> onSolve());
        mazePanel.setCellClickHandler(this::onCellClick);
    }

    private void onLoad() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Загрузить лабиринт");
        int result = fileChooser.showOpenDialog(mazePanel);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                Path path = fileChooser.getSelectedFile().toPath();
                Maze maze = service.load(path);
                mazePanel.setMaze(maze);
                mazePanel.setStartCell(null);
                mazePanel.setEndCell(null);
                mazePanel.setSolutionPath(null);
                updateStatusLabel();
            } catch (Exception e) {
                showError("Не удалось загрузить: " + e.getMessage());
            }
        }
    }

    private void onSave() {
        Maze maze = mazePanel.getMaze();
        if (maze == null) {
            showError("Сначала сгенерируйте или загрузите лабиринт.");
            return;
        }
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Сохранить лабиринт");
        fileChooser.setSelectedFile(new java.io.File("maze.txt"));
        int result = fileChooser.showSaveDialog(mazePanel);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                Path path = fileChooser.getSelectedFile().toPath();
                service.save(maze, path);
            } catch (Exception e) {
                showError("Не удалось сохранить: " + e.getMessage());
            }
        }
    }

    private void onGenerate() {
        try {
            int rows = parseDimension(controlPanel.getXField().getText(), "x");
            int cols = parseDimension(controlPanel.getYField().getText(), "y");
            Maze maze = service.generate(rows, cols);
            mazePanel.setMaze(maze);
            mazePanel.setStartCell(null);
            mazePanel.setEndCell(null);
            mazePanel.setSolutionPath(null);
            updateStatusLabel();
        } catch (NumberFormatException e) {
            showError("Размер должен быть целым числом.");
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        }
    }

    private void onSolve() {
        Point start = mazePanel.getStartCell();
        Point end = mazePanel.getEndCell();

        if (start == null || end == null) {
            showError("Кликните на две клетки, чтобы задать путь.");
            return;
        }

        Maze maze = mazePanel.getMaze();
        if (maze == null) {
            showError("Сначала сгенерируйте лабиринт.");
            return;
        }

        try {
            List<Point> path = service.solve(maze, start, end);
            if (path.isEmpty()) {
                showError("Путь между клетками не найден.");
            } else {
                mazePanel.setSolutionPath(path);
            }
        } catch (Exception e) {
            showError("Ошибка при поиске пути: " + e.getMessage());
        }
    }

    private void onCellClick(Point cell) {
        // Если путь уже нарисован — сбросить его при любом клике
        if (mazePanel.getSolutionPath() != null && !mazePanel.getSolutionPath().isEmpty()) {
            mazePanel.setSolutionPath(null);
        }

        Point currentStart = mazePanel.getStartCell();
        Point currentEnd = mazePanel.getEndCell();

        if (currentStart == null) {
            mazePanel.setStartCell(cell);
        } else if (currentEnd == null) {
            mazePanel.setEndCell(cell);
        } else {
            // Обе выбраны — сбросить start, end остаётся
            mazePanel.setStartCell(cell);
            mazePanel.setEndCell(null);
        }

        updateStatusLabel();
        mazePanel.repaint();
    }

    private void updateStatusLabel() {
        StringBuilder text = new StringBuilder("Клетки: ");
        Point start = mazePanel.getStartCell();
        Point end = mazePanel.getEndCell();

        if (start != null) {
            text.append("(").append(start.row()).append(",").append(start.col()).append(")");
        }
        if (end != null) {
            text.append(" -> (").append(end.row()).append(",").append(end.col()).append(")");
        }

        controlPanel.getStatusLabel().setText(text.toString());
    }

    private int parseDimension(String text, String name) {
        try {
            return Integer.parseInt(text.trim());
        } catch (NumberFormatException e) {
            throw new NumberFormatException(name + ": не число");
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(
                mazePanel,
                message,
                "Ошибка",
                JOptionPane.ERROR_MESSAGE);
    }
}
