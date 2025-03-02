package com.rope.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
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


public class Nivel1 implements Screen{

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
    private Set<Body> collidedStars = new HashSet<>();
    private Set<Body> collidedRana = new HashSet<>();

    
   private float time = 0f;
    private float forceMagnitude = 1.0f;
    private Body ballBody;
    private Star[] star;
    private Rana rana; 
    Body bodyBox;
    
   private DistanceJoint distanceJoint;
   private int puntos = 0; 
    

    @Override
    public void show() {

       
        batch = new SpriteBatch();
        bodiesToRemove = new Array<Body>();

        // Crear el mundo de Box2D
        world = new World(new Vector2(0, -55f), true);
        debugRenderer = new Box2DDebugRenderer();
        
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Fixture fixtureA = contact.getFixtureA();
                Fixture fixtureB = contact.getFixtureB();

                // Verificar si el dulce (ballBody) esta involucrado en la colisión con una estrella
                if (isCollidingWithStar(fixtureA, fixtureB)) {
                    
                    if (fixtureA.getBody().getUserData() instanceof Sprite && fixtureB.getBody().getUserData() instanceof Star) {
                        handleStarCollision(fixtureB.getBody());
                    } else if (fixtureA.getBody().getUserData() instanceof Star && fixtureB.getBody().getUserData() instanceof Sprite) {
                        handleStarCollision(fixtureA.getBody());
                    }
                } else if (isCollidingWithRana(fixtureA, fixtureB)) {
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
        
        
        star = new Star[3];
        
        star = new Star[]{
            new Star(world, 0, -1),
            new Star(world, 0, -4),
            new Star(world, 0, -7),
        };

        rana = new Rana(world, 0, -12);
        
        
        // Configurar la cámara
        camera = new OrthographicCamera(Gdx.graphics.getWidth() / PIXELS_TO_METER,
                                        Gdx.graphics.getHeight() / PIXELS_TO_METER);
        camera.position.set(0, 0, 0);  // Centrar la cámara en el origen
        camera.update();

        // Definir el cuerpo - círculo
        BodyDef ballDef = new BodyDef();
        ballDef.type = BodyDef.BodyType.DynamicBody;
        ballDef.position.set(0, 3);  // Posición inicial en el mundo

        // Definir la forma 
        CircleShape shape = new CircleShape();
        shape.setRadius(0.5f);  

        // Definir las propiedades de la fixture del circulo
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 5f; //para los otros juegos puede tener menor densidad
        fixtureDef.friction = 0.000001f;
        fixtureDef.restitution = 0.000001f;

        // Crear el cuerpo del circulo en el mundo
        ballBody = world.createBody(ballDef);
        ballBody.createFixture(fixtureDef);

        // textura y crear el sprite
        boxSprite = new Sprite(new Texture("dulce.png"));
        
       
        float desiredSpriteSizeInMeters = 0.08f;  // El tamaño deseado del sprite en metros
        float spriteSizeInPixels = desiredSpriteSizeInMeters * PIXELS_TO_METER;  // Convertir a píxeles
        boxSprite.setSize(spriteSizeInPixels, spriteSizeInPixels);  // Ajustar el tamaño del sprite
        boxSprite.setOrigin(boxSprite.getWidth()/2, boxSprite.getHeight()/2); //centrar el sprite porque si no sale volando en las colisiones

        // Asignar el sprite al cuerpo 
        ballBody.setUserData(boxSprite);

        //caja
        BodyDef bodyDef = new BodyDef();
        FixtureDef fixtureDEF = new FixtureDef();
        
        bodyDef.position.y = 12; 
        PolygonShape box = new PolygonShape();
        box.setAsBox(.25f, .25f);
        fixtureDEF.shape = box; 
        
        bodyBox = world.createBody(bodyDef);
        bodyBox.createFixture(fixtureDEF);
        box.dispose();
        
        //distancia entre el punto y dulce- hacer cuerda
        DistanceJointDef distanceJointDef = new DistanceJointDef();
        distanceJointDef.bodyA = ballBody; 
        distanceJointDef.bodyB = bodyBox; 
        distanceJointDef.length = 9; 
        distanceJoint = (DistanceJoint) world.createJoint(distanceJointDef);
        
        
        //detectar que se toco la cuerda para soltar el dulce
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
               
                Vector3 worldCoordinates = camera.unproject(new Vector3(screenX, screenY, 0));

                // Verificar si el clic ocurrió sobre la cuerda
                if (isTouchingRope(worldCoordinates.x, worldCoordinates.y)) {
                    // Romper la cuerda destruyendo el joint
                    world.destroyJoint(distanceJoint);
                    distanceJoint = null;
                }
                return true;
            }
        });
        
        
        
       
        shape.dispose();//liberar recursos
        
    }
    
    
