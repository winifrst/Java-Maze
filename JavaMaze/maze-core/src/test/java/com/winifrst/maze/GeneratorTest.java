package com.winifrst.maze;

import com.winifrst.maze.generator.EllerGenerator;
import com.winifrst.maze.model.Maze;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class GeneratorTest {

    @Test
    public void genTest10() {
        EllerGenerator generator = new EllerGenerator();
        Maze maze = generator.generate(10, 10);

        assertTrue(maze != null);
        assertTrue(maze.getRows() == 10);
        assertTrue(maze.getCols() == 10);
    }

    @Test
    public void genTest1() {
        EllerGenerator generator = new EllerGenerator();
        Maze maze = generator.generate(1, 1);

        assertTrue(maze != null);
        assertTrue(maze.getRows() == 1);
        assertTrue(maze.getCols() == 1);
    }

    @Test
    public void genTestEdge() {
        EllerGenerator generator = new EllerGenerator();
        Maze maze = generator.generate(1, 50);
        assertTrue(maze != null);
        assertTrue(maze.getRows() == 1);
        assertTrue(maze.getCols() == 50);
    }
}
