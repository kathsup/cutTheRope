package com.rope.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class SettingsScreen implements Screen, IdiomaManager.IdiomaListener {

    private Stage stage;
    private TextButton btnPreferencias, btnMiPerfil, btnRanking, btnEstadisticas, btnRegresar, btnSignOut;
    private main game;

    public SettingsScreen(main game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Registrar esta pantalla en el IdiomaManager
        IdiomaManager.getInstancia().agregarListener("SettingsScreen", this);

        // Crear la interfaz de usuario
        crearUI();
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
        labelStyle.fontColor = Color.WHITE;

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.fontColor = Color.WHITE;

        // Crear los botones
        btnPreferencias = new TextButton("", buttonStyle);
        btnMiPerfil = new TextButton("", buttonStyle);
        btnRanking = new TextButton("", buttonStyle);
        btnEstadisticas = new TextButton("", buttonStyle);
        btnRegresar = new TextButton("", buttonStyle);
        btnSignOut = new TextButton("", buttonStyle);

        // Agregar los botones a la tabla
        table.add(btnPreferencias).pad(10).row();
        table.add(btnMiPerfil).pad(10).row();
        table.add(btnRanking).pad(10).row();
        table.add(btnEstadisticas).pad(10).row();
        table.add(btnRegresar).pad(20).row();
        table.add(btnSignOut).pad(30).row();

        // Configurar eventos de los botones
        btnPreferencias.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new AjustesScreen(game));
            }
        });

        btnMiPerfil.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new PerfilScreen(game));
            }
        });

        btnRanking.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                    game.setScreen(new RankingScreen(game));

            }
        });

        btnEstadisticas.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                System.out.println("Estadísticas presionado");
                // Aquí puedes cambiar a la pantalla de Estadísticas
                 game.setScreen(new Estadisticas(game));

            }
        });

        btnRegresar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new mapa(game));
            }
        });

       btnSignOut.addListener(new ClickListener() {
    @Override
    public void clicked(InputEvent event, float x, float y) {
        // Detener la música al cerrar sesión
        game.detenerMusica();

        // Cerrar la sesión del usuario
        Usuario.cerrarSesion();

        // Redirigir al menú de inicio
        game.setScreen(new MenuInicio(game));
    }
});

        // Actualizar los textos según el idioma actual
        actualizarTextos();
    }

    private void actualizarTextos() {
        btnPreferencias.setText(IdiomaManager.getInstancia().getTexto("preferencias"));
        btnMiPerfil.setText(IdiomaManager.getInstancia().getTexto("mi_perfil"));
        btnRanking.setText(IdiomaManager.getInstancia().getTexto("ranking"));
        btnEstadisticas.setText(IdiomaManager.getInstancia().getTexto("estadisticas"));
        btnRegresar.setText(IdiomaManager.getInstancia().getTexto("regresar_mapa"));
        btnSignOut.setText(IdiomaManager.getInstancia().getTexto("cerrar_sesion"));
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
        Gdx.gl.glClearColor(0.4f, 0.6f, 0.9f, 1);
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
        // Remover el listener cuando la pantalla se destruya
        IdiomaManager.getInstancia().removerListener("SettingsScreen");
        stage.dispose();
    }
}