package com.winifrst.maze.desktop;

import com.winifrst.maze.MazeService;
import com.winifrst.maze.model.Maze;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public final class DesktopApp {

    private DesktopApp() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DesktopApp::createAndShowWindow);
    }

    private static void createAndShowWindow() {
        MazeService service = new MazeService();
        MazePanel mazePanel = new MazePanel();
        ControlPanel controlPanel = new ControlPanel();

        // Контроллер связывает UI и сервис, вешает обработчики.
        new MazeController(service, mazePanel, controlPanel);

        // Сгенерировать стартовый лабиринт.
        Maze initial = service.generate(10, 10);
        mazePanel.setMaze(initial);

        JFrame frame = new JFrame("Maze");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(controlPanel, BorderLayout.NORTH);
        frame.add(mazePanel, BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}