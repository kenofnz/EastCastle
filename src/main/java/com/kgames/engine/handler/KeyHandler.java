package com.kgames.engine.handler;

import com.kgames.engine.Core;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    @Override
    public void keyTyped(final KeyEvent e) {
        Core.getLogicModule().getScreen().keyTyped(e);
    }

    @Override
    public void keyPressed(final KeyEvent e) {
        Core.getLogicModule().getScreen().keyPressed(e);
    }

    @Override
    public void keyReleased(final KeyEvent e) {
        Core.getLogicModule().getScreen().keyReleased(e);
    }
}
