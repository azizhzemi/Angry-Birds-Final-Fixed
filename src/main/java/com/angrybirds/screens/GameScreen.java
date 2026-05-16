package com.angrybirds.screens;

import com.angrybirds.AngryBirdsGame;
import com.angrybirds.entities.Bird;
import com.angrybirds.entities.Block;
import com.angrybirds.entities.Pig;
import com.angrybirds.entities.Slingshot;
import com.angrybirds.game.GameInputProcessor;
import com.angrybirds.physics.PhysicsWorld;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameScreen implements Screen {

    private static final int LEVEL_COUNT = 5;

    private final AngryBirdsGame game;
    private final int levelIndex;
    private final BitmapFont titleFont;
    private final BitmapFont hudFont;
    private final BitmapFont messageFont;
    private final OrthographicCamera camera;
    private final Viewport viewport;
    private final ShapeRenderer shapes;
    private final Texture levelBackground;

    private PhysicsWorld physicsWorld;
    private Slingshot slingshot;
    private Bird currentBird;
    private List<Block> blocks;
    private List<Pig> pigs;
    private int birdsLeft;
    private int score;
    private boolean debugMode;
    private boolean shotCounted;
    private boolean levelWon;
    private boolean levelLost;
    private float settleTimer;
    private float elapsedTime;

    public GameScreen(AngryBirdsGame game, int levelIndex) {
        this.game = game;
        this.levelIndex = Math.max(0, Math.min(levelIndex, LEVEL_COUNT - 1));

        titleFont = new BitmapFont();
        titleFont.getData().setScale(2.5f);
        titleFont.setColor(Color.WHITE);

        hudFont = new BitmapFont();
        hudFont.getData().setScale(1.35f);
        hudFont.setColor(Color.WHITE);

        messageFont = new BitmapFont();
        messageFont.getData().setScale(3.0f);
        messageFont.setColor(Color.WHITE);

        camera = new OrthographicCamera();
        viewport = new FitViewport(AngryBirdsGame.V_WIDTH, AngryBirdsGame.V_HEIGHT, camera);
        viewport.apply(true);
        shapes = new ShapeRenderer();
        levelBackground = new Texture(Gdx.files.internal("level_background_" + (this.levelIndex + 1) + ".png"));

        initGame();
    }

    private void initGame() {
        physicsWorld = new PhysicsWorld();
        physicsWorld.createStaticBody(AngryBirdsGame.V_WIDTH / 2f, 52, AngryBirdsGame.V_WIDTH, 42);
        physicsWorld.setContactListener(new DamageContactListener());

        blocks = new ArrayList<>();
        pigs = new ArrayList<>();
        birdsLeft = 4 - Math.min(levelIndex, 1);
        score = 0;
        shotCounted = false;
        levelWon = false;
        levelLost = false;
        settleTimer = 0f;
        elapsedTime = 0f;

        buildLevel(levelIndex);

        slingshot = new Slingshot(210, 62);
        spawnBird();

        GameInputProcessor gameInput = new GameInputProcessor(viewport, slingshot);
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(gameInput);
        multiplexer.addProcessor(new com.badlogic.gdx.InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.D) {
                    debugMode = !debugMode;
                    return true;
                }
                if (keycode == Input.Keys.ESCAPE) {
                    game.showLevelSelect();
                    return true;
                }
                if (keycode == Input.Keys.R) {
                    resetGame();
                    return true;
                }
                if (keycode == Input.Keys.SPACE && (levelWon || levelLost)) {
                    if (levelWon && levelIndex < LEVEL_COUNT - 1) {
                        game.showGame(levelIndex + 1);
                    } else {
                        resetGame();
                    }
                    return true;
                }
                return false;
            }
        });
        Gdx.input.setInputProcessor(multiplexer);
    }

    private void buildLevel(int index) {
        switch (index) {
            case 0:
                addTower(810, 2);
                addPig(810, 220, 18);
                addBlock(810, 300, 120, 24, Block.BlockType.WOOD);
                addPig(810, 340, 18);
                break;
            case 1:
                addBlock(760, 138, 35, 120, Block.BlockType.WOOD);
                addBlock(880, 138, 35, 120, Block.BlockType.WOOD);
                addBlock(820, 212, 180, 28, Block.BlockType.WOOD);
                addBlock(785, 286, 35, 120, Block.BlockType.ICE);
                addBlock(855, 286, 35, 120, Block.BlockType.ICE);
                addBlock(820, 360, 140, 24, Block.BlockType.WOOD);
                addPig(820, 252, 17);
                addPig(820, 402, 18);
                break;
            case 2:
                addBlock(700, 138, 36, 120, Block.BlockType.WOOD);
                addBlock(820, 138, 36, 120, Block.BlockType.WOOD);
                addBlock(940, 138, 36, 120, Block.BlockType.WOOD);
                addBlock(820, 212, 300, 28, Block.BlockType.WOOD);
                addBlock(760, 292, 36, 125, Block.BlockType.ICE);
                addBlock(880, 292, 36, 125, Block.BlockType.ICE);
                addBlock(820, 368, 190, 26, Block.BlockType.WOOD);
                addPig(760, 252, 16);
                addPig(880, 252, 16);
                addPig(820, 410, 19);
                break;
            case 3:
                addBlock(760, 106, 170, 28, Block.BlockType.WOOD);
                addBlock(700, 173, 32, 105, Block.BlockType.ICE);
                addBlock(820, 173, 32, 105, Block.BlockType.ICE);
                addBlock(760, 238, 160, 26, Block.BlockType.WOOD);
                addBlock(910, 106, 150, 28, Block.BlockType.WOOD);
                addBlock(860, 180, 32, 120, Block.BlockType.WOOD);
                addBlock(960, 180, 32, 120, Block.BlockType.WOOD);
                addBlock(910, 253, 140, 24, Block.BlockType.ICE);
                addPig(760, 278, 17);
                addPig(910, 293, 17);
                addPig(835, 128, 15);
                break;
            default:
                addBlock(690, 146, 36, 130, Block.BlockType.WOOD);
                addBlock(790, 146, 36, 130, Block.BlockType.ICE);
                addBlock(890, 146, 36, 130, Block.BlockType.WOOD);
                addBlock(990, 146, 36, 130, Block.BlockType.ICE);
                addBlock(840, 226, 380, 30, Block.BlockType.WOOD);
                addBlock(740, 308, 34, 130, Block.BlockType.ICE);
                addBlock(940, 308, 34, 130, Block.BlockType.ICE);
                addBlock(840, 388, 250, 26, Block.BlockType.WOOD);
                addBlock(840, 472, 150, 24, Block.BlockType.ICE);
                addPig(740, 268, 16);
                addPig(940, 268, 16);
                addPig(840, 428, 18);
                addPig(840, 512, 19);
                break;
        }
    }

    private void addTower(float centerX, int floors) {
        for (int i = 0; i < floors; i++) {
            float y = 136 + i * 112;
            addBlock(centerX - 55, y, 30, 105, Block.BlockType.WOOD);
            addBlock(centerX + 55, y, 30, 105, Block.BlockType.WOOD);
            addBlock(centerX, y + 66, 150, 25, i % 2 == 0 ? Block.BlockType.WOOD : Block.BlockType.ICE);
        }
    }

    private void addBlock(float x, float y, float width, float height, Block.BlockType type) {
        blocks.add(new Block(physicsWorld, x, y, width, height, true, type));
    }

    private void addPig(float x, float y, float radius) {
        pigs.add(new Pig(physicsWorld, x, y, radius));
    }

    private void spawnBird() {
        if (birdsLeft <= 0) {
            currentBird = null;
            slingshot.setBird(null);
            return;
        }
        currentBird = new Bird(physicsWorld, 0, 0, 21);
        slingshot.setBird(currentBird);
        shotCounted = false;
        settleTimer = 0f;
    }

    @Override public void show() {}

    @Override
    public void render(float delta) {
        physicsWorld.update(delta);
        updateGame(delta);
        drawScene();

        if (debugMode) {
            physicsWorld.renderDebug(camera);
        }
    }

    private void updateGame(float delta) {
        elapsedTime += delta;
        if (currentBird != null) {
            currentBird.update();
            if (currentBird.getState() == Bird.State.FLYING && !shotCounted) {
                birdsLeft--;
                shotCounted = true;
            }
        }

        for (Block block : blocks) {
            block.update();
        }
        for (Pig pig : pigs) {
            pig.update();
        }

        removeDestroyedObjects();

        if (!levelWon && pigs.isEmpty()) {
            levelWon = true;
            score += birdsLeft * 1000;
            slingshot.setBird(null);
            return;
        }

        if (levelWon || levelLost || currentBird == null) {
            return;
        }

        if (shotCounted && (currentBird.isStopped() || currentBird.isOutOfBounds(AngryBirdsGame.V_WIDTH, AngryBirdsGame.V_HEIGHT))) {
            settleTimer += delta;
        } else {
            settleTimer = 0f;
        }

        if (settleTimer > 1.1f) {
            physicsWorld.destroyBody(currentBird.getBody());
            currentBird.dispose();
            if (birdsLeft > 0) {
                spawnBird();
            } else if (!pigs.isEmpty()) {
                currentBird = null;
                slingshot.setBird(null);
                levelLost = true;
            }
        }
    }

    private void removeDestroyedObjects() {
        Iterator<Pig> pigIterator = pigs.iterator();
        while (pigIterator.hasNext()) {
            Pig pig = pigIterator.next();
            if (pig.isDestroyed() || pig.getBody().getPosition().y * PhysicsWorld.PPM < 20) {
                physicsWorld.destroyBody(pig.getBody());
                pig.dispose();
                pigIterator.remove();
                score += 5000;
            }
        }

        Iterator<Block> blockIterator = blocks.iterator();
        while (blockIterator.hasNext()) {
            Block block = blockIterator.next();
            if (block.isDestroyed() || block.getBody().getPosition().y * PhysicsWorld.PPM < 10) {
                physicsWorld.destroyBody(block.getBody());
                block.dispose();
                blockIterator.remove();
                score += 300;
            }
        }
    }

    private void drawScene() {
        Gdx.gl.glClearColor(0.45f, 0.76f, 0.96f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.draw(levelBackground, 0, 0, AngryBirdsGame.V_WIDTH, AngryBirdsGame.V_HEIGHT);
        game.batch.end();

        shapes.setProjectionMatrix(camera.combined);
        shapes.begin(ShapeRenderer.ShapeType.Filled);
        shapes.setColor(0.36f, 0.58f, 0.16f, 0.94f);
        shapes.rect(0, 0, AngryBirdsGame.V_WIDTH, 84);
        shapes.setColor(0.24f, 0.38f, 0.10f, 0.96f);
        shapes.rect(0, 0, AngryBirdsGame.V_WIDTH, 36);
        shapes.end();

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        slingshot.draw(game.batch);
        if (currentBird != null) {
            currentBird.draw(game.batch);
        }
        for (Block block : blocks) {
            block.draw(game.batch);
        }
        for (Pig pig : pigs) {
            pig.draw(game.batch);
        }
        drawHud();
        game.batch.end();
    }

    private void drawHud() {
        String header = "Level " + (levelIndex + 1) + "   Birds: " + birdsLeft + "   Pigs: " + pigs.size() + "   Score: " + score;
        titleFont.draw(game.batch, header, 36, AngryBirdsGame.V_HEIGHT - 32);

        String hint = "Drag the bird and release  |  R Restart  |  ESC Levels  |  D Debug";
        hudFont.draw(game.batch, hint, 36, 38);

        if (levelWon) {
            String msg = levelIndex < LEVEL_COUNT - 1 ? "LEVEL CLEARED!  SPACE: NEXT" : "ALL LEVELS CLEARED!";
            messageFont.draw(game.batch, msg, centeredX(messageFont, msg), 410);
        } else if (levelLost) {
            String msg = "OUT OF BIRDS!  SPACE: TRY AGAIN";
            messageFont.draw(game.batch, msg, centeredX(messageFont, msg), 410);
        }
    }

    private float centeredX(BitmapFont font, String text) {
        return (AngryBirdsGame.V_WIDTH - textWidth(font, text)) / 2f;
    }

    private float textWidth(BitmapFont f, String s) {
        return f.getCache().addText(s, 0, 0).width;
    }

    private void resetGame() {
        disposeGameObjects();
        initGame();
    }

    private void disposeGameObjects() {
        if (physicsWorld != null) {
            physicsWorld.dispose();
        }
        if (slingshot != null) {
            slingshot.dispose();
        }
        if (currentBird != null) {
            currentBird.dispose();
        }
        if (blocks != null) {
            for (Block block : blocks) {
                block.dispose();
            }
        }
        if (pigs != null) {
            for (Pig pig : pigs) {
                pig.dispose();
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
        titleFont.dispose();
        hudFont.dispose();
        messageFont.dispose();
        shapes.dispose();
        levelBackground.dispose();
        disposeGameObjects();
    }

    private class DamageContactListener implements ContactListener {
        @Override public void beginContact(Contact contact) {}
        @Override public void endContact(Contact contact) {}
        @Override public void preSolve(Contact contact, Manifold oldManifold) {}

        @Override
        public void postSolve(Contact contact, ContactImpulse impulse) {
            float force = 0f;
            for (float normalImpulse : impulse.getNormalImpulses()) {
                force += normalImpulse;
            }
            if (force < 0.35f) {
                return;
            }
            if (!shotCounted || elapsedTime < 0.6f) {
                return;
            }
            Object a = contact.getFixtureA().getBody().getUserData();
            Object b = contact.getFixtureB().getBody().getUserData();
            applyDamage(a, force, b instanceof Bird);
            applyDamage(b, force, a instanceof Bird);
        }

        private void applyDamage(Object target, float force, boolean hitByBird) {
            float damage = force * (hitByBird ? 34f : 15f);
            if (target instanceof Pig) {
                ((Pig) target).damage(damage);
            } else if (target instanceof Block) {
                ((Block) target).damage(damage);
            }
        }
    }
}
