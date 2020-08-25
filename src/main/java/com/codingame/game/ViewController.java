package com.codingame.game;

import com.codingame.gameengine.core.MultiplayerGameManager;
import com.codingame.gameengine.module.entities.*;
import com.codingame.gameengine.module.entities.Rectangle;
import com.codingame.gameengine.module.tooltip.TooltipModule;

import java.awt.*;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;

public class ViewController {
    private Game game;
    private GraphicEntityModule module;
    private TooltipModule tooltipModule;
    private Group boardGroup;
    private double dx;
    private double dy;
    private MultiplayerGameManager<Player> gameManager;

    private ArrayList<IViewPart> parts = new ArrayList<>();
    public ViewController(Game game, GraphicEntityModule module, MultiplayerGameManager<Player> gameManager, TooltipModule tooltipModule) {
        this.game = game;
        this.module = module;
        this.tooltipModule = tooltipModule;
        int height = module.getWorld().getHeight()-50;
        int width = module.getWorld().getWidth()/5*4-50;

        dx = (double)width/ Constants.WIDTH;
        dy = (double)height / Constants.HEIGHT;
        this.gameManager = gameManager;
        double min = Math.min(dx, dy); // Squares
        dx = min;
        dy = min;
        double diffY = module.getWorld().getHeight() -dy * Constants.HEIGHT;
        double diffX = module.getWorld().getWidth()-dx*Constants.WIDTH;

        module.createRectangle().setWidth(module.getWorld().getWidth())
                .setHeight(module.getWorld().getHeight())
                .setZIndex(-10)
                .setFillColor(0x343434);
        boardGroup = module.createGroup()
                .setX(module.getWorld().getWidth()/5)
                .setY((int)(diffY/2));

        // grid
        for(int i = 0; i < Constants.HEIGHT + 1; i++){
            boardGroup.add(module.createLine().setLineWidth(2)
                    .setZIndex(-5)
                    .setLineColor(0xababab)
                    .setX(0).setX2(getPos(Constants.WIDTH))
                    .setY(getPos(i)).setY2(getPos(i)));
            for(int x = 0; x < Constants.WIDTH; x++){
                if(i >= Constants.HEIGHT) break;
                Rectangle cell = module.createRectangle()
                        .setWidth((int)dx)
                        .setHeight((int)dy)
                        .setX(getPos(x))
                        .setY(getPos(i))
                        .setAlpha(0);
                tooltipModule.setTooltipText(cell, "x: " + x + "\ny: " + i);
                boardGroup.add(cell);
            }
        }

        for(int i = 0; i < Constants.WIDTH + 1; i++){
            boardGroup.add(module.createLine().setLineWidth(2)
                    .setZIndex(-5)
                    .setLineColor(0xababab)
                    .setY(0).setY2(getPos(Constants.HEIGHT))
                    .setX(getPos(i)).setX2(getPos(i)));
        }

        for(Snake snake : game.snakes){
            parts.add(new SnakeViewPart(snake));
            parts.add(new PlayerViewPart(snake));
        }
        parts.add(new FoodViewPart(game));
    }

    private int getPos(double pos){
        return (int)(dx*pos);
    }


    public void onTurn() {
        // View part
        ArrayList<IViewPart> toRemove = new ArrayList<>();
        for(IViewPart part : parts) {
            if (!part.onTurn()) {
                toRemove.add(part);
            }
        }

        for(IViewPart part : toRemove){
            parts.remove(part);
        }
    }

    interface IViewPart{
        // returns true if this item is to be persisted.
        boolean onTurn();
    }
    private static int playerY;

    class PlayerViewPart implements IViewPart{
        private Snake player;
        private Rectangle playerFrame;
        private Text scoreText;
        private Text playerNameText;
        private Group group;

        public PlayerViewPart(Snake player){
            this.player = player;
            group = module.createGroup()
                    .setX(25)
                    .setY(playerY + 50);
            playerY += module.getWorld().getHeight()/4;
            playerFrame = module.createRectangle()
                    .setWidth(module.getWorld().getWidth()/5-50)
                    .setHeight(module.getWorld().getHeight()/4-100)
                    .setAlpha(0.0)
                    .setLineWidth(5)
                    .setLineColor(gameManager.getPlayer(player.id).getColorToken())
                    .setFillColor(gameManager.getPlayer(player.id).getColorToken());

            group.add(playerFrame);
            group.add(module.createRectangle()
                    .setWidth(module.getWorld().getWidth()/5-50)
                    .setHeight(module.getWorld().getHeight()/4-100)
                    .setFillAlpha(0.0)
                    .setLineWidth(5)
                    .setLineColor(gameManager.getPlayer(player.id).getColorToken()));

            group.add(playerNameText = module.createText().setText(gameManager.getPlayer(player.id).getNicknameToken())
                    .setY(15)
                    .setStrokeColor(0xababab)
                    .setFillColor(0xffffff)
                    .setX((module.getWorld().getWidth()/5-50)/2)
                    .setAnchorX(0.5)
                    .setAnchorY(0));

            scoreText = module.createText().setText("0")
                    .setX((module.getWorld().getWidth()/5-50)-50)
                    .setY((module.getWorld().getHeight()/4-50)/2)
                    .setStrokeColor(0xababab)
                    .setFillColor(0xffffff)
                    .setAnchorY(0.5)
                    .setFontSize(50)
                    .setAnchorX(1);
            group.add(scoreText);

            group.add(module.createSprite().setImage(gameManager.getPlayer(player.id).getAvatarToken())
                    .setX(25)
                    .setY((module.getWorld().getHeight()/4-50)/2)
                    .setAnchorX(0)
                    .setAnchorY(0.5)
                    .setBaseHeight(100)
                    .setBaseWidth(100));
        }

