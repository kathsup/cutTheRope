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

        // Definir el cuerpo de la estrella
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody; 
        bodyDef.position.set(x, y);  // Posición en el mundo

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.25f, 0.25f);  // Tamaño de la estrella

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.00001f;
        fixtureDef.friction = 0.00001f;
        fixtureDef.restitution = 0.00001f;

        body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);

        // Crear el sprite para la estrella y asignar la imagen
        sprite = new Sprite(new Texture("estrella.png"));
        sprite.setSize(2f, 2f);  // Ajusta el tamaño del sprite

        // Asignar el sprite 
        body.setUserData(this); 

        
        shape.dispose();
    }

    //  dibujar la estrella
    public void draw(SpriteBatch batch) {
        sprite.setPosition(body.getPosition().x - sprite.getWidth() / 2,
                           body.getPosition().y - sprite.getHeight() / 2);
        sprite.draw(batch);
    }

    
    //para obtener el cuerpo
    public Body getBody() {
        return body;
    }
    
   
}

