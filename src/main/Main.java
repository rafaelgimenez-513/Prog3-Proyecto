package main;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;

import domain.Categoria;
import domain.Juego;
import domain.TipoUsuario;
import domain.Usuario;
import gui.VentanaPrincipal;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            //Usuarios
            List<Usuario> listaUsuarios = new ArrayList<>();
            Usuario cliente = new Usuario("cliente", "1234", TipoUsuario.CLIENTE);
            Usuario dev1 = new Usuario("Valve", "1234", TipoUsuario.DESARROLLADOR);
            Usuario dev2 = new Usuario("Scott", "1234", TipoUsuario.DESARROLLADOR);
            Usuario admin = new Usuario("admin", "1234", TipoUsuario.ADMINISTRADOR);

            listaUsuarios.add(cliente);
            listaUsuarios.add(dev1);
            listaUsuarios.add(dev2);
            listaUsuarios.add(admin);

            //juegos y redes
            List<Juego> listaJuegos = new ArrayList<>();
            listaJuegos.add(new Juego(cargarIcono("minecraft.png"), "Minecraft", 520.0, Categoria.JUEGO, dev1, 340, 1500));
            listaJuegos.add(new Juego(cargarIcono("counter-strike.png"), "Counter-Strike", 850.0, Categoria.JUEGO, dev1, 510, 1280));
            listaJuegos.add(new Juego(cargarIcono("dragon-quest.png"), "Dragon Quest", 640.0, Categoria.JUEGO, dev1, 95, 430));
            listaJuegos.add(new Juego(cargarIcono("discord.png"), "Discord", 140.0, Categoria.REDES, dev1, 890, 2100));
            listaJuegos.add(new Juego(cargarIcono("subnautica.png"), "Subnautica", 710.0, Categoria.JUEGO, dev1, 130, 620));
            listaJuegos.add(new Juego(cargarIcono("youtube.png"), "YouTube", 95.0, Categoria.REDES, dev1, 1200, 3400));
            listaJuegos.add(new Juego(cargarIcono("fortnite.png"), "Fortnite", 920.0, Categoria.JUEGO, dev1, 780, 1950));
            listaJuegos.add(new Juego(cargarIcono("fnaf2.png"), "Five Nights at Freddy's 2", 260.0, Categoria.JUEGO, dev2, 85, 740));
            listaJuegos.add(new Juego(cargarIcono("fnaf3.png"), "Five Nights at Freddy's 3", 290.0, Categoria.JUEGO, dev2, 70, 680));
            listaJuegos.add(new Juego(cargarIcono("fnaf4.png"), "Five Nights at Freddy's 4", 330.0, Categoria.JUEGO, dev2, 110, 810));

            //inicio de sesión
            VentanaPrincipal.abrirLogin(listaUsuarios, listaJuegos);
        });
    }

    private static Icon cargarIcono(String nombreArchivo) {
        ImageIcon icono = new ImageIcon("resources/images/" + nombreArchivo);
        if (icono.getIconWidth() <= 0) {
            return null;
        }
        Image escalada = icono.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        return new ImageIcon(escalada);
    }
}