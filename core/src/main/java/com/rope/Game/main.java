package com.rope.Game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;


/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all
 * platforms.
 */
public class main extends Game{
    private Screen[] niveles;
    public static main instance; 
    public boolean[] nivelesDesbloqueados;

    @Override
    public void create() {
        nivelesDesbloqueados = new boolean[]{true, false, false, false, false};  // Solo el nivel 1 está desbloqueado al inicio
        setScreen(new MenuInicio(this));  // Mostrar el mapa
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

    
}
