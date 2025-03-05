/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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

/**
 *
 * @author Lenovo
 */
public class pruebaburbuja implements Screen {
    private World world;
    private Box2DDebugRenderer debugRenderer;
    private OrthographicCamera camera;

    private final float TIMESTEP = 1 / 60f;
    private final int VELOCITYITERATIONS = 8, POSITIONITERATIONS = 3;

    private Sprite boxSprite;
    private final float PIXELS_TO_METER = 32;  
    private SpriteBatch batch;
    private Sprite bubbleSprite;

    private Array<Body> tmpBodies = new Array<Body>();
    private Array<Body> bodiesToRemove;
    private Set<Integer> collidedStars = new HashSet<>();
    private Set<Body> collidedRana = new HashSet<>();
    private Set<Body> collidedBubbles = new HashSet<>(); // Conjunto para las burbujas que ya han colisionado
    private Body bubbleBody; // Cuerpo de la burbuja


    
   private float time = 0f;
    private float forceMagnitude = 1.0f;
    private Body ballBody;
    
    private Texture[] starTextures; // Arreglo de texturas de estrellas
    private Vector2[] starPositions; // Arreglo de posiciones de estrellas
    private Rectangle[] starRectangles;
    private boolean[] starCollected;
    
    //private Star[] star;
    private Rana rana; 
    Body bodyBox;
    
   private DistanceJoint distanceJoint;
   private int puntos = 0; 
    

    @Override
    public void show() {

       
        batch = new SpriteBatch();
        bodiesToRemove = new Array<Body>();

        // Crear el mundo de Box2D
        world = new World(new Vector2(0, -25f), true);
        debugRenderer = new Box2DDebugRenderer();
        
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Fixture fixtureA = contact.getFixtureA();
                Fixture fixtureB = contact.getFixtureB();

                // Verificar si el dulce (ballBody) esta involucrado en la colisiÃ³n con una estrella
                if (isCollidingWithStar(fixtureA, fixtureB)) {
                    // La lÃ³gica de colisiÃ³n con estrellas ahora se maneja en render()
                } else if (isCollidingWithRana(fixtureA, fixtureB)) {
                    handleRanaCollision(rana.getBody());
                }else if (isCollidingWithRana(fixtureA, fixtureB)) {
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
        
        
       /* star = new Star[3];
        
        star = new Star[]{
            new Star(world, 0, -1),
            new Star(world, 0, -4),
            new Star(world, 0, -7),
        };*/
       
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
        
        // Crear rectÃ¡ngulos para las estrellas
        starRectangles = new Rectangle[starTextures.length];
        for (int i = 0; i < starTextures.length; i++) {
            starRectangles[i] = new Rectangle(starPositions[i].x - 1, starPositions[i].y - 1, 2, 2); // Ajusta el tamaÃ±o
        }

        rana = new Rana(world, 0, -12);
        
        starCollected = new boolean[starTextures.length];
        for (int i = 0; i < starCollected.length; i++) {
            starCollected[i] = false;
        }
        
        // Configurar la cÃ¡mara
        camera = new OrthographicCamera(Gdx.graphics.getWidth() / PIXELS_TO_METER,
                                        Gdx.graphics.getHeight() / PIXELS_TO_METER);
        camera.position.set(0, 0, 0);  // Centrar la cÃ¡mara en el origen
        camera.update();

        
        bubbleSprite = new Sprite(new Texture("burbuja.png"));
        bubbleSprite.setSize(1.0f, 1.0f); // Establecer el tamaÃ±o de la burbuja
        
        BodyDef bubbleDef = new BodyDef();
        bubbleDef.type = BodyDef.BodyType.DynamicBody;
        bubbleDef.position.set(0, -10); // PosiciÃ³n inicial de la burbuja
        CircleShape bubbleShape = new CircleShape();
        bubbleShape.setRadius(0.5f); // Radio de la burbu
        
        FixtureDef bubbleFixtureDef = new FixtureDef();
        bubbleFixtureDef.shape = bubbleShape;
        bubbleFixtureDef.density = 1.0f;
        bubbleFixtureDef.restitution = 0.5f;

        bubbleBody = world.createBody(bubbleDef);
        bubbleBody.createFixture(bubbleFixtureDef);
        bubbleShape.dispose();
        
        // Definir el cuerpo - cÃ­rculo
        BodyDef ballDef = new BodyDef();
        ballDef.type = BodyDef.BodyType.DynamicBody;
        ballDef.position.set(0, 3);  // PosiciÃ³n inicial en el mundo

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
        
       
        float desiredSpriteSizeInMeters = 0.05f;  // El tamaÃ±o deseado del sprite en metros
        float spriteSizeInPixels = desiredSpriteSizeInMeters * PIXELS_TO_METER;  // Convertir a pÃ­xeles
        boxSprite.setSize(spriteSizeInPixels, spriteSizeInPixels);  // Ajustar el tamaÃ±o del sprite
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

                // Verificar si el clic ocurriÃ³ sobre la cuerda
                if (isTouchingRope(worldCoordinates.x, worldCoordinates.y)) {
                    // Romper la cuerda destruyendo el joint
                    world.destroyJoint(distanceJoint);
                    distanceJoint = null;
                }
                return true;
            }
        });
        
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Fixture fixtureA = contact.getFixtureA();
                Fixture fixtureB = contact.getFixtureB();

                // Verificar si la burbuja colide con algo
                if (isCollidingWithBubble(fixtureA, fixtureB)) {
                    handleBubbleCollision(fixtureA.getBody());
                }
            }

            @Override
            public void endContact(Contact contact) {}

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {}

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {}
        });
    
        
       
        shape.dispose();//liberar recursos
        
    }
    
    
