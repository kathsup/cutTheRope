package com.rope.Game;

import com.badlogic.gdx.Game;
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

public class Nivel2 extends NivelBase implements Screen {

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
    private Set<Body> collidedBubbles = new HashSet<>(); // Conjunto para las burbujas que ya han colisionado
    private Bubble bubble; // Instancia de la burbuja

    private float time = 0f;
    private float forceMagnitude = 1.0f;
    private Body ballBody;

    private Texture[] starTextures; // Arreglo de texturas de estrellas
    private Vector2[] starPositions; // Arreglo de posiciones de estrellas
    private Rectangle[] starRectangles;
    private boolean[] starCollected;

    private Rana rana;
    Body bodyBox;

    private DistanceJoint distanceJoint;
    private int puntos = 0;
    private main game;  // Referencia al objeto game para manejar el estado global del juego

    // Constructor que acepta game
    public Nivel2(main game) {
        this.game = game;  // Guardar la referencia de game para usar en todo el nivel
    }
   
    
    
    @Override
    public void show() {
        
        super.show(); 
        
       // setNiveles(main.getInstance().getNiveles());
        System.out.println("Cámara inicializada: " + (camera != null));
        batch = new SpriteBatch();
        bodiesToRemove = new Array<Body>();

        // Crear el mundo de Box2D
        world = new World(new Vector2(0, -25f), true);
        debugRenderer = new Box2DDebugRenderer();

        bubble = new Bubble(world, 0, -10, new Texture("burbuja.png"));

        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Fixture fixtureA = contact.getFixtureA();
                Fixture fixtureB = contact.getFixtureB();

                // Verificar si el dulce (ballBody) está involucrado en la colisión con una estrella
                if (isCollidingWithStar(fixtureA, fixtureB)) {
                    // La lógica de colisión con estrellas ahora se maneja en render()
                } //else if (isCollidingWithRana(fixtureA, fixtureB)) {
                    //handleRanaCollision(rana.getBody());
                /*}*/ else if (isCollidingWithBubble(fixtureA, fixtureB)) {
                    handleBubbleCollision(bubble.getBody());
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
            new Vector2(0, 7),
            new Vector2(0, -2),
            new Vector2(0, -4)
        };

        // Crear rectángulos para las estrellas
        starRectangles = new Rectangle[starTextures.length];
        for (int i = 0; i < starTextures.length; i++) {
            starRectangles[i] = new Rectangle(starPositions[i].x - 1, starPositions[i].y - 1, 2, 2); // Ajusta el tamaño
        }

        rana = new Rana(world, 0, -12);

        starCollected = new boolean[starTextures.length];
        for (int i = 0; i < starCollected.length; i++) {
            starCollected[i] = false;
        }

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

        // Definir las propiedades de la fixture del círculo
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 5f; //para los otros juegos puede tener menor densidad
        fixtureDef.friction = 0.000001f;
        fixtureDef.restitution = 0.000001f;

        // Crear el cuerpo del círculo en el mundo
        ballBody = world.createBody(ballDef);
        ballBody.createFixture(fixtureDef);

        // textura y crear el sprite
        boxSprite = new Sprite(new Texture("dulce.png"));

        float desiredSpriteSizeInMeters = 0.05f;  // El tamaño deseado del sprite en metros
        float spriteSizeInPixels = desiredSpriteSizeInMeters * PIXELS_TO_METER;  // Convertir a píxeles
        boxSprite.setSize(spriteSizeInPixels, spriteSizeInPixels);  // Ajustar el tamaño del sprite
        boxSprite.setOrigin(boxSprite.getWidth() / 2, boxSprite.getHeight() / 2); //centrar el sprite porque si no sale volando en las colisiones

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

        //detectar que se tocó la cuerda para soltar el dulce
     
    Gdx.input.setInputProcessor(new InputAdapter() {
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 worldCoordinates = camera.unproject(new Vector3(screenX, screenY, 0));

        // Verificar si se tocó la burbuja antes de explotarla
        if (bubble != null && bubble.isCollected() && isTouchingBubble(worldCoordinates.x, worldCoordinates.y)) {
            System.out.println("¡Clic sobre la burbuja!");
            explodeBubble();
            return true;
        }

        // Verificar si el clic ocurrió sobre la cuerda
        if (isTouchingRope(worldCoordinates.x, worldCoordinates.y)) {
            if (distanceJoint != null) {
                System.out.println("Destruyendo la cuerda...");
                world.destroyJoint(distanceJoint);
                distanceJoint = null; // Establecer la referencia a null
            } else {
                System.out.println("La cuerda ya ha sido destruida.");
            }
        }
        return true;
    }
});
    }
