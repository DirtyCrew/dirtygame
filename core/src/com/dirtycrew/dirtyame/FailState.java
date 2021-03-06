package com.dirtycrew.dirtyame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Jared on 1/24/15.
 */
public class FailState implements IGameState {

    SpriteBatch hudBatch;
    OrthographicCamera hudCamera;
    BitmapFont font;

    @Override
    public void init(Dirty game){
        font = new BitmapFont();
        font.setColor(Color.RED);

        hudBatch = new SpriteBatch();

        hudCamera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);

        game.gameManager.numDeaths++;
    }

    @Override
    public void shutdown(){

    }

    @Override
    public void update(Dirty game, float delta){
        if(Gdx.input.isKeyPressed(Input.Keys.NUM_1)) {
            game.gameManager.changeState(GameManager.GameState.Start);
            try {
                Thread.sleep(100);
            } catch(Exception e) {

            }
        } else if(Gdx.input.isKeyPressed(Input.Keys.NUM_2)) {
            game.gameManager.changeState(GameManager.GameState.Play, game.map);
            try {
                Thread.sleep(100);
            } catch(Exception e) {

            }
        }

        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            game.gameManager.changeState(GameManager.GameState.Start, game.map);
            try {
                Thread.sleep(100);
            } catch(Exception e) {

            }
        }


    }

    @Override
    public void render(Dirty game){
        game.batch.begin();
        Gdx.gl.glClearColor(0/255f, 206/255f, 0/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.end();

        hudCamera.update();
        hudBatch.setProjectionMatrix(hudCamera.projection);
        hudBatch.begin();

        Texture texture = new Texture("death_screen.png");
        Sprite backgroundSprite = new Sprite(texture);
        backgroundSprite.setSize(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
        backgroundSprite.setPosition(-Constants.VIEWPORT_WIDTH/2, -Constants.VIEWPORT_HEIGHT/2);
        backgroundSprite.draw(hudBatch);

//        font.setScale(0.5f);
//        font.draw(hudBatch, "Fail", 0, 0);

        hudBatch.end();
    }

}
