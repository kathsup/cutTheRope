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

        IdiomaManager.getInstancia().agregarListener("SettingsScreen", this);

        crearUI();
        actualizarTextos();
    }

    private void crearUI() {
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        BitmapFont font = new BitmapFont();
        font.getData().setScale(2);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = Color.WHITE;

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.fontColor = Color.WHITE;

        btnPreferencias = new TextButton("", buttonStyle);
        btnMiPerfil = new TextButton("", buttonStyle);
        btnRanking = new TextButton("", buttonStyle);
        btnEstadisticas = new TextButton("", buttonStyle);
        btnRegresar = new TextButton("", buttonStyle);
        btnSignOut = new TextButton("", buttonStyle);

        table.add(btnPreferencias).pad(10).row();
        table.add(btnMiPerfil).pad(10).row();
        table.add(btnRanking).pad(10).row();
        table.add(btnEstadisticas).pad(10).row();
        table.add(btnRegresar).pad(20).row();
        table.add(btnSignOut).pad(30).row();

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
                game.detenerMusica();

                Usuario.cerrarSesion();

                game.setScreen(new MenuInicio(game));
            }
        });

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
        IdiomaManager.getInstancia().removerListener("SettingsScreen");
        stage.dispose();
    }
}
