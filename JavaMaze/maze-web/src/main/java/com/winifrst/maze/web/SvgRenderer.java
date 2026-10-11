package com.winifrst.maze.web;

import com.winifrst.maze.model.Cell;
import com.winifrst.maze.model.Maze;
import com.winifrst.maze.model.Point;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class SvgRenderer {

    private static final int SIZE = 500;
    private static final int WALL_WIDTH = 2;
    private static final String WALL_COLOR = "#000000";
    private static final String PATH_COLOR = "#008000";
    private static final String BACKGROUND = "#ffffff";

    public String render(Maze maze) {
        return render(maze, null);
    }

    public String render(Maze maze, List<Point> solution) {
        if (maze == null) {
            return "";
        }
        int rows = maze.getRows();
        int cols = maze.getCols();
        double cellW = (double) SIZE / cols;
        double cellH = (double) SIZE / rows;

        StringBuilder svg = new StringBuilder();
        svg.append("<svg xmlns=\"http://www.w3.org/2000/svg\" ")
                .append("viewBox=\"0 0 ").append(SIZE).append(' ').append(SIZE).append("\" ")
                .append("width=\"").append(SIZE).append("\" ")
                .append("height=\"").append(SIZE).append("\">");

        // Фон
        svg.append("<rect width=\"").append(SIZE).append("\" height=\"").append(SIZE)
                .append("\" fill=\"").append(BACKGROUND).append("\"/>");

        // Стены
        svg.append("<g stroke=\"").append(WALL_COLOR)
                .append("\" stroke-width=\"").append(WALL_WIDTH)
                .append("\" stroke-linecap=\"square\">");

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Cell cell = maze.getCell(r, c);
                double x = c * cellW;
                double y = r * cellH;

                if (cell.hasWallRight()) {
                    appendLine(svg, x + cellW, y, x + cellW, y + cellH);
                }
                if (cell.hasWallBottom()) {
                    appendLine(svg, x, y + cellH, x + cellW, y + cellH);
                }
            }
        }

        // Внешняя рамка (левая и верхняя границы)
        appendLine(svg, 0, 0, SIZE, 0);
        appendLine(svg, 0, 0, 0, SIZE);

        svg.append("</g>");

        // Путь решения
        if (solution != null && solution.size() > 1) {
            svg.append("<polyline fill=\"none\" stroke=\"").append(PATH_COLOR)
                    .append("\" stroke-width=\"").append(WALL_WIDTH)
                    .append("\" stroke-linejoin=\"round\" points=\"");
            for (int i = 0; i < solution.size(); i++) {
                Point p = solution.get(i);
                double x = p.col() * cellW + cellW / 2;
                double y = p.row() * cellH + cellH / 2;
                if (i > 0) {
                    svg.append(' ');
                }
                svg.append(fmt(x)).append(',').append(fmt(y));
            }
            svg.append("\"/>");
        }

        svg.append("</svg>");
        return svg.toString();
    }

    private void appendLine(StringBuilder sb, double x1, double y1, double x2, double y2) {
        sb.append("<line x1=\"").append(fmt(x1))
                .append("\" y1=\"").append(fmt(y1))
                .append("\" x2=\"").append(fmt(x2))
                .append("\" y2=\"").append(fmt(y2))
                .append("\"/>");
    }

    /** Формат числа с 2 знаками после запятой, без локали. */
    private String fmt(double value) {
        return String.format(java.util.Locale.ROOT, "%.2f", value);
    }
}