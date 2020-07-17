package com.codingame.game;

import com.codingame.gameengine.core.AbstractPlayer;

public interface IReferee {
    String sendInput(int player, String[] inputs)  throws AbstractPlayer.TimeoutException;
    void disablePlayer(int player);
    void updateScore(int player, int score);
    void addGameSummary(String message);
    void addTooltip(int player, String message);
    void endGame();
}