        @Override
        public boolean onTurn() {
            if(game.currentPlayer == player.id) {
                playerFrame.setAlpha(1.0, Curve.IMMEDIATE);
                playerNameText
                        .setStrokeColor(0x000000, Curve.IMMEDIATE)
                        .setFillColor(0x000000, Curve.IMMEDIATE);
                scoreText
                        .setStrokeColor(0x000000, Curve.IMMEDIATE)
                        .setFillColor(0x000000, Curve.IMMEDIATE);
            }
            else {
                playerFrame.setAlpha(0.0, Curve.IMMEDIATE);
                playerNameText
                        .setStrokeColor(0xababab, Curve.IMMEDIATE)
                        .setFillColor(0xffffff, Curve.IMMEDIATE);
                scoreText
                        .setStrokeColor(0xababab, Curve.IMMEDIATE)
                        .setFillColor(0xffffff, Curve.IMMEDIATE);
            }
            module.commitEntityState(0.0, playerFrame);
            scoreText.setText(player.score+"");

            if(player.isDead){
                Sprite txt = module.createSprite()
                        .setImage("cross.png")
                        .setTint(0xff0000)
                        .setBaseWidth(120)
                        .setBaseHeight(120)
                        .setX(15)
                        .setScale(0.0)
                        .setY((module.getWorld().getHeight()/4-50)/2 - 10)
                        .setAnchorX(0)
                        .setAnchorY(0.5);
                group.add(txt);
                module.commitEntityState(0.0, txt, group);
                txt.setScale(1.0, Curve.EASE_OUT);
                playerFrame.setAlpha(0.0, Curve.NONE);
                playerNameText
                        .setStrokeColor(0xababab, Curve.NONE)
                        .setFillColor(0xffffff, Curve.NONE);
                scoreText
                        .setStrokeColor(0xababab, Curve.NONE)
                        .setFillColor(0xffffff, Curve.NONE);
            }
            return !player.isDead;
        }
    }

    class FoodViewPart implements IViewPart{
        private Game game;
        private HashMap<Point, Circle> foodMap = new HashMap<>();

        public FoodViewPart(Game game){
            this.game = game;
        }

        @Override
        public boolean onTurn() {
            ArrayList<Point> toRemove = new ArrayList<>();
            for(Point food : foodMap.keySet()){
                if(!game.food.contains(food)){
                    toRemove.add(food);
                }
            }
            for(Point food : toRemove){
                foodMap.get(food).setScale(0, Curve.EASE_OUT);
                foodMap.remove(food);
            }

            for(Point food : game.food){
                if(!foodMap.containsKey(food)){
                    Circle foodView = module.createCircle()
                            .setFillColor(0x87821a)
                            .setLineColor(0x000000)
                            .setRadius((int)(dx/2.5))
                            .setX((int)(getPos(food.x+0.5)))
                            .setY((int)(getPos(food.y+0.5)))
                            .setScale(0);
                    boardGroup.add(foodView);
                    module.commitEntityState(0.0, foodView, boardGroup);
                    foodView.setScale(1.0, Curve.EASE_OUT);
                    foodMap.put(food, foodView);
                }
            }

            return true;
        }
    }

    class SnakeViewPart implements IViewPart {
        private Snake model;
        private ArrayList<Circle> parts = new ArrayList<>();
        public SnakeViewPart(Snake model){
            this.model = model;
            onTurn();
        }
        @Override
        public boolean onTurn() {
            if(parts.size() != model.snake.size()){
                Point point = model.snake.get(0).point;
                Circle rect = module.createCircle()
                        .setRadius((int)(dx/2.5))
                        .setFillColor(gameManager.getPlayer(model.id).getColorToken())
                        .setLineColor(0xababab)
                        .setLineWidth(1)
                        .setX((int)(getPos(point.x+0.5)))
                        .setY((int)(getPos(point.y+0.5)))
                        .setZIndex(1);
                boardGroup.add(rect);
                module.commitEntityState(0.0, rect);
                parts.add(0, rect);
                tooltipModule.setTooltipText(rect, "PlayerId: " + model.id);
            }

            for(int i = 0; i < model.snake.size(); i++){
                parts.get(i).setX(getPos(model.snake.get(i).point.x+0.5))
                        .setY(getPos(model.snake.get(i).point.y+0.5));

                if(model.isDead){
                    parts.get(i).setScale(0.0, Curve.EASE_OUT);
                }
            }

            return !model.isDead;
        }
    }
}