//para detectar si se toca con una estrella
    
    private boolean isCollidingWithBubble(Fixture fixtureA, Fixture fixtureB) {
    Body bodyA = fixtureA.getBody();
    Body bodyB = fixtureB.getBody();

    // Comprobar si el body A o B es la burbuja
    if (bodyA == bubbleBody || bodyB == bubbleBody) {
        // TambiÃ©n comprueba si la otra parte de la colisiÃ³n es el confite
        if (bodyA == ballBody || bodyB == ballBody) {
            return true;
        }
    }
    return false;
}

    // Manejar la colisiÃ³n de la burbuja (desaparece al colisionar)
   // Manejar la colisiÃ³n de la burbuja (desaparece al colisionar)
private void handleBubbleCollision(Body otherBody) {
    if (!collidedBubbles.contains(bubbleBody)) {
        System.out.println("Â¡La burbuja ha colisionado!");

        // Destruir la burbuja al colisionar
        world.destroyBody(bubbleBody); 
        collidedBubbles.add(bubbleBody); // Marcar la burbuja como colisionada
        bubbleBody = null; // Remover el cuerpo de la burbuja

        // Aplicar una fuerza al confite para que "vuele" (por ejemplo, hacia arriba)
        if (ballBody != null) {
            // Puedes ajustar la magnitud y la direcciÃ³n de la fuerza
            Vector2 force = new Vector2(0, 500); // Fuerza hacia arriba
            ballBody.applyForceToCenter(force, true); // Aplicar la fuerza al centro del confite
        }
    }
}


    private boolean isCollidingWithStar(Fixture fixtureA, Fixture fixtureB) {
        // La lÃ³gica de colisiÃ³n con estrellas ahora se maneja en render()
        return false;
    }
    
    //manejar que pasa
   private void handleStarCollision(int starIndex) {
        if (!collidedStars.contains(starIndex)) {
            puntos += 1;
            System.out.println("Â¡Colision con estrella! Puntos: " + puntos);
            collidedStars.add(starIndex);
            starCollected[starIndex] = true; // Marcar la estrella como recolectada
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
        System.out.println("Â¡La rana se comio el dulce!");
        bodiesToRemove.add(ballBody);
        collidedRana.add(ranaBody);
        ballBody = null;
        boxSprite.getTexture().dispose();
        boxSprite = null;

        rana.setEatingTexture(); // Cambiar a la textura de comiendo

        // Usar un Timer para volver a la textura normal despuÃ©s de un breve retraso
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

    // Configurar la matriz de proyecciÃ³n para dibujar sprites
    batch.setProjectionMatrix(camera.combined);
    
    time += delta;

        //cambia la posicion de la cuerda
        float direction = (float) Math.sin(time * 2 * Math.PI); // Cambia cada 0.5 segundos

       if (ballBody != null) { // Verificar si ballBody es null, porque si no sale exception porque se pudo haber borrado
        Rectangle ballRect = new Rectangle(ballBody.getPosition().x - 0.5f, ballBody.getPosition().y - 0.5f, 1, 1); // RectÃ¡ngulo del dulce
        for (int i = 0; i < starRectangles.length; i++) {
            if (!starCollected[i] && ballRect.overlaps(starRectangles[i])) { // Verificar si la estrella no ha sido recolectada y hay colisiÃ³n
                handleStarCollision(i);
            }
        }
    }
     if (bubbleBody != null) {
            bubbleBody.setLinearVelocity(0, 2); // Movimiento hacia arriba
        }    
        
    // Dibujar los sprites
    batch.begin();
    
   /* for (Star s : star) {
        s.draw(batch);
    }*/
    
   for (int i = 0; i < starTextures.length; i++) {
            if (!starCollected[i]) { // Verificar si la estrella ha sido recolectada
                batch.draw(starTextures[i], starPositions[i].x - 1, starPositions[i].y - 1, 2, 2);
            }
        }
   
    rana.draw(batch);
    
    world.getBodies(tmpBodies);
    for (Body body : tmpBodies) {
        if (body.getUserData() != null && body.getUserData() instanceof Sprite) {
            
            if (body == ballBody && ballBody == null) {
                continue; // Saltar si el dulce ha sido comido
            }
            
            
            Sprite sprite = (Sprite) body.getUserData();

            // Obtener el tamaÃ±o del sprite
            float spriteWidth = sprite.getWidth();
            float spriteHeight = sprite.getHeight();

            // Ajustar la posiciÃ³n del sprite para que estÃ© centrado en el cuerpo
            sprite.setPosition(
                body.getPosition().x - spriteWidth / 2,
                body.getPosition().y - spriteHeight / 2
            );

            // Actualizar la rotaciÃ³n del sprite basado en el cuerpo de Box2D
            sprite.setRotation(body.getAngle() * MathUtils.radiansToDegrees);
            sprite.draw(batch);
        }
    }
    
    if (bubbleBody != null) {
            bubbleSprite.setPosition(bubbleBody.getPosition().x - bubbleSprite.getWidth() / 2, 
                                     bubbleBody.getPosition().y - bubbleSprite.getHeight() / 2);
            bubbleSprite.draw(batch);
   }
    
    
    batch.end();
}

private boolean isTouchingRope(float touchX, float touchY) {
        // Obtener las posiciones de los cuerpos conectados por la cuerda
        Vector2 ballPosition = ballBody.getPosition();
        Vector2 boxPosition = bodyBox.getPosition();

        //hasta donde se acepta que se toco la cuerda
        float tolerance = 0.5f;

        // Verificar si el clic estÃ¡ dentro del rango de la cuerda - entre el dulce y la caja
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
      /* for (Star s : star) {
        if (s.getBody() != null && s.getBody().getWorld() != null) {
            s.getBody().getWorld().destroyBody(s.getBody());
        }
        
    }*/
      
      if (starTextures != null) {
            for (Texture texture : starTextures) {
                if (texture != null) {
                    texture.dispose();
                }
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

    // Destruir el renderer de depuraciÃ³n
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
