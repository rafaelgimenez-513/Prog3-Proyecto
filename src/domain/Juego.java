package domain;

import javax.swing.Icon;

public class Juego {
    private Icon icono;
    private String nombre;
    private double peso;
    private Categoria categoria;
    private Usuario desarrollador;
    private int jugadoresSimultaneos;
    private int ventas;
    public Juego(Icon icono, String nombre, double peso, Categoria categoria, Usuario desarrollador,
            int jugadoresSimultaneos, int ventas) {
        super();
        this.icono = icono;
        this.nombre = nombre;
        this.peso = peso;
        this.categoria = categoria;
        this.desarrollador = desarrollador;
        this.jugadoresSimultaneos = jugadoresSimultaneos;
        this.ventas = ventas;
    }
    public Icon getIcono() {
        return icono;
    }
    public void setIcono(Icon icono) {
        this.icono = icono;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public double getPeso() {
        return peso;
    }
    public void setPeso(double peso) {
        this.peso = peso;
    }
    public Categoria getCategoria() {
        return categoria;
    }
    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
    public Usuario getDesarrollador() {
        return desarrollador;
    }
    public void setDesarrollador(Usuario desarrollador) {
        this.desarrollador = desarrollador;
    }
    public int getJugadoresSimultaneos() {
        return jugadoresSimultaneos;
    }
    public void setJugadoresSimultaneos(int jugadoresSimultaneos) {
        this.jugadoresSimultaneos = jugadoresSimultaneos;
    }
    public int getVentas() {
        return ventas;
    }
    public void setVentas(int ventas) {
        this.ventas = ventas;
    }
    @Override
    public String toString() {
        return "Juego [icono=" + icono + ", nombre=" + nombre + ", peso=" + peso + ", categoria=" + categoria
                + ", desarrollador=" + desarrollador + ", jugadoresSimultaneos=" + jugadoresSimultaneos + ", ventas="
                + ventas + "]";
    }



}