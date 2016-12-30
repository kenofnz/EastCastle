package com.kgames.engine;

import com.kgames.engine.exception.CoreNotStartedException;
import com.kgames.engine.logic.LogicModule;
import com.kgames.engine.render.RenderModule;
import com.kgames.engine.sound.SoundModule;
import java.util.concurrent.Executors;

public class Core {

    private static LogicModule LOGIC_MODULE;
    private static SoundModule SOUND_MODULE;
    private static RenderModule RENDER_MODULE;

    public static RenderModule getRenderModule() {
        if (RENDER_MODULE == null) {
            throw new CoreNotStartedException();
        }
        return RENDER_MODULE;
    }

    public static SoundModule getSoundModule() {
        if (SOUND_MODULE == null) {
            throw new CoreNotStartedException();
        }
        return SOUND_MODULE;
    }

    public static LogicModule getLogicModule() {
        if (LOGIC_MODULE == null) {
            throw new CoreNotStartedException();
        }
        return LOGIC_MODULE;
    }

    public static void setup() {
        LOGIC_MODULE = new LogicModule();
        SOUND_MODULE = new SoundModule();
        RENDER_MODULE = new RenderModule();
        Executors.newSingleThreadExecutor().execute(SOUND_MODULE);
    }
}
