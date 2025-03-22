package com.rope.Game;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Usuario{
   private static Usuario usuarioLogueado;

    // Datos del usuario
    private String nombreUsuario;
    private String contrasena;
    private String nombreCompleto;
    private Date fechaRegistro;
    private Date ultimaSesion;
    private int progresoJuego;
    private int puntajeMaximo;
    private long tiempoTotalJugado;
    private List<String> historialPartidas;
    private String avatar;
    private int ranking;
    private String idioma;
    private boolean[] nivelesDesbloqueados;

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
        this.avatar = "anonimo.png";
        this.ranking = 0;
        this.idioma = "es"; // Idioma predeterminado
        this.nivelesDesbloqueados = new boolean[]{true, false, false, false, false};
    }

    // Getters y Setters
    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
        guardarUsuario();
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
        guardarUsuario();
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
        guardarUsuario();
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
        guardarUsuario();
    }

    public Date getUltimaSesion() {
        return ultimaSesion;
    }

    public void setUltimaSesion(Date ultimaSesion) {
        this.ultimaSesion = ultimaSesion;
        guardarUsuario();
    }

    public int getProgresoJuego() {
        return progresoJuego;
    }

    public void setProgresoJuego(int progresoJuego) {
        this.progresoJuego = progresoJuego;
        guardarUsuario();
    }

    public int getPuntajeMaximo() {
        return puntajeMaximo;
    }

    public void setPuntajeMaximo(int puntajeMaximo) {
        this.puntajeMaximo = puntajeMaximo;
        guardarUsuario();
    }

    public long getTiempoTotalJugado() {
        return tiempoTotalJugado;
    }

    public void setTiempoTotalJugado(long tiempoTotalJugado) {
        this.tiempoTotalJugado = tiempoTotalJugado;
        guardarUsuario();
    }

    public List<String> getHistorialPartidas() {
        return historialPartidas;
    }

    public void setHistorialPartidas(List<String> historialPartidas) {
        this.historialPartidas = historialPartidas;
        guardarUsuario();
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
        guardarUsuario();
    }

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
        guardarUsuario();
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
        guardarUsuario();
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

    public boolean[] getNivelesDesbloqueados() {
        return nivelesDesbloqueados;
    }

    public void setNivelesDesbloqueados(boolean[] nivelesDesbloqueados) {
        this.nivelesDesbloqueados = nivelesDesbloqueados;
        guardarUsuario();
    }
    
    public void desbloquearNivel(int nivel) {
        if (nivel < nivelesDesbloqueados.length) {
            nivelesDesbloqueados[nivel] = true;
            System.out.println("Nivel " + (nivel + 1) + " desbloqueado para el usuario " + this.nombreUsuario);
            guardarUsuario();
        }
    }
    
    // Método para guardar el usuario en un archivo binario
    public void guardarUsuario() {
        String rutaBase = "C:\\Users\\Lenovo\\Desktop\\gameRope\\usuarios";
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
            raf.writeUTF(nombreUsuario);
            raf.writeUTF(contrasena);
            raf.writeUTF(nombreCompleto);
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
            raf.writeInt(nivelesDesbloqueados.length);
            for (boolean nivelDesbloqueado : nivelesDesbloqueados) {
                raf.writeBoolean(nivelDesbloqueado);
            }
            System.out.println("Usuario guardado correctamente.");
        } catch (IOException e) {
            System.out.println("Error al guardar el usuario:");
            e.printStackTrace();
        }
    }

    // Método para cargar toda la información del usuario desde el archivo binario
    public void cargarUsuarioCompleto() {
        String rutaBase = "C:\\Users\\Lenovo\\Desktop\\gameRope\\usuarios";
        String rutaArchivo = rutaBase + nombreUsuario + "\\datos_usuario.dat";
        File archivo = new File(rutaArchivo);

        if (!archivo.exists()) {
            System.out.println("El archivo no existe: " + rutaArchivo);
            return;
        }

        try (RandomAccessFile raf = new RandomAccessFile(archivo, "r")) {
            // Leer todos los campos
            this.nombreUsuario = raf.readUTF();
            this.contrasena = raf.readUTF();
            this.nombreCompleto = raf.readUTF();
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
            int numNiveles = raf.readInt();
            this.nivelesDesbloqueados = new boolean[numNiveles];
            for (int i = 0; i < numNiveles; i++) {
                this.nivelesDesbloqueados[i] = raf.readBoolean();
            }

            System.out.println("Información completa del usuario cargada correctamente.");
        } catch (IOException e) {
            System.out.println("Error al cargar la información completa del usuario:");
            e.printStackTrace();
        }
    }
}



