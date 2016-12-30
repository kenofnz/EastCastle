package com.kgames.engine.screen;

import java.awt.Graphics2D;
import java.awt.event.FocusListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public abstract class Screen implements KeyListener, MouseListener, MouseMotionListener, FocusListener {

    public abstract void update();

    public abstract void draw(Graphics2D g);

}
