package com.rope.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import java.util.ArrayList;
import java.util.List;

public class Estadisticas implements Screen{
    private Stage stage;
    private main game;
    private BitmapFont font;
    private Table tablaEstadisticas;
    
    public Estadisticas(main game) {
        this.game = game;
        this.stage = new Stage();
        
        // Crear la fuente para el texto
        font = new BitmapFont();
        font.getData().setScale(2);
        
        // Estilo para las etiquetas
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        
        // Estilo para los botones (como estaba en tu código original)
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        
        // Crear botón de regresar
        TextButton botonRegresar = new TextButton("Regresar", buttonStyle);
        botonRegresar.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                // Regresar a la pantalla principal de ajustes
                game.setScreen(new SettingsScreen(game));
            }
        });
        
        // Configurar la posición del botón como en tu código original
        botonRegresar.setPosition(300, 100);
        
        // Crear la tabla de estadísticas
        crearTablaEstadisticas(labelStyle);
        
        // Añadir los componentes al stage
        stage.addActor(tablaEstadisticas);
        stage.addActor(botonRegresar);
    }
    
    private void crearTablaEstadisticas(Label.LabelStyle labelStyle) {
        // Crear la tabla para las estadísticas
        tablaEstadisticas = new Table();
        tablaEstadisticas.setPosition(100, 400);
        tablaEstadisticas.setSize(500, 300);
        
        // Título de la tabla
        Label tituloEstadisticas = new Label("ESTADÍSTICAS DEL USUARIO", labelStyle);
        tablaEstadisticas.add(tituloEstadisticas).colspan(2).padBottom(40).row();
        
        // Obtener el usuario actual
        Usuario usuario = Usuario.getUsuarioLogueado();
        
        if (usuario != null) {
            // Contar niveles completados
            int nivelesCompletados = 0;
            boolean[] nivelesDesbloqueados = usuario.getNivelesCompletados();
            for (int i = 0; i < nivelesDesbloqueados.length; i++) {
                if (nivelesDesbloqueados[i]) {
                    nivelesCompletados++;
                }
            }
            
            // Obtener cantidad total de niveles
            int totalNiveles = nivelesDesbloqueados.length;
            
            // Obtener historial de partidas
            int partidasRealizadas = usuario.getHistorialPartidas() != null ? 
                                    usuario.getHistorialPartidas().size() : 0;
            
            // Obtener puntos totales
            int puntosTotales = usuario.getPuntajeMaximo();
            
            // Añadir filas de estadísticas
            agregarFilaEstadistica("Niveles Completados:", nivelesCompletados + "/" + totalNiveles, labelStyle);
            agregarFilaEstadistica("Partidas Realizadas:", String.valueOf(partidasRealizadas), labelStyle);
            agregarFilaEstadistica("Puntos Totales:", String.valueOf(puntosTotales), labelStyle);
            
            // Tiempo total jugado formateado
            long tiempoTotal = usuario.getTiempoTotalJugado(); // en milisegundos
            String tiempoFormateado = formatearTiempo(tiempoTotal);
            agregarFilaEstadistica("Tiempo Total Jugado:", tiempoFormateado, labelStyle);
            
            // Nombre de usuario
            agregarFilaEstadistica("Usuario:", usuario.getNombreUsuario(), labelStyle);
        } else {
            // Si no hay usuario logueado
            tablaEstadisticas.add(new Label("No hay usuario logueado", labelStyle)).colspan(2).row();
        }
    }
    
    private void agregarFilaEstadistica(String titulo, String valor, Label.LabelStyle estilo) {
        Label labelTitulo = new Label(titulo, estilo);
        Label labelValor = new Label(valor, estilo);
        
        tablaEstadisticas.add(labelTitulo).padRight(50).padBottom(15);
        tablaEstadisticas.add(labelValor).padBottom(15).row();
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
    
    // Método estático para registrar una nueva partida en el historial del usuario
    public static void registrarPartida(Usuario usuario, int nivelJugado, int puntosObtenidos, long tiempoJugado) {
        if (usuario != null) {
            // Crear un registro de la partida con formato: "Nivel-Puntos-Tiempo"
            String registro = "Nivel" + nivelJugado + "-" + puntosObtenidos + "pts-" + formatearTiempoCorto(tiempoJugado);
            
            // Obtener la lista actual de partidas
            List<String> historial = usuario.getHistorialPartidas();
            if (historial == null) {
                historial = new ArrayList<>();
            }
            
            // Añadir el nuevo registro
            historial.add(registro);
            
            // Actualizar el historial en el usuario
            usuario.setHistorialPartidas(historial);
            
            // Actualizar el tiempo total jugado
            usuario.setTiempoTotalJugado(usuario.getTiempoTotalJugado() + tiempoJugado);
            
            // Guardar los cambios
            usuario.guardarUsuario();
        }
    }
    
    // Método auxiliar para formatear el tiempo en formato corto (para el historial)
    private static String formatearTiempoCorto(long milisegundos) {
        long segundos = milisegundos / 1000;
        long minutos = segundos / 60;
        segundos %= 60;
        
        return String.format("%02dm%02ds", minutos, segundos);
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
    }
}