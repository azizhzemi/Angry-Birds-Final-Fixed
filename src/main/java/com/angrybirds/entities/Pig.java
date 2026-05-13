package com.angrybirds.entities;

import com.angrybirds.physics.PhysicsWorld;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Pig {
    private Body body;
    private Texture texture;
    private float radius;
    private Vector2 visualPosition;

    public Pig(PhysicsWorld physicsWorld, float x, float y, float radius) {
        this.radius = radius;
        this.body = physicsWorld.createPigBody(x, y, radius);
        this.visualPosition = new Vector2(x, y);

        // Create a detailed pig texture with face
        int size = (int)radius * 2 + 2;
        Pixmap pixmap = new Pixmap(size, size, Pixmap.Format.RGBA8888);
        int center = (int)radius + 1;
        int r = (int)radius;

        // Main body - pink circle
        pixmap.setColor(new Color(1.0f, 0.75f, 0.85f, 1.0f)); // Pink
        pixmap.fillCircle(center, center, r);

        // Snout - darker pink circle at bottom
        pixmap.setColor(new Color(0.95f, 0.65f, 0.75f, 1.0f)); // Darker pink
        pixmap.fillCircle(center, center - r/3, (int)(r * 0.4f));

        // Snout nostrils
        pixmap.setColor(Color.BLACK);
        int nostrilRadius = Math.max(1, (int)(r * 0.08f));
        pixmap.fillCircle(center - (int)(r * 0.15f), center - r/3, nostrilRadius);
        pixmap.fillCircle(center + (int)(r * 0.15f), center - r/3, nostrilRadius);

        // Eyes - black circles
        int eyeRadius = Math.max(1, (int)(r * 0.15f));
        pixmap.setColor(Color.BLACK);
        pixmap.fillCircle(center - (int)(r * 0.3f), center + (int)(r * 0.2f), eyeRadius);
        pixmap.fillCircle(center + (int)(r * 0.3f), center + (int)(r * 0.2f), eyeRadius);

        // Eye highlights - white
        pixmap.setColor(Color.WHITE);
        int highlightRadius = Math.max(1, (int)(r * 0.06f));
        pixmap.fillCircle(center - (int)(r * 0.25f), center + (int)(r * 0.25f), highlightRadius);
        pixmap.fillCircle(center + (int)(r * 0.35f), center + (int)(r * 0.25f), highlightRadius);

        // Ears - small circles on top
        pixmap.setColor(new Color(1.0f, 0.75f, 0.85f, 1.0f)); // Pink
        int earRadius = Math.max(1, (int)(r * 0.2f));
        pixmap.fillCircle(center - (int)(r * 0.4f), center + (int)(r * 0.7f), earRadius);
        pixmap.fillCircle(center + (int)(r * 0.4f), center + (int)(r * 0.7f), earRadius);

        // Inner ear - lighter pink
        pixmap.setColor(new Color(1.0f, 0.85f, 0.95f, 1.0f)); // Light pink
        pixmap.fillCircle(center - (int)(r * 0.4f), center + (int)(r * 0.7f), Math.max(1, (int)(r * 0.1f)));
        pixmap.fillCircle(center + (int)(r * 0.4f), center + (int)(r * 0.7f), Math.max(1, (int)(r * 0.1f)));

        // Outline
        pixmap.setColor(Color.BLACK);
        pixmap.drawCircle(center, center, r);

        this.texture = new Texture(pixmap);
        pixmap.dispose();
    }

    public void update() {
        visualPosition.set(body.getPosition().x * PhysicsWorld.PPM, body.getPosition().y * PhysicsWorld.PPM);
    }

    public void draw(SpriteBatch batch) {
        float x = visualPosition.x - radius;
        float y = visualPosition.y - radius;
        float rotation = body.getAngle() * com.badlogic.gdx.math.MathUtils.radiansToDegrees;

        batch.draw(texture, x, y, radius, radius, radius * 2, radius * 2, 1, 1, rotation, 0, 0, texture.getWidth(), texture.getHeight(), false, false);
    }

    public Body getBody() {
        return body;
    }

    public void dispose() {
        texture.dispose();
    }
}

