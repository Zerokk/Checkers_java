/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author zerok
 */
public class HumanPlayer extends Player {

    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public HumanPlayer(String nickname) {
        super(nickname);
    }

    @Override
    public String makePlay(Board gameboard) {
        String result = null;
        try {
            String response;
            System.out.println(">> Which token do you want to select? ");
            response = br.readLine();
            String[] coords = response.split(",");
            Token t = gameboard.selectToken(this, Integer.valueOf(coords[0]), Integer.valueOf(coords[1]));
            if (t != null) {

                System.out.println(">> Selected token '" + t.getTokenName() + "'. Where do you want to move it?");
                response = br.readLine();
                coords = response.split(",");
                result = gameboard.tryMove(this, t, Integer.valueOf(coords[0]), Integer.valueOf(coords[1]));

            } else {
                System.out.println("There are no tokens of yours in this tile. Restarting turn...");
            }
        } catch (IOException ex) {
            Logger.getLogger(HumanPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return result;
    }

}
