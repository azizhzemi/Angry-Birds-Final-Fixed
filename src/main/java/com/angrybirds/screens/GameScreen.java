package com.angrybirds.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import com.angrybirds.AngryBirdsGame;
import com.angrybirds.entities.Bird;
import com.angrybirds.entities.Slingshot;
import com.angrybirds.entities.Block;
import com.angrybirds.entities.Pig;
import com.angrybirds.game.GameInputProcessor;
import com.angrybirds.physics.PhysicsWorld;

public class GameScreen implements Screen {

    private final AngryBirdsGame game;
    private final int levelIndex;

    private final BitmapFont titleFont;
    private final BitmapFont hintFont;
    private final OrthographicCamera camera;
    private final Viewport viewport;
    
    private PhysicsWorld physicsWorld;
    private Slingshot slingshot;
    private Bird currentBird;
    private java.util.List<Block> blocks;
    private java.util.List<Pig> pigs;
    private boolean debugMode = false;

    public GameScreen(AngryBirdsGame game, int levelIndex) {
        this.game = game;
        this.levelIndex = levelIndex;

        this.titleFont = new BitmapFont();
        this.titleFont.getData().setScale(2.5f);
        this.titleFont.setColor(Color.WHITE);

        this.hintFont = new BitmapFont();
        this.hintFont.getData().setScale(1.4f);
        this.hintFont.setColor(Color.WHITE);

        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(AngryBirdsGame.V_WIDTH, AngryBirdsGame.V_HEIGHT, camera);
        this.viewport.apply(true);
        
        initGame();
    }
    
    private void initGame() {
        physicsWorld = new PhysicsWorld();
        
        // Create ground
        physicsWorld.createStaticBody(AngryBirdsGame.V_WIDTH / 2f, 50, AngryBirdsGame.V_WIDTH, 20);
        
        blocks = new java.util.ArrayList<>();
        pigs = new java.util.ArrayList<>();

        // Create a more complex level structure similar to Angry Birds
        // Bottom level - wooden platform
        blocks.add(new Block(physicsWorld, 800, 120, 150, 30, false, Block.BlockType.WOOD)); // Bottom base (static)

        // Second level - wooden supports
        blocks.add(new Block(physicsWorld, 750, 180, 30, 100, true, Block.BlockType.WOOD)); // Left pillar
        blocks.add(new Block(physicsWorld, 850, 180, 30, 100, true, Block.BlockType.WOOD)); // Right pillar

        // Third level - wooden platform
        blocks.add(new Block(physicsWorld, 800, 250, 120, 25, true, Block.BlockType.WOOD));

        // Fourth level - ice/glass blocks with pigs
        blocks.add(new Block(physicsWorld, 760, 310, 40, 40, true, Block.BlockType.ICE)); // Left ice block
        blocks.add(new Block(physicsWorld, 840, 310, 40, 40, true, Block.BlockType.ICE)); // Right ice block

        // Pig on ice blocks
        pigs.add(new Pig(physicsWorld, 800, 330, 15));
        pigs.add(new Pig(physicsWorld, 760, 310, 12)); // Extra pig on left ice block
        pigs.add(new Pig(physicsWorld, 840, 310, 12)); // Extra pig on right ice block

        // Fifth level - more wooden blocks
        blocks.add(new Block(physicsWorld, 800, 380, 80, 25, true, Block.BlockType.WOOD));

        // Top level - ice blocks and pig
        blocks.add(new Block(physicsWorld, 770, 440, 35, 35, true, Block.BlockType.ICE)); // Left top ice
        blocks.add(new Block(physicsWorld, 830, 440, 35, 35, true, Block.BlockType.ICE)); // Right top ice

        // Top pig
        pigs.add(new Pig(physicsWorld, 800, 460, 15));

        // Add some variety with additional blocks
        blocks.add(new Block(physicsWorld, 800, 500, 70, 20, true, Block.BlockType.WOOD)); // Top wooden piece

        slingshot = new Slingshot(200, 60);
        currentBird = new Bird(physicsWorld, 0, 0, 20);
        slingshot.setBird(currentBird);
        
        // Setup input
        GameInputProcessor gameInput = new GameInputProcessor(viewport, slingshot);
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(gameInput);
        multiplexer.addProcessor(new com.badlogic.gdx.InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.D) {
                    debugMode = !debugMode;
                    return true;
                } else if (keycode == Input.Keys.ESCAPE) {
                    game.showMenu();
                    return true;
                } else if (keycode == Input.Keys.R) {
                    resetGame();
                    return true;
                }
                return false;
            }
        });
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override public void show() {}

    @Override
    public void render(float delta) {
        // handleInput();  // Removed since input is now handled in the InputProcessor

        // Update
        physicsWorld.update(delta);
        currentBird.update();
        for (Block block : blocks) {
            block.update();
        }
        for (Pig pig : pigs) {
            pig.update();
        }

        // Clear screen
        Gdx.gl.glClearColor(0.45f, 0.74f, 0.95f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();

        // Draw UI
        String header = "Level " + (levelIndex + 1);
        float hw = textWidth(titleFont, header);
        titleFont.draw(game.batch, header,
                (AngryBirdsGame.V_WIDTH - hw) / 2f,
                AngryBirdsGame.V_HEIGHT - 60f);

        String controls = "[ESC] Menu  -  [R] Restart  -  [D] Debug: " + (debugMode ? "ON" : "OFF");
        float cw = textWidth(hintFont, controls);
        hintFont.draw(game.batch, controls,
                (AngryBirdsGame.V_WIDTH - cw) / 2f,
                40f);
        
        // Draw Game Entities
        slingshot.draw(game.batch);
        currentBird.draw(game.batch);
        for (Block block : blocks) {
            block.draw(game.batch);
        }
        for (Pig pig : pigs) {
            pig.draw(game.batch);
        }

        game.batch.end();
        
        if (debugMode) {
            physicsWorld.renderDebug(camera);
        }
    }

    private float textWidth(BitmapFont f, String s) {
        return f.getCache().addText(s, 0, 0).width;
    }

    private void resetGame() {
        physicsWorld.dispose();
        slingshot.dispose();
        currentBird.dispose();
        for (Block block : blocks) {
            block.dispose();
        }
        for (Pig pig : pigs) {
            pig.dispose();
        }
        initGame();
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
        titleFont.dispose();
        hintFont.dispose();
        physicsWorld.dispose();
        slingshot.dispose();
        currentBird.dispose();
        for (Block block : blocks) {
            block.dispose();
        }
        for (Pig pig : pigs) {
            pig.dispose();
        }
    }
}
