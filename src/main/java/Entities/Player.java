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
public abstract class Player {

    public String nickname;
    public int points = 0;
    

    public Player(String nickname) {
        this.nickname = nickname;
    }

    
    /**
     * This method holds the logic for making up the player's decission and validating it against the
     * game board. The board will return whether the decission could be made or not, which later will
     * be checked by the MainClass to perform further actions.
     * @param gameboard the board instance to perform the actions on.
     * @return a response string with data about the validity of the play.
     */
    public abstract String makePlay(Board gameboard);
}