package com.winifrst.maze.desktop;

import com.winifrst.maze.model.Cell;
import com.winifrst.maze.model.Maze;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

public final class MazePanel extends JPanel {

    private static final int FIELD_SIZE = 500;

    private Maze maze;

    public Maze getMaze() {
        return maze;
    }

    public void setMaze(Maze maze) {
        this.maze = maze;
        repaint();
    }

    public MazePanel() {
        setPreferredSize(new Dimension(FIELD_SIZE, FIELD_SIZE));
        setBackground(Color.WHITE);
        this.maze = createTestMaze();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (maze == null) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2));

        int rows = maze.getRows();
        int cols = maze.getCols();
        double cellW = (double) getWidth() / cols;
        double cellH = (double) getHeight() / rows;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Cell cell = maze.getCell(r, c);

                double x = c * cellW;
                double y = r * cellH;

                if (cell.hasWallRight()) {
                    g2.drawLine(
                            (int) (x + cellW), (int) y,
                            (int) (x + cellW), (int) (y + cellH));
                }
                if (cell.hasWallBottom()) {
                    g2.drawLine(
                            (int) x, (int) (y + cellH),
                            (int) (x + cellW), (int) (y + cellH));
                }
            }
        }

        g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
    }

    private Maze createTestMaze() {
        Maze m = new Maze(3, 3);
        m.getCell(0, 0).setWallRight(false);
        m.getCell(0, 1).setWallBottom(false);
        m.getCell(1, 1).setWallRight(false);
        m.getCell(1, 2).setWallBottom(false);
        m.getCell(2, 0).setWallBottom(false);
        return m;
    }
}