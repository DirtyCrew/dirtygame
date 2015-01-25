package com.dirtycrew.dirtyame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by sturm on 1/25/15.
 */
public class ConfigState implements IGameState {

    @Override
    public void init(Dirty game) {

    }

    @Override
    public void shutdown() {

    }

    @Override
    public void update(Dirty game, float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_1)){
            game.setNumPlayers(1);
            game.gameManager.changeState(GameManager.GameState.Start);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.NUM_2)){
            game.setNumPlayers(2);
            game.gameManager.changeState(GameManager.GameState.Start);
        }
    }

    @Override
    public void render(Dirty game) {
        game.batch.begin();
        Gdx.gl.glClearColor(135 / 255f, 206 / 255f, 235 / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Texture texture = new Texture("number_of_players_screen.png");
        Sprite backgroundSprite = new Sprite(texture);
        backgroundSprite.setSize(650, 480);
        backgroundSprite.draw(game.batch);
        game.batch.end();
    }
}
