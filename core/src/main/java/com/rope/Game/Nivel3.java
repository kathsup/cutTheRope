package com.rope.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.DistanceJoint;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer;
import java.util.HashSet;
import java.util.Set;

public class Nivel3 extends NivelBase implements Screen {

    private World world;
    private Box2DDebugRenderer debugRenderer;
    private OrthographicCamera camera;

    private final float TIMESTEP = 1 / 60f;
    private final int VELOCITYITERATIONS = 8, POSITIONITERATIONS = 3;

    private Sprite boxSprite;
    private final float PIXELS_TO_METER = 32;
    private SpriteBatch batch;

    private Array<Body> tmpBodies = new Array<Body>();
    private Array<Body> bodiesToRemove;
    private Set<Integer> collidedStars = new HashSet<>();
    private Set<Body> collidedRana = new HashSet<>();
    private Set<Body> collidedBubbles = new HashSet<>();
    private Bubble bubble1;

    private float time = 0f;
    private float forceMagnitude = 1.0f;
    private Body ballBody;

    private Texture[] starTextures;
    private Vector2[] starPositions;
    private Rectangle[] starRectangles;
    private boolean[] starCollected;

    private Rana rana;
    private Body bodyBox;
    private Body bodyBox2;

    private DistanceJoint distanceJoint;
    private DistanceJoint distanceJoint2;

    private Spike spike;
    private SpikeMover spikeMover;

    private int puntos = 0;
    private main game;

    public Nivel3(main game) {
        this.game = game;
    }

    @Override
    public void show() {
        super.show();

        batch = new SpriteBatch();
        bodiesToRemove = new Array<Body>();

        tiempoInicio = System.currentTimeMillis();

        world = new World(new Vector2(0, -25f), true);
        debugRenderer = new Box2DDebugRenderer();

        bubble1 = new Bubble(world, 0, 0, new Texture("burbuja.png"));

        spike = new Spike(world, 0, -5);
        spikeMover = new SpikeMover(spike, 0.2f, -5f, 5f, world);
        spikeMover.start();

        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Fixture fixtureA = contact.getFixtureA();
                Fixture fixtureB = contact.getFixtureB();

                if (isCollidingWithBubble(fixtureA, fixtureB)) {
                    handleBubbleCollision(bubble1.getBody());
                }

            }

            @Override
            public void endContact(Contact contact) {
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {
            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {
            }
        });

        starTextures = new Texture[]{
            new Texture("estrella.png"),
            new Texture("estrella.png"),
            new Texture("estrella.png")
        };
        starPositions = new Vector2[]{
            new Vector2(0, 0),
            new Vector2(0, 10),
            new Vector2(0, -8)
        };

        starRectangles = new Rectangle[starTextures.length];
        for (int i = 0; i < starTextures.length; i++) {
            starRectangles[i] = new Rectangle(starPositions[i].x - 1, starPositions[i].y - 1, 2, 2);
        }

        rana = new Rana(world, 0, -12);

        starCollected = new boolean[starTextures.length];
        for (int i = 0; i < starCollected.length; i++) {
            starCollected[i] = false;
        }

        camera = new OrthographicCamera(Gdx.graphics.getWidth() / PIXELS_TO_METER,
                Gdx.graphics.getHeight() / PIXELS_TO_METER);
        camera.position.set(0, 0, 0);
        camera.update();

        BodyDef ballDef = new BodyDef();
        ballDef.type = BodyDef.BodyType.DynamicBody;
        ballDef.position.set(4, 6);

