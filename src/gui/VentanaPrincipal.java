package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
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
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import domain.Categoria;
import domain.Juego;
import domain.TipoUsuario;
import domain.Usuario;

public class VentanaPrincipal extends JFrame {

    private static final long serialVersionUID = 1L;

    private Usuario usuario;
    private List<Usuario> listaUsuarios;
    private List<Juego> listaJuegos;
    private JTabbedPane tabbedPane;

    private PanelTienda panelTienda;
    private PanelBiblioteca panelBiblioteca;
    private PanelRanking panelRanking;
    private PanelAdministrador panelAdmin;
    private DefaultTableModel modeloMisSubidas;

    public VentanaPrincipal(Usuario usuario, List<Usuario> listaUsuarios, List<Juego> juegos) {
        this.usuario = usuario;
        this.listaUsuarios = listaUsuarios;
        this.listaJuegos = juegos;

        setTitle("STEAM - " + usuario.getNombre() + " (" + usuario.getTipoUsuario() + ")");
        setSize(900, 580);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(TemaSteam.FONDO_OSCURO);

        //perfil y cerrar sesion
        JPanel barraSuperior = new JPanel(new BorderLayout());
        barraSuperior.setBackground(TemaSteam.FONDO_OSCURO);
        barraSuperior.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));

        JLabel lblBienvenida = new JLabel("STEAM  |  " + usuario.getNombre().toUpperCase() + "  [" + usuario.getTipoUsuario() + "]");
        lblBienvenida.setForeground(TemaSteam.AZUL_STEAM);
        lblBienvenida.setFont(new Font("SansSerif", Font.BOLD, 14));

        JPanel panelBotonesTop = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelBotonesTop.setBackground(TemaSteam.FONDO_OSCURO);

        JButton btnPerfil = new JButton("Mi Perfil");
        TemaSteam.estilizarBoton(btnPerfil, TemaSteam.FONDO_TARJETA);
        btnPerfil.addActionListener(e -> new PerfilDialog(this, usuario).setVisible(true));

        JButton btnCerrarSesion = new JButton("Cerrar sesión");
        TemaSteam.estilizarBoton(btnCerrarSesion, TemaSteam.ROJO_PELIGRO);
        btnCerrarSesion.addActionListener(e -> cerrarSesion());

        panelBotonesTop.add(btnPerfil);
        panelBotonesTop.add(btnCerrarSesion);

        barraSuperior.add(lblBienvenida, BorderLayout.WEST);
        barraSuperior.add(panelBotonesTop, BorderLayout.EAST);
        add(barraSuperior, BorderLayout.NORTH);

        //panel central
        tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(TemaSteam.FONDO_TARJETA);
        tabbedPane.setForeground(TemaSteam.TEXTO_BLANCO);
        tabbedPane.setFont(new Font("SansSerif", Font.BOLD, 13));

        if (usuario.getTipoUsuario() == TipoUsuario.CLIENTE) {
            panelTienda = new PanelTienda(usuario, listaJuegos);
            panelBiblioteca = new PanelBiblioteca(usuario);
            panelRanking = new PanelRanking(listaJuegos);

            panelTienda.setOnDescargaCompletada(() -> {
                panelBiblioteca.actualizarTabla();
                panelRanking.actualizarTabla();
            });
            panelBiblioteca.setOnBibliotecaModificada(() -> panelTienda.actualizarTabla());

            tabbedPane.addTab("  TIENDA  ", panelTienda);
            tabbedPane.addTab("  BIBLIOTECA  ", panelBiblioteca);
            tabbedPane.addTab("  RANKING TOP  ", panelRanking);

        } else if (usuario.getTipoUsuario() == TipoUsuario.DESARROLLADOR) {
            JPanel panelDev = crearPanelDesarrollador();
            panelTienda = new PanelTienda(usuario, listaJuegos);
            panelBiblioteca = new PanelBiblioteca(usuario);
            panelRanking = new PanelRanking(listaJuegos);

            panelTienda.setOnDescargaCompletada(() -> {
                panelBiblioteca.actualizarTabla();
                panelRanking.actualizarTabla();
            });
            panelBiblioteca.setOnBibliotecaModificada(() -> panelTienda.actualizarTabla());

            tabbedPane.addTab("  MIS PUBLICACIONES  ", panelDev);
            tabbedPane.addTab("  TIENDA  ", panelTienda);
            tabbedPane.addTab("  BIBLIOTECA  ", panelBiblioteca);
            tabbedPane.addTab("  RANKING TOP  ", panelRanking);

        } else if (usuario.getTipoUsuario() == TipoUsuario.ADMINISTRADOR) {
            panelAdmin = new PanelAdministrador(listaJuegos);
            panelRanking = new PanelRanking(listaJuegos);

            tabbedPane.addTab("  ADMINISTRADOR  ", panelAdmin);
            tabbedPane.addTab("  RANKING TOP  ", panelRanking);
        }

        tabbedPane.addChangeListener(e -> refrescarPestanaActual());

        //navegar por las pestañas con flechas izquierda/derecha
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

    private void cerrarSesion() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Quieres cerrar sesión y volver al inicio?",
                "Cerrar sesión",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            abrirLogin(listaUsuarios, listaJuegos);
        }
    }

    private void refrescarPestanaActual() {
        if (panelTienda != null) panelTienda.actualizarTabla();
        if (panelBiblioteca != null) panelBiblioteca.actualizarTabla();
        if (panelRanking != null) panelRanking.actualizarTabla();
        if (panelAdmin != null) panelAdmin.actualizarTabla();
        if (modeloMisSubidas != null) actualizarTablaDesarrollador();
    }

    private JPanel crearPanelDesarrollador() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 18, 15, 18));
        TemaSteam.estilizarPanel(panel);

        JLabel lblTitulo = new JLabel("MIS VIDEOJUEGOS PUBLICADOS");
        TemaSteam.estilizarTitulo(lblTitulo);
        panel.add(lblTitulo, BorderLayout.NORTH);

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

        JScrollPane scroll = new JScrollPane(tablaDev);
        TemaSteam.estilizarTabla(tablaDev, scroll);

        DefaultTableCellRenderer centrado = new DefaultTableCellRenderer();
        centrado.setHorizontalAlignment(SwingConstants.CENTER);
        tablaDev.getColumnModel().getColumn(2).setCellRenderer(centrado);
        tablaDev.getColumnModel().getColumn(3).setCellRenderer(centrado);
        tablaDev.getColumnModel().getColumn(4).setCellRenderer(centrado);

        panel.add(scroll, BorderLayout.CENTER);

        JButton btnSubir = new JButton("+  Subir nuevo videojuego");
        TemaSteam.estilizarBoton(btnSubir, TemaSteam.VERDE_STEAM);
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
        TemaSteam.estilizarPanel(panelSur);
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

    public static void abrirLogin(List<Usuario> listaUsuarios, List<Juego> listaJuegos) {
        IniciarSesion login = new IniciarSesion(null, listaUsuarios);
        login.setVisible(true);

        Usuario logueado = login.getUsuarioLogueado();
        if (logueado != null) {
            VentanaPrincipal ventana = new VentanaPrincipal(logueado, listaUsuarios, listaJuegos);
            ventana.setVisible(true);
        } else {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            List<Usuario> listaUsuarios = new ArrayList<>();
            Usuario cliente = new Usuario("cliente", "1234", TipoUsuario.CLIENTE);
            Usuario dev1 = new Usuario("Valve", "1234", TipoUsuario.DESARROLLADOR);
            Usuario dev2 = new Usuario("Scott", "1234", TipoUsuario.DESARROLLADOR);
            Usuario admin = new Usuario("admin", "1234", TipoUsuario.ADMINISTRADOR);

            listaUsuarios.add(cliente);
            listaUsuarios.add(dev1);
            listaUsuarios.add(dev2);
            listaUsuarios.add(admin);

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

            abrirLogin(listaUsuarios, listaJuegos);
        });
    }
}