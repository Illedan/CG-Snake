package com.codingame.game;

import com.codingame.gameengine.core.AbstractPlayer;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Game {
    private Random rnd;
    public int currentPlayer = -1;
    public ArrayList<Snake> snakes = new ArrayList<>();
    public ArrayList<Point> food = new ArrayList<>();
    private IReferee referee;
    private int maxTurns;

    public Game(int players, IReferee referee, long seed){
        maxTurns = 600/players;
        this.referee = referee;
        rnd = new Random(seed);
        for(int i = 0; i < players; i++){
            snakes.add(new Snake(i, findRandomEmpty()));
        }

        spawnFood();
    }

    public void onTurn() {
        for(Snake snake : snakes){
            referee.updateScore(snake.id, snake.score);
        }

        if(countAlivePlayers() < 2){
            referee.endGame();
            return;
        }

        currentPlayer = getNextPlayer();
        Snake currentSnake = snakes.get(currentPlayer);
        if (currentSnake.turns++ >= maxTurns){
            referee.endGame();
            return;
        }

        String[] output = createInput(currentPlayer);
        try {
            String input = referee.sendInput(currentPlayer, output);
            SnakeDirection direction = new SnakeDirection(input.trim());
            Point next = direction.getNext(currentSnake.snake.get(0).point);
            currentSnake.move(direction, hasFood(next));

            if(currentSnake.isDead){
                referee.addTooltip(currentSnake.id, "Died");
                referee.disablePlayer(currentSnake.id);
            }
            else if(isCollision(next, currentSnake.snake.get(0))){
                currentSnake.kill();
                referee.disablePlayer(currentSnake.id);
                referee.addTooltip(currentSnake.id, "Died");
            }
            else if(hasFood(next)){
                food.remove(next);
                spawnFood();
            }
        } catch (AbstractPlayer.TimeoutException e){
            currentSnake.kill();
            referee.disablePlayer(currentSnake.id);
            referee.addGameSummary("Timeout");
            referee.addTooltip(currentSnake.id, "Timeout!");
        } catch (Exception e){
            currentSnake.kill();
            referee.disablePlayer(currentSnake.id);
            referee.addGameSummary(e.getMessage());
            referee.addTooltip(currentSnake.id, "Crashed");
        }
    }

    private String[] createInput(int player){
        Snake currentSnake = snakes.get(player);
        ArrayList<String> inputs = new ArrayList<>();
        if (!currentSnake.isInitialized){
            inputs.add(Constants.WIDTH + " " + Constants.HEIGHT + " " + snakes.size() + " " + Constants.MAX_FOOD + " " + player);
        }

        inputs.add(countAlivePlayers() + "");
        for (Snake snake : snakes){
            if(snake.isDead) continue;
            inputs.add(snake.id + " " +  snake.score + " " + snake.snake.size() + " " + String.join(",",
                    Arrays.asList(snake.snake.stream().map(s -> s.point.x + "," + s.point.y).toArray()).toArray(new String[snake.snake.size()])));
        }

        for (Point food : food){
            inputs.add(food.x + " " + food.y);
        }
        currentSnake.isInitialized = true;
        return GetStringArray(inputs);
    }

    private int getNextPlayer(){
        for(int i = currentPlayer + 1; i < snakes.size(); i++){
            if(!snakes.get(i).isDead) return i;
        }

        for(int i = 0; i < snakes.size(); i++){
            if(!snakes.get(i).isDead) return i;
        }

        return 0; // should never occur.
    }

    private boolean isCollision(Point next, SnakePart head){
        if (next.x < 0 || next.y < 0 || next.x >= Constants.WIDTH || next.y >= Constants.HEIGHT) return true;
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

    public static String[] GetStringArray(ArrayList<String> arr)
    {
        String str[] = new String[arr.size()];
        for (int j = 0; j < arr.size(); j++) {
            str[j] = arr.get(j);
        }
        return str;
    }
}
