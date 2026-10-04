package com.winifrst.maze.render;

import com.winifrst.maze.model.Maze;
import com.winifrst.maze.model.Point;
import java.util.List;
import java.util.Set;

/**
 * Рисует лабиринт в консоли символами псевдографики (аналог C-версии).
 *
 * <p>Внешний вид для клетки 1x1:
 * <pre>
 * ┌─┬─┐
 * │ │ │
 * ├─┼─┤
 * │ │ │
 * └─┴─┘
 * </pre>
 *
 * <p>Размер клетки в символах задаётся в конструкторе: {@code cellWidth}
 * — сколько символов по горизонтали внутри клетки, {@code cellHeight}
 * — сколько строк по вертикали. Минимум 1x1.
 */
public final class ConsoleRenderer {

    // Углы и линии рамки
    private static final char TOP_LEFT     = '┌';
    private static final char TOP_RIGHT    = '┐';
    private static final char BOTTOM_LEFT  = '└';
    private static final char BOTTOM_RIGHT = '┘';

    private static final char H_LINE = '─';
    private static final char V_LINE = '│';

    private static final char T_DOWN  = '┬';  // ─┬─  верхняя граница, ответвление вниз
    private static final char T_UP    = '┴';  // ─┴─  нижняя граница, ответвление вверх
    private static final char T_RIGHT = '├';  // ├─   левая граница, ответвление вправо
    private static final char T_LEFT  = '┤';  // ─┤   правая граница, ответвление влево
    private static final char CROSS   = '┼';  // ─┼─  пересечение

    private static final char PATH = '*';

    private final int cellWidth;
    private final int cellHeight;

    /** Клетка 1x1 — компактный вариант. */
    public ConsoleRenderer() {
        this(1, 1);
    }

    public ConsoleRenderer(int cellWidth, int cellHeight) {
        this.cellWidth = Math.max(1, cellWidth);
        this.cellHeight = Math.max(1, cellHeight);
    }

    public String render(Maze maze) {
        return render(maze, List.of());
    }

    public String render(Maze maze, List<Point> path) {
        Set<Point> pathSet = path == null ? Set.of() : Set.copyOf(path);
        StringBuilder sb = new StringBuilder();
        int rows = maze.getRows();
        int cols = maze.getCols();

        // 1. Верхняя граница: ┌─┬─┐
        appendTopBorder(sb, cols);

        // 2. Для каждой строки: содержимое + горизонтальная линия под ней.
        for (int r = 0; r < rows; r++) {
            appendCellRows(sb, maze, r, pathSet);
            appendHorizontalLine(sb, maze, r);
        }
        return sb.toString();
    }

    /**
     * Верхняя граница: для каждой клетки рисуем cellWidth горизонтальных
     * линий и вертикальный разделитель. Углы — TOP_LEFT/TOP_RIGHT,
     * внутренние разделители — T_DOWN.
     */
    private void appendTopBorder(StringBuilder sb, int cols) {
        sb.append(TOP_LEFT);
        for (int c = 0; c < cols; c++) {
            appendChar(sb, H_LINE, cellWidth);
            if (c < cols - 1) {
                sb.append(T_DOWN);
            } else {
                sb.append(TOP_RIGHT);
            }
        }
        sb.append('\n');
    }

    /**
     * Содержимое строки клеток. Рисуется cellHeight раз подряд.
     * Внутри каждой клетки — cellWidth пробелов (или PATH, если клетка
     * на пути), между клетками — вертикальная стена или пробел.
     */
    private void appendCellRows(StringBuilder sb, Maze maze, int row, Set<Point> path) {
        int cols = maze.getCols();
        for (int h = 0; h < cellHeight; h++) {
            sb.append(V_LINE);
            for (int c = 0; c < cols; c++) {
                char fill = (h == 0 && path.contains(new Point(row, c))) ? PATH : ' ';
                appendChar(sb, fill, cellWidth);
                sb.append(maze.getCell(row, c).hasWallRight() ? V_LINE : ' ');
            }
            sb.append('\n');
        }
    }

    /**
     * Горизонтальная линия под строкой. Логика углов/разделителей:
     * <ul>
     *   <li>внутри строки: если у клетки есть нижняя стена — H_LINE,
     *       иначе пробелы;</li>
     *   <li>между клетками: CROSS, если под нами ещё есть строки
     *       и у текущей клетки есть нижняя стена (или стена соседа слева);
     *       иначе T_UP / T_DOWN в зависимости от положения;</li>
     *   <li>для последней строки — нижняя граница с BOTTOM_LEFT/RIGHT
     *       и T_UP.</li>
     * </ul>
     *
     * <p>Проще говоря, действуем как в C-версии: рисуем «нижнюю границу
     * строки», но углы выбираем по позиции.
     */
    private void appendHorizontalLine(StringBuilder sb, Maze maze, int row) {
        int cols = maze.getCols();
        boolean lastRow = (row == maze.getRows() - 1);

        // Левый угол: ├ для промежуточной строки, └ для последней.
        sb.append(lastRow ? BOTTOM_LEFT : T_RIGHT);

        for (int c = 0; c < cols; c++) {
            boolean wall = maze.getCell(row, c).hasWallBottom();
            appendChar(sb, wall ? H_LINE : ' ', cellWidth);

            if (c < cols - 1) {
                // Разделитель между клетками по горизонтали.
                if (lastRow) {
                    sb.append(T_UP);
                } else if (wall) {
                    sb.append(CROSS);
                } else {
                    sb.append(T_UP);
                }
            } else {
                // Правый угол строки: ┤ или ┘.
                sb.append(lastRow ? BOTTOM_RIGHT : T_LEFT);
            }
        }
        sb.append('\n');
    }

    /** Дописывает символ ch ровно count раз. */
    private void appendChar(StringBuilder sb, char ch, int count) {
        for (int i = 0; i < count; i++) {
            sb.append(ch);
        }
    }
}