package com.angrybirds.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Slingshot {
    private Vector2 position;
    private Bird currentBird;
    private Texture baseTexture;
    private float maxPullDistance = 100f;
    private float launchStrength = 2.0f;

    public Slingshot(float x, float y) {
        this.position = new Vector2(x, y);
        
        // Create a simple brown rectangle as placeholder for slingshot base
        Pixmap pixmap = new Pixmap(20, 100, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(0.5f, 0.3f, 0.1f, 1f));
        pixmap.fill();
        this.baseTexture = new Texture(pixmap);
        pixmap.dispose();
    }
    
    public void setBird(Bird bird) {
        this.currentBird = bird;
        if (bird != null) {
            bird.setVisualPosition(position.x, position.y + 80);
        }
    }
    
    public void draw(SpriteBatch batch) {
        batch.draw(baseTexture, position.x - 10, position.y);
        
        // Draw elastic if bird is being pulled
        if (currentBird != null && currentBird.getState() == Bird.State.ON_SLINGSHOT) {
            // In a real game, we would draw lines from the slingshot arms to the bird
        }
    }
    
    public void pullBird(Vector2 touchCoords) {
        if (currentBird == null || currentBird.getState() != Bird.State.ON_SLINGSHOT) return;
        
        Vector2 anchor = new Vector2(position.x, position.y + 80);
        Vector2 pull = touchCoords.cpy().sub(anchor);
        
        if (pull.len() > maxPullDistance) {
            pull.setLength(maxPullDistance);
        }
        
        currentBird.setVisualPosition(anchor.x + pull.x, anchor.y + pull.y);
    }
    
    public void releaseBird() {
        if (currentBird == null || currentBird.getState() != Bird.State.ON_SLINGSHOT) return;
        
        Vector2 anchor = new Vector2(position.x, position.y + 80);
        Vector2 birdPos = currentBird.getVisualPosition();
        Vector2 impulse = anchor.cpy().sub(birdPos).scl(launchStrength);
        
        currentBird.launch(impulse);
    }
    
    public boolean isTouchOnBird(Vector2 touchCoords) {
        if (currentBird == null) return false;
        return touchCoords.dst(currentBird.getVisualPosition()) < currentBird.getRadius() * 2;
    }
    
    public void dispose() {
        baseTexture.dispose();
    }
}
