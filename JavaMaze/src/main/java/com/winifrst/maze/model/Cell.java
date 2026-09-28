package com.winifrst.maze.model;

public final class Cell {
    private boolean hasWallRight = true;
    private boolean hasWallBottom = true;

//    public Cell() {
//        this.hasWallRight = true;
//        this.hasWallBottom = true;
//    }

    public boolean hasWallRight() {
        return hasWallRight;
    }

    public boolean hasWallBottom() {
        return hasWallBottom;
    }

    public void setWallRight(boolean wall) {
        this.hasWallRight = wall;
    }

    public void setWallBottom(boolean wall) {
        this.hasWallBottom = wall;
    }
}
