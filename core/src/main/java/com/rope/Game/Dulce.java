package com.rope.Game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class Dulce {

    private Body body;
    private Sprite sprite;
    private World world;
    private float pixelsToMeter;
    FixtureDef fixtureDef;

    public Dulce(World world, float x, float y, float pixelsToMeter) {
        this.world = world;
        this.pixelsToMeter = pixelsToMeter;

        BodyDef ballDef = new BodyDef();
        ballDef.type = BodyDef.BodyType.DynamicBody;
        ballDef.position.set(x, y);

        CircleShape shape = new CircleShape();
        shape.setRadius(0.5f);

        fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 5f;
        fixtureDef.friction = 0.000001f;
        fixtureDef.restitution = 0.000001f;

        body = world.createBody(ballDef);
        body.createFixture(fixtureDef);

        sprite = new Sprite(new Texture("dulce.png"));

        float desiredSpriteSizeInMeters = 0.05f;
        float spriteSizeInPixels = desiredSpriteSizeInMeters * pixelsToMeter;
        sprite.setSize(spriteSizeInPixels, spriteSizeInPixels);
        sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);

        body.setUserData(sprite);

        shape.dispose();
    }

    public Body getBody() {
        return body;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setDensity(float density) {
        if (fixtureDef != null) {
            fixtureDef.density = density;
            body.destroyFixture(body.getFixtureList().first());
            body.createFixture(fixtureDef);
        }
    }

    public void setFriction(float friction) {
        if (fixtureDef != null) {
            fixtureDef.friction = friction;
            body.destroyFixture(body.getFixtureList().first());
            body.createFixture(fixtureDef);
        }
    }

    public void dispose() {
        if (sprite != null && sprite.getTexture() != null) {
            sprite.getTexture().dispose();
        }
    }
}
