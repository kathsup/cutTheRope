package com.rope.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import com.badlogic.gdx.utils.ScreenUtils;




public class mapa implements Screen {
    

    private main game;
    private SpriteBatch batch;
    private Texture btnNivel1, btnNivel2, btnNivel3, btnNivel4, btnNivel5,btnSettings;
    private Texture btnNivelBloqueado;
    private Texture background; // Para el fondo
    private Vector2 posBtnNivel1, posBtnNivel2, posBtnNivel3, posBtnNivel4, posBtnNivel5,posBtnSettings;
    private float btnWidth, btnHeight;
    //private Music musica;

    public mapa(main game) {
        this.game = game;
        batch = new SpriteBatch();
        
        /*musica = Gdx.audio.newMusic(Gdx.files.internal("musica.mp3"));
        musica.setLooping(true);
        musica.setVolume(0.5f);*/

        // Cargar texturas para los botones
        btnNivel1 = new Texture("boton.png");
        btnNivel2 = new Texture("boton.png");
        btnNivel3 = new Texture("boton.png");
        btnNivel4 = new Texture("boton.png");
        btnNivel5 = new Texture("boton.png");
        btnSettings= new Texture("Settings.png");
        
        btnNivelBloqueado = new Texture("boton_bloqueado.png");

        // Cargar la textura del fondo
        background = new Texture("mapa.jpg");

        // Posiciones de los botones
        posBtnNivel1 = new Vector2(160, 200);
        posBtnNivel2 = new Vector2(400, 368);
        posBtnNivel3 = new Vector2(620, 500);
        posBtnNivel4 = new Vector2(400, 622);
        posBtnNivel5 = new Vector2(170, 675);
        posBtnSettings= new Vector2(650, 875);
        
        btnWidth = 70;  
        btnHeight = 70; 
    }

    @Override
    public void show() {
       /* Usuario usuario = Usuario.getUsuarioLogueado();
        if (usuario != null && usuario.isSonidoActivado()) {
            musica.play();
        }*/
    }
    
    @Override
public void render(float delta) {
    ScreenUtils.clear(0, 0, 0, 1);
    batch.begin();

    // Dibujar el fondo
    batch.draw(background, 0, 0);

    // Obtener los niveles desbloqueados desde game
    boolean[] nivelesDesbloqueados = game.getNivelesDesbloqueados();
    

    // Dibujar los botones de los niveles
    batch.draw(nivelesDesbloqueados[0] ? btnNivel1 : btnNivelBloqueado, posBtnNivel1.x, posBtnNivel1.y, btnWidth, btnHeight);
    batch.draw(nivelesDesbloqueados[1] ? btnNivel2 : btnNivelBloqueado, posBtnNivel2.x, posBtnNivel2.y, btnWidth, btnHeight);
    batch.draw(nivelesDesbloqueados[2] ? btnNivel3 : btnNivelBloqueado, posBtnNivel3.x, posBtnNivel3.y, btnWidth, btnHeight);
    batch.draw(nivelesDesbloqueados[3] ? btnNivel4 : btnNivelBloqueado, posBtnNivel4.x, posBtnNivel4.y, btnWidth, btnHeight);
    batch.draw(nivelesDesbloqueados[4] ? btnNivel5 : btnNivelBloqueado, posBtnNivel5.x, posBtnNivel5.y, btnWidth, btnHeight);

    // Dibujar el botón de Settings
    batch.draw(btnSettings, posBtnSettings.x, posBtnSettings.y, btnWidth, btnHeight);

    batch.end();

    // Detectar si el usuario hace clic en algún botón
    if (Gdx.input.isTouched()) {
        Vector2 touchPos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        touchPos.y = Gdx.graphics.getHeight() - touchPos.y;

        // Verificar si el botón de Settings fue presionado
        if (isButtonPressed(touchPos, posBtnSettings, btnWidth, btnHeight)) {
            game.setScreen(new SettingsScreen(game));  // Cambiar a la pantalla de Settings
        }
        // Verificar si los botones de los niveles fueron presionados
        else if (nivelesDesbloqueados[0] && isButtonPressed(touchPos, posBtnNivel1, btnWidth, btnHeight)) {
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
    public void hide() {
    //musica.stop();
    }

    @Override
    public void dispose() {
        batch.dispose();
        btnNivel1.dispose();
        btnNivel2.dispose();
        btnNivel3.dispose();
        btnNivel4.dispose();
        btnNivel5.dispose();
        btnSettings.dispose();
        btnNivelBloqueado.dispose();  
        background.dispose();
        /*if (musica != null) {
            musica.dispose();
        }*/
        
    }

    
}
