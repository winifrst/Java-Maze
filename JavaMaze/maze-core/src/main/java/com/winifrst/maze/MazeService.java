package com.winifrst.maze;

import com.winifrst.maze.generator.EllerGenerator;
import com.winifrst.maze.generator.MazeGenerator;
import com.winifrst.maze.io.MazeReader;
import com.winifrst.maze.io.MazeWriter;
import com.winifrst.maze.model.Maze;
import com.winifrst.maze.model.Point;
import com.winifrst.maze.solver.BfsSolver;
import com.winifrst.maze.solver.MazeSolver;

import java.nio.file.Path;
import java.util.*;

import static com.winifrst.maze.model.Maze.MAX_SIZE;

/**
 * Фасад над генератором, решателем и I/O.
 * <p>
 * Точка входа для всех операций с лабиринтом: генерация, решение, сохранение, загрузка.
 * Не знает ни о Swing, ни о Spring, ни о консоли — чистая бизнес-логика.
 * <p>
 * Для Spring-обёртки достаточно внедрить зависимости через конструктор:
 * <pre>{@code
 * @Service
 * class MyMazeService {
 *     private final MazeService mazeService;
 *     MyMazeService(MazeService mazeService) { this.mazeService = mazeService; }
 * }
 * }</pre>
 * <p>
 * Для тестов — использовать конструктор с моками:
 * <pre>{@code
 * MazeService service = new MazeService(mockGenerator, mockSolver, mockReader, mockWriter);
 * }</pre>
 */
public final class MazeService {

    private final MazeGenerator generator;
    private final MazeSolver solver;
    private final MazeReader reader;
    private final MazeWriter writer;

    /**
     * Полный конструктор — для DI и тестов.
     */
    public MazeService(MazeGenerator generator, MazeSolver solver,
                       MazeReader reader, MazeWriter writer) {
        this.generator = Objects.requireNonNull(generator, "generator");
        this.solver = Objects.requireNonNull(solver, "solver");
        this.reader = Objects.requireNonNull(reader, "reader");
        this.writer = Objects.requireNonNull(writer, "writer");
    }

    /** Конструктор по умолчанию: Эллер + BFS + стандартный I/O. */
    public MazeService() {
        this(new EllerGenerator(), new BfsSolver(), new MazeReader(), new MazeWriter());
    }

    /** Для тестов и воспроизводимости. */
    public MazeService(Random random) {
        this(new EllerGenerator(random), new BfsSolver(), new MazeReader(), new MazeWriter());
    }

    // ===================== Генерация =====================

    private static void validateDimensions(int rows, int cols) {
        if (rows < 1 || cols < 1) {
            throw new IllegalArgumentException(
                    "Размеры должны быть >= 1, но получили: " + rows + "x" + cols);
        }
        if (rows > MAX_SIZE || cols > MAX_SIZE) {
            throw new IllegalArgumentException(
                    "Размеры должны быть <= " + MAX_SIZE + ", но получили: " + rows + "x" + cols);
        }
    }

    /**
     * Генерирует идеальный лабиринт заданного размера.
     *
     * @param rows количество строк (должно быть >= 1)
     * @param cols количество столбцов (должно быть >= 1)
     * @return готовый лабиринт
     * @throws IllegalArgumentException если размеры некорректны
     */
    public Maze generate(int rows, int cols) {
        validateDimensions(rows, cols);
        return generator.generate(rows, cols);
    }

    // ===================== Решение =====================

    /**
     * Генерирует лабиринт и сразу находит решение.
     *
     * @return пара (лабиринт, путь решения); путь может быть пустым, если решения нет
     */
    public MazeWithSolution generateWithSolution(int rows, int cols) {
        Maze maze = generate(rows, cols);
        Point start = new Point(0, 0);
        Point end = new Point(rows - 1, cols - 1);
        List<Point> solution = solver.solve(maze, start, end);
        return new MazeWithSolution(maze, solution != null ? solution : Collections.emptyList());
    }

    // ===================== I/O =====================

