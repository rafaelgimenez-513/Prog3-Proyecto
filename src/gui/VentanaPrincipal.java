package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import domain.Categoria;
import domain.Juego;
import domain.TipoUsuario;
import domain.Usuario;

public class VentanaPrincipal extends JFrame {

    private static final long serialVersionUID = 1L;

    private Usuario usuario;
    private List<Juego> listaJuegos;
    private JTabbedPane tabbedPane;

    // Paneles de los distintos roles
    private PanelTienda panelTienda;
    private PanelBiblioteca panelBiblioteca;
    private PanelRanking panelRanking;
    private PanelAdministrador panelAdmin;
    private DefaultTableModel modeloMisSubidas;

    public VentanaPrincipal(Usuario usuario, List<Juego> juegos) {
        this.usuario = usuario;
        this.listaJuegos = juegos;

        setTitle("SteamApp Store - Sesión de " + usuario.getNombre() + " (" + usuario.getTipoUsuario() + ")");
        setSize(850, 540);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 1. Barra superior con información del usuario, perfil y cerrar sesión
        JPanel barraSuperior = new JPanel(new BorderLayout());
        JLabel lblBienvenida = new JLabel("  Bienvenido, " + usuario.getNombre() + " | Rol: " + usuario.getTipoUsuario());

        JPanel panelBotonesTop = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnPerfil = new JButton("Mi Perfil");
        btnPerfil.addActionListener(e -> new PerfilDialog(this, usuario).setVisible(true));
        panelBotonesTop.add(btnPerfil);

        barraSuperior.add(lblBienvenida, BorderLayout.WEST);
        barraSuperior.add(panelBotonesTop, BorderLayout.EAST);
        add(barraSuperior, BorderLayout.NORTH);

        // 2. Contenedor central de pestañas según el TipoUsuario
        tabbedPane = new JTabbedPane();

        if (usuario.getTipoUsuario() == TipoUsuario.CLIENTE) {
            panelTienda = new PanelTienda(usuario, listaJuegos);
            panelBiblioteca = new PanelBiblioteca(usuario);
            panelRanking = new PanelRanking(listaJuegos);

            // Al terminar una descarga en Tienda, refrescar Biblioteca y Ranking
            panelTienda.setOnDescargaCompletada(() -> {
                panelBiblioteca.actualizarTabla();
                panelRanking.actualizarTabla();
            });

            tabbedPane.addTab("Tienda", panelTienda);
            tabbedPane.addTab("Mi Biblioteca", panelBiblioteca);
            tabbedPane.addTab("Ranking TOP", panelRanking);

        } else if (usuario.getTipoUsuario() == TipoUsuario.DESARROLLADOR) {
            JPanel panelDev = crearPanelDesarrollador();
            panelTienda = new PanelTienda(usuario, listaJuegos);
            panelRanking = new PanelRanking(listaJuegos);

            tabbedPane.addTab("Mis Videojuegos (Desarrollador)", panelDev);
            tabbedPane.addTab("Vista Tienda", panelTienda);
            tabbedPane.addTab("Ranking TOP", panelRanking);

        } else if (usuario.getTipoUsuario() == TipoUsuario.ADMINISTRADOR) {
            panelAdmin = new PanelAdministrador(listaJuegos);
            panelRanking = new PanelRanking(listaJuegos);

            tabbedPane.addTab("Panel Administrador", panelAdmin);
            tabbedPane.addTab("Ranking TOP", panelRanking);
        }

        // Refrescar datos automáticamente cada vez que el usuario cambie de pestaña
        tabbedPane.addChangeListener(e -> refrescarPestanaActual());

        // KeyListener para navegar por las opciones de menú con flechas izquierda/derecha (Persona 4)
        tabbedPane.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int total = tabbedPane.getTabCount();
                int actual = tabbedPane.getSelectedIndex();
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    tabbedPane.setSelectedIndex((actual + 1) % total);
                } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    tabbedPane.setSelectedIndex((actual - 1 + total) % total);
                }
            }
        });

        add(tabbedPane, BorderLayout.CENTER);
    }

    private void refrescarPestanaActual() {
        if (panelTienda != null) panelTienda.actualizarTabla();
        if (panelBiblioteca != null) panelBiblioteca.actualizarTabla();
        if (panelRanking != null) panelRanking.actualizarTabla();
        if (panelAdmin != null) panelAdmin.actualizarTabla();
        if (modeloMisSubidas != null) actualizarTablaDesarrollador();
    }

    // Vista exclusiva del desarrollador conectada al DialogSubirJuego de Persona 3
    private JPanel crearPanelDesarrollador() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        String[] columnas = { "Icono", "Nombre", "Categoría", "Peso (MB)", "Ventas" };
        modeloMisSubidas = new DefaultTableModel(columnas, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? Icon.class : Object.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable tablaDev = new JTable(modeloMisSubidas);
        tablaDev.setRowHeight(48);
        panel.add(new JScrollPane(tablaDev), BorderLayout.CENTER);

        JButton btnSubir = new JButton("+ Subir nuevo videojuego");
        btnSubir.addActionListener(e -> {
            DialogSubirJuego dialog = new DialogSubirJuego(this, usuario);
            dialog.setVisible(true);
            Juego nuevo = dialog.getJuegoCreado();
            if (nuevo != null) {
                listaJuegos.add(nuevo);
                actualizarTablaDesarrollador();
                JOptionPane.showMessageDialog(this, "\"" + nuevo.getNombre() + "\" publicado en la tienda.");
            }
        });

        JPanel panelSur = new JPanel();
        panelSur.add(btnSubir);
        panel.add(panelSur, BorderLayout.SOUTH);

        actualizarTablaDesarrollador();
        return panel;
    }

    private void actualizarTablaDesarrollador() {
        modeloMisSubidas.setRowCount(0);
        for (Juego j : listaJuegos) {
            if (j.getDesarrollador() != null && j.getDesarrollador().equals(usuario)) {
                modeloMisSubidas.addRow(new Object[] {
                        j.getIcono(), j.getNombre(), j.getCategoria(), j.getPeso(), j.getVentas()
                });
            }
        }
    }

    private static Icon cargarIcono(String nombreArchivo) {
        ImageIcon icono = new ImageIcon("resources/images/" + nombreArchivo);
        if (icono.getIconWidth() <= 0) {
            return null;
        }
        Image escalada = icono.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        return new ImageIcon(escalada);
    }

    // Punto de entrada principal de la aplicación con datos iniciales
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // 1. Usuarios de prueba (uno de cada rol)
            List<Usuario> listaUsuarios = new ArrayList<>();
            Usuario cliente = new Usuario("cliente", "1234", TipoUsuario.CLIENTE);
            Usuario dev1 = new Usuario("Valve", "1234", TipoUsuario.DESARROLLADOR);
            Usuario dev2 = new Usuario("Scott", "1234", TipoUsuario.DESARROLLADOR);
            Usuario admin = new Usuario("admin", "1234", TipoUsuario.ADMINISTRADOR);

            listaUsuarios.add(cliente);
            listaUsuarios.add(dev1);
            listaUsuarios.add(dev2);
            listaUsuarios.add(admin);

            // 2. Catálogo inicial de juegos y redes con tus iconos de resources/images
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

            // 3. Lanzar diálogo de inicio de sesión
            IniciarSesion login = new IniciarSesion(null, listaUsuarios);
            login.setVisible(true);

            Usuario logueado = login.getUsuarioLogueado();
            if (logueado != null) {
                VentanaPrincipal ventana = new VentanaPrincipal(logueado, listaJuegos);
                ventana.setVisible(true);
            } else {
                System.exit(0);
            }
        });
    }
}