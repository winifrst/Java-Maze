package com.winifrst.maze.solver;

import com.winifrst.maze.model.Cell;
import com.winifrst.maze.model.Maze;
import com.winifrst.maze.model.Point;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class BfsSolver implements MazeSolver {

    private static final int[][] DIRECTIONS = {
            {-1, 0},  // вверх
            {1, 0},   // вниз
            {0, -1},  // влево
            {0, 1},   // вправо
    };

    @Override
    public List<Point> solve(Maze maze, Point start, Point end) {
        if (!maze.inBounds(start.row(), start.col())
                || !maze.inBounds(end.row(), end.col())) {
            throw new IllegalArgumentException(
                    "Начальная или конечная точка вне лабиринта: " + start + " -> " + end);
        }
        if (start.equals(end)) {
            return List.of(start);
        }

        Map<Point, Point> parent = new HashMap<>();
        Deque<Point> queue = new ArrayDeque<>();
        queue.add(start);
        parent.put(start, null);

        while (!queue.isEmpty()) {
            Point current = queue.poll();
            if (current.equals(end)) {
                return reconstructPath(parent, end);
            }
            for (int[] direction : DIRECTIONS) {
                int nextRow = current.row() + direction[0];
                int nextCol = current.col() + direction[1];
                if (!maze.inBounds(nextRow, nextCol)) {
                    continue;
                }
                if (!canMove(maze, current, nextRow, nextCol)) {
                    continue;
                }
                Point next = new Point(nextRow, nextCol);
                if (parent.containsKey(next)) {
                    continue;
                }
                parent.put(next, current);
                queue.add(next);
            }
        }
        return Collections.emptyList();
    }

    private boolean canMove(Maze maze, Point from, int toRow, int toCol) {
        int deltaRow = toRow - from.row();
        int deltaCol = toCol - from.col();

        if (deltaRow == 0 && deltaCol == 1) {
            return !maze.getCell(from.row(), from.col()).hasWallRight();
        }
        if (deltaRow == 0 && deltaCol == -1) {
            return !maze.getCell(toRow, toCol).hasWallRight();
        }
        if (deltaRow == 1 && deltaCol == 0) {
            return !maze.getCell(from.row(), from.col()).hasWallBottom();
        }
        if (deltaRow == -1 && deltaCol == 0) {
            return !maze.getCell(toRow, toCol).hasWallBottom();
        }
        throw new IllegalStateException(
                "Несоседние клетки: " + from + " -> (" + toRow + "," + toCol + ")");
    }

    private List<Point> reconstructPath(Map<Point, Point> parent, Point end) {
        List<Point> path = new ArrayList<>();
        for (Point current = end; current != null; current = parent.get(current)) {
            path.add(current);
        }
        Collections.reverse(path);
        return path;
    }
}