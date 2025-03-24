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

public class Nivel5 extends NivelBase implements Screen {

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

    private float time = 0f;
    private float forceMagnitude = 1.0f;
    private Body ballBody;

    private Texture[] starTextures;
    private Vector2[] starPositions;
    private Rectangle[] starRectangles;
    private boolean[] starCollected;

    private Rana rana;
    Body bodyBox;

    private DistanceJoint distanceJoint, distanceJoint2;
    private int puntos = 0;
    private main game;

    public Nivel5(main game) {
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

        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Fixture fixtureA = contact.getFixtureA();
                Fixture fixtureB = contact.getFixtureB();
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
            new Vector2(3, 0),
            new Vector2(4, -2),
            new Vector2(5, -4)
        };

        starRectangles = new Rectangle[starTextures.length];
        for (int i = 0; i < starTextures.length; i++) {
            starRectangles[i] = new Rectangle(starPositions[i].x - 1, starPositions[i].y - 1, 2, 2);
        }

        rana = new Rana(world, 8, -11);

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
        ballDef.position.set(0, 3);

        CircleShape shape = new CircleShape();
        shape.setRadius(0.5f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.1f;
        fixtureDef.restitution = 0.01f;

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

        bodyDef.position.y = 12;
        PolygonShape box = new PolygonShape();
        box.setAsBox(.25f, .25f);
        fixtureDEF.shape = box;

        bodyBox = world.createBody(bodyDef);
        bodyBox.createFixture(fixtureDEF);
        box.dispose();

        DistanceJointDef distanceJointDef = new DistanceJointDef();
        distanceJointDef.bodyA = ballBody;
        distanceJointDef.bodyB = bodyBox;
        distanceJointDef.length = 10;
        distanceJoint = (DistanceJoint) world.createJoint(distanceJointDef);

        BodyDef leftAnchorDef = new BodyDef();
        leftAnchorDef.type = BodyDef.BodyType.StaticBody;
        leftAnchorDef.position.set(-12, 12);
        Body leftAnchorBody = world.createBody(leftAnchorDef);

        DistanceJointDef distanceJointDef2 = new DistanceJointDef();
        distanceJointDef2.bodyA = ballBody;
        distanceJointDef2.bodyB = leftAnchorBody;
        distanceJointDef2.length = 10;
        distanceJoint2 = (DistanceJoint) world.createJoint(distanceJointDef2);

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                Vector3 worldCoordinates = camera.unproject(new Vector3(screenX, screenY, 0));

                int touchedRope = getTouchedRope(worldCoordinates.x, worldCoordinates.y);
                if (touchedRope != -1) {
                    switch (touchedRope) {
                        case 1:
                            if (distanceJoint != null) {
                                world.destroyJoint(distanceJoint);
                                distanceJoint = null;
                            }
                            break;
                        case 2:
                            if (distanceJoint2 != null) {
                                world.destroyJoint(distanceJoint2);
                                distanceJoint2 = null;
                            }
                            break;
                        default:
                            break;
                    }
                }
                return true;
            }
        });
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
        // Limpiar la pantalla
        ScreenUtils.clear(0.65f, 0.53f, 0.36f, 1);
        super.render(delta);

        world.step(TIMESTEP, VELOCITYITERATIONS, POSITIONITERATIONS);

        debugRenderer.render(world, camera.combined);

        batch.setProjectionMatrix(camera.combined);

        batch.begin();

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

        for (Body body : bodiesToRemove) {
            world.destroyBody(body);
        }
        bodiesToRemove.clear();
    }

    private int getTouchedRope(float touchX, float touchY) {
        if (ballBody == null) {
            return -1;
        }

        Vector2 ballPosition = ballBody.getPosition();

        float tolerance = 0.5f;

        if (distanceJoint != null) {
            Vector2 boxPosition = bodyBox.getPosition();
            if (isPointNearLine(touchX, touchY, ballPosition.x, ballPosition.y, boxPosition.x, boxPosition.y, tolerance)) {
                return 1;
            }
        }

        if (distanceJoint2 != null) {
            Vector2 leftAnchorPosition = distanceJoint2.getBodyB().getPosition();
            if (isPointNearLine(touchX, touchY, ballPosition.x, ballPosition.y, leftAnchorPosition.x, leftAnchorPosition.y, tolerance)) {
                return 2;
            }
        }

        return -1;
    }

    private boolean isPointNearLine(float px, float py, float x1, float y1, float x2, float y2, float tolerance) {
        float A = px - x1;
        float B = py - y1;
        float C = x2 - x1;
        float D = y2 - y1;

        float dot = A * C + B * D;
        float lenSq = C * C + D * D;
        float param = (lenSq != 0) ? dot / lenSq : -1;

        float xx, yy;

        if (param < 0) {
            xx = x1;
            yy = y1;
        } else if (param > 1) {
            xx = x2;
            yy = y2;
        } else {
            xx = x1 + param * C;
            yy = y1 + param * D;
        }

        float dx = px - xx;
        float dy = py - yy;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        return distance <= tolerance;
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
                usuario.marcarNivelComoCompletado(4);
                usuario.registrarPartidaJugada(5, estrellasRecolectadas, System.currentTimeMillis() - tiempoInicio);
            }
        }
    }

    @Override
    public void verificarCondicionesPerdida() {
        if (ballBody != null && (ballBody.getPosition().y < -15 || ballBody.getPosition().y > 18)) {

            perderNivel();

            perderNivel();

        }
    }

    @Override
    protected void reiniciarNivel() {
        if (!perdidaProcesada) {
            perdidaProcesada = true;

            Usuario usuario = Usuario.getUsuarioLogueado();
            if (usuario != null) {
                usuario.registrarPartidaJugada(5, estrellasRecolectadas, System.currentTimeMillis() - tiempoInicio);
            }

            mostrarCuadroDerrota();
        }
    }
}
