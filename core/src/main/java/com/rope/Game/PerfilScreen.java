package com.rope.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
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
import java.text.SimpleDateFormat;
import java.util.Date;

public class PerfilScreen implements Screen {
    private Stage stage;
    private main game;
    private Image avatarImage; // Referencia a la imagen del avatar

    public PerfilScreen(main game) {
        this.game = game; // Recibe la instancia de la clase principal
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage); // Habilitar el Stage para manejar la entrada

        // Crear una fuente básica
        BitmapFont font = new BitmapFont();

        // Crear estilos para los componentes
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font; // Asignar la fuente al estilo de Label

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font; // Asignar la fuente al estilo de TextButton

        // Crear la tabla para organizar los componentes
        Table table = new Table();
        table.setFillParent(true); // Hacer que la tabla ocupe toda la pantalla
        stage.addActor(table);

        // Obtener el usuario logueado
        Usuario usuario = Usuario.getUsuarioLogueado();

        // Verificar si hay un usuario logueado
        if (usuario != null) {
            // Mostrar la imagen del usuario (avatar)
            Texture avatarTexture = new Texture(Gdx.files.internal(usuario.getAvatar()));
            avatarImage = new Image(avatarTexture);
            avatarImage.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    abrirSeleccionImagen(usuario); // Llamar al método para seleccionar una imagen
                }
            });
            table.add(avatarImage).size(100, 100).pad(10).row(); // Tamaño de la imagen: 100x100 píxeles

            // Mostrar la fecha de creación de la cuenta
            Date fechaRegistro = usuario.getFechaRegistro();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String fechaRegistroStr = dateFormat.format(fechaRegistro);
            table.add(new Label("Fecha de creación: " + fechaRegistroStr, labelStyle)).pad(10).row();

            // Mostrar la información del usuario
            table.add(new Label("Nombre de usuario: " + usuario.getNombreUsuario(), labelStyle)).pad(10).row();
            table.add(new Label("Nombre completo: " + usuario.getNombreCompleto(), labelStyle)).pad(10).row();
            table.add(new Label("Progreso en el juego: " + usuario.getProgresoJuego() + " niveles", labelStyle)).pad(10).row();
            table.add(new Label("Puntaje máximo: " + usuario.getPuntajeMaximo(), labelStyle)).pad(10).row();
            table.add(new Label("Tiempo total jugado: " + usuario.getTiempoTotalJugado() + " segundos", labelStyle)).pad(10).row();
            table.add(new Label("Ranking: " + usuario.getRanking(), labelStyle)).pad(10).row();
        } else {
            // Si no hay usuario logueado, mostrar un mensaje
            table.add(new Label("No hay un usuario logueado.", labelStyle)).pad(10).row();
        }

        // Botón para regresar al menú principal
        TextButton botonRegresar = new TextButton("Regresar", buttonStyle);
        botonRegresar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SettingsScreen(game)); // Volver al menú de inicio
            }
        });
        table.add(botonRegresar).colspan(2).pad(20);
    }

    // Método para abrir la selección de imagen
    private void abrirSeleccionImagen(Usuario usuario) {
        // Abrir un diálogo para seleccionar una imagen
        FileHandle fileHandle = Gdx.files.external("C:\\Users\\fdhg0\\Documents\\NetBeansProjects\\cutTheRope-master\\Profilepic"); // Directorio inicial (puedes cambiarlo)
        FileHandle[] archivos = fileHandle.list(); // Listar archivos en el directorio

        // Mostrar un diálogo simple para seleccionar una imagen
        for (FileHandle archivo : archivos) {
            if (archivo.name().endsWith(".png") || archivo.name().endsWith(".jpg")) {
                System.out.println("Archivo de imagen encontrado: " + archivo.name());
                // Aquí puedes implementar un diálogo para seleccionar el archivo
                // Por simplicidad, seleccionamos el primer archivo de imagen encontrado
                actualizarImagenPerfil(usuario, archivo);
                break;
            }
        }
    }

    // Método para actualizar la imagen de perfil
    private void actualizarImagenPerfil(Usuario usuario, FileHandle archivo) {
        // Actualizar la imagen del perfil
        String nuevaRutaAvatar = archivo.path(); // Obtener la ruta de la nueva imagen
        usuario.setAvatar(nuevaRutaAvatar); // Actualizar el avatar en el objeto Usuario
        usuario.guardarUsuario(); // Guardar los cambios en el archivo del usuario

        // Actualizar la imagen mostrada en la pantalla
        Texture nuevaTextura = new Texture(archivo);
        avatarImage.setDrawable(new TextureRegionDrawable(new TextureRegion(nuevaTextura)));
        System.out.println("Imagen de perfil actualizada: " + nuevaRutaAvatar);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage); // Habilitar el Stage para manejar la entrada
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
        stage.getViewport().update(width, height, true); // Actualizar el viewport al cambiar el tamaño de la pantalla
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