// Método para verificar si el clic está sobre la burbuja
private boolean isTouchingBubble(float touchX, float touchY) {
    if (bubble == null) {
        System.out.println("La burbuja es null.");
        return false;
    }

    // Obtener la posición de la burbuja
    Vector2 bubblePos = bubble.getBody().getPosition();
    float bubbleRadius = 1.5f; // Ajusta esto al tamaño real de la burbuja (debe coincidir con BUBBLE_RADIUS)

    // Calcular la distancia entre el clic y el centro de la burbuja
    float distance = (float) Math.sqrt(Math.pow(touchX - bubblePos.x, 2) + Math.pow(touchY - bubblePos.y, 2));

    // Mensajes de depuración
    System.out.println("Posición del clic: (" + touchX + ", " + touchY + ")");
    System.out.println("Posición de la burbuja: (" + bubblePos.x + ", " + bubblePos.y + ")");
    System.out.println("Distancia al centro de la burbuja: " + distance);

    // Verificar si el clic está dentro del área de la burbuja
    boolean isTouching = distance <= bubbleRadius;
    System.out.println("¿El clic está sobre la burbuja? " + isTouching);
    return isTouching;
}

    private boolean isCollidingWithBubble(Fixture fixtureA, Fixture fixtureB) {
        if (bubble == null) {
        return false; // Si la burbuja es null, no hay colisión
    }
        
        Body bodyA = fixtureA.getBody();
        Body bodyB = fixtureB.getBody();

        // Comprobar si el body A o B es la burbuja
        if (bodyA == bubble.getBody() || bodyB == bubble.getBody()) {
            // También comprueba si la otra parte de la colisión es el confite
            if (bodyA == ballBody || bodyB == ballBody) {
                return true;
            }
        }
        return false;
    }

    private void handleBubbleCollision(Body bubbleBody) {
        if (!collidedBubbles.contains(bubbleBody)) {
            System.out.println("¡La burbuja ha sido recolectada!");

            // Marcar la burbuja como recolectada
            bubble.setCollected(true);
            bubble.applyFloatForce(); // Hacer que la burbuja flote hacia arriba

            // Detener el movimiento del dulce
            ballBody.setLinearVelocity(0, 0);
        }
    }

    private void explodeBubble() {
        if (bubble != null) {
            System.out.println("¡La burbuja ha explotado!");

            // Destruir la burbuja
            world.destroyBody(bubble.getBody());
            bubble.dispose();
            bubble = null;

            // Permitir que el dulce caiga
            ballBody.setLinearVelocity(0, -5); // Aplicar una velocidad hacia abajo
        }
    }

    private boolean isCollidingWithStar(Fixture fixtureA, Fixture fixtureB) {
        // La lógica de colisión con estrellas ahora se maneja en render()
        return false;
    }

    private void handleStarCollision(int starIndex) {
        if (!collidedStars.contains(starIndex)) {
            puntos += 1;
            estrellasRecolectadas += 1; 
            System.out.println("¡Colisión con estrella! Puntos: " + puntos);
            collidedStars.add(starIndex);
            starCollected[starIndex] = true; // Marcar la estrella como recolectada
        }
    }

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

 /*private void handleRanaCollision(Body ranaBody) {
    if (!collidedRana.contains(ranaBody)) {
        System.out.println("¡La rana se comió el dulce!");
        bodiesToRemove.add(ballBody); // Marcar el confite para eliminarlo
        collidedRana.add(ranaBody);  // Evitar múltiples colisiones

        // Liberar recursos del sprite del dulce
        if (boxSprite != null) {
            boxSprite.getTexture().dispose();
            boxSprite = null;
        }

        // Establecer ballBody en null para evitar usos posteriores
        ballBody = null;

        // Cambiar la textura de la rana
        rana.setEatingTexture();

        // Programar un retraso para volver a la textura normal
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                rana.setNormalTexture();
            }
        }, 0.09f); // Duración de la animación de comer
    }
}*/

   @Override
