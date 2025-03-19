/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package musica;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class Musica {
    private Music music;

    public Musica(String filePath) {
        music = Gdx.audio.newMusic(Gdx.files.internal(filePath));
    }

    public void reproducir() {
        if (music != null) {
            music.setLooping(true);
            music.play();
        }
    }

    public void pausar() {
        if (music != null && music.isPlaying()) {
            music.pause();
        }
    }

    public void detener() {
        if (music != null && music.isPlaying()) {
            music.stop();
        }
    }

    public void liberarRecursos() {
        if (music != null) {
            music.dispose();
            music = null;
        }
    }

    public boolean estaReproduciendo() {
        return music != null && music.isPlaying();
    }
}