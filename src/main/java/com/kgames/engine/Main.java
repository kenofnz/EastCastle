package com.kgames.engine;

import com.kgames.engine.handler.FocusHandler;
import com.kgames.engine.handler.KeyHandler;
import com.kgames.engine.handler.MouseHandler;
import com.kgames.engine.render.RenderModule;
import com.kgames.engine.screen.ScreenTitle;
import java.awt.Dimension;
import java.util.Arrays;
import java.util.HashSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.JFrame;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

public class Main {

    private static final KeyHandler KEY_HANDLER = new KeyHandler();
    private static final MouseHandler MOUSE_HANDLER = new MouseHandler();
    private static final FocusHandler FOCUS_HANDLER = new FocusHandler();

    public static void main(final String[] args) {
        final HashSet<String> arguments = new HashSet<>();
        arguments.addAll(Arrays.asList(args));

        javax.swing.SwingUtilities.invokeLater(() -> {
            createAndShowGUI();
        });

        Core.setup();
    }

    private static void createAndShowGUI() {

        final JFrame frame = new JFrame(Globals.WINDOW_TITLE);
        final RenderModule renderModule = Core.getRenderModule();

        // frame.setUndecorated(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        if (Globals.WINDOW_SCALE_ENABLED) {
            frame.getContentPane().setPreferredSize(new Dimension((int) (Globals.WINDOW_WIDTH * Globals.WINDOW_SCALE), (int) (Globals.WINDOW_HEIGHT * Globals.WINDOW_SCALE)));
        } else {
            frame.getContentPane().setPreferredSize(new Dimension(Globals.WINDOW_WIDTH, Globals.WINDOW_HEIGHT));
        }
        frame.pack();
        frame.setLocationRelativeTo(null);

        frame.getContentPane().add(renderModule, null);
        frame.setVisible(true);

        renderModule.setLayout(null);
        renderModule.setFocusable(true);
        renderModule.addKeyListener(KEY_HANDLER);
        renderModule.addMouseMotionListener(MOUSE_HANDLER);
        renderModule.addMouseListener(MOUSE_HANDLER);
        renderModule.addFocusListener(FOCUS_HANDLER);
        renderModule.requestFocus();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                // Shutdown logic on jvm exit
                // shutdown sound module
            }
        });
        final ScheduledExecutorService service = Executors.newScheduledThreadPool(2, new BasicThreadFactory.Builder()
                .namingPattern("Client-Runner-%d")
                .daemon(false)
                .priority(Thread.NORM_PRIORITY)
                .build());
        Core.getLogicModule().setScreen(new ScreenTitle());
        service.scheduleAtFixedRate(Core.getLogicModule(), 0, 1, TimeUnit.MILLISECONDS);
        service.scheduleAtFixedRate(renderModule, 0, Globals.RENDER_UPDATE, TimeUnit.MICROSECONDS);
    }
}
