package com.winifrst.maze;

import com.winifrst.maze.model.Maze;
import com.winifrst.maze.render.ConsoleRenderer;
import java.util.Scanner;

/**
 * Простой консольный фронт: генерация и отрисовка лабиринта.
 * Потом заменим на Swing/Spring, а этот файл оставим для отладки.
 */
public final class Main {

    public static void main(String[] args) {
        MazeService service = new MazeService();
        ConsoleRenderer renderer = new ConsoleRenderer();
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Maze (консольный режим) ===");
        System.out.print("Введите rows cols (например, 10 10): ");
        int rows = scanner.nextInt();
        int cols = scanner.nextInt();

        Maze maze = service.generate(rows, cols);
        System.out.println(renderer.render(maze));
    }

    private Main() {
        // utility class
    }
}