package com.codingame.game;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Game {
    private Random rnd;
    private int currentPlayer = -1;
    public ArrayList<Snake> snakes = new ArrayList<>();
    public ArrayList<Point> food = new ArrayList<>();
    private IReferee referee;

    public Game(int players, IReferee referee, long seed){
        this.referee = referee;
        rnd = new Random(seed);
        for(int i = 0; i < players; i++){
            snakes.add(new Snake(i, findRandomEmpty()));
        }

        spawnFood();
    }

    public void onTurn() {
        if(countAlivePlayers() < 2){
            for(Snake snake : snakes){
                referee.updateScore(snake.id, snake.score);
            }

            referee.endGame();
            return;
        }

        currentPlayer = getNextPlayer();
        String[] output = createInput(currentPlayer);
        Snake currentSnake = snakes.get(currentPlayer);
        try {
            String input = referee.sendInput(currentPlayer, output);
            SnakeDirection direction = new SnakeDirection(input.trim());
            Point next = direction.getNext(currentSnake.snake.get(0).point);
            currentSnake.move(direction, hasFood(next));
            if(isCollision(next, currentSnake.snake.get(0))){
                currentSnake.kill();
            }

            if(hasFood(next)){
                food.remove(next);
                spawnFood();
            }
        } catch (Exception e){

        }
    }

    private String[] createInput(int player){
        // send initial
        String[] inputs = new String[50];
        // Initialize
        return inputs;
    }

    private int getNextPlayer(){
        for(int i = currentPlayer + 1; i < snakes.size(); i++){
            if(!snakes.get(i).isDead) return i;
        }

        return 0;
    }

    private boolean isCollision(Point next, SnakePart head){
        if(next.x < 0 || next.y < 0 || next.x >= Constants.WIDTH || next.y >= Constants.HEIGHT) return true;
        return hasSnakePart(next, head);
    }

    private Point findRandomEmpty(){
        Point p = new Point(rnd.nextInt(Constants.WIDTH), rnd.nextInt(Constants.HEIGHT));
        while(hasFood(p) || hasSnakePart(p)){
            p = new Point(rnd.nextInt(Constants.WIDTH), rnd.nextInt(Constants.HEIGHT));
        }

        return p;
    }

    private boolean hasFood(Point point){
        for(Point p : food){
            if(point.equals(p)) return true;
        }
        return false;
    }

    private boolean hasSnakePart(Point point){
        for(Snake snake : snakes){
            if(snake.isDead) continue;
            for(SnakePart part : snake.snake){
                if(part.point.equals(point)) return true;
            }
        }

        return false;
    }

    private void spawnFood(){
        while(food.size() < Constants.MAX_FOOD){
            food.add(findRandomEmpty());
        }
    }

    private boolean hasSnakePart(Point point, SnakePart ignoredPart){
        for(Snake snake : snakes){
            if(snake.isDead) continue;
            for(SnakePart part : snake.snake){
                if(part == ignoredPart) continue;
                if(part.point.equals(point)) return true;
            }
        }

        return false;
    }

    private int countAlivePlayers(){
        int count = 0;
        for(Snake snake : snakes){
            if(!snake.isDead) count++;
        }

        return count;
    }
}
