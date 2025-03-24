package com.rope.Game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;


public class Rana {
   private Body body;
    private Sprite sprite;
    private World world;
    private Texture normalTexture; 
    private Texture eatingTexture;

    public Rana(World world, float x, float y) {
        this.world = world;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;  
        bodyDef.position.set(x, y); 

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.5f, 0.5f);  

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0.6f;

        body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);

        normalTexture = new Texture("rana.png");
        eatingTexture = new Texture("ranaC.png"); 

        sprite = new Sprite(normalTexture);
        sprite.setSize(2f, 2f);
        
         body.setUserData(sprite);

        
        shape.dispose();
    }

    public void draw(SpriteBatch batch) {
        Sprite ranaSprite = (Sprite) body.getUserData();
        ranaSprite.setPosition(body.getPosition().x - ranaSprite.getWidth() / 2,
                body.getPosition().y - ranaSprite.getHeight() / 2);
        ranaSprite.draw(batch);
    }
    
    public void setEatingTexture() {
        sprite.setTexture(eatingTexture);
    }

    public void setNormalTexture() {
        sprite.setTexture(normalTexture);
    }

    public void dispose() {
        normalTexture.dispose();
        eatingTexture.dispose(); 
    }
    
    public Body getBody() {
        return body;
    }
    
    
}
