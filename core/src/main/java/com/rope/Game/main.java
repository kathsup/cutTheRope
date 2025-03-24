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
        // Inicializar la música, pero no reproducirla todavía
        musica = Gdx.audio.newMusic(Gdx.files.internal("musica.mp3"));
        musica.setLooping(true);
        musica.setVolume(0.5f);

        // Mostrar la pantalla de inicio
        setScreen(new MenuInicio(this));
    }

    // Método para iniciar la música si el usuario tiene habilitado el sonido
    public void iniciarMusica() {
        Usuario usuario = Usuario.getUsuarioLogueado();
        if (usuario != null && usuario.isSonidoActivado()) {
            musica.play(); // Reproducir música si el sonido está activado
        } else {
            musica.stop(); // Detener la música si el sonido está desactivado
        }
    }

    // Método para detener la música
    public void detenerMusica() {
        musica.stop();
    }
    
    // Método para pausar la música
    public void pausarMusica() {
        musica.pause();
    }

    // Método para reanudar la música
    public void reanudarMusica() {
        Usuario usuario = Usuario.getUsuarioLogueado();
        if (usuario != null && usuario.isSonidoActivado()) {
            musica.play();
        }
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
            usuarioActual.guardarCambios();
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
