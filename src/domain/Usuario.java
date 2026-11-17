package domain;

import java.util.List;

import javax.swing.Icon;

import java.util.ArrayList;

public class Usuario {

    private String nombre;
    private String contraseña;
    private TipoUsuario tipoUsuario;
    private ArrayList<Juego> biblioteca;
    
    private Icon avatar;
    private String biografia;
    private String estadoConexion;
    

    public Usuario(String nombre, String contraseña, TipoUsuario tipoUsuario) {
        super();
        this.nombre = nombre;
        this.contraseña = contraseña;
        this.tipoUsuario = tipoUsuario;
        this.biblioteca = new ArrayList<>();
        this.biografia = "Sin información adicional.";
        this.estadoConexion = "En línea";
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getContraseña() {
        return contraseña;
    }
    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }
    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }
    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }
    public List<Juego> getBiblioteca() {
    	return biblioteca;
    }
    public void agregarJuegoBiblioteca(Juego juego) {
        if (!biblioteca.contains(juego)) {
            biblioteca.add(juego);
        }
    }
    public void eliminarJuegoBiblioteca(Juego juego) {
    	biblioteca.remove(juego);
    }
    public Icon getAvatar() {
        return avatar;
    }

    public void setAvatar(Icon avatar) {
        this.avatar = avatar;
    }

    public String getBiografia() {
        return biografia;
    }

    public void setBiografia(String biografia) {
        this.biografia = biografia;
    }

    public String getEstadoConexion() {
        return estadoConexion;
    }

    public void setEstadoConexion(String estadoConexion) {
        this.estadoConexion = estadoConexion;
    }
    

    @Override
    public String toString() {
        return "Usuario [nombre=" + nombre + ", contraseña=" + contraseña + ", tipoUsuario=" + tipoUsuario + "]";
    }



}