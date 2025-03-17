/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.rope.Game;

/**
 *
 * @author fdhg0
 */
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    // Datos del usuario
    private String nombreUsuario; // Identificador único
    private String contrasena; // Almacenada de manera segura (encriptada en un caso real)
    private String nombreCompleto;
    private Date fechaRegistro;
    private Date ultimaSesion;
    private int progresoJuego; // Niveles completados
    private int puntajeMaximo;
    private long tiempoTotalJugado; // En segundos
    private List<String> historialPartidas; // Registro de partidas
    private Preferencias preferencias; // Preferencias de juego
    private String avatar; // Ruta de la imagen de perfil
    private int ranking; // Puntuación general

    // Constructor
    public Usuario(String nombreUsuario, String contrasena, String nombreCompleto) {
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.nombreCompleto = nombreCompleto;
        this.fechaRegistro = new Date();
        this.ultimaSesion = new Date();
        this.progresoJuego = 0;
        this.puntajeMaximo = 0;
        this.tiempoTotalJugado = 0;
        this.historialPartidas = new ArrayList<>();
        this.preferencias = new Preferencias();
        this.avatar = "default_avatar.png"; // Ruta por defecto
        this.ranking = 0;
    }

    // Getters y Setters
    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Date getUltimaSesion() {
        return ultimaSesion;
    }

    public void setUltimaSesion(Date ultimaSesion) {
        this.ultimaSesion = ultimaSesion;
    }

    public int getProgresoJuego() {
        return progresoJuego;
    }

    public void setProgresoJuego(int progresoJuego) {
        this.progresoJuego = progresoJuego;
    }

    public int getPuntajeMaximo() {
        return puntajeMaximo;
    }

    public void setPuntajeMaximo(int puntajeMaximo) {
        this.puntajeMaximo = puntajeMaximo;
    }

    public long getTiempoTotalJugado() {
        return tiempoTotalJugado;
    }

    public void setTiempoTotalJugado(long tiempoTotalJugado) {
        this.tiempoTotalJugado = tiempoTotalJugado;
    }

    public List<String> getHistorialPartidas() {
        return historialPartidas;
    }

    public void setHistorialPartidas(List<String> historialPartidas) {
        this.historialPartidas = historialPartidas;
    }

    public Preferencias getPreferencias() {
        return preferencias;
    }

    public void setPreferencias(Preferencias preferencias) {
        this.preferencias = preferencias;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    // Método para agregar una partida al historial
    public void agregarPartida(String partida) {
        this.historialPartidas.add(partida);
    }

    // Método para guardar el usuario en un archivo binario usando RandomAccessFile
    public void guardarUsuario() {
        // Nombre de la carpeta directamente
        String rutaCarpeta = "usuarios/" + nombreUsuario + "/";
        File carpeta = new File(rutaCarpeta);

        // Crear la carpeta si no existe
        if (!carpeta.exists()) {
            System.out.println("Creando carpeta para el usuario: " + carpeta.getAbsolutePath());
            if (carpeta.mkdirs()) {
                System.out.println("Carpeta creada exitosamente.");
            } else {
                System.out.println("Error al crear la carpeta.");
                return;
            }
        }

        // Ruta del archivo
        String rutaArchivo = rutaCarpeta + "datos_usuario.dat";
        System.out.println("Guardando archivo en: " + rutaArchivo);

        try (RandomAccessFile raf = new RandomAccessFile(rutaArchivo, "rw")) {
            // Escribir datos en el archivo
            raf.writeUTF(nombreUsuario);
            raf.writeUTF(contrasena);
            raf.writeUTF(nombreCompleto);
            raf.writeLong(fechaRegistro.getTime());
            raf.writeLong(ultimaSesion.getTime());
            raf.writeInt(progresoJuego);
            raf.writeInt(puntajeMaximo);
            raf.writeLong(tiempoTotalJugado);

            // Escribir historial de partidas
            raf.writeInt(historialPartidas.size());
            for (String partida : historialPartidas) {
                raf.writeUTF(partida);
            }

            // Escribir preferencias
            raf.writeInt(preferencias.getVolumen());
            raf.writeUTF(preferencias.getIdioma());
            raf.writeUTF(preferencias.getControles());

            // Escribir avatar y ranking
            raf.writeUTF(avatar);
            raf.writeInt(ranking);

            System.out.println("Usuario guardado correctamente.");
        } catch (IOException e) {
            System.out.println("Error al guardar el usuario:");
            e.printStackTrace();
        }
    }

    // Método para cargar un usuario desde un archivo binario usando RandomAccessFile
    public static Usuario cargarUsuario(String nombreUsuario) {
        // Nombre de la carpeta directamente
        String rutaCarpeta = "usuarios/" + nombreUsuario + "/";
        String rutaArchivo = rutaCarpeta + "datos_usuario.dat";
        File archivo = new File(rutaArchivo);

        // Verificar si el archivo existe
        if (!archivo.exists()) {
            System.out.println("El archivo no existe: " + rutaArchivo);
            return null;
        }

        System.out.println("Cargando archivo desde: " + rutaArchivo);

        try (RandomAccessFile raf = new RandomAccessFile(archivo, "r")) {
            // Leer datos del archivo
            String contrasena = raf.readUTF();
            String nombreCompleto = raf.readUTF();
            Date fechaRegistro = new Date(raf.readLong());
            Date ultimaSesion = new Date(raf.readLong());
            int progresoJuego = raf.readInt();
            int puntajeMaximo = raf.readInt();
            long tiempoTotalJugado = raf.readLong();

            // Leer historial de partidas
            int numPartidas = raf.readInt();
            List<String> historialPartidas = new ArrayList<>();
            for (int i = 0; i < numPartidas; i++) {
                historialPartidas.add(raf.readUTF());
            }

            // Leer preferencias
            int volumen = raf.readInt();
            String idioma = raf.readUTF();
            String controles = raf.readUTF();

            // Leer avatar y ranking
            String avatar = raf.readUTF();
            int ranking = raf.readInt();

            // Crear el objeto Usuario
            Usuario usuario = new Usuario(nombreUsuario, contrasena, nombreCompleto);
            usuario.setFechaRegistro(fechaRegistro);
            usuario.setUltimaSesion(ultimaSesion);
            usuario.setProgresoJuego(progresoJuego);
            usuario.setPuntajeMaximo(puntajeMaximo);
            usuario.setTiempoTotalJugado(tiempoTotalJugado);
            usuario.setHistorialPartidas(historialPartidas);
            usuario.getPreferencias().setVolumen(volumen);
            usuario.getPreferencias().setIdioma(idioma);
            usuario.getPreferencias().setControles(controles);
            usuario.setAvatar(avatar);
            usuario.setRanking(ranking);

            System.out.println("Usuario cargado correctamente.");
            return usuario;
        } catch (IOException e) {
            System.out.println("Error al cargar el usuario:");
            e.printStackTrace();
            return null;
        }
    }
}
