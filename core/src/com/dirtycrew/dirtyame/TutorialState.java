package com.dirtycrew.dirtyame;

/**
 * Created by sturm on 1/25/15.
 */
public class TutorialState implements IGameState  {
    long start;
    long duration = 3000;
    @Override
    public void init(Dirty game) {
        start = System.currentTimeMillis();
    }

    @Override
    public void shutdown() {

    }

    @Override
    public void update(Dirty game, float delta) {
        if(System.currentTimeMillis() - start >= duration) {
            game.gameManager.changeState(GameManager.GameState.Config);
        }
    }

    @Override
    public void render(Dirty game) {

    }
}
