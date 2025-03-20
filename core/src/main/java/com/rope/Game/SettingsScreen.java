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
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class SettingsScreen implements Screen {
    private Stage stage;
    private TextButton btnPreferencias, btnMiPerfil, btnRanking, btnEstadisticas, btnRegresar,btnSignOut;
    private main game; // Referencia a la clase principal

    public SettingsScreen(main game) {
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

        // Crear los botones
        btnPreferencias = new TextButton("Preferencias", buttonStyle);
        btnMiPerfil = new TextButton("Mi Perfil", buttonStyle);
        btnRanking = new TextButton("Ranking", buttonStyle);
        btnEstadisticas = new TextButton("Estadísticas", buttonStyle);
        btnRegresar = new TextButton("Regresar al Mapa", buttonStyle);
        btnSignOut= new TextButton("Sign out", buttonStyle);

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
                System.out.println("Preferencias presionado");
                // Aquí puedes cambiar a la pantalla de Preferencias
                 game.setScreen(new AjustesScreen(game));
            }
        });

        btnMiPerfil.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Mi Perfil presionado");
                // Aquí puedes cambiar a la pantalla de Mi Perfil
                // game.setScreen(new MiPerfilScreen(game));
            }
        });

        btnRanking.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Ranking presionado");
                // Aquí puedes cambiar a la pantalla de Ranking
                // game.setScreen(new RankingScreen(game));
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
                System.out.println("Regresar presionado");
                // Volver a la pantalla anterior (por ejemplo, el menú principal)
                game.setScreen(new mapa(game));
            }
        });
        
        btnSignOut.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Regresar presionado");
                // Volver a la pantalla anterior (por ejemplo, el menú principal)
                game.setScreen(new MenuInicio(game));
            }
        });
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