package com.codingame.game;

import com.codingame.gameengine.core.AbstractPlayer;

public interface IReferee {
    String sendInput(String[] inputs)  throws AbstractPlayer.TimeoutException;
    void updateScore(double score);
    void addGameSummary(String message);
    void addTooltip(String message);
    void endGame();
}
