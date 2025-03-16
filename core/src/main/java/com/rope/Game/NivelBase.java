package com.rope.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Timer;

public abstract class NivelBase implements Screen {
    
    protected World world;
    protected Dulce dulce;
    protected Rana rana;
    protected boolean nivelCompletado = false;
    protected int puntos = 0;
    protected int estrellasRecolectadas = 0;
    protected boolean nivelPerdido = false;
    public main game;
    
    
    private Texture cuadroVictoriaTexture, cuadroDerrotaTexture;
    private boolean mostrarCuadroVictoria = false, mostrarCuadroDerrota = false;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private boolean mapaLlamado = false;
    protected boolean perdidaProcesada = false;

    

    // Inicializar la cámara y el batch
    public void show() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);  // Ajusta el tamaño según lo que necesites
        batch = new SpriteBatch();
        game = (main) Gdx.app.getApplicationListener(); 
    }
    
    // Método abstracto para definir las condiciones de victoria en cada nivel
    public abstract void verificarCondicionesVictoria();

    // Método abstracto para manejar las acciones después de completar el nivel
    public abstract void manejarVictoria();

    public abstract void verificarCondicionesPerdida();
    
    // Método común para mostrar el cuadro de victoria con botones de opciones
    protected void mostrarCuadroVictoria() {
          mostrarCuadroVictoria = true;

        cuadroVictoriaTexture = new Texture(Gdx.files.internal("cuadro_victoria.png"));

        // Usar un temporizador para retrasar la llamada a regresarAlMapa()
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                regresarAlMapa();
            }
        }, 1); // Retraso de 2 segundos (ajusta según sea necesario)
    }

        protected void mostrarCuadroDerrota() {
          mostrarCuadroDerrota = true;

        cuadroDerrotaTexture = new Texture(Gdx.files.internal("cuadro_derrota.png"));

        // Usar un temporizador para retrasar la llamada a regresarAlMapa()
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                regresarAlMapa();
            }
        }, 1); // Retraso de 2 segundos (ajusta según sea necesario)
    }
     
     
    // Método para recolectar estrellas (común a todos los niveles)
    protected void recolectarEstrella(int starIndex) {
        puntos += 1;
        estrellasRecolectadas += 1;
        System.out.println("¡Estrella recolectada! Estrellas totales: " + estrellasRecolectadas);
    }

    // Método para manejar la pérdida del nivel
    protected void perderNivel() {
        nivelPerdido = true;
        reiniciarNivel();  
        perdidaProcesada = true;
    }

    protected abstract void reiniciarNivel();  // Cada nivel lo implementará según su lógica

    @Override
    public void render(float delta) {
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        
        // Verificar las condiciones de victoria en cada cuadro de renderizado
        if (!nivelCompletado && !nivelPerdido && !perdidaProcesada) {
            verificarCondicionesVictoria();  // Verificar si se completó el nivel
            verificarCondicionesPerdida();   // Verificar si se perdió el nivel
        } else if (nivelCompletado) {
            manejarVictoria();  // Manejar la victoria
        }else if (nivelPerdido){
           reiniciarNivel();
           nivelPerdido = false;
           perdidaProcesada = false; 
        }

        // Si debemos mostrar el cuadro de victoria, dibujarlo
        if (mostrarCuadroVictoria) {
            batch.begin();

            // Dibujar el cuadro de victoria
            batch.draw(cuadroVictoriaTexture, 180, 190, 450, 125);  // Ajusta la posición y tamaño del cuadro
            batch.end();
        }
        
        if (mostrarCuadroDerrota) {
            batch.begin();

            // Dibujar el cuadro de victoria
            batch.draw(cuadroDerrotaTexture, 180, 190, 450, 125);  // Ajusta la posición y tamaño del cuadro
            batch.end();
            mostrarCuadroDerrota = false;
        }
    }

   /* protected void regresarAlMapa() {
        if(!mapaLlamado){
            System.out.println("Regresando al mapa...");
            game.setScreen(new mapa(game));
            mapaLlamado = true;
        }
    }*/
    
    protected void regresarAlMapa() {
    if (!mapaLlamado) {
        System.out.println("Regresando al mapa...");
        
        // Verificar si el nivel actual está completado
        if (nivelCompletado) {
            // Desbloquear el siguiente nivel
            if (game != null) {
                // Determinar el índice del nivel actual basado en la clase del nivel
                int nivelActual = obtenerIndiceNivelActual();
                game.desbloquearNivel(nivelActual); // Desbloquear el siguiente nivel
            }
        }
        
        game.setScreen(new mapa(game));
        mapaLlamado = true;
    }
}

// Método auxiliar para obtener el índice del nivel actual
private int obtenerIndiceNivelActual() {
    if (this instanceof Nivel1) {
        return 1; // Nivel 1
    } else if (this instanceof Nivel2) {
        return 2; // Nivel 2
    } else if (this instanceof Nivel3) {
        return 3; // Nivel 3
    } else if (this instanceof Nivel4) {
        return 4; // Nivel 4
    } else if (this instanceof Nivel5) {
        return 5; // Nivel 5
    } else {
        return 0; // Nivel desconocido
    }
}


    @Override
     public void dispose() {
        // Liberar recursos
        if (cuadroVictoriaTexture != null) {
            cuadroVictoriaTexture.dispose();
        }
        if (batch != null) {
            batch.dispose();
        }
    }
}

