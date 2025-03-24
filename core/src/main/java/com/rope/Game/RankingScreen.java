package com.rope.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RankingScreen implements Screen {
    private Stage stage;
    private main game;
    private List<String> rankingUsuarios; // Cambiado a List<String>
    private SpriteBatch batch;
    private Texture backgroundTexture;

    public RankingScreen(main game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        batch = new SpriteBatch();

        // Cargar y ordenar el ranking de usuarios
        rankingUsuarios = cargarRankingUsuarios(); // Ahora es compatible con List<String>
        backgroundTexture = new Texture(Gdx.files.internal("preferencias.jpg"));

        // Crear la interfaz de usuario
        crearUI();
    }

    private List<String> cargarRankingUsuarios() {
    List<String> ranking = new ArrayList<>();
    String rutaBase = "C:\\Users\\Lenovo\\Desktop\\gameRope\\usuarios\\";
    File carpetaUsuarios = new File(rutaBase);

    if (carpetaUsuarios.exists() && carpetaUsuarios.isDirectory()) {
        // Recorrer todas las carpetas dentro de la carpeta de usuarios
        for (File carpetaUsuario : carpetaUsuarios.listFiles()) {
            if (carpetaUsuario.isDirectory()) {
                // Construir la ruta correcta al archivo datos_usuario.dat dentro de la carpeta del usuario
                String rutaArchivo = carpetaUsuario.getAbsolutePath() + "\\datos_usuario.dat";
                File archivoDatos = new File(rutaArchivo);

                if (archivoDatos.exists()) {
                    // Crear un usuario vacío y cargar sus datos desde el archivo
                    Usuario usuario = new Usuario(carpetaUsuario.getName(), "", ""); // Pasar el nombre de usuario al constructor
                    usuario.cargarUsuario(); // Cargar los datos desde el archivo

                    // Agregar al ranking en formato "nombreUsuario:puntajeMaximo"
                    ranking.add(usuario.getNombreUsuario() + ":" + usuario.getPuntajeMaximo());
                } else {
                    System.out.println("El archivo no existe: " + rutaArchivo);
                }
            }
        }
    } else {
        System.out.println("La carpeta de usuarios no existe: " + rutaBase);
    }

    // Ordenar el ranking antes de devolverlo
    ordenarRanking(ranking);

    return ranking;
}

    private void ordenarRanking(List<String> ranking) {
    // Implementación del algoritmo de burbuja para ordenar la lista
    int n = ranking.size();
    for (int i = 0; i < n - 1; i++) {
        for (int j = 0; j < n - i - 1; j++) {
            // Obtener los puntajes de los usuarios
            int puntaje1 = Integer.parseInt(ranking.get(j).split(":")[1]);
            int puntaje2 = Integer.parseInt(ranking.get(j + 1).split(":")[1]);

            // Comparar los puntajes y ordenar de mayor a menor
            if (puntaje1 < puntaje2) {
                // Intercambiar los elementos si están en el orden incorrecto
                String temp = ranking.get(j);
                ranking.set(j, ranking.get(j + 1));
                ranking.set(j + 1, temp);
            }
        }
    }
}

    private void crearUI() {
        Table table = new Table();
        table.setFillParent(true); // Hacer que la tabla ocupe toda la pantalla
        stage.addActor(table);

        // Crear una fuente básica
        BitmapFont font = new BitmapFont();
        font.getData().setScale(2);

        // Crear estilos para los componentes
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = com.badlogic.gdx.graphics.Color.WHITE;

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;

        // Título del ranking
        Label titulo = new Label("Ranking de Jugadores", labelStyle);
        table.add(titulo).colspan(2).pad(20).row();

        // Mostrar la lista de usuarios
        for (int i = 0; i < rankingUsuarios.size(); i++) {
            String[] usuarioInfo = rankingUsuarios.get(i).split(":");
            String nombreUsuario = usuarioInfo[0];
            int puntajeMaximo = Integer.parseInt(usuarioInfo[1]);

            Label labelUsuario = new Label((i + 1) + ". " + nombreUsuario + " - Puntaje: " + puntajeMaximo, labelStyle);
            table.add(labelUsuario).pad(10).row();
        }

        // Botón para regresar
        TextButton botonRegresar = new TextButton("Regresar", buttonStyle);
        botonRegresar.addListener(new com.badlogic.gdx.scenes.scene2d.utils.ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                game.setScreen(new SettingsScreen(game)); // Volver a la pantalla de ajustes
            }
        });
        table.add(botonRegresar).colspan(2).pad(20);
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