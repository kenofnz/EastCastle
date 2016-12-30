package com.kgames.engine;

import com.esotericsoftware.minlog.Log;
import static com.esotericsoftware.minlog.Log.*;
import com.esotericsoftware.minlog.Log.Logger;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

public class Globals {

    public static DecimalFormat NUMBER_FORMAT = new DecimalFormat();
    public static DecimalFormat TIME_NUMBER_FORMAT = new DecimalFormat();

    public static final String DEV_PASSPHRASE = "amFwAkjuy0K/lSvUUyZvdiIFdn/Dzu/OAxStgUEdLKk=";

    public static boolean DEBUG_MODE = true;

    public final static byte GAME_MAJOR_VERSION = 0,
            GAME_MINOR_VERSION = 0,
            GAME_UPDATE_NUMBER = 1;

    private final static String GAME_DEV_STATE = "Prototype";

    public final static String GAME_RELEASE_VERSION = GAME_DEV_STATE + " " + GAME_MAJOR_VERSION + "." + GAME_MINOR_VERSION + "."
            + GAME_UPDATE_NUMBER;

    public final static String WINDOW_TITLE = "EastCastle " + GAME_RELEASE_VERSION;
    public final static boolean WINDOW_SCALE_ENABLED = false;
    public final static double WINDOW_SCALE = 1.5D;
    public final static int WINDOW_WIDTH = 1280;
    public final static int WINDOW_HEIGHT = 720;

    private final static Random RNG = new Random();

    // Render 60 fps in microseconds
    public final static int RENDER_FPS = 60;
    public final static long RENDER_UPDATE = 1000000 / RENDER_FPS;

    private static Logger logger = null;
    private static final String LOG_DIR = "logs";
    private static final String ERR_LOG_FILE = "error.log";
    private static final String LOG_FILE = "log.log";

    public static ExecutorService LOG_THREAD = Executors.newSingleThreadExecutor(
            new BasicThreadFactory.Builder()
                    .namingPattern("Logger-%d")
                    .daemon(true)
                    .priority(Thread.MIN_PRIORITY)
                    .build());

    private static void loadNumberFormats() {
        NUMBER_FORMAT.setGroupingSize(3);
        NUMBER_FORMAT.setGroupingUsed(true);
        NUMBER_FORMAT.setDecimalSeparatorAlwaysShown(false);
        NUMBER_FORMAT.setMaximumFractionDigits(2);

        TIME_NUMBER_FORMAT.setDecimalSeparatorAlwaysShown(false);
        TIME_NUMBER_FORMAT.setMaximumFractionDigits(1);
    }

    private static void loadSound() {
    }

    public static final long nsToMs(final long time) {
        return TimeUnit.MILLISECONDS.convert(time, TimeUnit.NANOSECONDS);
    }

    public static final long msToNs(final long time) {
        return TimeUnit.NANOSECONDS.convert(time, TimeUnit.MILLISECONDS);
    }

    public static final int rng(final int i) {
        if (i > 0) {
            return RNG.nextInt(i);
        }
        return -1;
    }

    public static final BufferedImage loadTextureResource(String path) {
        try {
            InputStream resource = loadResourceAsStream(path);
            if (resource != null) {
                return ImageIO.read(loadResourceAsStream(path));
            }
        } catch (IOException ex) {
            System.err.println("Failed to load texture: " + path);
        }
        return null;
    }

    public static final InputStream loadResourceAsStream(String path) {
        try {
            return FileUtils.openInputStream(new File("resources/" + path));
        } catch (IOException ex) {
            //System.err.println("Failed to load resource: " + path);
        }
        return null;
    }

    public enum BGMs {
        TEST((byte) 0x00, "Test.ogg");

        private static final Map<Byte, BGMs> lookup = new HashMap<Byte, BGMs>();

        static {
            for (BGMs bgm : BGMs.values()) {
                lookup.put(bgm.getBgmCode(), bgm);
            }
        }

        public static BGMs get(byte code) {
            return lookup.get(code);
        }

        private final byte bgmCode;
        private final String resourcePath;

        BGMs(byte bgmCode, String resourcePath) {
            this.bgmCode = bgmCode;
            this.resourcePath = resourcePath;
        }

        public byte getBgmCode() {
            return this.bgmCode;
        }

        public String getResourcePath() {
            return this.resourcePath;
        }
    }

    public enum SFXs {
        TEST((byte) 0x00, "test.wav");

        private static final Map<Byte, SFXs> lookup = new HashMap<Byte, SFXs>();

        static {
            for (SFXs sfx : SFXs.values()) {
                lookup.put(sfx.getSfxCode(), sfx);
            }
        }

        public static SFXs get(byte code) {
            return lookup.get(code);
        }

        private final byte sfxCode;
        private final String resourcePath;

        SFXs(byte sfxCode, String resourcePath) {
            this.sfxCode = sfxCode;
            this.resourcePath = "sfx/" + resourcePath;
        }

        public byte getSfxCode() {
            return this.sfxCode;
        }

        public String getResourcePath() {
            return this.resourcePath;
        }
    }

    public final static void log(final Class category, final String info, final int logLevel) {
        final String className = category.getSimpleName();
        final Runnable logging = () -> {
            if (logger == null) {
                logger = new Logger() {
                    @Override
                    public void log(int level, String category, String message, Throwable ex) {
                        StringBuilder builder = new StringBuilder(256);
                        builder.append('[');
                        builder.append(String.format("%1$td/%1$tm/%1$tY %1$tT %1$tZ", System.currentTimeMillis()));
                        builder.append(']');

                        builder.append(' ');
                        switch (level) {
                            case LEVEL_ERROR:
                                builder.append(" ERROR: ");
                                break;
                            case LEVEL_WARN:
                                builder.append("  WARN: ");
                                break;
                            case LEVEL_INFO:
                                builder.append("  INFO: ");
                                break;
                            case LEVEL_DEBUG:
                                builder.append(" DEBUG: ");
                                break;
                            case LEVEL_TRACE:
                                builder.append(" TRACE: ");
                                break;
                        }
                        builder.append(' ');

                        builder.append('[');
                        builder.append(category);
                        builder.append("] ");
                        builder.append(message);
                        if (ex != null) {
                            StringWriter writer = new StringWriter(256);
                            ex.printStackTrace(new PrintWriter(writer));
                            builder.append('\n');
                            builder.append(writer.toString().trim());
                        }
                        System.out.println(builder);

                        String logFile;
                        switch (level) {
                            case LEVEL_ERROR:
                                logFile = ERR_LOG_FILE;
                                break;
                            default:
                                logFile = LOG_FILE;
                                break;
                        }
                        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(new File(LOG_DIR, logFile), true)))) {
                            out.println(builder);
                        } catch (final IOException e) {
                            System.err.println(e);
                        }
                    }
                };
            };
            Log.setLogger(logger);

            switch (logLevel) {
                case LEVEL_ERROR:
                    Log.error(className, info);
                    break;
                default:
                    Log.info(className, info);
                    break;
            }
        };
        LOG_THREAD.execute(logging);
    }

    public static final void logError(final String errorMessage, final Exception e) {
        Log.error(errorMessage, e);
    }
}