//para detectar si se toca con una estrella
    private boolean isCollidingWithStar(Fixture fixtureA, Fixture fixtureB) {
    Body bodyA = fixtureA.getBody();
    Body bodyB = fixtureB.getBody();

    if (bodyA.getUserData() instanceof Sprite && bodyB.getUserData() instanceof Star) {
        handleStarCollision(bodyB);
        return true;
    } else if (bodyA.getUserData() instanceof Star && bodyB.getUserData() instanceof Sprite) {
        handleStarCollision(bodyA); 
        return true;
    }
    return false;
    }
    
    //manejar que pasa
    private void handleStarCollision(Body starBody) {
    if (!collidedStars.contains(starBody)) {
        puntos += 1;
        System.out.println("¡Colision con estrella! Puntos: " + puntos);
        bodiesToRemove.add(starBody);
        collidedStars.add(starBody); // Marcar la estrella como colisionada para luego borrarla
       
        
        // Eliminar la estrella del arreglo star
        for (int i = 0; i < star.length; i++) {
            if (star[i].getBody() == starBody) {
                star[i] = null;
                break;
            }
        }
        // Reconstruir el array de stars sin los nulls porque si no hay exceptions
        Star[] newStar = new Star[star.length -1 ];
        int j = 0;
        for (int i = 0; i < star.length; i++){
            if(star[i] != null){
                newStar[j] = star[i];
                j++;
            }
        }
        star = newStar;
    }
}
   
    //lo mismo de la estrella pero con la rana
    private boolean isCollidingWithRana(Fixture fixtureA, Fixture fixtureB) {
    Body bodyA = fixtureA.getBody();
    Body bodyB = fixtureB.getBody();

    if (bodyA == ballBody && bodyB == rana.getBody()) {
        return true;
    } else if (bodyB == ballBody && bodyA == rana.getBody()) {
        return true;
    }
    return false;
}
    
    private void handleRanaCollision(Body ranaBody) {
    if (!collidedRana.contains(ranaBody)) {
        System.out.println("¡La rana se comio el dulce!");
        bodiesToRemove.add(ballBody);
        collidedRana.add(ranaBody);
        ballBody = null;
        boxSprite.getTexture().dispose();
        boxSprite = null;

        rana.setEatingTexture(); // Cambiar a la textura de comiendo

        // Usar un Timer para volver a la textura normal después de un breve retraso
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                rana.setNormalTexture(); // Volver a la textura normal
            }
        }, 0.09f); // lo que dura la otra imagen
    }
}
    
    
   @Override
public void render(float delta) {
    // Limpiar la pantalla
    ScreenUtils.clear(0, 0, 0, 1);

    // Actualizar el mundo de Box2D
    world.step(TIMESTEP, VELOCITYITERATIONS, POSITIONITERATIONS);
    
    for (Body body : bodiesToRemove) {
        world.destroyBody(body);
    }
    bodiesToRemove.clear();

    // Dibujar el debug de Box2D 
    debugRenderer.render(world, camera.combined);

    // Configurar la matriz de proyección para dibujar sprites
    batch.setProjectionMatrix(camera.combined);

    time += delta;

        //cambia la posicion de la cuerda
        float direction = (float) Math.sin(time * 2 * Math.PI); // Cambia cada 0.5 segundos

        if (ballBody != null) { // Verificar si ballBody es null, porque si no sale exception porque se pudo haber borrado
        ballBody.applyForceToCenter(direction * forceMagnitude, 0, true);
    }
        
        
    // Dibujar los sprites
    batch.begin();
    
    for (Star s : star) {
        s.draw(batch);
    }
    
    rana.draw(batch);
    
    world.getBodies(tmpBodies);
    for (Body body : tmpBodies) {
        if (body.getUserData() != null && body.getUserData() instanceof Sprite) {
            
            if (body == ballBody && ballBody == null) {
                continue; // Saltar si el dulce ha sido comido
            }
            
            
            Sprite sprite = (Sprite) body.getUserData();

            // Obtener el tamaño del sprite
            float spriteWidth = sprite.getWidth();
            float spriteHeight = sprite.getHeight();

            // Ajustar la posición del sprite para que esté centrado en el cuerpo
            sprite.setPosition(
                body.getPosition().x - spriteWidth / 2,
                body.getPosition().y - spriteHeight / 2
            );

            // Actualizar la rotación del sprite basado en el cuerpo de Box2D
            sprite.setRotation(body.getAngle() * MathUtils.radiansToDegrees);
            sprite.draw(batch);
        }
    }
    
    
    
    batch.end();
}

private boolean isTouchingRope(float touchX, float touchY) {
        // Obtener las posiciones de los cuerpos conectados por la cuerda
        Vector2 ballPosition = ballBody.getPosition();
        Vector2 boxPosition = bodyBox.getPosition();

        //hasta donde se acepta que se toco la cuerda
        float tolerance = 0.5f;

        // Verificar si el clic está dentro del rango de la cuerda - entre el dulce y la caja
        return (touchX > Math.min(ballPosition.x, boxPosition.x) - tolerance &&
                touchX < Math.max(ballPosition.x, boxPosition.x) + tolerance &&
                touchY > Math.min(ballPosition.y, boxPosition.y) - tolerance &&
                touchY < Math.max(ballPosition.y, boxPosition.y) + tolerance);
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
       for (Star s : star) {
        if (s.getBody() != null && s.getBody().getWorld() != null) {
            s.getBody().getWorld().destroyBody(s.getBody());
        }
        
    }

    // Destruir la rana
    if (rana != null) {
        rana.dispose();
    }

    // Destruir el mundo de Box2D
    if (world != null) {
        world.dispose();
        
    }

    // Destruir el renderer de depuración
    if (debugRenderer != null) {
        debugRenderer.dispose();
    }

    // Destruir la textura del dulce
    if (boxSprite != null && boxSprite.getTexture() != null) {
        boxSprite.getTexture().dispose();
    }

    // Destruir el batch de sprites
    if (batch != null) {
        batch.dispose();
    }
    }
}