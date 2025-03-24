package com.rope.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RankingScreen implements Screen {
    private Stage stage;
    private main game;

    private List<String> rankingUsuarios; 
    private Texture backgroundTexture;

    private SpriteBatch batch;
    private String tituloRankingTexto;
    private String regresarTexto;
    private String puntajeTexto; 

    private Label tituloRanking;
    private TextButton botonRegresar;
    public RankingScreen(main game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        batch = new SpriteBatch();


        rankingUsuarios = cargarRankingUsuarios(); 
        backgroundTexture = new Texture(Gdx.files.internal("preferencias.jpg"));
        actualizarTextos();

        crearUI();
    }

    private List<String> cargarRankingUsuarios() {
        List<String> ranking = new ArrayList<>();
        String rutaBase = "C:\\Users\\fdhg0\\Documents\\NetBeansProjects\\cutTheRope-master\\usuarios\\";
        File carpetaUsuarios = new File(rutaBase);

        if (carpetaUsuarios.exists() && carpetaUsuarios.isDirectory()) {
            for (File carpetaUsuario : carpetaUsuarios.listFiles()) {
                if (carpetaUsuario.isDirectory()) {
                    String rutaArchivo = carpetaUsuario.getAbsolutePath() + "\\datos_usuario.dat";
                    File archivoDatos = new File(rutaArchivo);

                    if (archivoDatos.exists()) {
                        Usuario usuario = new Usuario(carpetaUsuario.getName(), "", ""); 
                        usuario.cargarUsuario();

                        ranking.add(usuario.getNombreUsuario() + ":" + usuario.getPuntajeMaximo());
                    } else {
                    }
                }
            }
        } else {
        }

        ordenarRanking(ranking);

        return ranking;
    }

    private void ordenarRanking(List<String> ranking) {
        int n = ranking.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                int puntaje1 = Integer.parseInt(ranking.get(j).split(":")[1]);
                int puntaje2 = Integer.parseInt(ranking.get(j + 1).split(":")[1]);

                if (puntaje1 < puntaje2) {
                    String temp = ranking.get(j);
                    ranking.set(j, ranking.get(j + 1));
                    ranking.set(j + 1, temp);
                }
            }
        }
    }

    private void crearUI() {
        BitmapFont font = new BitmapFont();
        font.getData().setScale(2);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = com.badlogic.gdx.graphics.Color.WHITE;

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;

        tituloRanking = new Label(tituloRankingTexto, labelStyle);
        tituloRanking.setPosition(
            (Gdx.graphics.getWidth() - tituloRanking.getWidth()) / 2, 
            Gdx.graphics.getHeight() - 100 
        );
        stage.addActor(tituloRanking);

        float yPos = Gdx.graphics.getHeight() - 150; 
        for (int i = 0; i < rankingUsuarios.size(); i++) {
            String[] usuarioInfo = rankingUsuarios.get(i).split(":");
            String nombreUsuario = usuarioInfo[0];
            int puntajeMaximo = Integer.parseInt(usuarioInfo[1]);

            Label labelUsuario = new Label((i + 1) + ". " + nombreUsuario + " - " + puntajeTexto + ": " + puntajeMaximo, labelStyle);
            labelUsuario.setPosition(100, yPos); // Posicionar manualmente
            stage.addActor(labelUsuario);

            yPos -= 30; // Espacio entre usuarios
        }

        // Botón para regresar
        botonRegresar = new TextButton(regresarTexto, buttonStyle);
        botonRegresar.setPosition(
            (Gdx.graphics.getWidth() - botonRegresar.getWidth()) / 2, // Centrar horizontalmente
            50 // Posicionar en la parte inferior
        );
        botonRegresar.addListener(new com.badlogic.gdx.scenes.scene2d.utils.ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                game.setScreen(new SettingsScreen(game)); // Volver a la pantalla de ajustes
            }
        });
        stage.addActor(botonRegresar);
    }

    // Método para actualizar los textos según el idioma
    private void actualizarTextos() {
        Usuario usuario = Usuario.getUsuarioLogueado();
        if (usuario != null) {
            String idioma = usuario.getIdioma();
            switch (idioma) {
                case "es":
                    tituloRankingTexto = "Ranking de Jugadores";
                    regresarTexto = "Regresar";
                    puntajeTexto = "Puntaje"; // Texto en español
                    break;
                case "en":
                    tituloRankingTexto = "Player Ranking";
                    regresarTexto = "Back";
                    puntajeTexto = "Score"; // Texto en inglés
                    break;
                case "fr":
                    tituloRankingTexto = "Classement des Joueurs";
                    regresarTexto = "Retour";
                    puntajeTexto = "Score"; // Texto en francés
                    break;
            }
        }

        // Actualizar los textos de los componentes de la interfaz
        if (tituloRanking != null) {
            tituloRanking.setText(tituloRankingTexto);
        }
        if (botonRegresar != null) {
            botonRegresar.setText(regresarTexto);
        }
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
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        backgroundTexture.dispose();
        stage.dispose();
    }
}