/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.rope.Game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Bubble {

    private Body bubbleBody; // Cuerpo físico de la burbuja
    private Sprite bubbleSprite; // Sprite para la representación visual
    private boolean isFloating; // Indica si la burbuja está flotando
    private boolean isCollected; // Indica si la burbuja ha sido recolectada

    // Constantes para configurar la burbuja
    private static final float BUBBLE_RADIUS = 1.5f; // Radio de la burbuja en metros (aumentado para cubrir el dulce)
    private static final float BUBBLE_DENSITY = 0.1f; // Densidad de la burbuja
    private static final float BUBBLE_FRICTION = 0f; // Fricción de la burbuja
    private static final float BUBBLE_RESTITUTION = 3f; // Rebote de la burbuja
    private static final float FLOAT_FORCE = 7f; // Fuerza de flotación hacia arriba

    public Bubble(World world, float x, float y, Texture bubbleTexture) {
        // Crear la forma de la burbuja
        CircleShape bubbleShape = new CircleShape();
        bubbleShape.setRadius(BUBBLE_RADIUS);

        // Definir el cuerpo físico de la burbuja
        BodyDef bubbleDef = new BodyDef();
        bubbleDef.type = BodyDef.BodyType.KinematicBody; // La burbuja es cinemática (no se ve afectada por la gravedad)
        bubbleDef.position.set(x, y); // Posición inicial

        // Definir las propiedades de la fixture
        FixtureDef bubbleFixtureDef = new FixtureDef();
        bubbleFixtureDef.shape = bubbleShape;
        bubbleFixtureDef.density = BUBBLE_DENSITY;
        bubbleFixtureDef.friction = BUBBLE_FRICTION;
        bubbleFixtureDef.restitution = BUBBLE_RESTITUTION;

        // Crear el cuerpo en el mundo
        bubbleBody = world.createBody(bubbleDef);
        bubbleBody.createFixture(bubbleFixtureDef);

        // Crear el sprite de la burbuja
        bubbleSprite = new Sprite(bubbleTexture);
        bubbleSprite.setSize(BUBBLE_RADIUS * 2, BUBBLE_RADIUS * 2); // Tamaño del sprite (aumentado)
        bubbleSprite.setOrigin(bubbleSprite.getWidth() / 2, bubbleSprite.getHeight() / 2); // Centrar el sprite

        // Liberar la forma de la burbuja
        bubbleShape.dispose();

        isFloating = false; // Inicialmente, la burbuja no está flotando
        isCollected = false; // Inicialmente, la burbuja no ha sido recolectada
    }

    /**
     * Aplica una fuerza de flotación a la burbuja.
     */
    public void applyFloatForce() {
        if (!isFloating) {
            bubbleBody.setLinearVelocity(0, FLOAT_FORCE); // Mover la burbuja hacia arriba más lentamente
            isFloating = true; // Marcar como flotando
        }
    }

    /**
     * Detiene la flotación de la burbuja.
     */
    public void stopFloating() {
        if (isFloating) {
            bubbleBody.setLinearVelocity(0, 0); // Detener el movimiento
            isFloating = false; // Marcar como no flotando
        }
    }

    /**
     * Actualiza la posición y rotación del sprite de la burbuja.
     */
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

    /**
     * Libera los recursos de la burbuja.
     */
    public void dispose() {
        if (bubbleSprite != null && bubbleSprite.getTexture() != null) {
            bubbleSprite.getTexture().dispose(); // Liberar la textura
        }
    }
}