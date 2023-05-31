package com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.vertex.inspector.preview;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureAtlasLoader;
import com.badlogic.gdx.assets.loaders.resolvers.AbsoluteFileHandleResolver;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.vertex.SimpleAnimationVertexModel;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;

public class PreviewGdxApplicationListener implements ApplicationListener {

    private AbsoluteFileHandleResolver resolver;
    private AssetManager assetManager;

    private SpriteBatch batch;

    // A variable for tracking elapsed time for the animation
    private float stateTime;

    private boolean looping = false;
    private SimpleAnimationVertexModel model;
    private Animation<TextureRegion> animation;
    private TextureAtlas textureAtlas;
    private String pathToAtlas;

    private final InvalidationListener invalidationListener = new InvalidationListener() {
        @Override
        public void invalidated(Observable observable) {
            looping = model.loopingProperty().get();

            Object[] regions = textureAtlas.getRegions().shrink();
            int length = model.getTo() - model.getFrom();
            TextureRegion[] keyFramesArray = new TextureRegion[length];
            System.arraycopy(regions, model.getFrom(), keyFramesArray, 0, length);

            animation = new Animation<TextureRegion>(1f / model.getFrameFrequency(), keyFramesArray);
            animation.setPlayMode(model.getMode());
        }
    };

    private Camera camera;
    private Viewport viewport;

    private float scale = 1;

    @Override
    public void create() {
        resolver = new AbsoluteFileHandleResolver();
        assetManager = new AssetManager(resolver);

        camera = new OrthographicCamera(
                Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight()
        );
        viewport = new FitViewport(300, 300, camera);
        viewport.apply(true);
        camera.update();

        batch = new SpriteBatch(10);

        assetManager.setLoader(TextureAtlas.class, new TextureAtlasLoader(resolver));
        assetManager.load(pathToAtlas, TextureAtlas.class);
        assetManager.finishLoading();
        this.textureAtlas = assetManager.get(pathToAtlas, TextureAtlas.class);

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean scrolled(float amountX, float amountY) {
                scale -= amountY / 10f;
                if(scale < 0) {
                    scale = 0;
                }

                return false;
            }
        });
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void render() {
        clearScreen();                      // очищаем экран

        if (animation == null) {
            return;
        }

        stateTime += Gdx.graphics.getDeltaTime();
        // Get current frame of animation for the current stateTime
        TextureRegion currentFrame = animation.getKeyFrame(stateTime, looping);


        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);
        batch.begin();

        batch.draw(
                currentFrame,
                (viewport.getWorldWidth() - currentFrame.getRegionWidth() * scale) / 2f,
                (viewport.getWorldHeight() - currentFrame.getRegionHeight() * scale) / 2f,
                currentFrame.getRegionWidth() * scale,
                currentFrame.getRegionHeight() * scale
        );

        batch.end();
        camera.update();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        assetManager.dispose();
        textureAtlas.dispose();
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    public void setAtlasTexture(String pathToAtlas) {
        this.pathToAtlas = pathToAtlas;
    }

    public void bindModel(SimpleAnimationVertexModel model) {
        this.model = model;
        stateTime = 0;

        model.frameFrequencyProperty().addListener(invalidationListener);
        model.fromProperty().addListener(invalidationListener);
        model.toProperty().addListener(invalidationListener);
        model.loopingProperty().addListener(invalidationListener);
        model.modeProperty().addListener(invalidationListener);

        invalidationListener.invalidated(null);
    }

    public void unbindModel(SimpleAnimationVertexModel model) {
        stateTime = 0;
        looping = false;

        model.frameFrequencyProperty().removeListener(invalidationListener);
        model.fromProperty().removeListener(invalidationListener);
        model.toProperty().removeListener(invalidationListener);
        model.loopingProperty().removeListener(invalidationListener);
        model.modeProperty().removeListener(invalidationListener);
        this.model = null;

        animation = null;
    }

}
