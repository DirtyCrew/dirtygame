package com.dirtycrew.dirtyame;

import com.badlogic.gdx.Game;

/**
 * Created by sturm on 1/24/15.
 */
public class GameManager {

    public enum GameState {
        Start,
        Play,
        Finish,
        Fail
    }
    //Attributes
    IGameState currentStateObj;
    GameState currentState;
    GameState nextState;
    Dirty game;

    //Methods
    public GameManager(Dirty game){
        this.currentState = GameState.Start;
        this.nextState = GameState.Start;
        this.currentStateObj = new StartState(this, game);
        this.game = game;
    }

    public IGameState getState(){
        return this.currentStateObj;
    }

    public void changeState(GameState state) {
        if(currentState == state) {
           return;
        }
        nextState = state;
    }

    public void update() {
        if(nextState != currentState) {
            currentStateObj.shutdown();
            switch(nextState) {
                case Play:
                    currentStateObj = new PlayState(this, 180000);
                    break;
                case Start:
                    currentStateObj = new StartState(this, game);
                    break;
                case Finish:
                    currentStateObj = new FinishState();
                    break;
            }
            currentStateObj.init(game);
            currentState = nextState;
        }
    }
}
