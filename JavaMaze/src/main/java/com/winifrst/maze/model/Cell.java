package com.winifrst.maze.model;

public final class Cell {
    private boolean wallRight = true;
    private boolean wallBottom = true;

    public boolean hasWallRight() {
        return wallRight;
    }

    public boolean hasWallBottom() {
        return wallBottom;
    }

    public void setWallRight(boolean wallRight) {
        this.wallRight = wallRight;
    }

    public void setWallBottom(boolean wallBottom) {
        this.wallBottom = wallBottom;
    }
}