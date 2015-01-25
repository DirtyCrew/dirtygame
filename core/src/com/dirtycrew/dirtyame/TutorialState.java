package com.dirtycrew.dirtyame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by sturm on 1/25/15.
 */
public class TutorialState implements IGameState  {
    long start;
    long duration = 10000;
    OrthographicCamera titleCamera;
    SpriteBatch titleBatch;

    private final static int titleCameraZoom = 16;

    @Override
    public void init(Dirty game) {
        start = System.currentTimeMillis();

        titleCamera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
        titleCamera.zoom += titleCameraZoom;//16

        titleBatch = new SpriteBatch();
    }

    @Override
    public void shutdown() {

    }

    @Override
    public void update(Dirty game, float delta) {
        if(System.currentTimeMillis() - start >= duration || Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)) {
            game.gameManager.changeState(GameManager.GameState.Config);
        }
    }

    @Override
    public void render(Dirty game) {
        game.batch.begin();
        Gdx.gl.glClearColor(135 / 255f, 206 / 255f, 235 / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Texture texture = new Texture("control_screen.png");
        Sprite backgroundSprite = new Sprite(texture);
        backgroundSprite.setSize(650, 480);
        backgroundSprite.draw(game.batch);
        game.batch.end();
    }
}
