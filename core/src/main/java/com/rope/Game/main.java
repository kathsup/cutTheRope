package com.rope.Game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.sun.tools.javac.Main;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;



/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all
 * platforms.
 */
public class main extends Game{
    private Screen[] niveles;
    public static main instance; 
    public boolean[] nivelesDesbloqueados;
    //private Musica musicaFondo; // Instancia de Musica
    private Music musica; 
    public boolean sonidoActivado = true;

    @Override
    public void create() {
        nivelesDesbloqueados = new boolean[]{true, false, false, false, false};  // Solo el nivel 1 está desbloqueado al inicio
        
        //musicaFondo = new Musica("musicaproyecto.mp3");
        //musicaFondo.reproducir(); // Iniciar la reproducción

        //setScreen(new mapa(this));  // Mostrar el mapa
        musica = Gdx.audio.newMusic(Gdx.files.internal("musica.mp3"));
         musica.setLooping(true);
        musica.setVolume(0.5f);
         if (sonidoActivado) {
            musica.play();
        }
        setScreen(new MenuInicio(this));  // Mostrar el mapa
        

    }
    
    public void alternarSonido() {
        sonidoActivado = !sonidoActivado;

        
        if (sonidoActivado) {
            musica.play();
        } else {
            musica.pause(); 
        }
    }

    // Método para desbloquear un nivel
    public void desbloquearNivel(int nivel) {
        if (nivel < nivelesDesbloqueados.length) {
            nivelesDesbloqueados[nivel] = true;
            System.out.println("Nivel " + (nivel + 1) + " desbloqueado!");
        }
    }

    // Método para obtener los niveles desbloqueados
    public boolean[] getNivelesDesbloqueados() {
        return nivelesDesbloqueados;
    }

    @Override
    public void dispose() {
        super.dispose();
        if (musica != null) {
            musica.dispose();
        }
        
    }
    
}
