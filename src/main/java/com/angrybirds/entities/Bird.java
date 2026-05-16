package com.angrybirds.entities;

import com.angrybirds.physics.PhysicsWorld;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Bird {
    public enum State { ON_SLINGSHOT, FLYING, HIT }
    
    private Body body;
    private Texture texture;
    private float radius;
    private State state;
    private Vector2 visualPosition;
    
    public Bird(PhysicsWorld physicsWorld, float x, float y, float radius) {
        this.radius = radius;
        this.body = physicsWorld.createBirdBody(x, y, radius);
        this.body.setUserData(this);
        this.body.setActive(false); // Disable physics until launched
        this.state = State.ON_SLINGSHOT;
        this.visualPosition = new Vector2(x, y);
        
        // Create a simple green circle texture as placeholder
        Pixmap pixmap = new Pixmap((int)radius * 2 + 2, (int)radius * 2 + 2, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(0.86f, 0.08f, 0.05f, 1f));
        pixmap.fillCircle((int)radius + 1, (int)radius + 1, (int)radius);
        pixmap.setColor(new Color(1f, 0.82f, 0.18f, 1f));
        pixmap.fillTriangle((int)(radius * 1.6f), (int)(radius + 1), (int)(radius * 2.2f), (int)(radius * 0.8f), (int)(radius * 2.2f), (int)(radius * 1.2f));
        pixmap.setColor(Color.WHITE);
        pixmap.fillCircle((int)(radius * 0.78f), (int)(radius * 1.35f), Math.max(2, (int)(radius * 0.18f)));
        pixmap.setColor(Color.BLACK);
        pixmap.fillCircle((int)(radius * 0.84f), (int)(radius * 1.35f), Math.max(1, (int)(radius * 0.08f)));
        pixmap.setColor(Color.BLACK);
        pixmap.drawCircle((int)radius + 1, (int)radius + 1, (int)radius);
        this.texture = new Texture(pixmap);
        pixmap.dispose();
    }
    
    public void update() {
        if (state == State.FLYING || state == State.HIT) {
            visualPosition.set(body.getPosition().x * PhysicsWorld.PPM, body.getPosition().y * PhysicsWorld.PPM);
        }
    }
    
    public void draw(SpriteBatch batch) {
        float x = visualPosition.x - radius;
        float y = visualPosition.y - radius;
        float rotation = body.getAngle() * MathUtils.radiansToDegrees;
        
        batch.draw(texture, x, y, radius, radius, radius * 2, radius * 2, 1, 1, rotation, 0, 0, texture.getWidth(), texture.getHeight(), false, false);
    }
    
    public void launch(Vector2 impulse) {
        state = State.FLYING;
        body.setActive(true);
        body.applyLinearImpulse(impulse.scl(1/PhysicsWorld.PPM), body.getWorldCenter(), true);
    }

    public boolean isStopped() {
        return state == State.FLYING && body.getLinearVelocity().len() < 0.25f;
    }

    public boolean isOutOfBounds(float width, float height) {
        return visualPosition.x < -80 || visualPosition.x > width + 120 || visualPosition.y < -80 || visualPosition.y > height + 160;
    }
    
    public void setVisualPosition(float x, float y) {
        this.visualPosition.set(x, y);
        if (state == State.ON_SLINGSHOT) {
            body.setTransform(x / PhysicsWorld.PPM, y / PhysicsWorld.PPM, 0);
        }
    }
    
    public Vector2 getVisualPosition() {
        return visualPosition;
    }
    
    public float getRadius() {
        return radius;
    }
    
    public Body getBody() {
        return body;
    }
    
    public State getState() {
        return state;
    }
    
    public void dispose() {
        texture.dispose();
    }
}
