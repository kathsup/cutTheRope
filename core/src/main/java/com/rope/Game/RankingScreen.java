package com.rope.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
    private List<String> rankingUsuarios; // Lista de usuarios en el ranking

    // Textos según el idioma
    private String tituloRankingTexto;
    private String regresarTexto;
    private String puntajeTexto; // Nueva variable para "Puntaje"

    // Referencias a los elementos de la interfaz
    private Label tituloRanking;
    private TextButton botonRegresar;

    public RankingScreen(main game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Cargar y ordenar el ranking de usuarios
        rankingUsuarios = cargarRankingUsuarios();

        // Inicializar textos según el idioma del usuario
        actualizarTextos();

        // Crear la interfaz de usuario
        crearUI();
    }

    private List<String> cargarRankingUsuarios() {
        List<String> ranking = new ArrayList<>();
        String rutaBase = "C:\\Users\\fdhg0\\Documents\\NetBeansProjects\\cutTheRope-master\\usuarios\\";
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
        tituloRanking = new Label(tituloRankingTexto, labelStyle);
        tituloRanking.setPosition(
            (Gdx.graphics.getWidth() - tituloRanking.getWidth()) / 2, // Centrar horizontalmente
            Gdx.graphics.getHeight() - 100 // Posicionar en la parte superior
        );
        stage.addActor(tituloRanking);

        // Mostrar la lista de usuarios
        float yPos = Gdx.graphics.getHeight() - 150; // Posición inicial para la lista de usuarios
        for (int i = 0; i < rankingUsuarios.size(); i++) {
            String[] usuarioInfo = rankingUsuarios.get(i).split(":");
            String nombreUsuario = usuarioInfo[0];
            int puntajeMaximo = Integer.parseInt(usuarioInfo[1]);

            // Usar la variable puntajeTexto para mostrar el texto correcto según el idioma
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
        stage.dispose();
    }
}