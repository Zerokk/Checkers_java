/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import Enums.StrategyType;

/**
 *
 * @author zerok
 */
public class AttackStrategy extends Strategy {

    public Token targetTokenRef;

    public AttackStrategy(StrategyType action, Token sourceTokenRef, Token targetTokenRef) {
        super(action, sourceTokenRef);
        this.targetTokenRef = targetTokenRef;
    }

    @Override
    public boolean checkValidity() {
        String coordinates = sourceTokenRef.getAttackCoordinates(targetTokenRef);
        if (coordinates != null) {
            String[] parts = coordinates.split(",");
            targetX = Integer.valueOf(parts[0]);
            targetY = Integer.valueOf(parts[1]);

            Board gameboard = Board.boardInstance;
            if (sourceTokenRef.getPlayer() == gameboard.player1) {
                if (sourceTokenRef.getY() > targetY || sourceTokenRef.isKing()) {
                    return super.checkValidity();
                } else {
                    return false;
                }
            } else {
                if (sourceTokenRef.getY() < targetY || sourceTokenRef.isKing()) {
                    return super.checkValidity();
                } else {
                    return false;
                }
            }

        } else {
            return false;
        }

    }

}
