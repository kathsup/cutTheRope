package com.rope.Game;

import java.util.HashMap;
import java.util.Map;

public class IdiomaManager {

    private static IdiomaManager instancia;
    private String idiomaActual;
    private Map<String, String> textos;
    private Map<String, IdiomaListener> listeners;

    private IdiomaManager() {
        Usuario usuario = Usuario.getUsuarioLogueado();
        this.idiomaActual = (usuario != null) ? usuario.getIdioma() : "es";
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
        textos.clear();

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
        } else if (idiomaActual.equals("en")) {
            textos.put("titulo_menu_inicio", "Main Menu");
            textos.put("boton_iniciar_sesion", "Login");
            textos.put("boton_crear_cuenta", "Create Account");
            textos.put("preferencias", "Preferences");
            textos.put("mi_perfil", "My Profile");
            textos.put("ranking", "Ranking");
            textos.put("estadisticas", "Statistics");
            textos.put("regresar_mapa", "Back to Map");
            textos.put("cerrar_sesion", "Sign out");
        } else if (idiomaActual.equals("fr")) {
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
        Usuario usuario = Usuario.getUsuarioLogueado();
        if (usuario != null) {
            usuario.setIdioma(idioma);
            usuario.guardarUsuario();
        }

        this.idiomaActual = idioma;
        cargarTextos();
        notificarCambioIdioma();
    }

    public String getTexto(String clave) {
        return textos.getOrDefault(clave, "[" + clave + "]");
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
