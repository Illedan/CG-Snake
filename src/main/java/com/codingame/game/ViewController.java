package com.codingame.game;

import com.codingame.gameengine.core.MultiplayerGameManager;
import com.codingame.gameengine.module.entities.*;
import com.codingame.gameengine.module.entities.Rectangle;

import java.awt.*;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;

public class ViewController {
    private Game game;
    private GraphicEntityModule module;
    private Group boardGroup;
    private double dx;
    private double dy;
    private MultiplayerGameManager<Player> gameManager;

    private ArrayList<IViewPart> parts = new ArrayList<>();
    public ViewController(Game game, GraphicEntityModule module, MultiplayerGameManager<Player> gameManager) {
        this.game = game;
        this.module = module;
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
        }
        parts.add(new FoodViewPart(game));
    }

    private int getPos(int pos){
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
                foodMap.get(food).setScale(0, Curve.ELASTIC);
                foodMap.remove(food);
            }

            for(Point food : game.food){
                if(!foodMap.containsKey(food)){
                    Circle foodView = module.createCircle()
                            .setFillColor(0x87821a)
                            .setLineColor(0x000000)
                            .setRadius((int)(dx/2.5))
                            .setX((int)(getPos(food.x)+dx*0.5))
                            .setY((int)(getPos(food.y)+dy*0.5))
                            .setScale(0);
                    boardGroup.add(foodView);
                    module.commitEntityState(0.0, foodView, boardGroup);
                    foodView.setScale(1.0, Curve.ELASTIC);
                    foodMap.put(food, foodView);
                }
            }

            return true;
        }
    }

    class SnakeViewPart implements IViewPart {
        private Snake model;
        private ArrayList<Rectangle> parts = new ArrayList<>();
        public SnakeViewPart(Snake model){
            this.model = model;
            onTurn();
        }
        @Override
        public boolean onTurn() {
            if(parts.size() != model.snake.size()){
                Point point = model.snake.get(0).point;
                Rectangle rect = module.createRectangle()
                        .setWidth((int)dx)
                        .setHeight((int)dy)
                        .setFillColor(gameManager.getPlayer(model.id).getColorToken())
                        .setLineColor(0xababab)
                        .setLineWidth(1)
                        .setX((int)(getPos(point.x)))
                        .setY((int)(getPos(point.y)))
                        .setZIndex(1);
                boardGroup.add(rect);
                module.commitEntityState(0.0, rect);
                parts.add(0, rect);
            }

            for(int i = 0; i < model.snake.size(); i++){
                parts.get(i).setX(getPos(model.snake.get(i).point.x))
                        .setY(getPos(model.snake.get(i).point.y));

                if(model.isDead){
                    parts.get(i).setScale(0.0, Curve.ELASTIC);
                }
            }

            return !model.isDead;
        }
    }
}
