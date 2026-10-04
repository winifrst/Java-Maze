package com.winifrst.maze.exception;

public class MazeException extends RuntimeException {

    public MazeException(String message) {
        super(message);
    }

    public MazeException(String message, Throwable cause) {
        super(message, cause);
    }
}