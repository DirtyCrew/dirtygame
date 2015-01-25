package com.dirtycrew.dirtyame;

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
        game.setNumPlayers(1);
        game.gameManager.changeState(GameManager.GameState.Start);
    }

    @Override
    public void render(Dirty game) {

    }
}
