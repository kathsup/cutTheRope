package com.rope.Game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;



public class mapa implements Screen {

    private main game;
    private SpriteBatch batch;
    private Texture btnNivel1, btnNivel2, btnNivel3, btnNivel4, btnNivel5;
    private Texture btnNivelBloqueado;
    private Texture background; // Para el fondo
    private Vector2 posBtnNivel1, posBtnNivel2, posBtnNivel3, posBtnNivel4, posBtnNivel5;
    private float btnWidth, btnHeight;

    public mapa(main game) {
        this.game = game;
        batch = new SpriteBatch();

        // Cargar texturas para los botones
        btnNivel1 = new Texture("boton.png");
        btnNivel2 = new Texture("boton.png");
        btnNivel3 = new Texture("boton.png");
        btnNivel4 = new Texture("boton.png");
        btnNivel5 = new Texture("boton.png");
        btnNivelBloqueado = new Texture("boton_bloqueado.png");

        // Cargar la textura del fondo
        background = new Texture("mapa.jpg");

        // Posiciones de los botones
        posBtnNivel1 = new Vector2(160, 200);
        posBtnNivel2 = new Vector2(400, 368);
        posBtnNivel3 = new Vector2(620, 500);
        posBtnNivel4 = new Vector2(400, 622);
        posBtnNivel5 = new Vector2(170, 675);

        // Tamaño de los botones
        btnWidth = 70;  // Ancho deseado para los botones
        btnHeight = 70; // Alto deseado para los botones
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        batch.begin();

        // Dibujar el fondo
        batch.draw(background, 0, 0);

        // Obtener los niveles desbloqueados desde game
        boolean[] nivelesDesbloqueados = game.getNivelesDesbloqueados();

        // Dibujar los botones de nivel, mostrando el botón bloqueado o desbloqueado
        batch.draw(nivelesDesbloqueados[0] ? btnNivel1 : btnNivelBloqueado, posBtnNivel1.x, posBtnNivel1.y, btnWidth, btnHeight);
        batch.draw(nivelesDesbloqueados[1] ? btnNivel2 : btnNivelBloqueado, posBtnNivel2.x, posBtnNivel2.y, btnWidth, btnHeight);
        batch.draw(nivelesDesbloqueados[2] ? btnNivel3 : btnNivelBloqueado, posBtnNivel3.x, posBtnNivel3.y, btnWidth, btnHeight);
        batch.draw(nivelesDesbloqueados[3] ? btnNivel4 : btnNivelBloqueado, posBtnNivel4.x, posBtnNivel4.y, btnWidth, btnHeight);
        batch.draw(nivelesDesbloqueados[4] ? btnNivel5 : btnNivelBloqueado, posBtnNivel5.x, posBtnNivel5.y, btnWidth, btnHeight);

        batch.end();

        // Detectar si el usuario hace clic en algún botón desbloqueado
        if (Gdx.input.isTouched()) {
            Vector2 touchPos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            touchPos.y = Gdx.graphics.getHeight() - touchPos.y;

            // Verificar si el botón fue presionado solo si el nivel está desbloqueado
            if (nivelesDesbloqueados[0] && isButtonPressed(touchPos, posBtnNivel1, btnWidth, btnHeight)) {
                game.setScreen(new Nivel1(game));  // Pasar el objeto game al Nivel 1
            } else if (nivelesDesbloqueados[1] && isButtonPressed(touchPos, posBtnNivel2, btnWidth, btnHeight)) {
                game.setScreen(new Nivel2(game));  // Nivel 2
            } else if (nivelesDesbloqueados[2] && isButtonPressed(touchPos, posBtnNivel3, btnWidth, btnHeight)) {
                game.setScreen(new Nivel3(game));  // Nivel 3
            } else if (nivelesDesbloqueados[3] && isButtonPressed(touchPos, posBtnNivel4, btnWidth, btnHeight)) {
                game.setScreen(new Nivel4(game));  // Nivel 4
            } else if (nivelesDesbloqueados[4] && isButtonPressed(touchPos, posBtnNivel5, btnWidth, btnHeight)) {
                game.setScreen(new Nivel5(game));  // Nivel 5
            }
        }
    }

    // Método para verificar si un botón fue presionado
    private boolean isButtonPressed(Vector2 touchPos, Vector2 buttonPos, float buttonWidth, float buttonHeight) {
        return touchPos.x > buttonPos.x && touchPos.x < buttonPos.x + buttonWidth
                && touchPos.y > buttonPos.y && touchPos.y < buttonPos.y + buttonHeight;
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        btnNivel1.dispose();
        btnNivel2.dispose();
        btnNivel3.dispose();
        btnNivel4.dispose();
        btnNivel5.dispose();
        btnNivelBloqueado.dispose();  
        background.dispose(); // Disponer el fondo también
    }

    @Override
    public void show() {}
}
