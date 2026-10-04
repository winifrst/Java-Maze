package com.winifrst.maze.desktop;

import com.winifrst.maze.MazeService;
import com.winifrst.maze.model.Maze;
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
    }

    private void onGenerate() {
        try {
            int rows = parseDimension(controlPanel.getRowsField().getText(), "rows");
            int cols = parseDimension(controlPanel.getColsField().getText(), "cols");
            Maze maze = service.generate(rows, cols);
            mazePanel.setMaze(maze);
        } catch (NumberFormatException e) {
            showError("Размер должен быть целым числом.");
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        }
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