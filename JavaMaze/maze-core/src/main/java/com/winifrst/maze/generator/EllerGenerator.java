package com.winifrst.maze.generator;

import com.winifrst.maze.model.Maze;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

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
            for (int c = 0; c < cols; c++) {
                if (setId[c] == 0) {
                    setId[c] = nextSetId++;
                }
            }

            for (int c = 0; c < cols - 1; c++) {
                boolean differentSets = setId[c] != setId[c + 1];
                boolean lastRow = (r == rows - 1);

                if (differentSets && (lastRow || random.nextBoolean())) {
                    maze.getCell(r, c).setWallRight(false);
                    mergeSets(setId, setId[c], setId[c + 1]);
                }
            }

            if (r < rows - 1) {
                int maxSetId = 0;
                for (int id : setId) {
                    if (id > maxSetId) maxSetId = id;
                }
                carveBottomWalls(maze, setId, r, cols, maxSetId);
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

            int requiredPassage = group.get(random.nextInt(group.size()));
            maze.getCell(row, requiredPassage).setWallBottom(false);

            for (int c : group) {
                if (c != requiredPassage && random.nextBoolean()) {
                    maze.getCell(row, c).setWallBottom(false);
                }
            }
        }
    }

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