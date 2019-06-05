/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import Enums.StrategyType;
import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 *
 * @author zerok
 */
public class MachinePlayer extends Player {

    public MachinePlayer(String nickname) {
        super(nickname);
    }

    @Override
    public String makePlay(Board gameboard) {
        try {
            // Simulate a "time to choose" of the AI
            sleep(1500);
        } catch (InterruptedException ex) {
            Logger.getLogger(MachinePlayer.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Get AI's tokens
        ArrayList<Token> myTokens = findMyTokens(gameboard.getMatrix());
        // Look for strategies
        ArrayList<Strategy> strategies = new ArrayList();
        getStrategies(myTokens, gameboard, strategies);
        // Find the highly scored strategies and randomly choose between them
        ArrayList<Strategy> bestStrategies = getBestStrategies(strategies);

        // Pick up the best strategy, or randomly pick between the best choices
        Strategy s;
        if (bestStrategies.size() == 1) {
            s = bestStrategies.get(0);
        } else {
            // If there are many top strategies, choose one at random
            Random r = new Random();
            int randInt = r.nextInt(bestStrategies.size());
            s = bestStrategies.get(randInt);
        }
        // Make a final choose and perform a valid strategy
        return performMove(s, gameboard, strategies);
    }

    /**
     * This method will have a 70% chance of performing the best strategy. But
     * for making the AI a little less easy to guess, it will in the other 30%
     * of times, a strategy chose at random. If the given strategy return a
     * faily message from the gameboard, the function calls itself recursively
     * until it returns a valid one
     *
     * @param s
     * @param gameboard
     * @return
     */
    private String performMove(Strategy s, Board gameboard, ArrayList<Strategy> strategies) {
        Random r = new Random();
        int rand = r.nextInt(10);
        if (rand <= 7) {
            String result = gameboard.tryMove(this, s.sourceTokenRef, s.targetX, s.targetY);
            if (result != null && result.split(":")[0].equals("true")) {
                return result;
            }
        }

        // If calling the best strategy fails or rand is superior to 7, choose a random strategy
        rand = r.nextInt(strategies.size());
        Strategy randomStrategy = strategies.get(rand);
        boolean validStrategy = false;
        do {
            validStrategy = randomStrategy.getStrategyValidity() != -1 ? true : false;
            if(!validStrategy){
                rand = r.nextInt(strategies.size());
                randomStrategy = strategies.get(rand);
            }
        }while (validStrategy == false);
            
            String result = gameboard.tryMove(this, randomStrategy.sourceTokenRef, randomStrategy.targetX, randomStrategy.targetY);

            if (result != null && result.split(":")[0].equals("true")) {
                return result;
            } else {
                return performMove(s, gameboard, strategies);
            }

        }

    

    

    private void getStrategies(ArrayList<Token> myTokens, Board gameboard, ArrayList<Strategy> strategies) {
        for (Token t : myTokens) {
            canKing(t, gameboard, strategies);
            canAttack(t, gameboard, strategies);
            canMove(t, gameboard, strategies);
        }
    }

    private ArrayList<Strategy> getBestStrategies(ArrayList<Strategy> strategies) {
        ArrayList<Strategy> bestStrategies = new ArrayList();
        int maxPoints = 0;
        for (Strategy s : strategies) {
            int points = s.getStrategyValidity();
            if (points > maxPoints) {
                // If this strategy has more points, remove previously added strategies and add this one
                maxPoints = points;
                bestStrategies.clear();
                bestStrategies.add(s);
            } else if (points == maxPoints) {
                // If has the maximum number of points, add to the list
                bestStrategies.add(s);
            }
        }
        return bestStrategies;

    }

    private void canKing(Token t, Board gameboard, ArrayList<Strategy> strategies) {
        // Kinging for player 1, from top to bottom
        if (t.getY() == 1 && t.getPlayer() == gameboard.player1) {
            Token foundToken = null;
            try {
                foundToken = gameboard.getMatrix()[t.getY() + 1][t.getX() - 1];
                if (foundToken == null) {
                    // If there isn't any token, add strategy
                    strategies.add(new Strategy(StrategyType.KING, t.getX() - 1, t.getY() + 1, t));
                } else {
                    // Otherwise, look at the other tile
                    foundToken = gameboard.getMatrix()[t.getY() + 1][t.getX() + 1];
                    if (foundToken == null) {
                        strategies.add(new Strategy(StrategyType.KING, t.getX() + 1, t.getY() + 1, t));
                    }

                }
            } catch (Exception e) {
                // just catch silent array access exception
            }
        }
        // Kinging for player 2, from bottom to top
        if (t.getY() == 6 && t.getPlayer() == gameboard.player2) {
            Token foundToken = null;
            try {
                foundToken = gameboard.getMatrix()[t.getY() - 1][t.getX() - 1];
                if (foundToken == null) {
                    // If there isn't any token, add strategy
                    strategies.add(new Strategy(StrategyType.KING, t.getX() - 1, t.getY() - 1, t));
                } else {
                    // Otherwise, look at the other tile
                    foundToken = gameboard.getMatrix()[t.getY() - 1][t.getX() + 1];
                    if (foundToken == null) {
                        strategies.add(new Strategy(StrategyType.KING, t.getX() + 1, t.getY() - 1, t));
                    }

                }
            } catch (Exception e) {
                // just catch silent array access exception
            }
        }
    }

    private void canMove(Token t, Board gameboard, ArrayList<Strategy> strategies) {
        // If this                 
        boolean leftTop = safeMoveCheck(gameboard, t.getX() - 1, t.getY() + 1);
        boolean rightTop = safeMoveCheck(gameboard, t.getX() + 1, t.getY() + 1);
        boolean leftBot = safeMoveCheck(gameboard, t.getX() - 1, t.getY() - 1);
        boolean rightBot = safeMoveCheck(gameboard, t.getX() + 1, t.getY() - 1);

        // If the tile is null, it means it is empty.
        // Also, check if this token for this player can make the move
        if (leftTop && (this != gameboard.player1 || t.isKing())) {
            strategies.add(
                    new Strategy(StrategyType.MOVE, t.getX() - 1, t.getY() + 1, t)
            );
        }
        if (rightTop && (this != gameboard.player1 || t.isKing())) {
            strategies.add(
                    new Strategy(StrategyType.MOVE, t.getX() + 1, t.getY() + 1, t)
            );
        }
        if (leftBot && (this != gameboard.player2 || t.isKing())) {
            strategies.add(
                    new Strategy(StrategyType.MOVE, t.getX() - 1, t.getY() - 1, t)
            );
        }
        if (rightBot && (this != gameboard.player2 || t.isKing())) {
            strategies.add(
                    new Strategy(StrategyType.MOVE, t.getX() + 1, t.getY() - 1, t)
            );
        }

    }

    private boolean safeMoveCheck(Board gameboard, int x, int y) {
        try {
            return gameboard.getMatrix()[y][x] == null ? true : false;
        } catch (Exception e) {
            return false;
        }
    }

    private void canAttack(Token t, Board gameboard, ArrayList<Strategy> strategies) {
        Token[][] matrix = gameboard.getMatrix();
        Token enemyToken = null;

        for (int y = t.getY() - 1; y <= t.getY() + 1; y++) {
            for (int x = t.getX() - 1; x <= t.getX() + 1; x++) {
                if (y >= 0 && x >= 0 && y < matrix.length && x < matrix[0].length) {
                    Token foundToken = matrix[y][x];
                    if (foundToken != null && foundToken.getPlayer() != this) {
                        // one token per token, so break if found
                        enemyToken = foundToken;
                        break;
                    }
                }
            }
            if (enemyToken != null) {
                // add and exit from this loop
                strategies.add(
                        new AttackStrategy(StrategyType.ATTACK, t, enemyToken)
                );
                break;
            }
        }

    }

    private ArrayList<Token> findMyTokens(Token[][] matrix) {
        ArrayList<Token> tokens = new ArrayList();
        for (int y = 0; y < matrix.length; y++) {
            for (int x = 0; x < matrix[y].length; x++) {
                Token t = matrix[y][x];
                if (t != null && t.getPlayer() == this) {
                    tokens.add(t);
                }
            }
        }
        return tokens;
    }

}
