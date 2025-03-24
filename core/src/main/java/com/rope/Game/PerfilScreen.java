package com.rope.Game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.awt.*;
import java.io.File;
import java.text.SimpleDateFormat;

public class PerfilScreen implements Screen, IdiomaManager.IdiomaListener {

    private Stage stage;
    private main game;
    private Image avatarImage;
    private Label labelNombreUsuario, labelNombreCompleto, labelFechaRegistro;
    private TextButton botonCambiarFoto;
    private TextButton botonRegresar;
    private TextButton botonEliminarCuenta;
    private SpriteBatch batch;
    private Texture backgroundTexture;

    public PerfilScreen(main game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        batch = new SpriteBatch();
        IdiomaManager.getInstancia().agregarListener("PerfilScreen", this);
        backgroundTexture = new Texture(Gdx.files.internal("preferencias.jpg"));

        crearUI();
        actualizarTextos();
    }

    private void crearUI() {
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Usuario usuario = Usuario.getUsuarioLogueado();

        BitmapFont font = new BitmapFont();
        font.getData().setScale(2);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = Color.WHITE;

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.fontColor = Color.WHITE;

        Texture avatarTexture = new Texture(Gdx.files.internal(usuario.getAvatar()));
        avatarImage = new Image(avatarTexture);
        table.add(avatarImage).size(100, 100).pad(10).row();

        botonCambiarFoto = new TextButton("", buttonStyle);
        botonCambiarFoto.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                cambiarFotoPerfil();
            }
        });
        table.add(botonCambiarFoto).colspan(2).pad(10).row();

        labelNombreUsuario = new Label("", labelStyle);
        labelNombreCompleto = new Label("", labelStyle);
        labelFechaRegistro = new Label("", labelStyle);

        table.add(labelNombreUsuario).pad(10).row();
        table.add(labelNombreCompleto).pad(10).row();
        table.add(labelFechaRegistro).pad(10).row();

        botonRegresar = new TextButton("", buttonStyle);
        botonRegresar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SettingsScreen(game));
            }
        });
        table.add(botonRegresar).colspan(2).pad(20).row();

        botonEliminarCuenta = new TextButton("", buttonStyle);
        botonEliminarCuenta.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                eliminarCuenta();
            }
        });

        table.add(botonEliminarCuenta).colspan(2).pad(50).row();

        actualizarTextos();
    }

    private void eliminarCuenta() {
        Usuario usuario = Usuario.getUsuarioLogueado();
        if (usuario != null) {
            String rutaBase = "C:\\Users\\fdhg0\\Documents\\NetBeansProjects\\cutTheRope-master\\usuarios\\";
            String rutaCarpetaUsuario = rutaBase + usuario.getNombreUsuario();

            File carpetaUsuario = new File(rutaCarpetaUsuario);

            if (!carpetaUsuario.exists()) {
                return;
            }

            Usuario.cerrarSesion();

            if (eliminarArchivosYCarpeta(carpetaUsuario)) {

                game.setScreen(new MenuInicio(game));
            } else {
            }
        }
    }

    private boolean eliminarArchivosYCarpeta(File file) {
        if (file == null || !file.exists()) {
            return false;
        }

        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File child : files) {
                    eliminarArchivosYCarpeta(child);
                }
            }
        }

        return file.delete();
    }

    private void cambiarFotoPerfil() {
        if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
            FileDialog fileDialog = new FileDialog((Frame) null, "Seleccionar imagen", FileDialog.LOAD);
            fileDialog.setFilenameFilter((dir, name) -> name.endsWith(".png") || name.endsWith(".jpg"));
            fileDialog.setVisible(true);

            String rutaArchivo = fileDialog.getDirectory() + fileDialog.getFile();
            if (rutaArchivo != null && !rutaArchivo.isEmpty()) {
                String nombreArchivo = new File(rutaArchivo).getName();

                Usuario usuario = Usuario.getUsuarioLogueado();
                usuario.setAvatar(nombreArchivo);
                usuario.guardarUsuario();

                Texture nuevaTextura = new Texture(Gdx.files.internal(usuario.getAvatar()));
                avatarImage.setDrawable(new TextureRegionDrawable(new TextureRegion(nuevaTextura)));

            }
        } else {
        }
    }

    private void actualizarTextos() {
        Usuario usuario = Usuario.getUsuarioLogueado();
        if (usuario != null) {
            String idioma = IdiomaManager.getInstancia().getIdiomaActual();
            switch (idioma) {
                case "es":
                    labelNombreUsuario.setText("Nombre de usuario: " + usuario.getNombreUsuario());
                    labelNombreCompleto.setText("Nombre completo: " + usuario.getNombreCompleto());
                    labelFechaRegistro.setText("Fecha de creación: " + new SimpleDateFormat("dd/MM/yyyy").format(usuario.getFechaRegistro()));
                    botonCambiarFoto.setText("Cambiar Foto de Perfil");
                    botonRegresar.setText("Regresar");
                    botonEliminarCuenta.setText("Eliminar Cuenta"); // Texto para el botón de eliminar cuenta
                    break;
                case "en":
                    labelNombreUsuario.setText("Username: " + usuario.getNombreUsuario());
                    labelNombreCompleto.setText("Full name: " + usuario.getNombreCompleto());
                    labelFechaRegistro.setText("Creation date: " + new SimpleDateFormat("dd/MM/yyyy").format(usuario.getFechaRegistro()));
                    botonCambiarFoto.setText("Change Profile Picture");
                    botonRegresar.setText("Back");
                    botonEliminarCuenta.setText("Delete Account"); // Texto para el botón de eliminar cuenta
                    break;
                case "fr":
                    labelNombreUsuario.setText("Nom d'utilisateur: " + usuario.getNombreUsuario());
                    labelNombreCompleto.setText("Nom complet: " + usuario.getNombreCompleto());
                    labelFechaRegistro.setText("Date de création: " + new SimpleDateFormat("dd/MM/yyyy").format(usuario.getFechaRegistro()));
                    botonCambiarFoto.setText("Changer la photo de profil");
                    botonRegresar.setText("Retour");
                    botonEliminarCuenta.setText("Supprimer le compte"); // Texto para el botón de eliminar cuenta
                    break;
            }
        }
    }

    @Override
    public void onIdiomaCambiado(String nuevoIdioma) {
        actualizarTextos();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();
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
        IdiomaManager.getInstancia().removerListener("PerfilScreen");
        batch.dispose();
        backgroundTexture.dispose();
        stage.dispose();
    }
}
