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
    private float launchStrength = 3.1f;

    public Slingshot(float x, float y) {
        this.position = new Vector2(x, y);
        
        Pixmap pixmap = new Pixmap(70, 120, Pixmap.Format.RGBA8888);
        pixmap.setColor(0, 0, 0, 0);
        pixmap.fill();
        pixmap.setColor(new Color(0.36f, 0.17f, 0.05f, 1f));
        pixmap.fillRectangle(30, 30, 12, 90);
        pixmap.fillRectangle(12, 62, 12, 58);
        pixmap.fillRectangle(48, 62, 12, 58);
        pixmap.setColor(new Color(0.18f, 0.08f, 0.03f, 1f));
        pixmap.drawRectangle(30, 30, 12, 90);
        pixmap.drawRectangle(12, 62, 12, 58);
        pixmap.drawRectangle(48, 62, 12, 58);
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
        batch.draw(baseTexture, position.x - 35, position.y);
        
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
