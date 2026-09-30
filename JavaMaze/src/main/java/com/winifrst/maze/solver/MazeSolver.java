package com.winifrst.maze.solver;

import com.winifrst.maze.model.Maze;
import com.winifrst.maze.model.Point;
import java.util.List;

public interface MazeSolver {

    List<Point> solve(Maze maze, Point start, Point end);
}