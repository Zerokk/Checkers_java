/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import Entities.Board;
import Entities.Token;

/**
 *
 * @author zerok
 */
public class Renderer {

    Board board;
    StringBuilder sb = new StringBuilder();
    
    Renderer(Board board) {
        this.board = board;
    }

    public void render() {
        // Render from the top
        System.out.println("     0  1  2  3  4  5  6  7   ");
        System.out.println("———————————————————");
        
        for (int y = 7; y >= 0; y--) {
            sb.delete(0, sb.length());
            sb.append(y).append(" | ");
            for (int x = 0; x < 8; x++) {
                Token t = board.getMatrix()[y][x];
                sb.append(renderTile(t));
            }
            sb.append(" | ");
            System.out.println(sb.toString());
        }
        System.out.println("———————————————————");

    }
    
    
    private String renderTile(Token t){
        String tokenStr;
        
        if(t != null){
            if(board.player1 == t.getPlayer()){
                tokenStr = t.isKing() ? "#O#": " O ";
            }else{
                tokenStr = t.isKing() ? "#X#": " X ";
            }
        }else{
            tokenStr = " · ";
        }
        return tokenStr;
    }
    
}
