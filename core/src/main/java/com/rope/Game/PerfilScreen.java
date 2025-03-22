package com.rope.Game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;

public class PerfilScreen implements Screen, IdiomaManager.IdiomaListener {

    private Stage stage;
    private main game;
    private Image avatarImage;
    private Label labelNombreUsuario, labelNombreCompleto, labelProgreso, labelPuntaje, labelTiempo, labelRanking, labelFechaRegistro;
    private TextButton botonCambiarFoto;
    private TextButton botonRegresar;
    private TextButton botonEliminarCuenta; // Nuevo botón para eliminar la cuenta

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

        // Botón para cambiar la foto de perfil
        botonCambiarFoto = new TextButton("", buttonStyle);
        botonCambiarFoto.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                cambiarFotoPerfil();
            }
        });
        table.add(botonCambiarFoto).colspan(2).pad(10).row();

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

        // Botón para eliminar la cuenta (en la esquina inferior derecha)
        // Botón para eliminar la cuenta (en la esquina inferior derecha, pero más centrado)
        botonEliminarCuenta = new TextButton("", buttonStyle);
        botonEliminarCuenta.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                eliminarCuenta();
            }
        });

// Posicionar el botón en la esquina inferior derecha, pero con un margen más pequeño
        float marginRight = 100; // Margen derecho de 100px (ajusta este valor según sea necesario)
        float marginBottom = 20; // Margen inferior de 20px
        botonEliminarCuenta.setPosition(
                Gdx.graphics.getWidth() - botonEliminarCuenta.getWidth() - marginRight, // Margen derecho ajustado
                marginBottom // Margen inferior
        );
        stage.addActor(botonEliminarCuenta); // Agregar el botón directamente al stagectamente al stage

        // Actualizar los textos según el idioma actual
        actualizarTextos();
    }

    // Método para eliminar la cuenta
    private void eliminarCuenta() {
        Usuario usuario = Usuario.getUsuarioLogueado();
        if (usuario != null) {
            String rutaBase = "C:\\Users\\fdhg0\\Documents\\NetBeansProjects\\cutTheRope-master\\usuarios\\";
            String rutaCarpetaUsuario = rutaBase + usuario.getNombreUsuario();

            // Crear un objeto File para la carpeta del usuario
            File carpetaUsuario = new File(rutaCarpetaUsuario);

            // Eliminar la carpeta y su contenido
            if (eliminarArchivosYCarpeta(carpetaUsuario)) {
                System.out.println("Cuenta eliminada: " + usuario.getNombreUsuario());

                // Cerrar sesión y redirigir al menú de inicio
                Usuario.cerrarSesion();
                game.setScreen(new MenuInicio(game));
            } else {
                System.out.println("Error al eliminar la cuenta: No se pudo borrar la carpeta del usuario.");
            }
        }
    }

    // Función para eliminar archivos y carpetas de manera recursiva
    private boolean eliminarArchivosYCarpeta(File file) {
        if (file == null || !file.exists()) {
            return false; // Si el archivo no existe, no hay nada que eliminar
        }

        // Si es un directorio, eliminar primero su contenido
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File child : files) {
                    eliminarArchivosYCarpeta(child); // Llamada recursiva para eliminar archivos y subdirectorios
                }
            }
        }

        // Eliminar el archivo o directorio vacío
        return file.delete();
    }

    // Método para cambiar la foto de perfil (solo en desktop)
    private void cambiarFotoPerfil() {
        if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
            FileDialog fileDialog = new FileDialog((Frame) null, "Seleccionar imagen", FileDialog.LOAD);
            fileDialog.setFilenameFilter((dir, name) -> name.endsWith(".png") || name.endsWith(".jpg"));
            fileDialog.setVisible(true);

            String rutaArchivo = fileDialog.getDirectory() + fileDialog.getFile();
            if (rutaArchivo != null && !rutaArchivo.isEmpty()) {
                // Copiar la imagen seleccionada a la carpeta del usuario
                String rutaBase = "C:\\Users\\fdhg0\\Documents\\NetBeansProjects\\cutTheRope-master\\usuarios\\";
                String rutaCarpeta = rutaBase + Usuario.getUsuarioLogueado().getNombreUsuario() + "\\";
                String nombreArchivo = "avatar.png"; // Nombre fijo para la imagen de perfil
                String rutaDestino = rutaCarpeta + nombreArchivo;

                try {
                    // Crear la carpeta del usuario si no existe
                    Files.createDirectories(Paths.get(rutaCarpeta));

                    // Copiar la imagen seleccionada a la carpeta del usuario
                    Files.copy(Paths.get(rutaArchivo), Paths.get(rutaDestino), StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("Imagen copiada a: " + rutaDestino);

                    // Actualizar la referencia en el objeto Usuario
                    Usuario.getUsuarioLogueado().setAvatar(rutaDestino);
                    Usuario.getUsuarioLogueado().guardarUsuario();

                    // Actualizar la imagen en la pantalla
                    Texture nuevaTextura = new Texture(Gdx.files.internal(rutaDestino));
                    avatarImage.setDrawable(new TextureRegionDrawable(new TextureRegion(nuevaTextura)));
                } catch (IOException e) {
                    System.out.println("Error al copiar la imagen:");
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("La función de cambiar foto de perfil solo está disponible en desktop.");
        }
    }

    // Método para actualizar los textos según el idioma
    private void actualizarTextos() {
        Usuario usuario = Usuario.getUsuarioLogueado();
        if (usuario != null) {
            String idioma = IdiomaManager.getInstancia().getIdiomaActual();
            switch (idioma) {
                case "es":
                    labelNombreUsuario.setText("Nombre de usuario: " + usuario.getNombreUsuario());
                    labelNombreCompleto.setText("Nombre completo: " + usuario.getNombreCompleto());
                    labelProgreso.setText("Progreso en el juego: " + usuario.getProgresoJuego() + " niveles");
                    labelPuntaje.setText("Puntaje máximo: " + usuario.getPuntajeMaximo());
                    labelTiempo.setText("Tiempo total jugado: " + usuario.getTiempoTotalJugado() + " segundos");
                    labelRanking.setText("Ranking: " + usuario.getRanking());
                    labelFechaRegistro.setText("Fecha de creación: " + new SimpleDateFormat("dd/MM/yyyy").format(usuario.getFechaRegistro()));
                    botonCambiarFoto.setText("Cambiar Foto de Perfil");
                    botonRegresar.setText("Regresar");
                    botonEliminarCuenta.setText("Eliminar Cuenta"); // Texto para el botón de eliminar cuenta
                    break;
                case "en":
                    labelNombreUsuario.setText("Username: " + usuario.getNombreUsuario());
                    labelNombreCompleto.setText("Full name: " + usuario.getNombreCompleto());
                    labelProgreso.setText("Game progress: " + usuario.getProgresoJuego() + " levels");
                    labelPuntaje.setText("Max score: " + usuario.getPuntajeMaximo());
                    labelTiempo.setText("Total time played: " + usuario.getTiempoTotalJugado() + " seconds");
                    labelRanking.setText("Ranking: " + usuario.getRanking());
                    labelFechaRegistro.setText("Creation date: " + new SimpleDateFormat("dd/MM/yyyy").format(usuario.getFechaRegistro()));
                    botonCambiarFoto.setText("Change Profile Picture");
                    botonRegresar.setText("Back");
                    botonEliminarCuenta.setText("Delete Account"); // Texto para el botón de eliminar cuenta
                    break;
                case "fr":
                    labelNombreUsuario.setText("Nom d'utilisateur: " + usuario.getNombreUsuario());
                    labelNombreCompleto.setText("Nom complet: " + usuario.getNombreCompleto());
                    labelProgreso.setText("Progrès du jeu: " + usuario.getProgresoJuego() + " niveaux");
                    labelPuntaje.setText("Score maximal: " + usuario.getPuntajeMaximo());
                    labelTiempo.setText("Temps total joué: " + usuario.getTiempoTotalJugado() + " secondes");
                    labelRanking.setText("Classement: " + usuario.getRanking());
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
        // Actualizar los textos cuando el idioma cambie
        actualizarTextos();
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
        // Remover el listener cuando la pantalla se destruya
        IdiomaManager.getInstancia().removerListener("PerfilScreen");
        stage.dispose();
    }
}
