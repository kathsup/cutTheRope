package com.rope.Game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;

public class main extends Game {

    private Screen[] niveles;
    public static main instance;

    private Music musica;

    @Override
    public void create() {
        musica = Gdx.audio.newMusic(Gdx.files.internal("musica.mp3"));
        musica.setLooping(true);
        musica.setVolume(0.5f);

        setScreen(new MenuInicio(this));
    }

    public void iniciarMusica() {
        Usuario usuario = Usuario.getUsuarioLogueado();
        if (usuario != null && usuario.isSonidoActivado()) {
            musica.play();
        } else {
            musica.stop();
        }
    }

    public void detenerMusica() {
        musica.stop();
    }

    public void pausarMusica() {
        musica.pause();
    }

    public void reanudarMusica() {
        Usuario usuario = Usuario.getUsuarioLogueado();
        if (usuario != null && usuario.isSonidoActivado()) {
            musica.play();
        }
    }

    public void desbloquearNivel(int nivel) {
        Usuario usuarioActual = Usuario.getUsuarioLogueado();

        if (usuarioActual != null) {
            usuarioActual.desbloquearNivel(nivel);
            usuarioActual.guardarCambios();
        } else {
        }
    }

    public boolean[] getNivelesDesbloqueados() {
        Usuario usuarioActual = Usuario.getUsuarioLogueado();

        if (usuarioActual != null) {
            return usuarioActual.getNivelesDesbloqueados();
        } else {
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
