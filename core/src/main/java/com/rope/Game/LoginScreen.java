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
import java.io.File;

public class LoginScreen implements Screen {

    private Stage stage;
    private TextField campoUsuario;
    private TextField campoContrasena;
    private TextButton botonLogin;
    private TextButton botonRegresar;
    private main game;

    public LoginScreen(main game) {
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

        botonLogin = new TextButton("Iniciar Sesión", buttonStyle);
        botonRegresar = new TextButton("Regresar", buttonStyle);

        table.add(etiquetaUsuario).pad(10);
        table.add(campoUsuario).width(200).pad(10);
        table.row();
        table.add(etiquetaContrasena).pad(10);
        table.add(campoContrasena).width(200).pad(10);
        table.row();
        table.add(botonLogin).colspan(2).pad(20);
        table.row();
        table.add(botonRegresar).colspan(2).pad(10);

        botonLogin.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                iniciarSesion();
            }
        });

        botonRegresar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuInicio(game));
            }
        });
    }

    private void iniciarSesion() {
        String nombreUsuario = campoUsuario.getText();
        String contrasena = campoContrasena.getText();

        String rutaBase = "C:\\Users\\fdhg0\\Documents\\NetBeansProjects\\cutTheRope-master\\usuarios\\";
        String rutaArchivo = rutaBase + nombreUsuario + "\\datos_usuario.dat";
        File archivo = new File(rutaArchivo);

        if (!archivo.exists()) {
            return;
        }

        Usuario usuario = new Usuario(nombreUsuario, "", "");
        usuario.cargarUsuario();

        if (usuario.getContrasena().equals(contrasena)) {

            usuario.iniciarSesion(contrasena);

            String idiomaUsuario = usuario.getIdioma();

            IdiomaManager.getInstancia().cambiarIdioma(idiomaUsuario);

            game.iniciarMusica();

            game.setScreen(new mapa(game));
        } else {
        }
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
