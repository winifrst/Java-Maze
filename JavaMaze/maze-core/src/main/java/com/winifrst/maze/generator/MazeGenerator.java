package com.winifrst.maze.generator;

import com.winifrst.maze.model.Maze;

public interface MazeGenerator {

    Maze generate(int rows, int cols);
}