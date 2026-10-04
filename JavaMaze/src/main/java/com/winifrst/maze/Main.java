package com.winifrst.maze;

import com.winifrst.maze.render.ConsoleRenderer;

import java.util.Scanner;

/**
 * Простой консольный фронт: генерация, отрисовка и решение лабиринта.
 */
public final class Main {

    private Main() {
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
                MazeService.MazeWithSolution result = service.generateWithSolution(rows, cols);
                System.out.println(renderer.render(result.maze()));

                if (!result.solution().isEmpty()) {
                    System.out.println("Решение (" + result.solution().size() + " шагов):");
                    System.out.println(renderer.render(result.maze(), result.solution()));
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