package com.angrybirds.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import com.angrybirds.AngryBirdsGame;

public class LevelSelectScreen implements Screen {

    private static final int LEVEL_COUNT = 5;
    private static final int UNLOCKED_COUNT = 1;
    private static final float CARD_SIZE = 140f;
    private static final float CARD_GAP = 30f;

    private final AngryBirdsGame game;
    private final ShapeRenderer shapes;
    private final BitmapFont titleFont;
    private final BitmapFont numberFont;
    private final BitmapFont hintFont;
    private final OrthographicCamera camera;
    private final Viewport viewport;

    public LevelSelectScreen(AngryBirdsGame game) {
        this.game = game;
        this.shapes = new ShapeRenderer();

        this.titleFont = new BitmapFont();
        this.titleFont.getData().setScale(3f);
        this.titleFont.setColor(Color.WHITE);

        this.numberFont = new BitmapFont();
        this.numberFont.getData().setScale(3f);
        this.numberFont.setColor(Color.BLACK);

        this.hintFont = new BitmapFont();
        this.hintFont.getData().setScale(1.4f);
        this.hintFont.setColor(Color.WHITE);

        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(AngryBirdsGame.V_WIDTH, AngryBirdsGame.V_HEIGHT, camera);
        this.viewport.apply(true);
    }

    @Override public void show() {}

    @Override
    public void render(float delta) {
        handleInput();

        Gdx.gl.glClearColor(0.40f, 0.70f, 0.92f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        float totalWidth = LEVEL_COUNT * CARD_SIZE + (LEVEL_COUNT - 1) * CARD_GAP;
        float startX = (AngryBirdsGame.V_WIDTH - totalWidth) / 2f;
        float y = (AngryBirdsGame.V_HEIGHT - CARD_SIZE) / 2f;

        shapes.setProjectionMatrix(camera.combined);
        shapes.begin(ShapeRenderer.ShapeType.Filled);
        for (int i = 0; i < LEVEL_COUNT; i++) {
            float x = startX + i * (CARD_SIZE + CARD_GAP);
            boolean unlocked = i < UNLOCKED_COUNT;
            if (unlocked) {
                shapes.setColor(1f, 0.97f, 0.85f, 1f);
            } else {
                shapes.setColor(0.6f, 0.6f, 0.62f, 1f);
            }
            shapes.rect(x, y, CARD_SIZE, CARD_SIZE);
        }
        shapes.end();

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();

        String title = "SELECT A LEVEL";
        float tw = textWidth(titleFont, title);
        titleFont.draw(game.batch, title,
                (AngryBirdsGame.V_WIDTH - tw) / 2f,
                AngryBirdsGame.V_HEIGHT - 100f);

        for (int i = 0; i < LEVEL_COUNT; i++) {
            float x = startX + i * (CARD_SIZE + CARD_GAP);
            boolean unlocked = i < UNLOCKED_COUNT;
            String label = unlocked ? String.valueOf(i + 1) : "X";
            numberFont.setColor(unlocked ? Color.BLACK : Color.DARK_GRAY);
            float lw = textWidth(numberFont, label);
            numberFont.draw(game.batch, label,
                    x + (CARD_SIZE - lw) / 2f,
                    y + CARD_SIZE / 2f + 25f);
        }

        String hint = "[1-" + LEVEL_COUNT + "] Play  -  [B / ESC] Back to Menu";
        float hw = textWidth(hintFont, hint);
        hintFont.draw(game.batch, hint,
                (AngryBirdsGame.V_WIDTH - hw) / 2f,
                100f);

        game.batch.end();
    }

    private float textWidth(BitmapFont f, String s) {
        return f.getCache().addText(s, 0, 0).width;
    }

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)
                || Gdx.input.isKeyJustPressed(Input.Keys.B)) {
            game.showMenu();
            return;
        }
        for (int i = 0; i < LEVEL_COUNT; i++) {
            int key = Input.Keys.NUM_1 + i;
            if (Gdx.input.isKeyJustPressed(key) && i < UNLOCKED_COUNT) {
                game.showGame(i);
                return;
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        shapes.dispose();
        titleFont.dispose();
        numberFont.dispose();
        hintFont.dispose();
    }
}
