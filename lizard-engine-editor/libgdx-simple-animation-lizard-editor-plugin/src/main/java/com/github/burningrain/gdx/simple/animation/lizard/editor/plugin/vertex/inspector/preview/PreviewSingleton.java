package com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.vertex.inspector.preview;

import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas;
import com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.vertex.SimpleAnimationVertexModel;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.WindowEvent;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.util.function.Consumer;

public final class PreviewSingleton {

    private static volatile PreviewSingleton instance;

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

    private Consumer<? super WindowEvent> consumer = new Consumer<WindowEvent>() {
        @Override
        public void accept(WindowEvent windowEvent) {
            if (WindowEvent.WINDOW_CLOSE_REQUEST == windowEvent.getEventType()) {
                libGdxPreview.dispose();
                //frame.dispatchEvent(new java.awt.event.WindowEvent(frame, java.awt.event.WindowEvent.WINDOW_CLOSING));
                frame.dispose();
                isActive = false;
            }
        }
    };

    public void showPreview() {
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

            LwjglAWTCanvas canvas = new LwjglAWTCanvas(libGdxPreview);
            canvas.getCanvas().setVisible(true);
            canvas.getCanvas().setSize(300, 300);

            // using SwingNode does not work.
            frame = new JFrame();
            frame.getContentPane().add(canvas.getCanvas());
            frame.pack();
            frame.setVisible(true);
            frame.setSize(300, 300);

            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    super.windowClosing(e);
                    isActive = false;
                }
            });
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
