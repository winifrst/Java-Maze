package com.winifrst.maze.render;

import com.winifrst.maze.model.Maze;
import com.winifrst.maze.model.Point;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Рисует лабиринт в консоли символами ASCII.
 *
 * <p>Пример для 2x2:
 * <pre>
 * +--+--+
 * |  |  |
 * +  +  +
 * |  |  |
 * +--+--+
 * </pre>
 *
 * <p>Если передан путь решения — клетки пути помечаются символом '*'.
 */
public final class ConsoleRenderer {

    private static final char WALL_H = '-';
    private static final char WALL_V = '|';
    private static final char CORNER = '+';
    private static final char PATH = '*';

    public String render(Maze maze) {
        return render(maze, List.of());
    }

    public String render(Maze maze, List<Point> path) {
        Set<Point> pathSet = path == null ? Set.of() : Set.copyOf(path);
        StringBuilder sb = new StringBuilder();

        for (int r = 0; r < maze.getRows(); r++) {
            // Верхняя линия ряда: +--+--+...
            appendHorizontalLine(sb, maze, r);
            // Содержимое ряда: |  |  |...
            appendCellLine(sb, maze, r, pathSet);
        }
        // Нижняя граница
        appendBottomLine(sb, maze);
        return sb.toString();
    }

    private void appendHorizontalLine(StringBuilder sb, Maze maze, int row) {
        for (int c = 0; c < maze.getCols(); c++) {
            sb.append(CORNER);
            sb.append(WALL_H);
            sb.append(WALL_H);
        }
        sb.append(CORNER).append('\n');
    }

    private void appendCellLine(StringBuilder sb, Maze maze, int row, Set<Point> path) {
        for (int c = 0; c < maze.getCols(); c++) {
            sb.append(WALL_V);
            sb.append(path.contains(new Point(row, c)) ? PATH : ' ');
            sb.append(' ');
        }
        // правая граница ряда — всегда стена
        sb.append(WALL_V).append('\n');
    }

    private void appendBottomLine(StringBuilder sb, Maze maze) {
        for (int c = 0; c < maze.getCols(); c++) {
            sb.append(CORNER);
            sb.append(WALL_H);
            sb.append(WALL_H);
        }
        sb.append(CORNER).append('\n');
    }
}