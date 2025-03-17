/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.rope.Game;

/**
 *
 * @author fdhg0
 */
import java.io.Serializable;

public class Preferencias implements Serializable {
    private static final long serialVersionUID = 1L;

    private int volumen;
    private String idioma;
    private String controles;

    public Preferencias() {
        this.volumen = 50; // Valor por defecto
        this.idioma = "español"; // Valor por defecto
        this.controles = "teclado"; // Valor por defecto
    }

    // Getters y Setters
    public int getVolumen() {
        return volumen;
    }

    public void setVolumen(int volumen) {
        this.volumen = volumen;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public String getControles() {
        return controles;
    }

    public void setControles(String controles) {
        this.controles = controles;
    }
}

