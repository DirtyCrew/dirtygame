package com.dirtycrew.dirtyame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by z084254 on 1/24/15.
 */
public class InputController {
    List<InputSet> inputSets;

    public InputController(){
        inputSets = new ArrayList<InputSet>();
    }

    public boolean isRightPressed(){
        for (InputSet set:inputSets){
            if (set.isRightPressed()){
                return true;
            }
        }
        return false;
    }

    public boolean isLeftPressed(){
        for (InputSet set:inputSets){
            if (set.isLeftPressed()){
                return true;
            }
        }
        return false;
    }

    public boolean isJumpPressed(){
        for (InputSet set:inputSets){
            if (set.isJumpPressed()){
                return true;
            }
        }
        return false;
    }

    public boolean isAttackPressed(){
        for (InputSet set:inputSets){
            if (set.isAttackPressed()){
                return true;
            }
        }
        return false;
    }

    public void randomizeControls(){
        for (InputSet set:inputSets){
            set.setAllInactive();
        }

        Random randomizer = new Random();
        inputSets.get(randomizer.nextInt(inputSets.size())).setRightActive(true);
        inputSets.get(randomizer.nextInt(inputSets.size())).setLeftActive(true);
        inputSets.get(randomizer.nextInt(inputSets.size())).setJumpActive(true);
        inputSets.get(randomizer.nextInt(inputSets.size())).setAttackActive(true);
    }
}
