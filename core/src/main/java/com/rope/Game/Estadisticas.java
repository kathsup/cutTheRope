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
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class Estadisticas implements Screen {
    private Stage stage;
    private main game;
    private BitmapFont font;
    private Table tablaEstadisticas;
    private SpriteBatch batch;
    private Texture backgroundTexture;
    


    // Textos según el idioma
    private String tituloEstadisticasTexto;
    private String nivelesCompletadosTexto;
    private String partidasRealizadasTexto;
    private String puntosTotalesTexto;
    private String tiempoTotalTexto;
    private String usuarioTexto;
    private String regresarTexto;

    // Referencias a los elementos de la interfaz
    private Label tituloEstadisticas;
    private Label labelNivelesCompletados;
    private Label labelPartidasRealizadas;
    private Label labelPuntosTotales;
    private Label labelTiempoTotal;
    private Label labelUsuario;
    private TextButton botonRegresar;
    


    public Estadisticas(main game) {
        this.game = game;
        this.stage = new Stage();

        // Crear la fuente para el texto
        font = new BitmapFont();
        font.getData().setScale(2);

        batch = new SpriteBatch();
        backgroundTexture = new Texture(Gdx.files.internal("preferencias.jpg"));
        


        // Inicializar textos según el idioma del usuario
        actualizarTextos();

        // Crear los elementos de la interfaz
        crearInterfaz();

        // Crear botón de regresar
        crearBotonRegresar();
    }

    private void crearInterfaz() {

        // Estilo para las etiquetas
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;

        // Título de la pantalla
        tituloEstadisticas = new Label(tituloEstadisticasTexto, labelStyle);
        tituloEstadisticas.setPosition(100, Gdx.graphics.getHeight() - 100); // Posición del título
        stage.addActor(tituloEstadisticas);

        // Obtener el usuario actual
        Usuario usuario = Usuario.getUsuarioLogueado();

        if (usuario != null) {
            // Contar niveles completados
            int contadorNivelesCompletados = 0;
            boolean[] nivelesCompletados = usuario.getNivelesCompletados();
            for (int i = 0; i < nivelesCompletados.length; i++) {
                if (nivelesCompletados[i]) {
                    contadorNivelesCompletados++;
                }
            }

            // Obtener cantidad total de niveles
            int totalNiveles = nivelesCompletados.length;

            // Obtener historial de partidas
            int partidasRealizadas = usuario.getHistorialPartidas() != null ? 
                                    usuario.getHistorialPartidas().size() : 0;

            // Obtener puntos totales
            int puntosTotales = usuario.getPuntajeMaximo();

            // Tiempo total jugado formateado
            long tiempoTotal = usuario.getTiempoTotalJugado(); // en milisegundos
            String tiempoFormateado = formatearTiempo(tiempoTotal);

            // Nombre de usuario
            String nombreUsuario = usuario.getNombreUsuario();

            // Crear y posicionar las etiquetas de estadísticas
            labelNivelesCompletados = new Label(nivelesCompletadosTexto + contadorNivelesCompletados + "/" + totalNiveles, labelStyle);
            labelNivelesCompletados.setPosition(100, Gdx.graphics.getHeight() - 150);
            stage.addActor(labelNivelesCompletados);

            labelPartidasRealizadas = new Label(partidasRealizadasTexto + partidasRealizadas, labelStyle);
            labelPartidasRealizadas.setPosition(100, Gdx.graphics.getHeight() - 200);
            stage.addActor(labelPartidasRealizadas);

            labelPuntosTotales = new Label(puntosTotalesTexto + puntosTotales, labelStyle);
            labelPuntosTotales.setPosition(100, Gdx.graphics.getHeight() - 250);
            stage.addActor(labelPuntosTotales);

            labelTiempoTotal = new Label(tiempoTotalTexto + tiempoFormateado, labelStyle);
            labelTiempoTotal.setPosition(100, Gdx.graphics.getHeight() - 300);
            stage.addActor(labelTiempoTotal);

            labelUsuario = new Label(usuarioTexto + nombreUsuario, labelStyle);
            labelUsuario.setPosition(100, Gdx.graphics.getHeight() - 350);
            stage.addActor(labelUsuario);
        } else {
            // Si no hay usuario logueado
            Label labelNoUsuario = new Label("No hay usuario logueado", labelStyle);
            labelNoUsuario.setPosition(100, Gdx.graphics.getHeight() - 150);
            stage.addActor(labelNoUsuario);
        }
    }

    private void crearBotonRegresar() {
        // Estilo para los botones
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;

        // Crear botón de regresar
        botonRegresar = new TextButton(regresarTexto, buttonStyle);
        botonRegresar.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Regresar a la pantalla principal de ajustes
                game.setScreen(new SettingsScreen(game));
            }
        });

        // Configurar la posición del botón
        botonRegresar.setPosition(100, 50);

        // Añadir el botón al stage
        stage.addActor(botonRegresar);
    }

    // Método para formatear el tiempo total en horas:minutos:segundos
    private String formatearTiempo(long milisegundos) {
        long segundos = milisegundos / 1000;
        long horas = segundos / 3600;
        segundos %= 3600;
        long minutos = segundos / 60;
        segundos %= 60;

        return String.format("%02d:%02d:%02d", horas, minutos, segundos);
    }

    // Método para actualizar los textos según el idioma
    private void actualizarTextos() {
        Usuario usuario = Usuario.getUsuarioLogueado();
        if (usuario != null) {
            String idioma = usuario.getIdioma();
            switch (idioma) {
                case "es":
                    tituloEstadisticasTexto = "ESTADÍSTICAS DEL USUARIO";
                    nivelesCompletadosTexto = "Niveles Completados: ";
                    partidasRealizadasTexto = "Partidas Realizadas: ";
                    puntosTotalesTexto = "Puntos Totales: ";
                    tiempoTotalTexto = "Tiempo Total Jugado: ";
                    usuarioTexto = "Usuario: ";
                    regresarTexto = "Regresar";
                    break;
                case "en":
                    tituloEstadisticasTexto = "USER STATISTICS";
                    nivelesCompletadosTexto = "Completed Levels: ";
                    partidasRealizadasTexto = "Games Played: ";
                    puntosTotalesTexto = "Total Points: ";
                    tiempoTotalTexto = "Total Played Time: ";
                    usuarioTexto = "User: ";
                    regresarTexto = "Back";
                    break;
                case "fr":
                    tituloEstadisticasTexto = "STATISTIQUES DE L'UTILISATEUR";
                    nivelesCompletadosTexto = "Niveaux Complétés: ";
                    partidasRealizadasTexto = "Parties Jouées: ";
                    puntosTotalesTexto = "Points Totaux: ";
                    tiempoTotalTexto = "Temps Total Joué: ";
                    usuarioTexto = "Utilisateur: ";
                    regresarTexto = "Retour";
                    break;
            }
        }
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        // Limpiar la pantalla
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        
         batch.begin();
        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();
        

        // Actualizar y dibujar el stage
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
        if (stage != null) {
            stage.dispose();
        }
        if (font != null) {
            font.dispose();
        }
        
        batch.dispose();
        backgroundTexture.dispose();
    }
}