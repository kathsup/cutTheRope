package com.rope.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import java.text.SimpleDateFormat;

public class PerfilScreen implements Screen, IdiomaManager.IdiomaListener {

    private Stage stage;
    private main game;
    private Image avatarImage;
    private Label labelNombreUsuario, labelNombreCompleto, labelProgreso, labelPuntaje, labelTiempo, labelRanking, labelFechaRegistro;
    private TextButton botonRegresar;

    public PerfilScreen(main game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Registrar esta pantalla en el IdiomaManager
        IdiomaManager.getInstancia().agregarListener("PerfilScreen", this);

        // Crear la interfaz de usuario
        crearUI();
    }

    private void crearUI() {
        Table table = new Table();
        table.setFillParent(true); // Hacer que la tabla ocupe toda la pantalla
        stage.addActor(table);

        // Obtener el usuario logueado
        Usuario usuario = Usuario.getUsuarioLogueado();

        // Crear una fuente básica
        BitmapFont font = new BitmapFont();

        // Crear estilos para los componentes
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = Color.WHITE;

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.fontColor = Color.WHITE;

        // Mostrar la imagen del usuario (avatar)
        Texture avatarTexture = new Texture(Gdx.files.internal(usuario.getAvatar()));
        avatarImage = new Image(avatarTexture);
        table.add(avatarImage).size(100, 100).pad(10).row();

        // Mostrar la información del usuario
        labelNombreUsuario = new Label("", labelStyle);
        labelNombreCompleto = new Label("", labelStyle);
        labelProgreso = new Label("", labelStyle);
        labelPuntaje = new Label("", labelStyle);
        labelTiempo = new Label("", labelStyle);
        labelRanking = new Label("", labelStyle);
        labelFechaRegistro = new Label("", labelStyle);

        table.add(labelNombreUsuario).pad(10).row();
        table.add(labelNombreCompleto).pad(10).row();
        table.add(labelProgreso).pad(10).row();
        table.add(labelPuntaje).pad(10).row();
        table.add(labelTiempo).pad(10).row();
        table.add(labelRanking).pad(10).row();
        table.add(labelFechaRegistro).pad(10).row();

        // Botón para regresar
        botonRegresar = new TextButton("", buttonStyle);
        botonRegresar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SettingsScreen(game));
            }
        });
        table.add(botonRegresar).colspan(2).pad(20);

        // Actualizar los textos según el idioma actual
        actualizarTextos();
    }

    // Método para actualizar los textos según el idioma
    private void actualizarTextos() {
        Usuario usuario = Usuario.getUsuarioLogueado();
        if (usuario != null) {
            String idioma = IdiomaManager.getInstancia().getIdiomaActual(); // Obtener el idioma actual
            switch (idioma) {
                case "es":
                    labelNombreUsuario.setText("Nombre de usuario: " + usuario.getNombreUsuario());
                    labelNombreCompleto.setText("Nombre completo: " + usuario.getNombreCompleto());
                    labelProgreso.setText("Progreso en el juego: " + usuario.getProgresoJuego() + " niveles");
                    labelPuntaje.setText("Puntaje máximo: " + usuario.getPuntajeMaximo());
                    labelTiempo.setText("Tiempo total jugado: " + usuario.getTiempoTotalJugado() + " segundos");
                    labelRanking.setText("Ranking: " + usuario.getRanking());
                    labelFechaRegistro.setText("Fecha de creación: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(usuario.getFechaRegistro()));
                    botonRegresar.setText("Regresar");
                    break;
                case "en":
                    labelNombreUsuario.setText("Username: " + usuario.getNombreUsuario());
                    labelNombreCompleto.setText("Full name: " + usuario.getNombreCompleto());
                    labelProgreso.setText("Game progress: " + usuario.getProgresoJuego() + " levels");
                    labelPuntaje.setText("Max score: " + usuario.getPuntajeMaximo());
                    labelTiempo.setText("Total time played: " + usuario.getTiempoTotalJugado() + " seconds");
                    labelRanking.setText("Ranking: " + usuario.getRanking());
                    labelFechaRegistro.setText("Creation date: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(usuario.getFechaRegistro()));
                    botonRegresar.setText("Back");
                    break;
                case "fr":
                    labelNombreUsuario.setText("Nom d'utilisateur: " + usuario.getNombreUsuario());
                    labelNombreCompleto.setText("Nom complet: " + usuario.getNombreCompleto());
                    labelProgreso.setText("Progrès du jeu: " + usuario.getProgresoJuego() + " niveaux");
                    labelPuntaje.setText("Score maximal: " + usuario.getPuntajeMaximo());
                    labelTiempo.setText("Temps total joué: " + usuario.getTiempoTotalJugado() + " secondes");
                    labelRanking.setText("Classement: " + usuario.getRanking());
                    labelFechaRegistro.setText("Date de création: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(usuario.getFechaRegistro()));
                    botonRegresar.setText("Retour");
                    break;
            }
        }
    }

    @Override
    public void onIdiomaCambiado(String nuevoIdioma) {
        // Actualizar los textos cuando el idioma cambie
        actualizarTextos();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
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
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        // Remover el listener cuando la pantalla se destruya
        IdiomaManager.getInstancia().removerListener("PerfilScreen");
        stage.dispose();
    }
}