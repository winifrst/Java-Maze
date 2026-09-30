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
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * Фасад над генератором, решателем и I/O.
 * Не знает ни о Swing, ни о Spring, ни о консоли.
 */
public final class MazeService {

    private final MazeGenerator generator;
    private final MazeSolver solver;
    private final MazeReader reader;
    private final MazeWriter writer;

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

    public Maze generate(int rows, int cols) {
        return generator.generate(rows, cols);
    }

    public List<Point> solve(Maze maze, Point start, Point end) {
        return solver.solve(maze, start, end);
    }

    public Maze load(Path path) {
        return reader.read(path);
    }

    public void save(Maze maze, Path path) {
        writer.write(maze, path);
    }
}