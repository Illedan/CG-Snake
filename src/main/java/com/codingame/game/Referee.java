package com.codingame.game;

import com.codingame.gameengine.core.AbstractPlayer;
import com.codingame.gameengine.module.endscreen.EndScreenModule;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.core.AbstractReferee;
import com.codingame.gameengine.core.MultiplayerGameManager;
import com.codingame.gameengine.module.tooltip.TooltipModule;
import com.google.inject.Inject;

import javax.swing.text.View;
import java.util.Arrays;

public class Referee extends AbstractReferee implements IReferee {
    @Inject private MultiplayerGameManager<Player> gameManager;
    @Inject private GraphicEntityModule graphicEntityModule;
    @Inject private EndScreenModule endScreenModule;
    @Inject private TooltipModule tooltipModule;
    private Game game;
    private ViewController viewController;

    @Override
    public void init() {
        long seed = gameManager.getSeed();
        gameManager.setFrameDuration(500);
        gameManager.setMaxTurns(601);
        gameManager.setTurnMaxTime(50);
        game = new Game(gameManager.getPlayerCount(), this, seed);
        viewController = new ViewController(game, graphicEntityModule, gameManager, tooltipModule);
    }

    @Override
    public void onEnd() {
        int[] scores = gameManager.getPlayers().stream().mapToInt(p -> p.getScore()).toArray();
        String[] texts = Arrays.asList(gameManager.getPlayers().stream().map(p -> p.getScore()+"").toArray()).toArray(new String[game.snakes.size()]);;
        endScreenModule.setScores(scores, texts);
        endScreenModule.setTitleRankingsSprite("logo.png");
    }


    @Override
    public void gameTurn(int turn) {
        game.onTurn();
        viewController.onTurn();
    }

    @Override
    public String sendInput(int player, String[] inputs)  throws AbstractPlayer.TimeoutException{
        Player p = gameManager.getPlayer(player);
        for(String s : inputs){
            p.sendInputLine(s);
        }

        p.execute();
        return p.getOutputs().get(0);
    }

    @Override
    public void disablePlayer(int player) {
        gameManager.getPlayer(player).deactivate();
    }

    @Override
    public void updateScore(int player, int score) {
        gameManager.getPlayer(player).setScore(score);
    }

    @Override
    public void addGameSummary(String message) {
        gameManager.addToGameSummary(message);
    }

    @Override
    public void addTooltip(int player, String message) {
        gameManager.addTooltip(gameManager.getPlayer(player), message);
    }

    @Override
    public void endGame() {
        gameManager.endGame();
    }
}
