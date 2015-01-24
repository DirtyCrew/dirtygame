package com.dirtycrew.dirtyame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.PovDirection;

/**
 * Created by z084254 on 1/24/15.
 */
public class InputSet{
    Controller controller;
    private boolean isController; //otherwise keyboard
    private int keyboardRight;
    private int keyboardLeft;
    private int jump;
    private int attack;
//    private PovDirection dpadRight;
//    private PovDirection dpadLeft;
    private int dpadRight;
    private int dpadLeft;
    private boolean rightActive;
    private boolean leftActive;
    private boolean jumpActive;
    private boolean attackActive;

    public InputSet(int right, int left, int jump, int attack){
        isController = false;
        keyboardRight = right;
        keyboardLeft = left;
        this.jump = jump;
        this.attack = attack;
        rightActive = true;
        leftActive = true;
        jumpActive = true;
        attackActive = true;
    }
    public InputSet(Controller controller, int right, int left, int jump, int attack){
        this.controller = controller;
        isController = true;
        dpadRight = right;
        dpadLeft = left;
        this.jump = jump;
        this.attack = attack;
        rightActive = true;
        leftActive = true;
        jumpActive = true;
        attackActive = true;
    }

    public boolean isAttackActive() {
        return attackActive;
    }

    public void setAttackActive(boolean attackActive) {
        this.attackActive = attackActive;
    }

    public int getJump() {
        return jump;
    }

    public void setJump(int jump) {
        this.jump = jump;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public boolean isRightActive() {
        return rightActive;
    }

    public void setRightActive(boolean rightActive) {
        this.rightActive = rightActive;
    }

    public boolean isLeftActive() {
        return leftActive;
    }

    public void setLeftActive(boolean leftActive) {
        this.leftActive = leftActive;
    }

    public boolean isJumpActive() {
        return jumpActive;
    }

    public void setJumpActive(boolean jumpActive) {
        this.jumpActive = jumpActive;
    }
    public void setAllInactive(){
        this.rightActive = false;
        this.leftActive = false;
        this.jumpActive = false;
        this.attackActive = false;
    }

    public boolean isRightPressed(){
        if (!rightActive)
            return false;
        if (isController){
            return controller.getButton(dpadRight);
        } else {
            return Gdx.input.isKeyPressed(keyboardRight);
        }
    }

    public boolean isLeftPressed(){
        if (!leftActive)
            return false;
        if (isController){
            return controller.getButton(dpadLeft);
        } else {
            return Gdx.input.isKeyPressed(keyboardLeft);
        }
    }

    public boolean isJumpPressed(){
        if (!jumpActive)
            return false;
        if (isController){
            return controller.getButton(jump);
        } else {
            return Gdx.input.isKeyPressed(jump);
        }
    }

    public boolean isAttackPressed() {
        if (!attackActive)
            return false;
        if (isController) {
            return controller.getButton(attack);
        } else {
            return Gdx.input.isKeyPressed(attack);
        }
    }
}