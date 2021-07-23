package com.codingame.game;

import com.codingame.gameengine.core.AbstractPlayer;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Game {
    private Random rnd;
    public int currentPlayer = 0;
    public ArrayList<Snake> snakes = new ArrayList<>();
    public ArrayList<Point> food = new ArrayList<>();
    private IReferee referee;
    private int maxTurns;
    public int numSteps;
    private long seed;
    private long secretSeed = 1337L; // This seed is to make local replication harder ;)
    private int maxFood;
    public Game(IReferee referee, long seed, int food){ // This code is written for a multiplayer game. Lazy factorization to published.
        maxTurns = 600;
        this.referee = referee;
        this.seed = seed;
        rnd = new Random(seed ^ secretSeed);
        maxFood = food;
        for(int i = 0; i < 1; i++){
            snakes.add(new Snake(i, findRandomEmpty()));
        }

        spawnInitialFood();
    }

    public void onTurn() {
        for(Snake snake : snakes){
            referee.updateScore(snake.score);
        }

        Snake currentSnake = snakes.get(0);
        if (currentSnake.turns++ >= maxTurns || numSteps >= 25000 || currentSnake.snake.size() == 600){
            EndGame();
            return;
        }

        String[] output = createInput();
        try {
            String input = referee.sendInput(output);
            if(input.trim().length() == 0) {
                referee.addGameSummary("Empty input.");
                EndGame();
                return;
            }

            for(char dir: input.trim().toCharArray()){
                SnakeDirection direction = new SnakeDirection(dir+"");
                numSteps++;
                Point next = direction.getNext(currentSnake.snake.get(0).point);
                currentSnake.move(direction, hasFood(next));

                if(currentSnake.isDead || numSteps >= 25000 || currentSnake.snake.size() == 600){
                    EndGame();
                    break;
                }
                else if(isCollision(next, currentSnake.snake.get(0))){
                    currentSnake.kill();
                    EndGame();
                    break;
                }
                else if(hasFood(next)){
                    food.remove(next);
                    spawnFood();
                }
            }

        } catch (AbstractPlayer.TimeoutException e){
            referee.addGameSummary("Timeout");
            referee.addTooltip("Timeout");
            EndGame();
        } catch (Exception e){
            referee.addGameSummary(e.getMessage());
            EndGame();
            referee.addTooltip("Crashed - Check game summary");
        }
    }

    private void EndGame(){
        double score = snakes.get(0).score + (snakes.get(0).score == 600 ?25000 - numSteps:0);
        referee.addGameSummary("Total steps: " + numSteps);
        referee.updateScore(score);
        referee.endGame();
    }

    private String[] createInput(){
        Snake currentSnake = snakes.get(0);
        ArrayList<String> inputs = new ArrayList<>();
        if (!currentSnake.isInitialized){
            inputs.add(Constants.WIDTH + " " + Constants.HEIGHT);
        }

        for (Snake snake : snakes){
            if(snake.isDead) continue;
            inputs.add(seed + " " + snake.score + " " + snake.snake.size() + " " + food.size());
            for(SnakePart sp: snake.snake){
                inputs.add(sp.point.x + " " + sp.point.y);
            }
        }
        for (Point food : food){
            inputs.add(food.x + " " + food.y);
        }
        currentSnake.isInitialized = true;
        return GetStringArray(inputs);
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

    private void spawnInitialFood(){
        while(food.size() < maxFood){
            food.add(findRandomEmpty());
        }
    }

    private void spawnFood() {
        if(food.size() >= maxFood) return;
        ArrayList<Point> freeCells = new ArrayList<>();
        for (int x = 0; x < Constants.WIDTH; x++) {
            if(x%2 == 0){ // This is flipping to make food spawn closer on minor snake changes (not wrapped)
                for (int y = 0; y < Constants.HEIGHT; y++) {
                    Point p = new Point(x, y);
                    if(hasFood(p) || hasSnakePart(p)) continue;
                    freeCells.add(p);
                }
            }else{
                for (int y = Constants.HEIGHT-1; y >= 0; y--) {
                    Point p = new Point(x, y);
                    if(hasFood(p) || hasSnakePart(p)) continue;
                    freeCells.add(p);
                }
            }
        }
        if(freeCells.size() == 0) return;
        Point spawnIndex = freeCells.get((int) seed % freeCells.size());
        food.add(spawnIndex);
        seed = seed * seed % 50515093L;
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
