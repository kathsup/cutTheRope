package com.rope.Game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class SpikeMover extends Thread {

    private Spike spike;
    private float speed;
    private float minX;
    private float maxX;
    private boolean movingRight = true;
    private Vector2 newPosition = new Vector2();
    private World world;
    private volatile boolean running = true;

    public SpikeMover(Spike spike, float speed, float minX, float maxX, World world) {
        this.spike = spike;
        this.speed = speed;
        this.minX = minX;
        this.maxX = maxX;
        this.world = world;
        setDaemon(true);
    }

    @Override
    public void run() {
        while (running && !Thread.currentThread().isInterrupted()) {
            try {
                float currentX = spike.getBody().getPosition().x;
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

                synchronized (newPosition) {
                    newPosition.set(currentX, spike.getBody().getPosition().y);
                }

                Thread.sleep(16);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public Vector2 getNewPosition() {
        synchronized (newPosition) {
            return new Vector2(newPosition);
        }
    }

    public void stopMoving() {
        running = false;
        interrupt();
    }
}
