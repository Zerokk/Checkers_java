/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.util.ArrayList;

/**
 *
 * @author zerok
 */
public class Token {

    private String tokenName;
    private Player player;
    private int x;
    private int y;
    private boolean isKing;

    Token(Player player, String tokenName, int startingX, int startingY) {
        x = startingX;
        y = startingY;
        isKing = false;
        this.player = player;
        this.tokenName = tokenName;
    }

    /**
     * Find whether this token can attack a given target token
     *
     * @param sourceToken the source token to attack with
     * @param enemyToken target token to check
     * @return
     */
    public String getAttackCoordinates(Token targetToken) {
        if (x == targetToken.getX() || y == targetToken.getY()) {
            // If X or Y are equal, then it's not a diagonal move.
            return null;
        }

        // This variable refers to in which quadrant, from the source token perspective, the target token is located
        int jumpX;
        int jumpY;
        if (x > targetToken.getX()) {
            if (y > targetToken.getY()) {
                jumpX = x - 2;
                jumpY = y - 2;
            } else {
                jumpX = x - 2;
                jumpY = y + 2;
            }
        } else {
            if (y > targetToken.getY()) {
                jumpX = x + 2;
                jumpY = y - 2;
            } else {
                jumpX = x + 2;
                jumpY = y + 2;
            }
        }

        return jumpX+","+jumpY;
    }

    /**
     * @return the isKing
     */
    public boolean isKing() {
        return isKing;
    }

    /**
     * @param isKing the isKing to set
     */
    public void setIsKing(boolean isKing) {
        this.isKing = isKing;
    }

    /**
     * @return the x
     */
    public int getX() {
        return x;
    }

    /**
     * @param x the x to set
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * @return the y
     */
    public int getY() {
        return y;
    }

    /**
     * @param y the y to set
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * @return the tokenName
     */
    public String getTokenName() {
        return tokenName;
    }
}
