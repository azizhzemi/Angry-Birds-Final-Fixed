package com.angrybirds;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.angrybirds.screens.GameScreen;
import com.angrybirds.screens.LevelSelectScreen;
import com.angrybirds.screens.MenuScreen;

public class AngryBirdsGame extends Game {

    public static final int V_WIDTH = 1280;
    public static final int V_HEIGHT = 720;

    public SpriteBatch batch;

    @Override
    public void create() {
        batch = new SpriteBatch();
        showMenu();
    }

    public void showMenu() {
        swapScreen(new MenuScreen(this));
    }

    public void showLevelSelect() {
        swapScreen(new LevelSelectScreen(this));
    }

    public void showGame(int levelIndex) {
        swapScreen(new GameScreen(this, levelIndex));
    }

    private void swapScreen(Screen next) {
        Screen previous = getScreen();
        setScreen(next);
        if (previous != null) {
            previous.dispose();
        }
    }

    @Override
    public void dispose() {
        if (getScreen() != null) {
            getScreen().dispose();
        }
        if (batch != null) {
            batch.dispose();
        }
    }
}
