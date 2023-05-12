package com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.vertex.inspector.preview;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by user on 28.02.2017.
 */
public final class ViewHelper {

    public static final float WORLD_WIDTH = 200;
    public static final float WORLD_HEIGHT = 200;

    public static Viewport viewport;
    public static Camera camera;

    static {
        camera = new OrthographicCamera(Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight());
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        camera.update();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
    }

    private ViewHelper(){}

}
