package com.rope.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MenuInicio implements Screen {

    private Stage stage;
    private TextButton botonLogin;
    private TextButton botonCrearCuenta;
    private Label titulo;
    private main game;

    public MenuInicio(main game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Crear la interfaz de usuario
        crearUI();
    }

    private void crearUI() {
        Table table = new Table();
        table.setFillParent(true); // Hacer que la tabla ocupe toda la pantalla
        stage.addActor(table);

        // Crear una fuente básica
        BitmapFont font = new BitmapFont();

        // Crear estilos para los componentes
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = Color.WHITE;

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.fontColor = Color.WHITE;

        // Definir textos directamente (sin usar IdiomaManager)
        titulo = new Label("Menú de Inicio", labelStyle);
        botonLogin = new TextButton("Iniciar Sesión", buttonStyle);
        botonCrearCuenta = new TextButton("Crear Cuenta", buttonStyle);

        // Agregar los componentes a la tabla
        table.add(titulo).colspan(2).padBottom(20).row();
        table.add(botonLogin).width(200).pad(10);
        table.add(botonCrearCuenta).width(200).pad(10);

        // Configurar el evento del botón de inicio de sesión
        botonLogin.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Cambiar a la pantalla de inicio de sesión
                game.setScreen(new LoginScreen(game));
            }
        });

        // Configurar el evento del botón de crear cuenta
        botonCrearCuenta.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Cambiar a la pantalla de creación de cuenta
                game.setScreen(new CrearCuentaScreen(game));
            }
        });
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage); // Habilitar el Stage para manejar la entrada
    }

    @Override
    public void render(float delta) {
        // Limpiar la pantalla
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Dibujar el Stage
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true); // Actualizar el viewport al cambiar el tamaño de la pantalla
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
    }
}