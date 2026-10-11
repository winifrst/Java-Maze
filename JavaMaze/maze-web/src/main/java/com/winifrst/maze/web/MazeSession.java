package com.winifrst.maze.web;

import com.winifrst.maze.model.Maze;
import com.winifrst.maze.model.Point;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
public class MazeSession {

    private Maze maze;
    private Point start;
    private Point end;
    private List<Point> solution;

    public Maze getMaze() {
        return maze;
    }

    public void setMaze(Maze maze) {
        this.maze = maze;
        this.start = null;
        this.end = null;
        this.solution = null;
    }

    public Point getStart() {
        return start;
    }

    public void setStart(Point start) {
        this.start = start;
    }

    public Point getEnd() {
        return end;
    }

    public void setEnd(Point end) {
        this.end = end;
    }

    public List<Point> getSolution() {
        return solution;
    }

    public void setSolution(List<Point> solution) {
        this.solution = solution;
    }

    public boolean hasMaze() {
        return maze != null;
    }
}