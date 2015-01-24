package com.dirtycrew.dirtyame;

import com.badlogic.gdx.Game;

/**
 * Created by sturm on 1/24/15.
 */
public class GameManager {

    //Attributes
    IGameState currentState;
    Dirty game;

    //Methods
    public GameManager(Dirty game){
        this.currentState = new StartState();
        this.currentState.init(game);
        this.game = game;
    }

    public IGameState getState(){
        return this.currentState;
    }

    public void transitionToState(IGameState gameState){
        this.currentState.shutdown();
        this.currentState = gameState;
        this.currentState.init(this.game);
    }
}
