package com.angrybirds.screens;

import com.angrybirds.AngryBirdsGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MenuScreen implements Screen {

    private final AngryBirdsGame game;
    private final OrthographicCamera camera;
    private final Viewport viewport;
    private final ShapeRenderer shapes;
    private final BitmapFont titleFont;
    private final BitmapFont buttonFont;
    private final BitmapFont smallFont;
    private final GlyphLayout layout;
    private final Vector3 pointer;
    private final MenuButton playButton;
    private final MenuButton levelsButton;
    private final MenuButton exitButton;

    private Texture birdTexture;
    private Texture pigTexture;
    private Texture crownTexture;
    private Texture backgroundTexture;
    private float time;

    public MenuScreen(AngryBirdsGame game) {
        this.game = game;
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(AngryBirdsGame.V_WIDTH, AngryBirdsGame.V_HEIGHT, camera);
        this.shapes = new ShapeRenderer();
        this.titleFont = new BitmapFont();
        this.buttonFont = new BitmapFont();
        this.smallFont = new BitmapFont();
        this.layout = new GlyphLayout();
        this.pointer = new Vector3();

        titleFont.getData().setScale(5.3f);
        buttonFont.getData().setScale(2.1f);
        smallFont.getData().setScale(1.25f);

        playButton = new MenuButton("PLAY", 440, 306, 400, 72);
        levelsButton = new MenuButton("LEVELS", 440, 218, 400, 72);
        exitButton = new MenuButton("EXIT", 440, 130, 400, 72);
    }

    @Override
    public void show() {
        viewport.apply(true);
        backgroundTexture = new Texture(Gdx.files.internal("background.jpg"));
        birdTexture = createBirdTexture(128);
        pigTexture = createPigTexture(96);
        crownTexture = createCrownTexture(90, 54);
    }

    @Override
    public void render(float delta) {
        time += delta;
        updatePointer();
        if (handleInput()) {
            return;
        }

        Gdx.gl.glClearColor(0.46f, 0.77f, 0.98f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        drawBackground();
        drawSprites();
        drawText();
    }

    private void updatePointer() {
        pointer.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(pointer);
        playButton.hovered = playButton.contains(pointer.x, pointer.y);
        levelsButton.hovered = levelsButton.contains(pointer.x, pointer.y);
        exitButton.hovered = exitButton.contains(pointer.x, pointer.y);
    }

    private boolean handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            game.showGame(0);
            return true;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.L)) {
            game.showLevelSelect();
            return true;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
            return true;
        }
        if (!Gdx.input.justTouched()) {
            return false;
        }
        if (playButton.contains(pointer.x, pointer.y)) {
            game.showGame(0);
            return true;
        } else if (levelsButton.contains(pointer.x, pointer.y)) {
            game.showLevelSelect();
            return true;
        } else if (exitButton.contains(pointer.x, pointer.y)) {
            Gdx.app.exit();
            return true;
        }
        return false;
    }

    private void drawBackground() {
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.draw(backgroundTexture, 0, 0, AngryBirdsGame.V_WIDTH, AngryBirdsGame.V_HEIGHT);
        game.batch.end();

        shapes.setProjectionMatrix(camera.combined);
        shapes.begin(ShapeRenderer.ShapeType.Filled);

        shapes.setColor(1f, 0.87f, 0.27f, 1f);
        shapes.circle(1060, 600 + (float) Math.sin(time * 0.8f) * 8f, 62);
        shapes.setColor(1f, 0.96f, 0.58f, 0.9f);
        shapes.circle(1040, 618 + (float) Math.sin(time * 0.8f) * 8f, 18);

        drawCloud(120 + cloudOffset(0.2f, 160), 595, 1f);
        drawCloud(555 + cloudOffset(0.13f, 220), 628, 0.78f);
        drawCloud(890 + cloudOffset(0.1f, 260), 510, 0.62f);

        shapes.setColor(0.55f, 0.82f, 0.28f, 1f);
        shapes.circle(170, 78, 210);
        shapes.circle(500, 70, 180);
        shapes.circle(970, 82, 240);
        shapes.setColor(0.37f, 0.62f, 0.18f, 1f);
        shapes.rect(0, 0, AngryBirdsGame.V_WIDTH, 92);
        shapes.setColor(0.26f, 0.45f, 0.12f, 1f);
        shapes.rect(0, 0, AngryBirdsGame.V_WIDTH, 38);

        drawSlingshot(162, 88);
        drawPigTower(930, 103);
        drawButton(playButton, 0.27f, 0.58f, 0.18f);
        drawButton(levelsButton, 0.76f, 0.50f, 0.18f);
        drawButton(exitButton, 0.73f, 0.20f, 0.16f);

        shapes.end();
    }

    private float cloudOffset(float speed, float width) {
        return ((time * 30f * speed) % (AngryBirdsGame.V_WIDTH + width)) - width;
    }

    private void drawCloud(float x, float y, float scale) {
        shapes.setColor(1f, 1f, 1f, 0.82f);
        shapes.circle(x, y, 34 * scale);
        shapes.circle(x + 42 * scale, y + 14 * scale, 45 * scale);
        shapes.circle(x + 92 * scale, y, 34 * scale);
        shapes.rect(x, y - 27 * scale, 92 * scale, 38 * scale);
    }

    private void drawSlingshot(float x, float y) {
        shapes.setColor(0.25f, 0.12f, 0.04f, 1f);
        shapes.rect(x + 28, y, 18, 118);
        shapes.rect(x + 4, y + 65, 18, 70);
        shapes.rect(x + 54, y + 65, 18, 70);
        shapes.setColor(0.10f, 0.05f, 0.02f, 1f);
        shapes.rect(x + 4, y + 112, 68, 8);
    }

    private void drawPigTower(float x, float y) {
        shapes.setColor(0.48f, 0.25f, 0.09f, 1f);
        shapes.rect(x, y, 36, 138);
        shapes.rect(x + 122, y, 36, 138);
        shapes.rect(x - 18, y + 138, 194, 28);
        shapes.setColor(0.65f, 0.83f, 0.92f, 0.9f);
        shapes.rect(x + 38, y + 28, 34, 110);
        shapes.rect(x + 86, y + 28, 34, 110);
        shapes.setColor(0.48f, 0.25f, 0.09f, 1f);
        shapes.rect(x + 18, y + 166, 122, 26);
    }

    private void drawButton(MenuButton button, float r, float g, float b) {
        float lift = button.hovered ? 6f : 0f;
        shapes.setColor(0.11f, 0.07f, 0.04f, 0.55f);
        shapes.rect(button.x + 8, button.y - 8 + lift, button.w, button.h);
        shapes.setColor(r, g, b, 1f);
        shapes.rect(button.x, button.y + lift, button.w, button.h);
        shapes.setColor(Math.min(r + 0.16f, 1f), Math.min(g + 0.16f, 1f), Math.min(b + 0.16f, 1f), 1f);
        shapes.rect(button.x, button.y + button.h - 16 + lift, button.w, 16);
        shapes.setColor(0.20f, 0.11f, 0.04f, 0.55f);
        shapes.rect(button.x + 22, button.y + 15 + lift, button.w - 44, 5);
        button.drawLift = lift;
    }

    private void drawSprites() {
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.draw(birdTexture, 118, 210 + (float) Math.sin(time * 2.1f) * 6f, 128, 128);
        game.batch.draw(pigTexture, 963, 296 + (float) Math.sin(time * 1.8f) * 4f, 96, 96);
        game.batch.draw(crownTexture, 965, 372 + (float) Math.sin(time * 1.8f) * 4f, 90, 54);
        game.batch.end();
    }

    private void drawText() {
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();

        drawShadowed(titleFont, "ANGRY BIRDS", 344, 604, Color.WHITE, new Color(0.22f, 0.05f, 0.04f, 1f), 5f);
        drawShadowed(smallFont, "PIGGY REVENGE", 528, 542, new Color(1f, 0.91f, 0.22f, 1f), new Color(0.20f, 0.08f, 0.03f, 1f), 3f);

        drawButtonText(playButton);
        drawButtonText(levelsButton);
        drawButtonText(exitButton);

        smallFont.setColor(1f, 1f, 1f, 0.85f);
        smallFont.draw(game.batch, "ENTER: PLAY    L: LEVELS    ESC: EXIT", 460, 58);

        game.batch.end();
    }

    private void drawButtonText(MenuButton button) {
        Color main = button.hovered ? new Color(1f, 0.94f, 0.28f, 1f) : Color.WHITE;
        drawShadowed(buttonFont, button.label, button.centeredTextX(buttonFont, layout), button.y + 49 + button.drawLift, main, Color.BLACK, 3f);
    }

    private void drawShadowed(BitmapFont font, String text, float x, float y, Color color, Color shadow, float offset) {
        font.setColor(shadow);
        font.draw(game.batch, text, x + offset, y - offset);
        font.setColor(color);
        font.draw(game.batch, text, x, y);
    }

    private Texture createBirdTexture(int size) {
        Pixmap p = new Pixmap(size, size, Pixmap.Format.RGBA8888);
        p.setColor(0, 0, 0, 0);
        p.fill();
        int c = size / 2;
        p.setColor(0.82f, 0.05f, 0.04f, 1f);
        p.fillCircle(c, c, 46);
        p.setColor(0.65f, 0.02f, 0.02f, 1f);
        p.fillCircle(c - 10, c - 18, 28);
        p.setColor(1f, 0.78f, 0.1f, 1f);
        p.fillTriangle(c + 36, c + 2, c + 72, c + 18, c + 72, c - 12);
        p.setColor(Color.WHITE);
        p.fillCircle(c - 14, c + 19, 12);
        p.fillCircle(c + 12, c + 19, 12);
        p.setColor(Color.BLACK);
        p.fillCircle(c - 10, c + 18, 5);
        p.fillCircle(c + 8, c + 18, 5);
        p.setColor(0.17f, 0.02f, 0.01f, 1f);
        p.fillTriangle(c - 34, c + 45, c - 6, c + 68, c - 20, c + 42);
        p.fillTriangle(c + 1, c + 43, c + 25, c + 68, c + 15, c + 40);
        Texture texture = new Texture(p);
        p.dispose();
        return texture;
    }

    private Texture createPigTexture(int size) {
        Pixmap p = new Pixmap(size, size, Pixmap.Format.RGBA8888);
        p.setColor(0, 0, 0, 0);
        p.fill();
        int c = size / 2;
        p.setColor(0.53f, 0.86f, 0.27f, 1f);
        p.fillCircle(c, c, 36);
        p.setColor(0.40f, 0.68f, 0.18f, 1f);
        p.fillCircle(c, c - 8, 17);
        p.setColor(Color.BLACK);
        p.fillCircle(c - 16, c + 12, 5);
        p.fillCircle(c + 16, c + 12, 5);
        p.fillCircle(c - 6, c - 9, 3);
        p.fillCircle(c + 6, c - 9, 3);
        p.setColor(0.53f, 0.86f, 0.27f, 1f);
        p.fillCircle(c - 28, c + 28, 11);
        p.fillCircle(c + 28, c + 28, 11);
        Texture texture = new Texture(p);
        p.dispose();
        return texture;
    }

    private Texture createCrownTexture(int width, int height) {
        Pixmap p = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        p.setColor(0, 0, 0, 0);
        p.fill();
        p.setColor(1f, 0.82f, 0.12f, 1f);
        p.fillTriangle(4, 8, 18, 50, 34, 8);
        p.fillTriangle(28, 8, 45, 52, 62, 8);
        p.fillTriangle(56, 8, 72, 50, 86, 8);
        p.fillRectangle(8, 6, 74, 14);
        p.setColor(0.58f, 0.27f, 0.06f, 1f);
        p.drawRectangle(8, 6, 74, 14);
        Texture texture = new Texture(p);
        p.dispose();
        return texture;
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
        buttonFont.dispose();
        smallFont.dispose();
        if (birdTexture != null) birdTexture.dispose();
        if (pigTexture != null) pigTexture.dispose();
        if (crownTexture != null) crownTexture.dispose();
        if (backgroundTexture != null) backgroundTexture.dispose();
    }

    private static class MenuButton {
        final String label;
        final float x;
        final float y;
        final float w;
        final float h;
        boolean hovered;
        float drawLift;

        MenuButton(String label, float x, float y, float w, float h) {
            this.label = label;
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
        }

        boolean contains(float px, float py) {
            return px >= x && px <= x + w && py >= y && py <= y + h;
        }

        float centeredTextX(BitmapFont font, GlyphLayout layout) {
            layout.setText(font, label);
            return x + (w - layout.width) / 2f;
        }
    }
}
