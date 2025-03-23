package com.rope.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;


  public class AjustesScreen implements Screen {
    private Stage stage;
    private main game;
    private SpriteBatch batch;
    private Texture backgroundTexture;

    // Textos según el idioma
    private String textoSonido;
    private String textoRegresar;
    private String textoCambiarIdioma;

    // Referencia al botón de regresar
    private TextButton botonRegresar;

    public AjustesScreen(main game) {
        this.game = game;
        stage = new Stage();
        batch = new SpriteBatch();
        Usuario usuario = Usuario.getUsuarioLogueado();
        backgroundTexture = new Texture(Gdx.files.internal("preferencias.jpg"));

        // Inicializar textos según el idioma del usuario
        actualizarTextos();

        BitmapFont font = new BitmapFont();
        font.getData().setScale(2);

        // Crear estilos para los componentes
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        
        // Crear el botón para activar/desactivar el sonido
        TextButton botonSonido = new TextButton(textoSonido + (usuario.isSonidoActivado() ? ": ON" : ": OFF"), buttonStyle);
        botonSonido.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                
                usuario.setSonidoActivado(!usuario.isSonidoActivado());
                if (usuario.isSonidoActivado()) {
                    game.iniciarMusica();
                } else {
                    game.pausarMusica();
                }
                // Cambiar el texto del botón según el estado del usuario
                botonSonido.setText(textoSonido + (usuario.isSonidoActivado() ? ": ON" : ": OFF"));
            }
        });
        // Añadir el botón a la escena
        stage.addActor(botonSonido);
        botonSonido.setPosition(300, 550);  // Ajusta la posición

        // Crear el botón para cambiar el idioma
        TextButton botonCambiarIdioma = new TextButton(textoCambiarIdioma, buttonStyle);
        botonCambiarIdioma.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Cambiar el idioma
                cambiarIdioma();
                // Actualizar los textos de la interfaz
                actualizarTextos();
                botonSonido.setText(textoSonido + (usuario.isSonidoActivado() ? ": ON" : ": OFF"));
                botonCambiarIdioma.setText(textoCambiarIdioma);
                // Actualizar el texto del botón de regresar
                botonRegresar.setText(textoRegresar);
            }
        });
        // Añadir el botón a la escena
        stage.addActor(botonCambiarIdioma);
        botonCambiarIdioma.setPosition(300, 500);  // Ajusta la posición

        // Crear el botón para regresar
        botonRegresar = new TextButton(textoRegresar, buttonStyle);
        botonRegresar.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Regresar a la pantalla principal de ajustes
                game.setScreen(new SettingsScreen(game)); // Cambiar a la pantalla principal o ajustes principal
            }
        });
        // Añadir el botón a la escena
        stage.addActor(botonRegresar);
        botonRegresar.setPosition(300, 450);
    }

    // Método para cambiar el idioma
   private void cambiarIdioma() {
    Usuario usuario = Usuario.getUsuarioLogueado();
    if (usuario != null) {
        String nuevoIdioma;
        switch (usuario.getIdioma()) {
            case "es":
                nuevoIdioma = "en";
                break;
            case "en":
                nuevoIdioma = "fr";
                break;
            case "fr":
                nuevoIdioma = "es";
                break;
            default:
                nuevoIdioma = "es";
                break;
        }

        // Actualizar el idioma del usuario
        usuario.setIdioma(nuevoIdioma);
        usuario.guardarUsuario(); // Guardar el cambio en el archivo binario

        // Notificar al IdiomaManager para que actualice los textos
        IdiomaManager.getInstancia().cambiarIdioma(nuevoIdioma);
    }
}

    // Método para actualizar los textos según el idioma
    private void actualizarTextos() {
        Usuario usuario = Usuario.getUsuarioLogueado();
        if (usuario != null) {
            String idioma = usuario.getIdioma();
            switch (idioma) {
                case "es":
                    textoSonido = "Sonido";
                    textoRegresar = "Regresar";
                    textoCambiarIdioma = "Cambiar idioma";
                    break;
                case "en":
                    textoSonido = "Sound";
                    textoRegresar = "Back";
                    textoCambiarIdioma = "Change language";
                    break;
                case "fr":
                    textoSonido = "Son";
                    textoRegresar = "Retour";
                    textoCambiarIdioma = "Changer de langue";
                    break;
            }
        }
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        // Renderizar la escena
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        stage.dispose();
        batch.dispose();
        backgroundTexture.dispose();
    }

    @Override
    public void resize(int i, int i1) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

}