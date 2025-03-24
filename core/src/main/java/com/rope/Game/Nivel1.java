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

public class Nivel1 extends NivelBase implements Screen {

    private World world;
    private Box2DDebugRenderer debugRenderer;
    private OrthographicCamera camera;

    private final float TIMESTEP = 1 / 60f;
    private final int VELOCITYITERATIONS = 8, POSITIONITERATIONS = 3;

    private final float PIXELS_TO_METER = 32;
    private SpriteBatch batch;
    private Array<Body> tmpBodies = new Array<Body>();
    private Array<Body> bodiesToRemove;
    private Set<Integer> collidedStars = new HashSet<>();
    private Set<Body> collidedRana = new HashSet<>();

    private float time = 0f;
    private float forceMagnitude = 1.0f;
    private Dulce dulce;

    private Texture[] starTextures;
    private Vector2[] starPositions;
    private Rectangle[] starRectangles;
    private boolean[] starCollected;

    private Rana rana;
    private Body bodyBox;

    private DistanceJoint distanceJoint;
    private int puntos = 0;
    private main game;

    public Nivel1(main game) {
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

                if (isCollidingWithRana(fixtureA, fixtureB)) {
                    handleRanaCollision(rana.getBody());
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
            new Vector2(0, -1),
            new Vector2(0, -4),
            new Vector2(0, -7)
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

        dulce = new Dulce(world, 0, 3, PIXELS_TO_METER);

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
        distanceJointDef.bodyA = dulce.getBody();
        distanceJointDef.bodyB = bodyBox;
        distanceJointDef.length = 9;
        distanceJoint = (DistanceJoint) world.createJoint(distanceJointDef);

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                Vector3 worldCoordinates = camera.unproject(new Vector3(screenX, screenY, 0));
                if (isTouchingRope(worldCoordinates.x, worldCoordinates.y)) {
                    world.destroyJoint(distanceJoint);
                    distanceJoint = null;
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

    private boolean isCollidingWithRana(Fixture fixtureA, Fixture fixtureB) {
        Body bodyA = fixtureA.getBody();
        Body bodyB = fixtureB.getBody();

        if (dulce != null && dulce.getBody() != null && bodyA == dulce.getBody() && bodyB == rana.getBody()) {
            return true;
        } else if (dulce != null && dulce.getBody() != null && bodyB == dulce.getBody() && bodyA == rana.getBody()) {
            return true;
        }
        return false;
    }

    private void handleRanaCollision(Body ranaBody) {
        if (!collidedRana.contains(ranaBody)) {
            if (dulce != null && dulce.getBody() != null) {
                bodiesToRemove.add(dulce.getBody());
            }
            collidedRana.add(ranaBody);
            if (dulce != null) {
                dulce.dispose();
                dulce = null;
            }

            rana.setEatingTexture();
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    rana.setNormalTexture();
                }
            }, 0.09f);
        }
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.65f, 0.53f, 0.36f, 1);
        super.render(delta);
        world.step(TIMESTEP, VELOCITYITERATIONS, POSITIONITERATIONS);

        for (Body body : bodiesToRemove) {
            world.destroyBody(body);
        }
        bodiesToRemove.clear();

        debugRenderer.render(world, camera.combined);

        batch.setProjectionMatrix(camera.combined);

        time += delta;

        float direction = (float) Math.sin(time * 2 * Math.PI);

        if (dulce != null && dulce.getBody() != null) {
            dulce.getBody().applyForceToCenter(direction * forceMagnitude, 0, true);
            Rectangle ballRect = new Rectangle(dulce.getBody().getPosition().x - 0.5f, dulce.getBody().getPosition().y - 0.5f, 1, 1);
            for (int i = 0; i < starRectangles.length; i++) {
                if (!starCollected[i] && ballRect.overlaps(starRectangles[i])) {
                    handleStarCollision(i);
                }
            }
        }

        batch.begin();

        for (int i = 0; i < starTextures.length; i++) {
            if (!starCollected[i]) {
                batch.draw(starTextures[i], starPositions[i].x - 1, starPositions[i].y - 1, 2, 2);
            }
        }

        rana.draw(batch);

        world.getBodies(tmpBodies);
        for (Body body : tmpBodies) {
            if (body.getUserData() != null && body.getUserData() instanceof Sprite) {

                if (dulce == null || body == dulce.getBody()) {
                    if (dulce == null) {
                        continue;
                    }
                }

                Sprite sprite = (Sprite) body.getUserData();

                float spriteWidth = sprite.getWidth();
                float spriteHeight = sprite.getHeight();

                sprite.setPosition(
                        body.getPosition().x - spriteWidth / 2,
                        body.getPosition().y - spriteHeight / 2
                );

                sprite.setRotation(body.getAngle() * MathUtils.radiansToDegrees);
                sprite.draw(batch);
            }
        }

        batch.end();
    }

    private boolean isTouchingRope(float touchX, float touchY) {
        if (dulce == null || dulce.getBody() == null) {
            return false;
        }
        Vector2 ballPosition = dulce.getBody().getPosition();
        Vector2 boxPosition = bodyBox.getPosition();

        float tolerance = 0.5f;

        return (touchX > Math.min(ballPosition.x, boxPosition.x) - tolerance
                && touchX < Math.max(ballPosition.x, boxPosition.x) + tolerance
                && touchY > Math.min(ballPosition.y, boxPosition.y) - tolerance
                && touchY < Math.max(ballPosition.y, boxPosition.y) + tolerance);
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

        if (dulce != null) {
            dulce.dispose();
        }

        if (batch != null) {
            batch.dispose();
        }
    }

    @Override
    public void verificarCondicionesVictoria() {
        if (dulce == null && estrellasRecolectadas >= 1 && !nivelCompletado) {
            nivelCompletado = true;

            Usuario usuario = Usuario.getUsuarioLogueado();
            if (usuario != null) {
                int nuevoPuntaje = usuario.getPuntajeMaximo() + estrellasRecolectadas;
                usuario.setPuntajeMaximo(nuevoPuntaje);
            }
        }
    }

    @Override
    public void manejarVictoria() {
        if (!mostrarCuadroVictoria) {
            mostrarCuadroVictoria = true;
            mostrarCuadroVictoria();
            Usuario usuario = Usuario.getUsuarioLogueado();
            if (usuario != null) {
                usuario.marcarNivelComoCompletado(0);
                usuario.registrarPartidaJugada(1, estrellasRecolectadas, System.currentTimeMillis() - tiempoInicio);
            }
            if (game != null) {
                game.desbloquearNivel(1);
            }
        }
    }

    @Override
    public void verificarCondicionesPerdida() {
        if (dulce != null && dulce.getBody().getPosition().y < -18) {
            perderNivel();
        }
    }

    @Override
    protected void reiniciarNivel() {
        if (!perdidaProcesada) {
            perdidaProcesada = true;
            Usuario usuario = Usuario.getUsuarioLogueado();
            if (usuario != null) {
                usuario.registrarPartidaJugada(1, estrellasRecolectadas, System.currentTimeMillis() - tiempoInicio);
            }
            mostrarCuadroDerrota();
        }
    }
}
