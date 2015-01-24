package com.dirtycrew.dirtyame;

import com.badlogic.gdx.Game;

/**
 * Created by sturm on 1/24/15.
 */
public class GameManager {

    public enum GameState {
        Start,
        Play,
        Finish
    }
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

    public void changeState(GameState state) {
        IGameState nextState = null;
        switch(state) {
            case Play:
                if(currentState.getClass() == PlayState.class) {
                    return;
                }

                nextState = new PlayState(this, 180000);
                break;
            case Start:
                if(currentState.getClass() == StartState.class) {
                    return;
                }

                nextState = new StartState();
                break;
            case Finish:
                if(currentState.getClass() == FinishState.class) {
                    return;
                }

                nextState = new FinishState();
                break;
        }

        currentState.shutdown();
        nextState.init(game);
        currentState = nextState;
    }
}
