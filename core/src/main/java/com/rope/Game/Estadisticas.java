package com.rope.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class Estadisticas implements Screen{
    private Stage stage;
    private main game;

    public Estadisticas(main game) {
        stage = new Stage();
        BitmapFont font = new BitmapFont();
        font.getData().setScale(2);

        // Crear estilos para los componentes
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;

        TextButton botonRegresar = new TextButton("Regresar", buttonStyle);
        botonRegresar.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
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
    public void render(float f) {
         Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        stage.act(f);
        stage.draw();
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

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
    }
    
}
