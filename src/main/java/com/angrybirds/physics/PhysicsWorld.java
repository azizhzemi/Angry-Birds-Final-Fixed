package com.angrybirds.physics;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class PhysicsWorld {
    public static final float PPM = 100f; // Pixels Per Meter
    
    private final World world;
    private final Box2DDebugRenderer debugRenderer;
    
    public PhysicsWorld() {
        this.world = new World(new Vector2(0, -9.81f), true);
        this.debugRenderer = new Box2DDebugRenderer();
    }
    
    public void update(float delta) {
        world.step(1/60f, 6, 2);
    }
    
    public void renderDebug(OrthographicCamera camera) {
        debugRenderer.render(world, camera.combined.cpy().scl(PPM));
    }
    
    public Body createBirdBody(float x, float y, float radius) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x / PPM, y / PPM);
        
        Body body = world.createBody(bodyDef);
        
        CircleShape shape = new CircleShape();
        shape.setRadius(radius / PPM);
        
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.3f;
        fixtureDef.restitution = 0.5f;
        
        body.createFixture(fixtureDef);
        shape.dispose();
        
        // Add damping for more realistic physics
        body.setLinearDamping(0.05f);
        body.setAngularDamping(0.1f);

        return body;
    }
    
    public Body createStaticBody(float x, float y, float width, float height) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x / PPM, y / PPM);
        
        Body body = world.createBody(bodyDef);
        
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2 / PPM, height / 2 / PPM);
        
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.friction = 0.5f;
        
        body.createFixture(fixtureDef);
        shape.dispose();

        return body;
    }

    public Body createBlockBody(float x, float y, float width, float height, boolean isDynamic) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = isDynamic ? BodyDef.BodyType.DynamicBody : BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x / PPM, y / PPM);

        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2 / PPM, height / 2 / PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = isDynamic ? 1.0f : 0.0f;
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution = 0.2f;

        body.createFixture(fixtureDef);
        shape.dispose();

        return body;
    }

    public Body createPigBody(float x, float y, float radius) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x / PPM, y / PPM);

        Body body = world.createBody(bodyDef);

        CircleShape shape = new CircleShape();
        shape.setRadius(radius / PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.8f; // Slightly less dense than bird
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0.3f;

        body.createFixture(fixtureDef);
        shape.dispose();

        // Add some damping
        body.setLinearDamping(0.1f);
        body.setAngularDamping(0.2f);

        return body;
    }
    
    public World getWorld() {
        return world;
    }
    
    public void dispose() {
        world.dispose();
        debugRenderer.dispose();
    }
}