    /**
     * Находит путь от start до end в лабиринте (BFS).
     *
     * @return список точек пути от начала до конца; пустой список, если решения нет
     */
    public List<Point> solve(Maze maze, Point start, Point end) {
        Objects.requireNonNull(maze, "maze");
        Objects.requireNonNull(start, "start");
        Objects.requireNonNull(end, "end");
        List<Point> solution = solver.solve(maze, start, end);
        return solution != null ? solution : Collections.emptyList();
    }

    /**
     * Загружает лабиринт из файла.
     */
    public Maze load(Path path) {
        Objects.requireNonNull(path, "path");
        return reader.read(path);
    }

    // ===================== Валидация =====================

    /**
     * Сохраняет лабиринт в файл.
     */
    public void save(Maze maze, Path path) {
        Objects.requireNonNull(maze, "maze");
        Objects.requireNonNull(path, "path");
        writer.write(maze, path);
    }

    /**
     * Проверяет, что лабиринт идеален (perfect):
     * <ul>
     *   <li>Все клетки связаны (BFS от (0,0) посещает все клетки)</li>
     *   <li>Нет циклов</li>
     * </ul>
     * <p>
     * Для больших лабиринтов проверка может занять время.
     */
    public boolean isPerfect(Maze maze) {
        Objects.requireNonNull(maze, "maze");
        if (maze.getRows() == 0 || maze.getCols() == 0) {
            return false;
        }
        int totalCells = maze.getRows() * maze.getCols();
        List<Point> visited = bfsVisitAll(maze, new Point(0, 0));
        return visited.size() == totalCells;
    }

    // ===================== Геттеры для DI =====================

    /**
     * Проверяет, что стартовая и конечная точки корректны для данного лабиринта.
     */
    public boolean isValidPoints(Maze maze, Point start, Point end) {
        Objects.requireNonNull(maze, "maze");
        Objects.requireNonNull(start, "start");
        Objects.requireNonNull(end, "end");
        return maze.inBounds(start.row(), start.col())
                && maze.inBounds(end.row(), end.col());
    }

    /**
     * Возвращает генератор — для Spring-биндинга и тестов.
     */
    public MazeGenerator getGenerator() {
        return generator;
    }

    // ===================== Внутренние методы =====================

    /**
     * Возвращает солвер — для Spring-биндинга и тестов.
     */
    public MazeSolver getSolver() {
        return solver;
    }

    /**
     * BFS, который собирает все достижимые клетки от start.
     */
    private List<Point> bfsVisitAll(Maze maze, Point start) {
        boolean[][] visited = new boolean[maze.getRows()][maze.getCols()];
        Queue<Point> queue = new ArrayDeque<>();
        List<Point> result = new ArrayList<>();

        queue.add(start);
        visited[start.row()][start.col()] = true;

        while (!queue.isEmpty()) {
            Point current = queue.poll();
            result.add(current);

            // Соседи: up, down, left, right
            int[][] deltas = {
                    {-1, 0}, {1, 0}, {0, -1}, {0, 1}
            };

            for (int[] d : deltas) {
                int nr = current.row() + d[0];
                int nc = current.col() + d[1];

                if (!maze.inBounds(nr, nc) || visited[nr][nc]) {
                    continue;
                }

                // Проверяем, нет стены между current и (nr, nc)
                boolean blocked = false;
                if (d[0] == 1) { // вниз
                    blocked = maze.getCell(current.row(), current.col()).hasWallBottom();
                } else if (d[0] == -1) { // вверх
                    blocked = maze.getCell(nr, nc).hasWallBottom();
                } else if (d[1] == 1) { // вправо
                    blocked = maze.getCell(current.row(), current.col()).hasWallRight();
                } else if (d[1] == -1) { // влево
                    blocked = maze.getCell(nr, nc).hasWallRight();
                }

                if (!blocked) {
                    visited[nr][nc] = true;
                    queue.add(new Point(nr, nc));
                }
            }
        }

        return result;
    }

    // ===================== DTO =====================

    /**
     * Нерезультат: лабиринт + найденный путь.
     */
    public record MazeWithSolution(Maze maze, List<Point> solution) {
        public MazeWithSolution {
            Objects.requireNonNull(maze, "maze");
        }
    }
}
