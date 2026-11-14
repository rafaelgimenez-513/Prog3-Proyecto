package domain;

import java.util.List;
import java.util.ArrayList;

public class Usuario {

    private String nombre;
    private String contraseña;
    private TipoUsuario tipoUsuario;
    private ArrayList<Juego> biblioteca;

    public Usuario(String nombre, String contraseña, TipoUsuario tipoUsuario) {
        super();
        this.nombre = nombre;
        this.contraseña = contraseña;
        this.tipoUsuario = tipoUsuario;
        this.biblioteca = new ArrayList<>();
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
    
    

    @Override
    public String toString() {
        return "Usuario [nombre=" + nombre + ", contraseña=" + contraseña + ", tipoUsuario=" + tipoUsuario + "]";
    }



}