/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import Entities.Board;
import Entities.HumanPlayer;
import Entities.MachinePlayer;
import Entities.Player;
import Entities.Token;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author zerok
 */
public class MainClass {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Welcome to Checkers!\n\n");
            System.out.println(">> Enter first player's nickname: ");
            String nick1 = br.readLine();
            System.out.println(">> Enter second player's nickname, or leave blank for creating a machine controlled player.");
            String nick2 = br.readLine();

            // Create players
            Player p1;
            Player p2;

            if (!nick1.equals("")) {
                // If nick isn't null, create normal player
                p1 = new HumanPlayer("[P1] " + nick2);
            } else {
                // If nick is null, create machine-controlled player
                System.out.println(">> As a nickname hasn't been provided, an AI player is spawned");
                p1 = new MachinePlayer("[AI] ");
            }

            if (!nick2.equals("")) {
                // If nick isn't null, create normal player
                p2 = new HumanPlayer("[P2] " + nick2);
            } else {
                // If nick is null, create machine-controlled player
                System.out.println(">> As a nickname hasn't been provided, an AI player is spawned");
                p2 = new MachinePlayer("[AI] ");
            }

            // Create the board
            Board gameboard = Board.getBoardInstance(p1, p2);
            Renderer eng = new Renderer(gameboard);
            System.out.println("\n-#####  RULES  #####  RULES  #####  RULES  #####  RULES  #####");
            System.out.println("- Write the coordinates as 'x,y', separating by a comma the x and y values.");
            System.out.println("- Player 1 plays at top, with tokens labelled as 'O'.");
            System.out.println("- Player 2 plays at bottom, with tokens labelled as 'X'.\n\n");
            // Create the game loop
            String response = "";
            Player currentPlayer = p1;
            int exit = 0;

            while (exit == 0) {
                // Render the board
                eng.render();

                // Try to make the play for the given player
                System.out.println("\n>> TURN FOR " + currentPlayer.nickname);
                String result = currentPlayer.makePlay(gameboard);
                if (result != null) {
                    String[] resultParts = result.split(":");
                    if (resultParts[0].equals("true")) {
                        System.out.println(resultParts[1]);
                        // Don't change current player if the latest one ate a token
                        if (!gameboard.playerRepeatsMove) {
                            currentPlayer = currentPlayer == p1 ? p2 : p1;
                        }
                        // Check whether a player has won the game or not
                        if (currentPlayer.points == 12) {
                            System.out.println("\n\n " + currentPlayer.nickname + " HAS WON THE GAME.\n\n Exitting from the game...");
                            exit = 1;
                        }
                    } else {
                        System.out.println(resultParts[1] + "\nRestarting turn...");
                    }
                }

            }
        } catch (IOException ex) {
            Logger.getLogger(MainClass.class.getName()).log(Level.SEVERE, null, ex);

        }
    }
}
