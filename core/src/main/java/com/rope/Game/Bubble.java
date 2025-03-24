/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.rope.Game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.*;

public class Bubble {

    private Body bubbleBody;
    private Sprite bubbleSprite;
    private boolean isFloating;
    private boolean isCollected;

    private static final float BUBBLE_RADIUS = 1.5f;
    private static final float BUBBLE_DENSITY = 0.1f;
    private static final float BUBBLE_FRICTION = 0f;
    private static final float BUBBLE_RESTITUTION = 3f;
    private static final float FLOAT_FORCE = 7f;

    public Bubble(World world, float x, float y, Texture bubbleTexture) {
        CircleShape bubbleShape = new CircleShape();
        bubbleShape.setRadius(BUBBLE_RADIUS);

        BodyDef bubbleDef = new BodyDef();
        bubbleDef.type = BodyDef.BodyType.KinematicBody;
        bubbleDef.position.set(x, y);

        FixtureDef bubbleFixtureDef = new FixtureDef();
        bubbleFixtureDef.shape = bubbleShape;
        bubbleFixtureDef.density = BUBBLE_DENSITY;
        bubbleFixtureDef.friction = BUBBLE_FRICTION;
        bubbleFixtureDef.restitution = BUBBLE_RESTITUTION;

        bubbleBody = world.createBody(bubbleDef);
        bubbleBody.createFixture(bubbleFixtureDef);

        bubbleSprite = new Sprite(bubbleTexture);
        bubbleSprite.setSize(BUBBLE_RADIUS * 2, BUBBLE_RADIUS * 2);
        bubbleSprite.setOrigin(bubbleSprite.getWidth() / 2, bubbleSprite.getHeight() / 2);

        bubbleShape.dispose();

        isFloating = false;
        isCollected = false;
    }

    public void applyFloatForce() {
        if (!isFloating) {
            bubbleBody.setLinearVelocity(0, FLOAT_FORCE);
            isFloating = true;
        }
    }

    public void stopFloating() {
        if (isFloating) {
            bubbleBody.setLinearVelocity(0, 0);
            isFloating = false;
        }
    }

    public void updateSprite() {
        bubbleSprite.setPosition(
                bubbleBody.getPosition().x - bubbleSprite.getWidth() / 2,
                bubbleBody.getPosition().y - bubbleSprite.getHeight() / 2
        );
        bubbleSprite.setRotation(bubbleBody.getAngle() * MathUtils.radiansToDegrees);
    }

    public Body getBody() {
        return bubbleBody;
    }

    public Sprite getSprite() {
        return bubbleSprite;
    }

    public boolean isCollected() {
        return isCollected;
    }

    public void setCollected(boolean collected) {
        isCollected = collected;
    }

    public void dispose() {
        if (bubbleSprite != null && bubbleSprite.getTexture() != null) {
            bubbleSprite.getTexture().dispose();
        }
    }
}
