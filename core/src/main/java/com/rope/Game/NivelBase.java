package com.rope.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
    protected long tiempoInicio;
    protected long tiempoTotal;

    private Texture cuadroVictoriaTexture, cuadroDerrotaTexture;
    protected boolean mostrarCuadroVictoria = false, mostrarCuadroDerrota = false;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private boolean mapaLlamado = false;
    protected boolean perdidaProcesada = false;

    public void show() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        batch = new SpriteBatch();
        game = (main) Gdx.app.getApplicationListener();
    }

    public abstract void verificarCondicionesVictoria();

    public abstract void manejarVictoria();

    public abstract void verificarCondicionesPerdida();

    protected void mostrarCuadroVictoria() {
        mostrarCuadroVictoria = true;

        String idioma = IdiomaManager.getInstancia().getIdiomaActual();

        switch (idioma) {
            case "es":
                cuadroVictoriaTexture = new Texture(Gdx.files.internal("cuadro_victoria_es.png"));
                break;
            case "en":
                cuadroVictoriaTexture = new Texture(Gdx.files.internal("cuadro_victoria_en.png"));
                break;
            case "fr":
                cuadroVictoriaTexture = new Texture(Gdx.files.internal("cuadro_victoria_fr.png"));
                break;
            default:
                cuadroVictoriaTexture = new Texture(Gdx.files.internal("cuadro_victoria_es.png")); // Por defecto en español
                break;
        }

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                regresarAlMapa();
            }
        }, 1);
    }

    protected void mostrarCuadroDerrota() {
        mostrarCuadroDerrota = true;

        String idioma = IdiomaManager.getInstancia().getIdiomaActual();

        switch (idioma) {
            case "es":
                cuadroDerrotaTexture = new Texture(Gdx.files.internal("cuadro_derrota_es.png"));
                break;
            case "en":
                cuadroDerrotaTexture = new Texture(Gdx.files.internal("cuadro_derrota_en.png"));
                break;
            case "fr":
                cuadroDerrotaTexture = new Texture(Gdx.files.internal("cuadro_derrota_fr.png"));
                break;
            default:
                cuadroDerrotaTexture = new Texture(Gdx.files.internal("cuadro_derrota_es.png")); // Por defecto en español
                break;
        }

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                regresarAlMapa();
            }
        }, 1);
    }

    protected void recolectarEstrella(int starIndex) {
        puntos += 1;
        estrellasRecolectadas += 1;
    }

    protected void perderNivel() {
        nivelPerdido = true;
        reiniciarNivel();
        perdidaProcesada = true;
    }

    protected abstract void reiniciarNivel();

    @Override
    public void render(float delta) {
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        if (!nivelCompletado && !nivelPerdido && !perdidaProcesada) {
            verificarCondicionesVictoria();
            verificarCondicionesPerdida();
        } else if (nivelCompletado) {
            manejarVictoria();
        } else if (nivelPerdido) {
            reiniciarNivel();
            nivelPerdido = false;
        }

        if (mostrarCuadroVictoria) {
            batch.begin();

            batch.draw(cuadroVictoriaTexture, 180, 190, 450, 125);
            batch.end();
        }

        if (mostrarCuadroDerrota) {
            batch.begin();

            batch.draw(cuadroDerrotaTexture, 180, 190, 450, 125);
            batch.end();
        }
    }

    protected void regresarAlMapa() {
        if (!mapaLlamado) {

            if (nivelCompletado) {
                if (game != null) {
                    int nivelActual = obtenerIndiceNivelActual();
                    game.desbloquearNivel(nivelActual);
                }
            }

            game.setScreen(new mapa(game));
            mapaLlamado = true;
        }
    }

    private int obtenerIndiceNivelActual() {
        if (this instanceof Nivel1) {
            return 1;
        } else if (this instanceof Nivel2) {
            return 2;
        } else if (this instanceof Nivel3) {
            return 3;
        } else if (this instanceof Nivel4) {
            return 4;
        } else if (this instanceof Nivel5) {
            return 5;
        } else {
            return 0;
        }
    }

    @Override
    public void dispose() {
        if (cuadroVictoriaTexture != null) {
            cuadroVictoriaTexture.dispose();
        }
        if (cuadroDerrotaTexture != null) {
            cuadroDerrotaTexture.dispose();
        }
        if (batch != null) {
            batch.dispose();
        }
    }
}