public void render(float delta) {
    // Limpiar la pantalla
    ScreenUtils.clear(0, 0, 0, 1);
    super.render(delta);

    // Actualizar el mundo de Box2D
    world.step(TIMESTEP, VELOCITYITERATIONS, POSITIONITERATIONS);

    // Dibujar el debug de Box2D
    debugRenderer.render(world, camera.combined);

    // Configurar la matriz de proyección para dibujar sprites
    batch.setProjectionMatrix(camera.combined);

    // Dibujar los sprites
    batch.begin();

    // Dibujar la burbuja
    if (bubble != null) {
        bubble.updateSprite();
        bubble.getSprite().draw(batch);

        // Si la burbuja está recolectada, mover el dulce junto con la burbuja
        if (bubble.isCollected() && ballBody != null) {
            ballBody.setTransform(bubble.getBody().getPosition(), 0);
            ballBody.setLinearVelocity(0, 2); // Velocidad del dulce (igual a la burbuja)
        }
    }

    // Dibujar el dulce solo si ballBody no es null
    if (ballBody != null && boxSprite != null) {
        boxSprite.setPosition(
            ballBody.getPosition().x - boxSprite.getWidth() / 2,
            ballBody.getPosition().y - boxSprite.getHeight() / 2
        );
        boxSprite.setRotation(ballBody.getAngle() * MathUtils.radiansToDegrees);
        boxSprite.draw(batch);
    }

    // Dibujar estrellas (si las tienes)
    for (int i = 0; i < starTextures.length; i++) {
        if (!starCollected[i]) {
            batch.draw(starTextures[i], starPositions[i].x - 1, starPositions[i].y - 1, 2, 2);
        }
    }

    // Dibujar la rana (si la tienes)
    if (rana != null) {
        rana.draw(batch);
    }

    batch.end();

    // Verificar colisiones con la burbuja
    if (bubble != null && ballBody != null) {
        Vector2 bubblePos = bubble.getBody().getPosition();
        Vector2 ballPos = ballBody.getPosition();

        // Verificar si la burbuja colisiona con el dulce
        if (bubblePos.dst(ballPos) < 1.0f && !bubble.isCollected()) { // Distancia de colisión
            handleBubbleCollision(bubble.getBody());
        }
    }

    // Verificar colisiones con las estrellas
    if (ballBody != null) {
        for (int i = 0; i < starRectangles.length; i++) {
            if (!starCollected[i] && starRectangles[i].contains(ballBody.getPosition().x, ballBody.getPosition().y)) {
                handleStarCollision(i); // Recolectar la estrella
            }
        }
    }

    // Verificar colisión con la rana
    if (rana != null && ballBody != null) {
        Vector2 ranaPos = rana.getBody().getPosition();
        Vector2 ballPos = ballBody.getPosition();

        // Verificar si el dulce colisiona con la rana
        //if (ranaPos.dst(ballPos) < 1.0f) { // Distancia de colisión
          //  handleRanaCollision(rana.getBody());
        //}
        
        
        
    }

    // Verificar clic del usuario para explotar la burbuja
    if (Gdx.input.justTouched() && bubble != null && bubble.isCollected()) {
        explodeBubble();
    }

    // Eliminar cuerpos marcados para eliminación
    for (Body body : bodiesToRemove) {
        world.destroyBody(body);
    }
    bodiesToRemove.clear();
}

    private boolean isTouchingRope(float touchX, float touchY) {
    // Verificar si ballBody es null
    if (ballBody == null) {
        return false;
    }

    // Obtener las posiciones de los cuerpos conectados por la cuerda
    Vector2 ballPosition = ballBody.getPosition();
    Vector2 boxPosition = bodyBox.getPosition();

    // Hasta donde se acepta que se tocó la cuerda
    float tolerance = 0.5f;

    // Verificar si el clic está dentro del rango de la cuerda - entre el dulce y la caja
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

        // Destruir la burbuja
        if (bubble != null) {
            bubble.dispose();
        }
    }

    @Override
    public void verificarCondicionesVictoria() {
        if (ballBody != null && rana != null) {
        Vector2 ranaPos = rana.getBody().getPosition();
        Vector2 ballPos = ballBody.getPosition();

        // Verificar si el dulce colisiona con la rana
        if (ranaPos.dst(ballPos) < 2.0f && estrellasRecolectadas >= 1) { // Distancia de colisión
            System.out.println("¡La rana se comió el dulce!");
            
            // Liberar recursos del sprite del dulce
            if (boxSprite != null) {
                boxSprite.getTexture().dispose();
                boxSprite = null;
            }
            
            world.destroyBody(ballBody);
            ballBody = null; // Marcar el dulce como comido
            nivelCompletado = true;
            System.out.println("¡Nivel 2 completado!");
            
            // Cambiar la textura de la rana
            rana.setEatingTexture();

            // Programar un retraso para volver a la textura normal
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    rana.setNormalTexture();
                }
            }, 0.09f); // Duración de la animación de comer
        }
    }
    }

    @Override
    public void manejarVictoria() {
        mostrarCuadroVictoria();
        if (game != null && game.getScreen() instanceof mapa) {
        game.desbloquearNivel(2);  // Desbloquear el Nivel 2 (índice 1)
    }
    }

    @Override
    public void verificarCondicionesPerdida() {
        if (ballBody != null && (ballBody.getPosition().y < -15 || ballBody.getPosition().y > 18)) { // Ajusta los límites según sea necesario
        perderNivel();
    }
    }

    @Override
    protected void reiniciarNivel() {
        
        //System.out.println("Reiniciando Nivel 2...");
        mostrarCuadroDerrota();// Recargar la pantalla del nivel 1
        
        
  
    }
    
}