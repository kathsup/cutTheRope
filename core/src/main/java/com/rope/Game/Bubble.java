/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.rope.Game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

/**
 *
 * @author Lenovo
 */
public class Bubble {
    
    private Body bubbleBody; // Cuerpo fÃ­sico de la burbuja
    private boolean isFloating; // Para controlar si estÃ¡ flotando

    public Bubble(World world, float x, float y) {
        // Crear la burbuja en el mundo fÃ­sico
        CircleShape bubbleShape = new CircleShape();
        bubbleShape.setRadius(2f); // Ajusta el tamaÃ±o de la burbuja

        BodyDef bubbleDef = new BodyDef();
        bubbleDef.type = BodyDef.BodyType.DynamicBody; // La burbuja serÃ¡ dinÃ¡mica para poder flotar
        bubbleDef.position.set(x, y);

        FixtureDef bubbleFixtureDef = new FixtureDef();
        bubbleFixtureDef.shape = bubbleShape;
        bubbleFixtureDef.density = 0.1f; // Baja densidad para simular flotabilidad
        bubbleFixtureDef.friction = 0f;
        bubbleFixtureDef.restitution = 0.5f;

        bubbleBody = world.createBody(bubbleDef);
        bubbleBody.createFixture(bubbleFixtureDef);

        bubbleShape.dispose(); // Liberar recursos de la forma
        isFloating = false; // Inicialmente no estÃ¡ flotando
    }

    public void applyFloatForce() {
        if (!isFloating) {
            // Aplica una fuerza hacia arriba (simula flotaciÃ³n)
            bubbleBody.applyForceToCenter(new Vector2(0, 10f), true); // Ajusta la fuerza segÃºn sea necesario
            isFloating = true; // Marcar como flotando
        }
    }

    public Body getBody() {
        return bubbleBody;
    }

}
