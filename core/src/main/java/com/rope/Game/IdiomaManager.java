package com.rope.Game;

import java.util.HashMap;
import java.util.Map;

public class IdiomaManager {

    private static IdiomaManager instancia;
    private String idiomaActual;
    private Map<String, String> textos;
    private Map<String, IdiomaListener> listeners;

    private IdiomaManager() {
        this.idiomaActual = "es"; // Idioma predeterminado
        this.textos = new HashMap<>();
        this.listeners = new HashMap<>();
        cargarTextos();
    }

    public static IdiomaManager getInstancia() {
        if (instancia == null) {
            instancia = new IdiomaManager();
        }
        return instancia;
    }

    private void cargarTextos() {
        // Limpiar los textos anteriores
        textos.clear();

        // Textos en español
        if (idiomaActual.equals("es")) {
            textos.put("titulo_menu_inicio", "Menú de Inicio");
            textos.put("boton_iniciar_sesion", "Iniciar Sesión");
            textos.put("boton_crear_cuenta", "Crear Cuenta");
            textos.put("preferencias", "Preferencias");
            textos.put("mi_perfil", "Mi Perfil");
            textos.put("ranking", "Ranking");
            textos.put("estadisticas", "Estadísticas");
            textos.put("regresar_mapa", "Regresar al Mapa");
            textos.put("cerrar_sesion", "Cerrar sesión");
        }
        // Textos en inglés
        else if (idiomaActual.equals("en")) {
            textos.put("titulo_menu_inicio", "Main Menu");
            textos.put("boton_iniciar_sesion", "Login");
            textos.put("boton_crear_cuenta", "Create Account");
            textos.put("preferencias", "Preferences");
            textos.put("mi_perfil", "My Profile");
            textos.put("ranking", "Ranking");
            textos.put("estadisticas", "Statistics");
            textos.put("regresar_mapa", "Back to Map");
            textos.put("cerrar_sesion", "Sign out");
        }
        // Textos en francés
        else if (idiomaActual.equals("fr")) {
            textos.put("titulo_menu_inicio", "Menu Principal");
            textos.put("boton_iniciar_sesion", "Connexion");
            textos.put("boton_crear_cuenta", "Créer un Compte");
            textos.put("preferencias", "Préférences");
            textos.put("mi_perfil", "Mon Profil");
            textos.put("ranking", "Classement");
            textos.put("estadisticas", "Statistiques");
            textos.put("regresar_mapa", "Retour à la carte");
            textos.put("cerrar_sesion", "Déconnexion");
        }
    }

    public void cambiarIdioma(String idioma) {
        this.idiomaActual = idioma;
        cargarTextos(); // Recargar los textos con el nuevo idioma
        notificarCambioIdioma();
    }

    public String getTexto(String clave) {
        return textos.getOrDefault(clave, "[" + clave + "]"); // Devuelve el texto o una clave entre corchetes si no se encuentra
    }

    public String getIdiomaActual() {
        return idiomaActual;
    }

    public void agregarListener(String nombre, IdiomaListener listener) {
        listeners.put(nombre, listener);
    }

    public void removerListener(String nombre) {
        listeners.remove(nombre);
    }

    private void notificarCambioIdioma() {
        for (IdiomaListener listener : listeners.values()) {
            listener.onIdiomaCambiado(idiomaActual);
        }
    }

    public interface IdiomaListener {
        void onIdiomaCambiado(String nuevoIdioma);
    }
}