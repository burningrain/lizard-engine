package com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.vertex.inspector.preview;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglCanvas;
import com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.vertex.SimpleAnimationVertexModel;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.WindowEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

import java.awt.event.WindowAdapter;
import java.util.function.Consumer;

public final class PreviewSingleton {

    private static volatile PreviewSingleton instance;

    private static final Logger LOGGER = LoggerFactory.getLogger(PreviewSingleton.class);

    private static final int FRAME_WIDTH = 300;
    private static final int FRAME_HEIGHT = 300;

    public static PreviewSingleton getInstance() {
        if (instance == null) {
            synchronized (PreviewSingleton.class) {
                if (instance == null) {
                    instance = new PreviewSingleton();
                }
            }
        }

        return instance;
    }

    private String pathToAtlas;
    private boolean isActive = false;
    private PreviewGdxApplicationListener libGdxPreview;
    private JFrame frame;

    private final Consumer<? super WindowEvent> consumer = new Consumer<WindowEvent>() {
        @Override
        public void accept(WindowEvent windowEvent) {
            if (WindowEvent.WINDOW_CLOSE_REQUEST == windowEvent.getEventType()) {
                if(!isActive) {
                    return;
                }

                SwingUtilities.invokeLater(() -> {
                    if(frame != null) {
                        frame.dispose();
                    }

                    try {
                        Gdx.app.exit();
                    } catch (Exception e) {
                        // java.lang.IllegalArgumentException: buffer not allocated with newUnsafeByteBuffer or already disposed
                        LOGGER.error(e.getMessage(), e);
                    }
                });
                isActive = false;
            }
        }
    };

    public void showPreview(double x, double y) {
        if (isActive) {
            return;
        }

        if (pathToAtlas == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "The path to TextureAtlas is not defined", ButtonType.YES);
            alert.show();
            return;
        }

        isActive = true;
        SwingUtilities.invokeLater(() -> {
            libGdxPreview = new PreviewGdxApplicationListener();
            libGdxPreview.setAtlasTexture(pathToAtlas);

            LwjglCanvas canvas = new LwjglCanvas(libGdxPreview);
            canvas.getCanvas().setVisible(true);
            //canvas.getCanvas().setSize(FRAME_WIDTH, FRAME_HEIGHT);

            // using SwingNode does not work.
            frame = new JFrame();
            frame.getContentPane().add(canvas.getCanvas());
            frame.pack();
            frame.setVisible(true);
            frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);

            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    super.windowClosing(e);
                    isActive = false;
                }
            });

            frame.setLocation((int) x, (int) y);
            frame.setAlwaysOnTop(true);
        });
    }

    public Consumer<? super WindowEvent> getConsumerOnWindowEvent() {
        return consumer;
    }

    public void loadAtlasTexture(String path) {
        this.pathToAtlas = path;
    }

    public void bindModel(SimpleAnimationVertexModel model) {
        if (libGdxPreview == null) {
            return;
        }

        libGdxPreview.bindModel(model);
    }

    public void unbindModel(SimpleAnimationVertexModel model) {
        if (libGdxPreview == null) {
            return;
        }

        libGdxPreview.unbindModel(model);
    }

}
