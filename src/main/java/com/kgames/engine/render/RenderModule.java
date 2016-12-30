package com.kgames.engine.render;

import com.kgames.engine.Core;
import com.kgames.engine.Globals;
import com.kgames.engine.screen.Screen;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.ImageCapabilities;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.VolatileImage;
import javax.swing.JPanel;

public class RenderModule extends JPanel implements Runnable {

    private int FPS = 0, FPSCount = 0;
    private Graphics2D bufferGraphics;
    private Screen screen = null;
    private boolean useGPU = true;
    private VolatileImage vBuffer;
    double lastFPSTime; // Last time FPS count reset

    double lastUpdateTime; // Last time we rendered

    @Override
    public void paintComponent(final Graphics g) {
        Graphics2D g2d;
        if (useGPU && vBuffer == null) {
            try {
                vBuffer = getGraphicsConfiguration().createCompatibleVolatileImage((int) (Globals.WINDOW_WIDTH * ((Globals.WINDOW_SCALE_ENABLED) ? Globals.WINDOW_SCALE : 1)), (int) (Globals.WINDOW_HEIGHT * ((Globals.WINDOW_SCALE_ENABLED) ? Globals.WINDOW_SCALE : 1)), new ImageCapabilities(true));
            } catch (AWTException ex) {
                useGPU = false;
            }
            bufferGraphics = vBuffer.createGraphics();
        }

        if (useGPU && vBuffer != null) {
            g2d = bufferGraphics;
        } else {
            g2d = (Graphics2D) g;
        }

        super.paintComponent(g2d);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
        final AffineTransform resetForm = g2d.getTransform();
        if (Globals.WINDOW_SCALE_ENABLED) {
            g2d.scale(Globals.WINDOW_SCALE, Globals.WINDOW_SCALE);
        }
        if (this.screen != null) {
            this.screen.draw(g2d);
        }

        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        g2d.setColor(Color.BLACK);
        g2d.drawString("FPS: " + this.FPS, 1220, 15);
        if (Globals.WINDOW_SCALE_ENABLED) {
            g2d.setTransform(resetForm);
        }

        if (useGPU && vBuffer != null) {
            g.drawImage(vBuffer, 0, 0, null);
        }
    }

    @Override
    public void run() {
        final long now = Core.getLogicModule().getTime(); // Get time now
        setScreen(Core.getLogicModule().getScreen());
        repaint();
        this.FPSCount++;

        if (now - this.lastFPSTime >= 1000000000) {
            this.FPS = this.FPSCount;
            this.FPSCount = 0;
            this.lastFPSTime = now;
        }
    }

    public void setScreen(final Screen s) {
        this.screen = s;
    }
}
