package com.codingame.game;

import com.codingame.gameengine.module.entities.GraphicEntityModule;

public class ViewController {
    private Game game;
    private GraphicEntityModule module;

    public ViewController(Game game, GraphicEntityModule module){
        this.game = game;

        this.module = module;
    }

    public void onTurn(){

    }
}