        CircleShape shape = new CircleShape();
        shape.setRadius(0.5f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 18f;
        fixtureDef.friction = 0.8f;
        fixtureDef.restitution = 0f;

        ballBody = world.createBody(ballDef);
        ballBody.createFixture(fixtureDef);

        boxSprite = new Sprite(new Texture("dulce.png"));

        float desiredSpriteSizeInMeters = 0.05f;
        float spriteSizeInPixels = desiredSpriteSizeInMeters * PIXELS_TO_METER;
        boxSprite.setSize(spriteSizeInPixels, spriteSizeInPixels);
        boxSprite.setOrigin(boxSprite.getWidth() / 2, boxSprite.getHeight() / 2);

        ballBody.setUserData(boxSprite);

        BodyDef bodyDef = new BodyDef();
        FixtureDef fixtureDEF = new FixtureDef();

        bodyDef.position.x = 5;
        bodyDef.position.y = 1;
        PolygonShape box = new PolygonShape();
        box.setAsBox(.25f, .25f);
        fixtureDEF.shape = box;

        bodyBox = world.createBody(bodyDef);
        bodyBox.createFixture(fixtureDEF);
        box.dispose();

        DistanceJointDef distanceJointDef = new DistanceJointDef();
        distanceJointDef.bodyA = ballBody;
        distanceJointDef.bodyB = bodyBox;
        distanceJointDef.length = 3;
        distanceJoint = (DistanceJoint) world.createJoint(distanceJointDef);

        BodyDef bodyDef2 = new BodyDef();
        FixtureDef fixtureDEF2 = new FixtureDef();

        bodyDef2.position.x = -1;
        bodyDef2.position.y = 6;
        PolygonShape box2 = new PolygonShape();
        box2.setAsBox(.25f, .25f);
        fixtureDEF2.shape = box2;

        bodyBox2 = world.createBody(bodyDef2);
        bodyBox2.createFixture(fixtureDEF2);
        box2.dispose();

        DistanceJointDef distanceJointDef2 = new DistanceJointDef();
        distanceJointDef2.bodyA = ballBody;
        distanceJointDef2.bodyB = bodyBox2;
        distanceJointDef2.length = 6;
        distanceJoint2 = (DistanceJoint) world.createJoint(distanceJointDef2);

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                Vector3 worldCoordinates = camera.unproject(new Vector3(screenX, screenY, 0));

                if (bubble1 != null && bubble1.isCollected() && isTouchingBubble(worldCoordinates.x, worldCoordinates.y)) {
                    explodeBubble();
                    return true;
                }

                int touchedRope = isTouchingRope(worldCoordinates.x, worldCoordinates.y);
                if (touchedRope != -1) {
                    if (touchedRope == 1 && distanceJoint != null) {
                        world.destroyJoint(distanceJoint);
                        distanceJoint = null;
                    } else if (touchedRope == 2 && distanceJoint2 != null) {
                        world.destroyJoint(distanceJoint2);
                        distanceJoint2 = null;
                    } else {
                    }
                }
                return true;
            }
        });
    }

    private boolean isTouchingBubble(float touchX, float touchY) {
        if (bubble1 == null) {
            return false;
        }

        Vector2 bubblePos = bubble1.getBody().getPosition();
        float bubbleRadius = 1.5f;

        float distance = (float) Math.sqrt(Math.pow(touchX - bubblePos.x, 2) + Math.pow(touchY - bubblePos.y, 2));

        boolean isTouching = distance <= bubbleRadius;
        return isTouching;
    }

    private boolean isCollidingWithBubble(Fixture fixtureA, Fixture fixtureB) {
        if (bubble1 == null) {
            return false;
        }

        Body bodyA = fixtureA.getBody();
        Body bodyB = fixtureB.getBody();

        if (bodyA == bubble1.getBody() || bodyB == bubble1.getBody()) {
            if (bodyA == ballBody || bodyB == ballBody) {
                return true;
            }
        }
        return false;
    }

    private void handleBubbleCollision(Body bubbleBody) {
        if (!collidedBubbles.contains(bubbleBody)) {

            bubble1.setCollected(true);
            bubble1.applyFloatForce();

            ballBody.setLinearVelocity(0, 0);
        }
    }

    private void explodeBubble() {
        if (bubble1 != null) {

            world.destroyBody(bubble1.getBody());
            bubble1.dispose();
            bubble1 = null;

            ballBody.setLinearVelocity(0, -5);
        }
    }

    private void handleStarCollision(int starIndex) {
        if (!collidedStars.contains(starIndex)) {
            puntos += 1;
            estrellasRecolectadas += 1;
            collidedStars.add(starIndex);
            starCollected[starIndex] = true;
        }
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.65f, 0.53f, 0.36f, 1);
        super.render(delta);
        verificarCondicionesVictoria();
        world.step(TIMESTEP, VELOCITYITERATIONS, POSITIONITERATIONS);

        debugRenderer.render(world, camera.combined);

        batch.setProjectionMatrix(camera.combined);

        if (spikeMover != null && spike != null) {
            Vector2 newPos = spikeMover.getNewPosition();
            synchronized (world) {
                spike.getBody().setTransform(newPos, spike.getBody().getAngle());
            }
        }

        batch.begin();

        if (spike != null) {
            spike.draw(batch);
        }

        if (bubble1 != null) {
            bubble1.updateSprite();
            bubble1.getSprite().draw(batch);

            if (bubble1.isCollected() && ballBody != null) {
                ballBody.setTransform(bubble1.getBody().getPosition(), 0);
                ballBody.setLinearVelocity(0, 2);
            }
        }

        if (ballBody != null && boxSprite != null) {
            boxSprite.setPosition(
                    ballBody.getPosition().x - boxSprite.getWidth() / 2,
                    ballBody.getPosition().y - boxSprite.getHeight() / 2
            );
            boxSprite.setRotation(ballBody.getAngle() * MathUtils.radiansToDegrees);
            boxSprite.draw(batch);
        }

        for (int i = 0; i < starTextures.length; i++) {
            if (!starCollected[i]) {
                batch.draw(starTextures[i], starPositions[i].x - 1, starPositions[i].y - 1, 2, 2);
            }
        }

        if (rana != null) {
            rana.draw(batch);
        }

        batch.end();

        if (bubble1 != null && ballBody != null) {
            Vector2 bubblePos = bubble1.getBody().getPosition();
            Vector2 ballPos = ballBody.getPosition();

            if (bubblePos.dst(ballPos) < 1.0f && !bubble1.isCollected()) {
                handleBubbleCollision(bubble1.getBody());
            }
        }

        if (ballBody != null) {
            for (int i = 0; i < starRectangles.length; i++) {
                if (!starCollected[i] && starRectangles[i].contains(ballBody.getPosition().x, ballBody.getPosition().y)) {
                    handleStarCollision(i);
                }
            }
        }

        if (rana != null && ballBody != null) {
            Vector2 ranaPos = rana.getBody().getPosition();
            Vector2 ballPos = ballBody.getPosition();

        }

        if (Gdx.input.justTouched() && bubble1 != null && bubble1.isCollected()) {
            explodeBubble();
        }

        for (Body body : bodiesToRemove) {
            world.destroyBody(body);
        }
        bodiesToRemove.clear();
    }

    private int isTouchingRope(float touchX, float touchY) {
        if (ballBody == null || bodyBox == null || bodyBox2 == null) {
            return -1;
        }

        Vector2 ballPosition = ballBody.getPosition();
        Vector2 boxPosition = bodyBox.getPosition();
        Vector2 box2Position = bodyBox2.getPosition();

        float tolerance = 0.5f;

        if (touchX > Math.min(ballPosition.x, boxPosition.x) - tolerance
                && touchX < Math.max(ballPosition.x, boxPosition.x) + tolerance
                && touchY > Math.min(ballPosition.y, boxPosition.y) - tolerance
                && touchY < Math.max(ballPosition.y, boxPosition.y) + tolerance) {
            return 1;
        }

        if (touchX > Math.min(ballPosition.x, box2Position.x) - tolerance
                && touchX < Math.max(ballPosition.x, box2Position.x) + tolerance
                && touchY > Math.min(ballPosition.y, box2Position.y) - tolerance
                && touchY < Math.max(ballPosition.y, box2Position.y) + tolerance) {
            return 2;
        }

        return -1;
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width / PIXELS_TO_METER;
        camera.viewportHeight = height / PIXELS_TO_METER;
        camera.update();
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        if (starTextures != null) {
            for (Texture texture : starTextures) {
                if (texture != null) {
                    texture.dispose();
                }
            }
        }

        if (spikeMover != null) {
            spikeMover.stopMoving();
            spikeMover = null;
        }

        if (spike != null) {
            spike.dispose();
            spike = null;
        }

        if (rana != null) {
            rana.dispose();
        }

        if (world != null) {
            world.dispose();
        }

        if (debugRenderer != null) {
            debugRenderer.dispose();
        }

        if (boxSprite != null && boxSprite.getTexture() != null) {
            boxSprite.getTexture().dispose();
        }

        if (batch != null) {
            batch.dispose();
        }

        if (bubble1 != null) {
            bubble1.dispose();
        }
    }

    @Override
    public void verificarCondicionesVictoria() {
        if (ballBody != null && rana != null && !nivelCompletado) {
            Vector2 ranaPos = rana.getBody().getPosition();
            Vector2 ballPos = ballBody.getPosition();

            if (ranaPos.dst(ballPos) < 2.0f && estrellasRecolectadas >= 1) {

                Usuario usuario = Usuario.getUsuarioLogueado();
                if (usuario != null) {
                    int nuevoPuntaje = usuario.getPuntajeMaximo() + estrellasRecolectadas;
                    usuario.setPuntajeMaximo(nuevoPuntaje);
                    usuario.guardarUsuario();
                }

                if (boxSprite != null) {
                    boxSprite.getTexture().dispose();
                    boxSprite = null;
                }

                world.destroyBody(ballBody);
                ballBody = null;

                nivelCompletado = true;

                rana.setEatingTexture();

                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        rana.setNormalTexture();
                    }
                }, 0.09f);
            }
        }
    }

    @Override
    public void manejarVictoria() {
        if (!mostrarCuadroVictoria) {
            mostrarCuadroVictoria();

            Usuario usuario = Usuario.getUsuarioLogueado();
            if (usuario != null) {
                usuario.marcarNivelComoCompletado(2);
                usuario.registrarPartidaJugada(3, estrellasRecolectadas, System.currentTimeMillis() - tiempoInicio);
            }

            if (game != null && game.getScreen() instanceof mapa) {
                game.desbloquearNivel(3);
            }
        }
    }

    @Override
    public void verificarCondicionesPerdida() {
        if (ballBody != null && (ballBody.getPosition().y < -15 || ballBody.getPosition().y > 18)) {
            perderNivel();
        }
    }

    @Override
    protected void reiniciarNivel() {
        if (!perdidaProcesada) {
            perdidaProcesada = true;

            Usuario usuario = Usuario.getUsuarioLogueado();
            if (usuario != null) {
                usuario.registrarPartidaJugada(3, estrellasRecolectadas, System.currentTimeMillis() - tiempoInicio);
            }

            mostrarCuadroDerrota();
        }
    }
}
