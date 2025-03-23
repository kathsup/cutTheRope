package com.rope.Game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;



/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all
 * platforms.
 */
public class main extends Game{
    private Screen[] niveles;
    public static main instance; 
    //public boolean[] nivelesDesbloqueados;
    //private Musica musicaFondo; // Instancia de Musica
    private Music musica; 
    //public boolean sonidoActivado = true;

    @Override
    public void create() {
        //nivelesDesbloqueados = new boolean[]{true, false, false, false, false};  // Solo el nivel 1 está desbloqueado al inicio
        
        //musicaFondo = new Musica("musicaproyecto.mp3");
        //musicaFondo.reproducir(); // Iniciar la reproducción

        //setScreen(new mapa(this));  // Mostrar el mapa
        musica = Gdx.audio.newMusic(Gdx.files.internal("musica.mp3"));
         musica.setLooping(true);
        musica.setVolume(0.5f);
         /*if (sonidoActivado) {
            musica.play();
        }*/
         
         Usuario usuario = Usuario.getUsuarioLogueado();
        if (usuario != null && usuario.isSonidoActivado()) {
            iniciarMusica();
        }
        setScreen(new MenuInicio(this));  // Mostrar el mapa
        

    }
    
    public void iniciarMusica() {
        if (!musica.isPlaying()) {
            musica.play();
        }
    }

    public void pausarMusica() {
        musica.pause();
    }

    public void detenerMusica() {
        musica.stop();
    }
    
    /*public void alternarSonido() {
        sonidoActivado = !sonidoActivado;

        
        if (sonidoActivado) {
            musica.play();
        } else {
            musica.pause(); 
        }
    }*/

    // Método para desbloquear un nivel
    /*public void desbloquearNivel(int nivel) {
        if (nivel < nivelesDesbloqueados.length) {
            nivelesDesbloqueados[nivel] = true;
            System.out.println("Nivel " + (nivel + 1) + " desbloqueado!");
        }
    }*/
    
    public void desbloquearNivel(int nivel) {
        Usuario usuarioActual = Usuario.getUsuarioLogueado();
        
        if (usuarioActual != null) {
            // Desbloquear nivel para el usuario actual
            usuarioActual.desbloquearNivel(nivel);
        } else {
            System.out.println("No hay usuario logueado para guardar el progreso.");
        }
    }

    // Método para obtener los niveles desbloqueados
    public boolean[] getNivelesDesbloqueados() {
        Usuario usuarioActual = Usuario.getUsuarioLogueado();
        
        if (usuarioActual != null) {
            // Obtener los niveles desbloqueados del usuario actual
            return usuarioActual.getNivelesDesbloqueados();
        } else {
            // Si no hay usuario logueado, devolvemos valores por defecto
            return new boolean[]{true, false, false, false, false};
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        if (musica != null) {
            musica.dispose();
        }
        
    }
    
}
