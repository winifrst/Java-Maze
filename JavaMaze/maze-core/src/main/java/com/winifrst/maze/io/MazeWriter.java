package com.winifrst.maze.io;

import com.winifrst.maze.exception.MazeException;
import com.winifrst.maze.model.Maze;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Пишет лабиринт в файл в формате задания.
 */
public final class MazeWriter {

    public void write(Maze maze, Path path) {
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            writer.write(maze.getRows() + " " + maze.getCols());
            writer.newLine();
            writeMatrix(writer, maze, true);
            writeMatrix(writer, maze, false);
        } catch (IOException e) {
            throw new MazeException("Не удалось записать лабиринт в " + path, e);
        }
    }

    private void writeMatrix(BufferedWriter writer, Maze maze, boolean rightWalls)
            throws IOException {
        for (int r = 0; r < maze.getRows(); r++) {
            StringBuilder line = new StringBuilder();
            for (int c = 0; c < maze.getCols(); c++) {
                if (c > 0) {
                    line.append(' ');
                }
                boolean wall = rightWalls
                        ? maze.getCell(r, c).hasWallRight()
                        : maze.getCell(r, c).hasWallBottom();
                line.append(wall ? 1 : 0);
            }
            writer.write(line.toString());
            writer.newLine();
        }
    }
}