package com.angrybirds.entities;

import com.angrybirds.physics.PhysicsWorld;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Block {
    public enum BlockType { WOOD, ICE }

    private Body body;
    private Texture texture;
    private float width;
    private float height;
    private Vector2 visualPosition;
    private BlockType type;

    public Block(PhysicsWorld physicsWorld, float x, float y, float width, float height, boolean isDynamic) {
        this(physicsWorld, x, y, width, height, isDynamic, BlockType.WOOD);
    }

    public Block(PhysicsWorld physicsWorld, float x, float y, float width, float height, boolean isDynamic, BlockType type) {
        this.width = width;
        this.height = height;
        this.type = type;
        this.body = physicsWorld.createBlockBody(x, y, width, height, isDynamic);
        this.visualPosition = new Vector2(x, y);

        // Create texture based on block type
        Pixmap pixmap = new Pixmap((int)width, (int)height, Pixmap.Format.RGBA8888);

        if (type == BlockType.ICE) {
            // Light blue/cyan color for ice blocks
            pixmap.setColor(new Color(0.7f, 0.9f, 1.0f, 0.8f)); // Light blue
            pixmap.fill();
            pixmap.setColor(new Color(0.5f, 0.8f, 1.0f, 1.0f)); // Darker blue border
            pixmap.drawRectangle(0, 0, (int)width, (int)height);
            // Add some ice pattern
            pixmap.setColor(new Color(1.0f, 1.0f, 1.0f, 0.6f)); // White highlights
            if (width > 20) pixmap.drawLine(5, 5, (int)width - 5, (int)height - 5);
        } else {
            // Brown color for wood blocks
            pixmap.setColor(new Color(0.5f, 0.3f, 0.1f, 1f)); // Brown
            pixmap.fill();
            pixmap.setColor(new Color(0.3f, 0.15f, 0.05f, 1f)); // Darker brown border
            pixmap.drawRectangle(0, 0, (int)width, (int)height);
            // Add wood texture
            pixmap.setColor(new Color(0.4f, 0.2f, 0.0f, 0.5f)); // Dark lines for wood grain
            for (int i = 0; i < height; i += 10) {
                pixmap.drawLine(0, i, (int)width, i);
            }
        }

        this.texture = new Texture(pixmap);
        pixmap.dispose();
    }

    public void update() {
        visualPosition.set(body.getPosition().x * PhysicsWorld.PPM, body.getPosition().y * PhysicsWorld.PPM);
    }

    public void draw(SpriteBatch batch) {
        float x = visualPosition.x - width / 2;
        float y = visualPosition.y - height / 2;
        float rotation = body.getAngle() * com.badlogic.gdx.math.MathUtils.radiansToDegrees;

        batch.draw(texture, x, y, width / 2, height / 2, width, height, 1, 1, rotation, 0, 0, texture.getWidth(), texture.getHeight(), false, false);
    }

    public Body getBody() {
        return body;
    }

    public void dispose() {
        texture.dispose();
    }
}

