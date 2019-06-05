/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import Enums.StrategyType;
import java.util.ArrayList;

/**
 *
 * @author zerok
 */
public class Strategy {

    public StrategyType action;
    protected int targetX;
    protected int targetY;
    private int points;
    public Token sourceTokenRef;

    Strategy(StrategyType action, Token sourceTokenRef) {
        this.action = action;
        this.sourceTokenRef = sourceTokenRef;
    }

    Strategy(StrategyType action, int targetX, int targetY, Token sourceTokenRef) {
        this.action = action;
        this.targetX = targetX;
        this.targetY = targetY;
        this.sourceTokenRef = sourceTokenRef;

    }

    /**
     * This method checks wheter the action is valid or not for the active board
     *
     * @return
     */
    protected boolean checkValidity() {
        boolean valid = false;
        try {
            Board gameboard = Board.boardInstance;
            Token tokenInTarget = gameboard.getMatrix()[targetY][targetX];
            if (tokenInTarget == null) {
                valid = true;
            }
        } catch (Exception err) {
            // should be triggered when ArrayIndexOutOfBoundsException is thrown
            valid = false;
        }
        return valid;
    }

    public int getStrategyValidity() {
        // The strategy validity is calculated here
        if (checkValidity()) {
            switch (action) {
                case MOVE:
                    points = 0;
                    break;
                case ESCAPE:
                    points = 1;
                    break;
                case ATTACK:
                    points = 2;
                    break;
                case KING:
                    points = 3;
                    break;
            }
        } else {
            points = -1;
        }
        return points;
    }
}
