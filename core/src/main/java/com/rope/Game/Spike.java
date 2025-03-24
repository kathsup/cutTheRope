package com.rope.Game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Spike {

    private Body body;
    private Sprite sprite;
    private World world;

    public Spike(World world, float x, float y) {
        this.world = world;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(x, y);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(1.5f, 0.25f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0.6f;

        body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);

        sprite = new Sprite(new Texture("barra.jpg"));
        sprite.setSize(5f, 1f);
        sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);

        body.setUserData(sprite);

        shape.dispose();
    }

    public void draw(SpriteBatch batch) {
        Sprite spikeSprite = (Sprite) body.getUserData();
        if (spikeSprite != null) {
            spikeSprite.setPosition(
                    body.getPosition().x - spikeSprite.getWidth() / 2,
                    body.getPosition().y - spikeSprite.getHeight() / 2
            );
            spikeSprite.draw(batch);
        }
    }

    public void dispose() {
        if (sprite != null && sprite.getTexture() != null) {
            sprite.getTexture().dispose();
        }
    }

    public Body getBody() {
        return body;
    }

}
