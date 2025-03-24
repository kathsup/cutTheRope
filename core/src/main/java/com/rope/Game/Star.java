package com.rope.Game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Star {

    private Body body;
    private Sprite sprite;
    private World world;

    public Star(World world, float x, float y) {
        this.world = world;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x, y);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.25f, 0.25f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.00001f;
        fixtureDef.friction = 0.00001f;
        fixtureDef.restitution = 0.00001f;

        body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);

        sprite = new Sprite(new Texture("estrella.png"));
        sprite.setSize(2f, 2f);

        body.setUserData(this);

        shape.dispose();
    }

    public void draw(SpriteBatch batch) {
        sprite.setPosition(body.getPosition().x - sprite.getWidth() / 2,
                body.getPosition().y - sprite.getHeight() / 2);
        sprite.draw(batch);
    }

    public Body getBody() {
        return body;
    }

}
