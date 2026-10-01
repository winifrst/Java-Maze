package com.winifrst.maze.generator;

import com.winifrst.maze.model.Maze;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * Генератор perfect maze по алгоритму Эллера.
 *
 * <p>Алгоритм обрабатывает строки сверху вниз. Для каждой строки:
 * <ol>
 *   <li>Присваивает уникальные номера множеств клеткам без множества.</li>
 *   <li>Случайно убирает правые стены между клетками из разных множеств,
 *       объединяя их множества.</li>
 *   <li>Случайно убирает нижние стены, гарантируя, что у каждого множества
 *       останется хотя бы одна клетка с нижней стеной (иначе получится
 *       изолированная область в следующей строке).</li>
 *   <li>Копирует номера множеств вниз для клеток без нижней стены;
 *       остальным сбрасывает множество.</li>
 * </ol>
 * Для последней строки вместо шага 3 убираются все правые стены между
 * разными множествами, чтобы лабиринт был связным.
 */
public final class EllerGenerator implements MazeGenerator {

    private final Random random;

    public EllerGenerator(Random random) {
        this.random = Objects.requireNonNull(random, "random");
    }

    public EllerGenerator() {
        this(new Random());
    }

    @Override
    public Maze generate(int rows, int cols) {
        Maze maze = new Maze(rows, cols);
        int[] setId = new int[cols];
        int nextSetId = 1;

        for (int r = 0; r < rows; r++) {
            // Шаг 1: присвоить уникальные множества клеткам без множества.
            for (int c = 0; c < cols; c++) {
                if (setId[c] == 0) {
                    setId[c] = nextSetId++;
                }
            }

            // Шаг 2: убрать некоторые правые стены между разными множествами.
            for (int c = 0; c < cols - 1; c++) {
                boolean differentSets = setId[c] != setId[c + 1];
                boolean lastRow = (r == rows - 1);

                if (differentSets && (lastRow || random.nextBoolean())) {
                    maze.getCell(r, c).setWallRight(false);
                    mergeSets(setId, setId[c], setId[c + 1]);
                }
            }

            // Шаг 3: убрать некоторые нижние стены. Для последней строки
            // нижние стены остаются на месте (это внешняя граница).
            if (r < rows - 1) {
                // Вычисляем реальный максимум множеств после объединений.
                int maxSetId = 0;
                for (int id : setId) {
                    if (id > maxSetId) maxSetId = id;
                }
                carveBottomWalls(maze, setId, r, cols, maxSetId);
                // Шаг 4: скопировать множества вниз, сбросить остальные.
                for (int c = 0; c < cols; c++) {
                    if (maze.getCell(r, c).hasWallBottom()) {
                        setId[c] = 0;
                    }
                }
            }
        }
        return maze;
    }

    private void carveBottomWalls(
            Maze maze,
            int[] setId,
            int row,
            int cols,
            int maxSetId
    ) {
        List<List<Integer>> cellsBySet = new ArrayList<>();
        for (int i = 0; i <= maxSetId; i++) {
            cellsBySet.add(new ArrayList<>());
        }

        for (int c = 0; c < cols; c++) {
            cellsBySet.get(setId[c]).add(c);
        }

        for (List<Integer> group : cellsBySet) {
            if (group.isEmpty()) {
                continue;
            }

            // Обязательный проход вниз для данного множества.
            int requiredPassage = group.get(random.nextInt(group.size()));
            maze.getCell(row, requiredPassage).setWallBottom(false);

            // Дополнительные случайные проходы вниз.
            for (int c : group) {
                if (c != requiredPassage && random.nextBoolean()) {
                    maze.getCell(row, c).setWallBottom(false);
                }
            }
        }
    }

    /** Заменяет все вхождения oldId на newId в массиве setId. */
    private void mergeSets(int[] setId, int oldId, int newId) {
        if (oldId == newId) {
            return;
        }
        for (int i = 0; i < setId.length; i++) {
            if (setId[i] == oldId) {
                setId[i] = newId;
            }
        }
    }
}