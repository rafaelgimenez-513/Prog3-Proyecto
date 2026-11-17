package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import domain.Categoria;
import domain.Juego;
import domain.TipoUsuario;
import domain.Usuario;

public class PerfilDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    private Usuario usuario;
    private List<Juego> listaJuegos;
    private JLabel lblAvatar;
    private JTextArea txtBiografia;

    public PerfilDialog(Frame parent, Usuario usuario, List<Juego> listaJuegos) {
        super(parent, "Perfil de Steam - " + usuario.getNombre(), true);
        this.usuario = usuario;
        this.listaJuegos = listaJuegos;

        setSize(580, 620);
        setResizable(false);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(12, 12));
        getContentPane().setBackground(TemaSteam.FONDO_PANEL);

        JPanel contenedorPrincipal = new JPanel();
        contenedorPrincipal.setLayout(new BoxLayout(contenedorPrincipal, BoxLayout.Y_AXIS));
        contenedorPrincipal.setBackground(TemaSteam.FONDO_PANEL);
        contenedorPrincipal.setBorder(BorderFactory.createEmptyBorder(16, 18, 12, 18));

        //Avatar, tipo y nivel
        contenedorPrincipal.add(crearCabeceraJugador());
        contenedorPrincipal.add(Box.createRigidArea(new Dimension(0, 14)));

        //estadisticas
        contenedorPrincipal.add(crearPanelEstadisticas());
        contenedorPrincipal.add(Box.createRigidArea(new Dimension(0, 14)));

        //actividad
        contenedorPrincipal.add(crearExpositorJuegos());

        add(contenedorPrincipal, BorderLayout.CENTER);

        //cambiar contraseña y cerrar
        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 10));
        panelSur.setBackground(TemaSteam.FONDO_OSCURO);
        panelSur.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, TemaSteam.BORDE_SUTIL));

        JButton btnCambiarPass = new JButton("Cambiar contraseña");
        TemaSteam.estilizarBoton(btnCambiarPass, TemaSteam.FONDO_TARJETA);
        btnCambiarPass.addActionListener(e -> abrirDialogoCambioContraseña());

        JButton btnCerrar = new JButton("Cerrar");
        TemaSteam.estilizarBoton(btnCerrar, TemaSteam.AZUL_STEAM);
        btnCerrar.addActionListener(e -> dispose());

        panelSur.add(btnCambiarPass);
        panelSur.add(btnCerrar);
        add(panelSur, BorderLayout.SOUTH);
    }

    private JPanel crearCabeceraJugador() {
        JPanel cabecera = new JPanel(new BorderLayout(14, 0));
        cabecera.setBackground(TemaSteam.FONDO_OSCURO);
        cabecera.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(TemaSteam.BORDE_SUTIL),
                BorderFactory.createEmptyBorder(14, 14, 14, 14)));
        cabecera.setMaximumSize(new Dimension(Integer.MAX_VALUE, 165));

        //icono
        JPanel panelAvatar = new JPanel();
        panelAvatar.setLayout(new BoxLayout(panelAvatar, BoxLayout.Y_AXIS));
        panelAvatar.setBackground(TemaSteam.FONDO_OSCURO);

        lblAvatar = new JLabel();
        lblAvatar.setPreferredSize(new Dimension(80, 80));
        lblAvatar.setMaximumSize(new Dimension(80, 80));
        lblAvatar.setMinimumSize(new Dimension(80, 80));
        lblAvatar.setHorizontalAlignment(SwingConstants.CENTER);
        lblAvatar.setOpaque(true);
        lblAvatar.setBackground(TemaSteam.FONDO_TARJETA);
        lblAvatar.setForeground(TemaSteam.TEXTO_BLANCO);
        lblAvatar.setFont(new Font("SansSerif", Font.BOLD, 28));
        lblAvatar.setBorder(BorderFactory.createLineBorder(TemaSteam.AZUL_STEAM, 2));
        lblAvatar.setAlignmentX(CENTER_ALIGNMENT);
        actualizarVisualAvatar();

        JButton btnAvatar = new JButton("Foto");
        btnAvatar.setFont(new Font("SansSerif", Font.BOLD, 11));
        btnAvatar.setBackground(TemaSteam.FONDO_TARJETA);
        btnAvatar.setForeground(TemaSteam.TEXTO_CLARO);
        btnAvatar.setFocusPainted(false);
        btnAvatar.setAlignmentX(CENTER_ALIGNMENT);
        btnAvatar.addActionListener(e -> seleccionarNuevoAvatar());

        panelAvatar.add(lblAvatar);
        panelAvatar.add(Box.createRigidArea(new Dimension(0, 5)));
        panelAvatar.add(btnAvatar);

        //nombre, rol, estado y biografia
        JPanel panelInfo = new JPanel();
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
        panelInfo.setBackground(TemaSteam.FONDO_OSCURO);

        JPanel filaNombre = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        filaNombre.setBackground(TemaSteam.FONDO_OSCURO);
        filaNombre.setAlignmentX(LEFT_ALIGNMENT);

        JLabel lblNombre = new JLabel(usuario.getNombre());
        lblNombre.setForeground(TemaSteam.TEXTO_BLANCO);
        lblNombre.setFont(new Font("SansSerif", Font.BOLD, 20));

        JLabel lblInsigniaRol = new JLabel(" " + usuario.getTipoUsuario().name() + " ");
        lblInsigniaRol.setOpaque(true);
        lblInsigniaRol.setForeground(TemaSteam.TEXTO_BLANCO);
        lblInsigniaRol.setFont(new Font("SansSerif", Font.BOLD, 11));
        if (usuario.getTipoUsuario() == TipoUsuario.ADMINISTRADOR) {
            lblInsigniaRol.setBackground(TemaSteam.ROJO_PELIGRO);
        } else if (usuario.getTipoUsuario() == TipoUsuario.DESARROLLADOR) {
            lblInsigniaRol.setBackground(TemaSteam.VERDE_STEAM);
        } else {
            lblInsigniaRol.setBackground(TemaSteam.AZUL_STEAM);
        }

        filaNombre.add(lblNombre);
        filaNombre.add(lblInsigniaRol);

        //estado
        JPanel filaEstado = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        filaEstado.setBackground(TemaSteam.FONDO_OSCURO);
        filaEstado.setAlignmentX(LEFT_ALIGNMENT);

        JLabel lblPunto = new JLabel("●");
        lblPunto.setForeground(new Color(102, 192, 244));
        lblPunto.setFont(new Font("SansSerif", Font.BOLD, 13));

        JComboBox<String> comboEstado = new JComboBox<>(new String[] { "En línea", "Ausente", "No molestar" });
        comboEstado.setSelectedItem(usuario.getEstadoConexion());
        TemaSteam.estilizarCombo(comboEstado);
        comboEstado.setFont(new Font("SansSerif", Font.PLAIN, 12));
        comboEstado.addActionListener(e -> {
            String sel = (String) comboEstado.getSelectedItem();
            usuario.setEstadoConexion(sel);
            if ("En línea".equals(sel)) {
                lblPunto.setForeground(new Color(102, 192, 244));
            } else if ("Ausente".equals(sel)) {
                lblPunto.setForeground(new Color(230, 180, 50));
            } else {
                lblPunto.setForeground(TemaSteam.ROJO_PELIGRO);
            }
        });

        filaEstado.add(lblPunto);
        filaEstado.add(comboEstado);

        //Biografía
        JPanel filaBio = new JPanel(new BorderLayout(6, 0));
        filaBio.setBackground(TemaSteam.FONDO_OSCURO);
        filaBio.setAlignmentX(LEFT_ALIGNMENT);
        filaBio.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));

        txtBiografia = new JTextArea(usuario.getBiografia(), 2, 20);
        txtBiografia.setLineWrap(true);
        txtBiografia.setWrapStyleWord(true);
        txtBiografia.setBackground(TemaSteam.FONDO_PANEL);
        txtBiografia.setForeground(TemaSteam.TEXTO_CLARO);
        txtBiografia.setCaretColor(TemaSteam.TEXTO_BLANCO);
        txtBiografia.setFont(new Font("SansSerif", Font.ITALIC, 12));
        txtBiografia.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(TemaSteam.BORDE_SUTIL),
                BorderFactory.createEmptyBorder(4, 6, 4, 6)));

        JButton btnGuardarBio = new JButton("Guardar bio");
        btnGuardarBio.setBackground(TemaSteam.FONDO_TARJETA);
        btnGuardarBio.setForeground(TemaSteam.TEXTO_BLANCO);
        btnGuardarBio.setFont(new Font("SansSerif", Font.BOLD, 11));
        btnGuardarBio.setFocusPainted(false);
        btnGuardarBio.addActionListener(e -> {
            usuario.setBiografia(txtBiografia.getText().trim());
            JOptionPane.showMessageDialog(this, "Biografía actualizada correctamente.");
        });

        filaBio.add(txtBiografia, BorderLayout.CENTER);
        filaBio.add(btnGuardarBio, BorderLayout.EAST);

        panelInfo.add(filaNombre);
        panelInfo.add(filaEstado);
        panelInfo.add(Box.createRigidArea(new Dimension(0, 4)));
        panelInfo.add(filaBio);

        //nivel
        JPanel panelNivel = new JPanel();
        panelNivel.setLayout(new BoxLayout(panelNivel, BoxLayout.Y_AXIS));
        panelNivel.setBackground(TemaSteam.FONDO_OSCURO);
        panelNivel.setPreferredSize(new Dimension(110, 80));

        int totalApps = usuario.getBiblioteca().size();
        int nivel = 1 + (totalApps * 2);
        int xpActual = (totalApps * 40) % 100;

        JLabel lblTextoNivel = new JLabel("Nivel Steam");
        lblTextoNivel.setForeground(TemaSteam.TEXTO_CLARO);
        lblTextoNivel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblTextoNivel.setAlignmentX(CENTER_ALIGNMENT);

        JLabel lblNumeroNivel = new JLabel(String.valueOf(nivel));
        lblNumeroNivel.setForeground(TemaSteam.TEXTO_BLANCO);
        lblNumeroNivel.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblNumeroNivel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNumeroNivel.setPreferredSize(new Dimension(46, 46));
        lblNumeroNivel.setMaximumSize(new Dimension(46, 46));
        lblNumeroNivel.setBorder(BorderFactory.createLineBorder(TemaSteam.VERDE_STEAM, 2));
        lblNumeroNivel.setAlignmentX(CENTER_ALIGNMENT);

        JProgressBar barraXp = new JProgressBar(0, 100);
        barraXp.setValue(xpActual);
        barraXp.setPreferredSize(new Dimension(95, 8));
        barraXp.setMaximumSize(new Dimension(95, 8));
        barraXp.setForeground(TemaSteam.VERDE_STEAM);
        barraXp.setBackground(TemaSteam.FONDO_PANEL);
        barraXp.setBorder(BorderFactory.createLineBorder(TemaSteam.BORDE_SUTIL));
        barraXp.setAlignmentX(CENTER_ALIGNMENT);

        panelNivel.add(lblTextoNivel);
        panelNivel.add(Box.createRigidArea(new Dimension(0, 6)));
        panelNivel.add(lblNumeroNivel);
        panelNivel.add(Box.createRigidArea(new Dimension(0, 8)));
        panelNivel.add(barraXp);

        cabecera.add(panelAvatar, BorderLayout.WEST);
        cabecera.add(panelInfo, BorderLayout.CENTER);
        cabecera.add(panelNivel, BorderLayout.EAST);

        return cabecera;
    }

    private JPanel crearPanelEstadisticas() {
        JPanel gridStats = new JPanel(new GridLayout(1, 3, 10, 0));
        gridStats.setBackground(TemaSteam.FONDO_PANEL);
        gridStats.setMaximumSize(new Dimension(Integer.MAX_VALUE, 82));

        List<Juego> biblio = usuario.getBiblioteca();
        double pesoTotal = 0;
        int contJuegos = 0;
        int contRedes = 0;

        for (Juego j : biblio) {
            pesoTotal += j.getPeso();
            if (j.getCategoria() == Categoria.JUEGO) {
                contJuegos++;
            } else if (j.getCategoria() == Categoria.REDES) {
                contRedes++;
            }
        }

        String catFavorita = "Sin definir";
        if (contJuegos > 0 || contRedes > 0) {
            catFavorita = (contJuegos >= contRedes) ? "JUEGO" : "REDES";
        }

        // Tarjeta de biblio
        gridStats.add(crearTarjetaDato("EN BIBLIOTECA", biblio.size() + " apps", TemaSteam.AZUL_STEAM));

        // Tarjeta espacio en disco
        String textoPeso = (pesoTotal >= 1000)
                ? String.format("%.2f GB", pesoTotal / 1024.0)
                : String.format("%.1f MB", pesoTotal);
        gridStats.add(crearTarjetaDato("ESPACIO EN DISCO", textoPeso, TemaSteam.TEXTO_BLANCO));

        // Tarjeta rol del usuario
        if (usuario.getTipoUsuario() == TipoUsuario.DESARROLLADOR && listaJuegos != null) {
            int ventasTotales = 0;
            int publicados = 0;
            for (Juego j : listaJuegos) {
                if (j.getDesarrollador() != null && j.getDesarrollador().equals(usuario)) {
                    publicados++;
                    ventasTotales += j.getVentas();
                }
            }
            gridStats.add(crearTarjetaDato("PUBLICADOS / VENTAS",
                    publicados + " apps (" + ventasTotales + " ventas)", new Color(139, 195, 74)));
        } else if (usuario.getTipoUsuario() == TipoUsuario.ADMINISTRADOR && listaJuegos != null) {
            gridStats.add(crearTarjetaDato("CATÁLOGO GLOBAL", listaJuegos.size() + " apps", TemaSteam.AZUL_STEAM));
        } else {
            gridStats.add(crearTarjetaDato("CATEGORÍA FAVORITA", catFavorita, new Color(139, 195, 74)));
        }

        return gridStats;
    }

    private JPanel crearTarjetaDato(String titulo, String valor, Color colorValor) {
        JPanel card = new JPanel(new BorderLayout(0, 4));
        card.setBackground(TemaSteam.FONDO_TARJETA);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(TemaSteam.BORDE_SUTIL),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        JLabel lblTit = new JLabel(titulo, SwingConstants.CENTER);
        lblTit.setForeground(TemaSteam.TEXTO_CLARO);
        lblTit.setFont(new Font("SansSerif", Font.BOLD, 10));

        JLabel lblVal = new JLabel(valor, SwingConstants.CENTER);
        lblVal.setForeground(colorValor);
        lblVal.setFont(new Font("SansSerif", Font.BOLD, 15));

        card.add(lblTit, BorderLayout.NORTH);
        card.add(lblVal, BorderLayout.CENTER);
        return card;
    }

    private JPanel crearExpositorJuegos() {
        JPanel panelExp = new JPanel(new BorderLayout(0, 8));
        panelExp.setBackground(TemaSteam.FONDO_OSCURO);
        panelExp.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(TemaSteam.BORDE_SUTIL),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)));

        boolean esDev = usuario.getTipoUsuario() == TipoUsuario.DESARROLLADOR;
        String tituloExpositor = esDev ? "EXPOSITOR DE MIS PUBLICACIONES" : "EXPOSITOR DE BIBLIOTECA";

        JLabel lblTituloExp = new JLabel(tituloExpositor);
        lblTituloExp.setForeground(TemaSteam.AZUL_STEAM);
        lblTituloExp.setFont(new Font("SansSerif", Font.BOLD, 12));
        panelExp.add(lblTituloExp, BorderLayout.NORTH);

        List<Juego> juegosMostrar = new ArrayList<>();
        if (esDev && listaJuegos != null) {
            for (Juego j : listaJuegos) {
                if (j.getDesarrollador() != null && j.getDesarrollador().equals(usuario)) {
                    juegosMostrar.add(j);
                }
            }
        } else {
            juegosMostrar.addAll(usuario.getBiblioteca());
        }

        if (juegosMostrar.isEmpty()) {
            JLabel lblVacio = new JLabel(
                    "Aún no hay aplicaciones para mostrar aquí. ¡Explora la tienda!",
                    SwingConstants.CENTER);
            lblVacio.setForeground(TemaSteam.TEXTO_CLARO);
            lblVacio.setFont(new Font("SansSerif", Font.ITALIC, 13));
            lblVacio.setPreferredSize(new Dimension(400, 140));
            panelExp.add(lblVacio, BorderLayout.CENTER);
            return panelExp;
        }

        String[] cols = { "Icono", "Nombre", "Categoría", "Peso (MB)" };
        DefaultTableModel modeloExp = new DefaultTableModel(cols, 0) {
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

        for (Juego j : juegosMostrar) {
            modeloExp.addRow(new Object[] { j.getIcono(), j.getNombre(), j.getCategoria(), j.getPeso() });
        }

        JTable tablaExp = new JTable(modeloExp);
        tablaExp.setRowHeight(46);

        JScrollPane scroll = new JScrollPane(tablaExp);
        TemaSteam.estilizarTabla(tablaExp, scroll);

        DefaultTableCellRenderer centrado = new DefaultTableCellRenderer();
        centrado.setHorizontalAlignment(SwingConstants.CENTER);
        tablaExp.getColumnModel().getColumn(2).setCellRenderer(centrado);
        tablaExp.getColumnModel().getColumn(3).setCellRenderer(centrado);

        panelExp.add(scroll, BorderLayout.CENTER);
        return panelExp;
    }

    private void actualizarVisualAvatar() {
        if (usuario.getAvatar() != null) {
            lblAvatar.setIcon(usuario.getAvatar());
            lblAvatar.setText("");
        } else {
            lblAvatar.setIcon(null);
            lblAvatar.setText(usuario.getNombre().substring(0, 1).toUpperCase());
        }
    }

    private void seleccionarNuevoAvatar() {
        JFileChooser selector = new JFileChooser(new File("resources/images"));
        selector.setFileFilter(new FileNameExtensionFilter("Imágenes (PNG, JPG, JPEG)", "png", "jpg", "jpeg"));

        if (selector.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File origen = selector.getSelectedFile();
            ImageIcon img = new ImageIcon(origen.getAbsolutePath());
            if (img.getIconWidth() <= 0) {
                JOptionPane.showMessageDialog(this, "No se pudo cargar la imagen.");
                return;
            }

            try {
                File carpeta = new File("resources/images");
                if (!carpeta.exists()) {
                    carpeta.mkdirs();
                }
                Path destino = Paths.get(carpeta.getPath(), origen.getName());
                if (!origen.toPath().toAbsolutePath().equals(destino.toAbsolutePath())) {
                    Files.copy(origen.toPath(), destino, StandardCopyOption.REPLACE_EXISTING);
                    destino.toFile().deleteOnExit();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            Image escalada = img.getImage().getScaledInstance(76, 76, Image.SCALE_SMOOTH);
            usuario.setAvatar(new ImageIcon(escalada));
            actualizarVisualAvatar();
        }
    }

    private void abrirDialogoCambioContraseña() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 8, 8));
        JPasswordField txtActual = new JPasswordField();
        JPasswordField txtNueva = new JPasswordField();

        panel.add(new JLabel("Contraseña actual:"));
        panel.add(txtActual);
        panel.add(new JLabel("Nueva contraseña:"));
        panel.add(txtNueva);

        int opcion = JOptionPane.showConfirmDialog(this, panel,
                "Cambiar contraseña", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (opcion == JOptionPane.OK_OPTION) {
            String actual = new String(txtActual.getPassword());
            String nueva = new String(txtNueva.getPassword()).trim();

            if (!usuario.getContraseña().equals(actual)) {
                JOptionPane.showMessageDialog(this, "La contraseña actual no es correcta.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (nueva.isEmpty()) {
                JOptionPane.showMessageDialog(this, "La nueva contraseña no puede estar vacía.",
                        "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            usuario.setContraseña(nueva);
            JOptionPane.showMessageDialog(this, "Contraseña cambiada con éxito.");
        }
    }
}