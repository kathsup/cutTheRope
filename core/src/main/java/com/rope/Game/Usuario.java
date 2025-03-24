package com.rope.Game;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Usuario {

    private static Usuario usuarioLogueado;

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
    private boolean[] nivelesCompletados;
    private boolean sonidoActivado;

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
        this.idioma = "es";
        this.nivelesDesbloqueados = new boolean[]{true, false, false, false, false};
        this.nivelesCompletados = new boolean[]{false, false, false, false, false};
        this.sonidoActivado = true;

    }

    public boolean isSonidoActivado() {
        return sonidoActivado;
    }

    public void setSonidoActivado(boolean sonidoActivado) {
        this.sonidoActivado = sonidoActivado;
    }

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
            this.ultimaSesion = new Date();
            guardarUsuario();
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
    }

    public void desbloquearNivel(int nivel) {
        if (nivel < nivelesDesbloqueados.length) {
            nivelesDesbloqueados[nivel] = true;
        }
    }

    public boolean[] getNivelesCompletados() {
        return nivelesCompletados;
    }

    public void setNivelesCompletados(boolean[] nivelesCompletados) {
        this.nivelesCompletados = nivelesCompletados;
    }

    public void marcarNivelComoCompletado(int indiceNivel) {
        if (indiceNivel >= 0 && indiceNivel < nivelesCompletados.length) {
            nivelesCompletados[indiceNivel] = true;
            guardarUsuario();
        }
    }

    public void guardarCambios() {
        guardarUsuario();
    }

    public void guardarUsuario() {
        String rutaBase = "\\Users\\fdhg0\\Documents\\NetBeansProjects\\cutTheRope-master\\usuarios\\";
        String rutaCarpeta = rutaBase + nombreUsuario + "\\";
        File carpeta = new File(rutaCarpeta);

        if (!carpeta.exists() && !carpeta.mkdirs()) {
            return;
        }

        String rutaArchivo = rutaCarpeta + "datos_usuario.dat";

        try (RandomAccessFile raf = new RandomAccessFile(rutaArchivo, "rw")) {
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
            raf.writeInt(nivelesCompletados.length);
            for (boolean nivelCompletado : nivelesCompletados) {
                raf.writeBoolean(nivelCompletado);
            }
            raf.writeBoolean(sonidoActivado);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cargarUsuario() {
        String rutaBase = "C:\\Users\\fdhg0\\Documents\\NetBeansProjects\\cutTheRope-master\\usuarios\\";
        String rutaArchivo = rutaBase + nombreUsuario + "\\datos_usuario.dat";
        File archivo = new File(rutaArchivo);

        if (!archivo.exists()) {
            System.out.println("El archivo no existe: " + rutaArchivo);
            return;
        }

        try (RandomAccessFile raf = new RandomAccessFile(archivo, "r")) {
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
            int numNivelesCompletados = raf.readInt();
            this.nivelesCompletados = new boolean[numNivelesCompletados];
            for (int i = 0; i < numNivelesCompletados; i++) {
                this.nivelesCompletados[i] = raf.readBoolean();
            }
            this.sonidoActivado = raf.readBoolean();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void registrarPartidaJugada(int nivel, int puntosObtenidos, long tiempoJugado) {
        String registro = "Nivel" + nivel + "-" + puntosObtenidos + "pts-" + formatearTiempo(tiempoJugado);

        if (this.historialPartidas == null) {
            this.historialPartidas = new ArrayList<>();
        }

        this.historialPartidas.add(registro);
        this.tiempoTotalJugado += tiempoJugado;

    }

    private String formatearTiempo(long milisegundos) {
        long segundos = milisegundos / 1000;
        long minutos = segundos / 60;
        segundos %= 60;

        return String.format("%02dm%02ds", minutos, segundos);
    }
}
