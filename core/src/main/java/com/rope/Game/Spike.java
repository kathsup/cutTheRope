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

        // Definir el cuerpo de la estrella
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;  
        bodyDef.position.set(x, y);  // Posición en el mundo

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(1.5f, 0.25f);  // Tamaño de la estrella

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0.6f;

        body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);

        //imagenes de la rana
        

        // sprite para la rana
        sprite = new Sprite(new Texture("spike.png"));
        sprite.setSize(5f, 1f);
        
         body.setUserData(sprite);

        
        shape.dispose();
    }

    //  dibujar la estrella
    public void draw(SpriteBatch batch) {
        Sprite ranaSprite = (Sprite) body.getUserData();
        ranaSprite.setPosition(body.getPosition().x - ranaSprite.getWidth() / 2,
                body.getPosition().y - ranaSprite.getHeight() / 2);
        ranaSprite.draw(batch);
    }
    
    
    public void dispose() {
        sprite.getTexture().dispose();
    }
    
    //obtener el cuerpo 
    public Body getBody() {
        return body;
    }
    
}
