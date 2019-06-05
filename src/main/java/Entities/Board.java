/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

/**
 * Singleton-style class that holds all the data of the game's board. Provide
 * its constructor with the board's side length.
 *
 * @author Pablo V.
 */
public class Board {

    static Board boardInstance;
    public Player player1; // player at the top
    public Player player2; // player at the bottom
    private Token[][] matrix;
    public boolean playerRepeatsMove = false;

    private Board(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    public static Board getBoardInstance(Player player1, Player player2) {

        if (boardInstance == null) {
            boardInstance = new Board(player1, player2);
            boardInstance.matrix = initializeMatrix(boardInstance, player1, player2);
        }

        return boardInstance;
    }

    public Token selectToken(Player player, int tokenX, int tokenY) {
        Token t = matrix[tokenY][tokenX];
        if (t != null && t.getPlayer().nickname.equals(player.nickname)) {
            return t;
        } else {
            return null;
        }

    }

    /**
     * Tries to move a token to the given coordinates.
     *
     * @param player player who tries to perform the move
     * @param token the token to move
     * @param newX X coordinate to move the token
     * @param newY Y coordinate to move the token
     * @return a string containing info about whether the token was moved or not
     */
    public String tryMove(Player player, Token token, int newX, int newY) {

        String result;
        playerRepeatsMove = false;
        int tokenX = token.getX();
        int tokenY = token.getY();

        // Check whether X and Y have changed from previous pos or not. If some of they didn't, they're moving horizontally or vertically, which is forbidden
        if (tokenX == newX || tokenY == newY) {
            return "false:You can't move vertically or horizontally!";
        }

        // If the token is not king, check if it did a backwards movement
        if (!token.isKing()) {
            boolean permitted = checkBackwardsMovements(player, token, newY);
            if (!permitted) {
                return "false:You can't move backwards with a non-king token!";
            }
        }

        int sumDistance = Math.abs(tokenX - newX) + Math.abs(tokenY - newY);

        // For this distance, a single movement was tried. Check if there is a token there, before.
        if (sumDistance == 2) {

            if (getMatrix()[newY][newX] == null) {
                getMatrix()[tokenY][tokenX] = null;
                getMatrix()[newY][newX] = token;
                token.setX(newX);
                token.setY(newY);
                result = "true:The token " + token.getTokenName() + " was moved.";
            } else {
                result = "false:There is another token at those coordinates.";
            }
        } else if (sumDistance == 4) {
            // Avoid doing the jump to a non-empty tile
            if (getMatrix()[newY][newX] != null) {
                return "false:There is another token at those coordinates.";

            }
            // For this distance, the user is trying to "eat" an enemy token.
            int relativeX = newX < tokenX ? tokenX - 1 : tokenX + 1;
            int relativeY = newY < tokenY ? tokenY - 1 : tokenY + 1;
            Token enemyToken = getMatrix()[relativeY][relativeX];
            if (enemyToken == null) {
                result = "false:You can't jump 2 tiles without eating an enemy token.";
            } else {
                // If there was an enemy token in that position, then remove the piece and perform the jump
                getMatrix()[tokenY][tokenX] = null;
                getMatrix()[relativeY][relativeX] = null;
                getMatrix()[newY][newX] = token;
                token.setX(newX);
                token.setY(newY);
                player.points++;
                playerRepeatsMove = true;
                result = "true:You ate the token " + enemyToken.getTokenName() + " and moved to the desired position. You can move again.";
            }
        } else {
            // If the sumDistance is other than 2 or 4, then it's a falsy movement
            result = "false:Wrong coordinates for the given token.";
        }

        // Check if the token reached the end and must become a king token
        if (player == player1) {
            // player at top, therefore token must reach Y=0
            if (token.getY() == 0) {
                System.out.println("The token " + token.getTokenName() + " has become a King Token! It can now move backwards.");
                token.setIsKing(true);
            }
        } else {
            if (token.getY() == 7) {
                System.out.println("The token " + token.getTokenName() + " has become a King Token! It can now move backwards.");
                token.setIsKing(true);
            }

        }

        return result;
    }

    private boolean checkBackwardsMovements(Player player, Token token, int newY) {
        if (player == player1) {
            if (token.getY() < newY) {
                return false;
            } else {
                return true;
            }
        } else {
            if (token.getY() > newY) {
                return false;
            } else {
                return true;
            }
        }
    }

    private static Token[][] initializeMatrix(Board boardInstance, Player p1, Player p2) {

        Token[][] matrix = new Token[8][8];
        int pAddedTokens = 0;
        // Bottom player (P2)
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 8; x++) {
                String tokenName = "[P2] T" + (++pAddedTokens);
                if (y % 2 == 0) {
                    matrix[y][x] = x % 2 == 0 ? new Token(p2, tokenName, x, y) : null;
                } else {
                    matrix[y][x] = x % 2 != 0 ? new Token(p2, tokenName, x, y) : null;
                }
            }
        }

        pAddedTokens = 0;
        // Top player (P1)
        for (int y = 7; y > 4; y--) {
            for (int x = 0; x < 8; x++) {
                String tokenName = "[P1] T" + (++pAddedTokens);
                if (y % 2 == 0) {
                    matrix[y][x] = x % 2 == 0 ? new Token(p1, tokenName, x, y) : null;
                } else {
                    matrix[y][x] = x % 2 != 0 ? new Token(p1, tokenName, x, y) : null;
                }

            }
        }
        return matrix;

    }

    /**
     * @return the matrix
     */
    public Token[][] getMatrix() {
        return matrix;
    }

}
