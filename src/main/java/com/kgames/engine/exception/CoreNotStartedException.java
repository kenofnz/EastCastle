package com.kgames.engine.exception;

public class CoreNotStartedException extends RuntimeException {

    public CoreNotStartedException() {
        super("Core has not started.");
    }
}
