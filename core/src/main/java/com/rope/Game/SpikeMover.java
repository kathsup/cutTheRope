package com.rope.Game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public class SpikeMover extends Thread {
    private Spike spike;
    private float speed;
    private float minX;
    private float maxX;
    private boolean movingRight = true;
    private Vector2 newPosition = new Vector2(); // Variable temporal para la nueva posición
    private World world; 

    public SpikeMover(Spike spike, float speed, float minX, float maxX, World world) {
        this.spike = spike;
        this.speed = speed;
        this.minX = minX;
        this.maxX = maxX;
        this.world = world;
    }

    
    
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) { // Verificar si el hilo fue interrumpido
            synchronized (world) { // Sincronizar con el mundo de Box2D
                Body spikeBody = spike.getBody();
                float currentX = spikeBody.getPosition().x;

                if (movingRight) {
                    currentX += speed;
                    if (currentX >= maxX) {
                        currentX = maxX;
                        movingRight = false;
                    }
                } else {
                    currentX -= speed;
                    if (currentX <= minX) {
                        currentX = minX;
                        movingRight = true;
                    }
                }

                newPosition.set(currentX, spikeBody.getPosition().y); // Guardar la nueva posición
            }

            try {
                Thread.sleep(16); // Aproximadamente 60 FPS
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restaurar el estado de interrupción
                break; // Salir del bucle
            }
        }
    }

    public Vector2 getNewPosition() {
        return newPosition;
    }
}
