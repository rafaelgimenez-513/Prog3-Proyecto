package domain;

public class Usuario {

    private String nombre;
    private String contraseña;
    private TipoUsuario tipoUsuario;

    public Usuario(String nombre, String contraseña, TipoUsuario tipoUsuario) {
        super();
        this.nombre = nombre;
        this.contraseña = contraseña;
        this.tipoUsuario = tipoUsuario;
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

    @Override
    public String toString() {
        return "Usuario [nombre=" + nombre + ", contraseña=" + contraseña + ", tipoUsuario=" + tipoUsuario + "]";
    }



}