package com.winifrst.maze.desktop;

import com.winifrst.maze.model.Cell;
import com.winifrst.maze.model.Maze;
import com.winifrst.maze.model.Point;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.JPanel;

public final class MazePanel extends JPanel {

    private static final int FIELD_SIZE = 500;
    private static final Color PATH_COLOR = new Color(0, 180, 0);

    private Maze maze;
    private Point startCell;
    private Point endCell;
    private List<Point> solutionPath;
    private Consumer<Point> cellClickHandler;

    public Maze getMaze() {
        return maze;
    }

    public void setMaze(Maze maze) {
        this.maze = maze;
        this.solutionPath = null;
        repaint();
    }

    public Point getStartCell() {
        return startCell;
    }

    public void setStartCell(Point cell) {
        this.startCell = cell;
    }

    public Point getEndCell() {
        return endCell;
    }

    public void setEndCell(Point cell) {
        this.endCell = cell;
    }

    public void setSolutionPath(List<Point> path) {
        this.solutionPath = path != null ? path : null;
        repaint();
    }

    public void setCellClickHandler(Consumer<Point> handler) {
        this.cellClickHandler = handler;
    }

    public MazePanel() {
        setPreferredSize(new Dimension(FIELD_SIZE, FIELD_SIZE));
        setBackground(Color.WHITE);
        this.maze = createTestMaze();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (maze == null || cellClickHandler == null) {
                    return;
                }
                int rows = maze.getRows();
                int cols = maze.getCols();
                double cellW = (double) getWidth() / cols;
                double cellH = (double) getHeight() / rows;

                int c = (int) (e.getX() / cellW);
                int r = (int) (e.getY() / cellH);

                if (r >= 0 && r < rows && c >= 0 && c < cols) {
                    cellClickHandler.accept(new Point(r, c));
                }
            }
        });
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

        // Подсветка выбранных клеток
        if (startCell != null) {
            drawCellHighlight(g2, startCell, new Color(100, 100, 255), cellW, cellH);
        }
        if (endCell != null && !endCell.equals(startCell)) {
            drawCellHighlight(g2, endCell, new Color(255, 100, 100), cellW, cellH);
        }

        // Отрисовка пути
        if (solutionPath != null && solutionPath.size() > 1) {
            g2.setColor(PATH_COLOR);
            g2.setStroke(new BasicStroke(2));
            for (int i = 0; i < solutionPath.size() - 1; i++) {
                Point p1 = solutionPath.get(i);
                Point p2 = solutionPath.get(i + 1);
                double x1 = p1.col() * cellW + cellW / 2;
                double y1 = p1.row() * cellH + cellH / 2;
                double x2 = p2.col() * cellW + cellW / 2;
                double y2 = p2.row() * cellH + cellH / 2;
                g2.draw(new Line2D.Double(x1, y1, x2, y2));
            }
        }

        g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
    }

    private void drawCellHighlight(Graphics2D g2, Point cell, Color color, double cellW, double cellH) {
        double x = cell.col() * cellW;
        double y = cell.row() * cellH;
        g2.setColor(color);
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect((int) x + 2, (int) y + 2, (int) cellW - 4, (int) cellH - 4, 6, 6);
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
