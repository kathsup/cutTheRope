package com.rope.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import java.util.Date;

public class CrearCuentaScreen implements Screen {

    private Stage stage;
    private TextField campoUsuario;
    private TextField campoContrasena;
    private TextField campoNombreCompleto;
    private TextButton botonCrearCuenta;
    private TextButton botonRegresar;
    private main game;

    public CrearCuentaScreen(main game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        BitmapFont font = new BitmapFont();
        font.getData().setScale(2);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;

        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.font = font;
        textFieldStyle.fontColor = com.badlogic.gdx.graphics.Color.WHITE;

        com.badlogic.gdx.graphics.Pixmap pixmap = new com.badlogic.gdx.graphics.Pixmap(1, 1, com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888);
        pixmap.setColor(com.badlogic.gdx.graphics.Color.DARK_GRAY);
        pixmap.fill();
        com.badlogic.gdx.graphics.Texture texture = new com.badlogic.gdx.graphics.Texture(pixmap);
        pixmap.dispose();

        textFieldStyle.background = new com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable(new com.badlogic.gdx.graphics.g2d.TextureRegion(texture));

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Label etiquetaUsuario = new Label("Nombre de usuario:", labelStyle);
        campoUsuario = new TextField("", textFieldStyle);

        Label etiquetaContrasena = new Label("Contraseña:", labelStyle);
        campoContrasena = new TextField("", textFieldStyle);
        campoContrasena.setPasswordMode(true);
        campoContrasena.setPasswordCharacter('*');

        Label etiquetaNombreCompleto = new Label("Nombre completo:", labelStyle);
        campoNombreCompleto = new TextField("", textFieldStyle);

        botonCrearCuenta = new TextButton("Crear Cuenta", buttonStyle);
        botonRegresar = new TextButton("Regresar", buttonStyle);

        table.add(etiquetaUsuario).pad(10);
        table.add(campoUsuario).width(200).pad(10);
        table.row();
        table.add(etiquetaContrasena).pad(10);
        table.add(campoContrasena).width(200).pad(10);
        table.row();
        table.add(etiquetaNombreCompleto).pad(10);
        table.add(campoNombreCompleto).width(200).pad(10);
        table.row();
        table.add(botonCrearCuenta).colspan(2).pad(20);
        table.row();
        table.add(botonRegresar).colspan(2).pad(10);

        botonCrearCuenta.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                crearCuenta();
            }
        });

        botonRegresar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuInicio(game));
            }
        });
    }

    private void crearCuenta() {
        String usuario = campoUsuario.getText();
        String contrasena = campoContrasena.getText();
        String nombreCompleto = campoNombreCompleto.getText();

        if (usuario.isEmpty() || contrasena.isEmpty() || nombreCompleto.isEmpty()) {
            
            return;
        }

        Usuario nuevoUsuario = new Usuario(usuario, contrasena, nombreCompleto);
        nuevoUsuario.setFechaRegistro(new Date());

        boolean[] nivelesIniciales = new boolean[]{true, false, false, false, false};
        nuevoUsuario.setNivelesDesbloqueados(nivelesIniciales);

        nuevoUsuario.guardarUsuario();

        game.setScreen(new MenuInicio(game));
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.8f, 0.7f, 0.6f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
        stage.dispose();
    }
}
