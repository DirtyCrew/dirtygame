package com.dirtycrew.dirtyame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

/**
 * Created by Jared on 1/24/15.
 */
public class FinishState implements IGameState {

    @Override
    public void init(Dirty game){

    }

    @Override
    public void shutdown(){

    }

    @Override
    public void update(Dirty game, float delta){

    }

    @Override
    public void render(Dirty game){
        game.batch.begin();
        Gdx.gl.glClearColor(0/255f, 206/255f, 0/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.end();
    }

}
