package com.kgames.engine.logic.entities.particles;

import com.kgames.engine.Core;
import com.kgames.engine.Globals;
import java.awt.Graphics2D;
import java.util.concurrent.Callable;

public abstract class Particle implements Callable<Particle> {

    protected long particleStartTime = 0;
    protected int duration;

    @Override
    public Particle call() {
        update();
        return this;
    }

    public void draw(final Graphics2D g) {
    }

    public boolean isExpired() {
        return Globals.nsToMs(Core.getLogicModule().getTime() - this.particleStartTime) >= this.duration;
    }

    public void setExpire() {
        this.duration = 0;
    }

    public void update() {
    }
}
