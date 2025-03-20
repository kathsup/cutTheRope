package com.rope.Game;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Usuario {

    private static Usuario usuarioLogueado;

    // Datos del usuario
    private String nombreUsuario;
    private String contrasena;
    private String nombreCompleto;
    private Date fechaRegistro;
    private Date ultimaSesion;
    private int progresoJuego; // Niveles completados
    private int puntajeMaximo;
    private long tiempoTotalJugado; // En segundos
    private List<String> historialPartidas; // Registro de partidas
    private String avatar; // Ruta de la imagen de perfil
    private int ranking; // Puntuación general
    private String idioma; // Idioma preferido

    // Constructor
    public Usuario(String nombreUsuario, String contrasena, String nombreCompleto) {
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.nombreCompleto = nombreCompleto;
        this.fechaRegistro = new Date(); // Fecha actual
        this.ultimaSesion = new Date(); // Fecha actual
        this.progresoJuego = 0; // Valor predeterminado
        this.puntajeMaximo = 0; // Valor predeterminado
        this.tiempoTotalJugado = 0; // Valor predeterminado
        this.historialPartidas = new ArrayList<>(); // Lista vacía
        this.avatar = "anonimo.png"; // Valor predeterminado
        this.ranking = 0; // Valor predeterminado
        this.idioma = "es"; // Valor predeterminado
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

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    // Métodos de sesión
    public boolean iniciarSesion(String contrasena) {
        if (this.contrasena.equals(contrasena)) {
            Usuario.usuarioLogueado = this;
            return true;
        }
        return false;
    }

    public static void cerrarSesion() {
        Usuario.usuarioLogueado = null;
    }

    public static Usuario getUsuarioLogueado() {
        return Usuario.usuarioLogueado;
    }

    // Método para agregar una partida al historial
    public void agregarPartida(String partida) {
        this.historialPartidas.add(partida);
    }

    // Método para guardar el usuario en un archivo binario
    public void guardarUsuario() {
    String rutaBase = "C:\\Users\\fdhg0\\Documents\\NetBeansProjects\\cutTheRope-master\\usuarios\\";
    String rutaCarpeta = rutaBase + nombreUsuario + "\\";
    File carpeta = new File(rutaCarpeta);

    if (!carpeta.exists() && !carpeta.mkdirs()) {
        System.out.println("Error al crear la carpeta.");
        return;
    }

    String rutaArchivo = rutaCarpeta + "datos_usuario.dat";
    System.out.println("Guardando archivo en: " + rutaArchivo);

    try (RandomAccessFile raf = new RandomAccessFile(rutaArchivo, "rw")) {
        // Guardar todos los campos
        raf.writeUTF(nombreUsuario); // Guardar nombre de usuario
        raf.writeUTF(contrasena);    // Guardar contraseña
        raf.writeUTF(nombreCompleto); // Guardar nombre completo
        raf.writeLong(fechaRegistro.getTime());
        raf.writeLong(ultimaSesion.getTime());
        raf.writeInt(progresoJuego);
        raf.writeInt(puntajeMaximo);
        raf.writeLong(tiempoTotalJugado);
        raf.writeInt(historialPartidas.size());
        for (String partida : historialPartidas) {
            raf.writeUTF(partida);
        }
        raf.writeUTF(avatar);
        raf.writeInt(ranking);
        raf.writeUTF(idioma);
        System.out.println("Usuario guardado correctamente.");
    } catch (IOException e) {
        System.out.println("Error al guardar el usuario:");
        e.printStackTrace();
    }
}

    // Método para cargar solo la información esencial (login)
    public static Usuario cargarUsuarioLogin(String nombreUsuario) {
        String rutaBase = "C:\\Users\\fdhg0\\Documents\\NetBeansProjects\\cutTheRope-master\\usuarios\\";
        String rutaArchivo = rutaBase + nombreUsuario + "\\datos_usuario.dat";
        File archivo = new File(rutaArchivo);

        if (!archivo.exists()) {
            System.out.println("El archivo no existe: " + rutaArchivo);
            return null;
        }

        try (RandomAccessFile raf = new RandomAccessFile(archivo, "r")) {
            // Leer solo los campos esenciales
            String nombreCompleto = raf.readUTF();
            String contrasena = raf.readUTF();

            // Crear el objeto Usuario con valores predeterminados
            Usuario usuario = new Usuario(nombreUsuario, contrasena, nombreCompleto);
            System.out.println("Usuario cargado correctamente (login).");
            return usuario;
        } catch (IOException e) {
            System.out.println("Error al cargar el usuario (login):");
            e.printStackTrace();
            return null;
        }
    }

    // Método para cargar toda la información del usuario (perfil)
    public void cargarUsuarioCompleto() {
        String rutaBase = "C:\\Users\\fdhg0\\Documents\\NetBeansProjects\\cutTheRope-master\\usuarios\\";
        String rutaArchivo = rutaBase + nombreUsuario + "\\datos_usuario.dat";
        File archivo = new File(rutaArchivo);

        if (!archivo.exists()) {
            System.out.println("El archivo no existe: " + rutaArchivo);
            return;
        }

        try (RandomAccessFile raf = new RandomAccessFile(archivo, "r")) {
            // Saltar los campos esenciales (ya cargados en el login)
            raf.readUTF(); // nombreUsuario
            raf.readUTF(); // contrasena
            raf.readUTF(); // nombreCompleto

            // Cargar el resto de la información
            this.fechaRegistro = new Date(raf.readLong());
            this.ultimaSesion = new Date(raf.readLong());
            this.progresoJuego = raf.readInt();
            this.puntajeMaximo = raf.readInt();
            this.tiempoTotalJugado = raf.readLong();
            int numPartidas = raf.readInt();
            this.historialPartidas = new ArrayList<>();
            for (int i = 0; i < numPartidas; i++) {
                this.historialPartidas.add(raf.readUTF());
            }
            this.avatar = raf.readUTF();
            this.ranking = raf.readInt();
            this.idioma = raf.readUTF();

            System.out.println("Información completa del usuario cargada correctamente.");
        } catch (IOException e) {
            System.out.println("Error al cargar la información completa del usuario:");
            e.printStackTrace();
        }
    }

    // Método para verificar la contraseña
    public static boolean verificarContrasena(String nombreUsuario, String contrasena) {
        String rutaBase = "C:\\Users\\fdhg0\\Documents\\NetBeansProjects\\cutTheRope-master\\usuarios\\";
        String rutaArchivo = rutaBase + nombreUsuario + "\\datos_usuario.dat";
        File archivo = new File(rutaArchivo);

        if (!archivo.exists()) {
            System.out.println("El archivo no existe: " + rutaArchivo);
            return false;
        }

        try (RandomAccessFile raf = new RandomAccessFile(archivo, "r")) {
            raf.readUTF(); // Ignorar nombre de usuario
            String contrasenaArchivo = raf.readUTF();
            return contrasenaArchivo.equals(contrasena);
        } catch (IOException e) {
            System.out.println("Error al verificar la contraseña:");
            e.printStackTrace();
            return false;
        }
    }
}
