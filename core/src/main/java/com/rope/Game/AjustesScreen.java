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
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class AjustesScreen implements Screen {
    private Stage stage;
    private main game;
    private SpriteBatch batch;
    private Texture backgroundTexture;

    public AjustesScreen(main game) {
        this.game = game;
        stage = new Stage();
        batch = new SpriteBatch();
        
        backgroundTexture = new Texture(Gdx.files.internal("preferencias.jpg"));
        
        BitmapFont font = new BitmapFont();
        font.getData().setScale(2);

        // Crear estilos para los componentes
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;

        // Crear el botón para activar/desactivar el sonido
        TextButton botonSonido = new TextButton("Sonido: ON", buttonStyle);
        botonSonido.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Alternar el estado del sonido
                game.alternarSonido();

                // Cambiar el texto del botón según el estado
                if (game.sonidoActivado) {
                    botonSonido.setText("Sonido: ON");
                } else {
                    botonSonido.setText("Sonido: OFF");
                }
            }
        });
        // Añadir el botón a la escena
        stage.addActor(botonSonido);
        botonSonido.setPosition(300, 550);  // Ajusta la posición
        
        TextButton botonRegresar = new TextButton("Regresar", buttonStyle);
        botonRegresar.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Regresar a la pantalla principal de ajustes
                game.setScreen(new SettingsScreen(game)); // Cambiar a la pantalla principal o ajustes principal
            }
        });

        // Añadir el botón de regresar a la escena
        stage.addActor(botonRegresar);
        botonRegresar.setPosition(300, 450);
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
    public void resume() {    }
}
