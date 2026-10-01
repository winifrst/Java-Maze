package com.winifrst.maze;

import com.winifrst.maze.model.Maze;
import com.winifrst.maze.model.Point;
import com.winifrst.maze.render.ConsoleRenderer;
import java.util.List;
import java.util.Scanner;

/**
 * Простой консольный фронт: генерация, отрисовка и решение лабиринта.
 */
public final class Main {

    private Main() {
        // utility class
    }

    public static void main(String[] args) {
        MazeService service = new MazeService();
        ConsoleRenderer renderer = new ConsoleRenderer();
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Maze (консольный режим) ===");

        while (true) {
            System.out.print("Введите rows cols (например, 10 10, или 'q' для выхода): ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("q") || input.equalsIgnoreCase("exit")) {
                System.out.println("Выход.");
                break;
            }

            String[] parts = input.split("\\s+");
            if (parts.length != 2) {
                System.out.println("Неверный формат. Введите два числа через пробел.");
                continue;
            }

            int rows, cols;
            try {
                rows = Integer.parseInt(parts[0]);
                cols = Integer.parseInt(parts[1]);
            } catch (NumberFormatException e) {
                System.out.println("Неверный формат. Введите два числа.");
                continue;
            }

            if (rows < 1 || cols < 1 || rows > 50 || cols > 50) {
                System.out.println("Размер должен быть от 1 до 50.");
                continue;
            }

            try {
                Maze maze = service.generate(rows, cols);
                System.out.println(renderer.render(maze));

                // Показать решение (от верх-лево до низ-право)
                Point start = new Point(0, 0);
                Point end = new Point(rows - 1, cols - 1);
                List<Point> solution = service.solve(maze, start, end);

                if (solution != null && !solution.isEmpty()) {
                    System.out.println("Решение (" + solution.size() + " шагов):");
                    System.out.println(renderer.render(maze, solution));
                } else {
                    System.out.println("Решение не найдено.");
                }

                System.out.println(); // пустая строка для разделения

            } catch (Exception e) {
                System.out.println("Ошибка генерации: " + e.getMessage());
            }
        }

        scanner.close();
    }
}