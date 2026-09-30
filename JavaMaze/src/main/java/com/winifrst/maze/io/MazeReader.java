package com.winifrst.maze.io;

import com.winifrst.maze.exception.MazeException;
import com.winifrst.maze.model.Maze;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.StringTokenizer;

/**
 * Читает лабиринт из файла в формате задания:
 * <pre>
 * rows cols
 * v[0][0] v[0][1] ... v[0][cols-1]   // 1 = стена справа
 * ...
 * h[0][0] h[0][1] ... h[0][cols-1]   // 1 = стена снизу
 * ...
 * </pre>
 */
public final class MazeReader {

    public Maze read(Path path) {
        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            int[] dimensions = readTwoInts(reader, "размеры лабиринта");
            int rows = dimensions[0];
            int cols = dimensions[1];
            Maze maze = new Maze(rows, cols);

            int[][] rightWalls = readMatrix(reader, rows, cols, "правые стены");
            int[][] bottomWalls = readMatrix(reader, rows, cols, "нижние стены");

            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    maze.getCell(r, c).setWallRight(rightWalls[r][c] != 0);
                    maze.getCell(r, c).setWallBottom(bottomWalls[r][c] != 0);
                }
            }
            return maze;
        } catch (IOException e) {
            throw new MazeException("Не удалось прочитать лабиринт из " + path, e);
        } catch (NumberFormatException e) {
            throw new MazeException("Некорректное число в файле " + path, e);
        }
    }

    private int[] readTwoInts(BufferedReader reader, String what) throws IOException {
        String line = nextNonEmptyLine(reader);
        StringTokenizer tokenizer = new StringTokenizer(line);
        if (tokenizer.countTokens() != 2) {
            throw new MazeException(
                    "Ожидалось 2 числа для " + what + ", получено: " + line);
        }
        return new int[]{
                Integer.parseInt(tokenizer.nextToken()),
                Integer.parseInt(tokenizer.nextToken())
        };
    }

    private int[][] readMatrix(BufferedReader reader, int rows, int cols, String what)
            throws IOException {
        int[][] matrix = new int[rows][cols];
        for (int r = 0; r < rows; r++) {
            String line = nextNonEmptyLine(reader);
            StringTokenizer tokenizer = new StringTokenizer(line);
            if (tokenizer.countTokens() != cols) {
                throw new MazeException(
                        "Строка " + r + " (" + what + "): ожидалось "
                                + cols + " значений, получено " + tokenizer.countTokens());
            }
            for (int c = 0; c < cols; c++) {
                int value = Integer.parseInt(tokenizer.nextToken());
                if (value != 0 && value != 1) {
                    throw new MazeException(
                            what + "[" + r + "][" + c + "]: ожидалось 0 или 1, получено " + value);
                }
                matrix[r][c] = value;
            }
        }
        return matrix;
    }

    private String nextNonEmptyLine(BufferedReader reader) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            if (!line.isBlank()) {
                return line;
            }
        }
        throw new MazeException("Неожиданный конец файла");
    }
}