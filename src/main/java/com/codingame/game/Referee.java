package com.codingame.game;

import com.codingame.gameengine.core.AbstractPlayer;
import com.codingame.gameengine.core.SoloGameManager;
import com.codingame.gameengine.module.endscreen.EndScreenModule;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.core.AbstractReferee;
import com.codingame.gameengine.core.MultiplayerGameManager;
import com.codingame.gameengine.module.tooltip.TooltipModule;
import com.google.inject.Inject;

import javax.swing.text.View;
import java.util.Arrays;

public class Referee extends AbstractReferee implements IReferee {
    @Inject private SoloGameManager<Player> gameManager;
    @Inject private GraphicEntityModule graphicEntityModule;
    @Inject private EndScreenModule endScreenModule;
    @Inject private TooltipModule tooltipModule;
    private Game game;
    private ViewController viewController;
    private double score;

    @Override
    public void init() {
        String[] input = gameManager.getTestCaseInput().get(0).split(":");
        long seed = Integer.parseInt(input[0]);
        int food = Integer.parseInt(input[1]);
        gameManager.setFrameDuration(500);
        gameManager.setMaxTurns(601);
        gameManager.setTurnMaxTime(50);
        game = new Game(this, seed, food);
        viewController = new ViewController(game, graphicEntityModule, gameManager, tooltipModule);
    }

    @Override
    public void onEnd() {
        gameManager.putMetadata("Points", String.valueOf(score));
        gameManager.winGame("score: " + score);

        endScreenModule.setScores(new int[]{(int)score}, new String[]{ String.valueOf(score) });
        endScreenModule.setTitleRankingsSprite("logo.png");
    }


    @Override
    public void gameTurn(int turn) {
        game.onTurn();
        viewController.onTurn();
    }

    @Override
    public String sendInput(String[] inputs)  throws AbstractPlayer.TimeoutException{
        Player p = gameManager.getPlayer();
        for(String s : inputs){
            p.sendInputLine(s);
        }

        p.execute();
        return p.getOutputs().get(0);
    }

    @Override
    public void updateScore(double score) {
        this.score = score;
    }

    @Override
    public void addGameSummary(String message) {
        gameManager.addToGameSummary(message);
    }

    @Override
    public void addTooltip(String message) {
        gameManager.addTooltip(gameManager.getPlayer(), message);
    }

    @Override
    public void endGame() {
        gameManager.winGame("score: " + score);
    }
}
