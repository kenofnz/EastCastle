package com.kgames.engine.logic;

import com.kgames.engine.Core;
import com.kgames.engine.screen.Screen;

public class LogicModule implements Runnable {

    private Screen screen;
    private long currentTime = 0;

    @Override
    public void run() {
        try {
            this.currentTime = System.nanoTime();
            this.screen.update();
        } catch (final Exception ex) {
        }
    }

    public Screen getScreen() {
        return this.screen;
    }

    public void setScreen(Screen screen) {
        Core.getLogicModule().screen = screen;
    }

    public long getTime() {
        return this.currentTime;
    }
}
