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
import java.util.Date;

public class LoginScreen implements Screen {
    private Stage stage;
    private TextField campoUsuario;
    private TextField campoContrasena;
    private TextButton botonLogin;
    private TextButton botonRegresar; // Nuevo botón de regreso
    private main game; // Referencia a la clase principal

    public LoginScreen(main game) {
        this.game = game; // Recibe la instancia de la clase principal
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage); // Habilitar el Stage para manejar la entrada

        // Crear una fuente básica
        BitmapFont font = new BitmapFont();

        // Crear estilos para los componentes
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font; // Asignar la fuente al estilo de Label

        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.font = font; // Asignar la fuente al estilo de TextField
        textFieldStyle.fontColor = com.badlogic.gdx.graphics.Color.WHITE; // Color del texto

        // Crear una textura sólida para el fondo
        com.badlogic.gdx.graphics.Pixmap pixmap = new com.badlogic.gdx.graphics.Pixmap(1, 1, com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888);
        pixmap.setColor(com.badlogic.gdx.graphics.Color.DARK_GRAY);
        pixmap.fill();
        com.badlogic.gdx.graphics.Texture texture = new com.badlogic.gdx.graphics.Texture(pixmap);
        pixmap.dispose(); // Liberar la memoria de la pixmap

        // Asignar la textura como fondo del TextField
        textFieldStyle.background = new com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable(new com.badlogic.gdx.graphics.g2d.TextureRegion(texture));

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font; // Asignar la fuente al estilo de TextButton

        // Crear la tabla para organizar los componentes
        Table table = new Table();
        table.setFillParent(true); // Hacer que la tabla ocupe toda la pantalla
        stage.addActor(table);

        // Crear los componentes
        Label etiquetaUsuario = new Label("Nombre de usuario:", labelStyle);
        campoUsuario = new TextField("", textFieldStyle);

        Label etiquetaContrasena = new Label("Contraseña:", labelStyle);
        campoContrasena = new TextField("", textFieldStyle);
        campoContrasena.setPasswordMode(true); // Ocultar la contraseña
        campoContrasena.setPasswordCharacter('*'); // Carácter para ocultar la contraseña

        botonLogin = new TextButton("Iniciar Sesión", buttonStyle);
        botonRegresar = new TextButton("Regresar", buttonStyle); // Nuevo botón de regreso

        // Agregar los componentes a la tabla
        table.add(etiquetaUsuario).pad(10);
        table.add(campoUsuario).width(200).pad(10);
        table.row();
        table.add(etiquetaContrasena).pad(10);
        table.add(campoContrasena).width(200).pad(10);
        table.row();
        table.add(botonLogin).colspan(2).pad(20);
        table.row();
        table.add(botonRegresar).colspan(2).pad(10); // Agregar el botón de regreso

        // Configurar el evento del botón de inicio de sesión
        botonLogin.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                iniciarSesion();
            }
        });

        // Configurar el evento del botón de regreso
        botonRegresar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuInicio(game)); // Volver al menú de inicio
            }
        });
    }

    private void iniciarSesion() {
    String nombreUsuario = campoUsuario.getText();
    String contrasena = campoContrasena.getText();

    if (Usuario.verificarContrasena(nombreUsuario, contrasena)) {
        System.out.println("Inicio de sesión exitoso");

        Usuario usuario = Usuario.cargarUsuarioLogin(nombreUsuario);
        if (usuario != null) {
            usuario.setUltimaSesion(new Date());

            usuario.guardarUsuario();

            usuario.iniciarSesion(contrasena);
            System.out.println("Usuario logueado: " + Usuario.getUsuarioLogueado().getNombreUsuario());

            game.setScreen(new mapa(game)); 
        } else {
            System.out.println("Error al cargar el usuario.");
        }
    } else {
        System.out.println("Usuario o contraseña incorrectos");
    }
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