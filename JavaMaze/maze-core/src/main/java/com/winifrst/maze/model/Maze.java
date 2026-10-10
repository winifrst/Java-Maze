package com.winifrst.maze.model;

public final class Maze {
    private final int rows;
    private final int cols;
    private final Cell[][] cells;
    public static final int MAX_SIZE = 50;

    public Maze(int rows, int cols) {
        if (rows < 1 || rows > MAX_SIZE || cols < 1 || cols > MAX_SIZE) {
            throw new IllegalArgumentException(
                    "Лабиринт должен быть в диапазоне от 1 до " + MAX_SIZE + ", но был задан размер: " + rows + " рядов и " + cols + " столбцов.");
        }

        this.rows = rows;
        this.cols = cols;
        this.cells = new Cell[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                cells[r][c] = new Cell();
            }
        }
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public Cell getCell(int row, int col) {
        checkBounds(row, col);
        return cells[row][col];
    }

    public boolean inBounds(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }

    private void checkBounds(int row, int col) {
        if (!inBounds(row, col)) {
            throw new IndexOutOfBoundsException(
                    "Клетка (" + row + "," + col + ") за пределами лабиринта: " + rows + "x" + cols);
        }
    }
}
