package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import domain.Juego;
import domain.Usuario;

public class PanelTienda extends JPanel {

    private static final long serialVersionUID = 1L;

    private Usuario usuarioActual;
    private List<Juego> listaJuegos;
    private List<Juego> juegosMostrados;

    private JTable tabla;
    private DefaultTableModel modelo;
    private JComboBox<String> comboFiltroCategoria;
    private JButton btnDescargar;
    private JProgressBar barraProgreso;
    private JLabel lblEstadoDescarga;

    private boolean descargando = false;
    private Runnable onDescargaCompletada;

    public PanelTienda(Usuario usuarioActual, List<Juego> listaJuegos) {
        this.usuarioActual = usuarioActual;
        this.listaJuegos = listaJuegos;
        this.juegosMostrados = new ArrayList<>();

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 18, 15, 18));
        TemaSteam.estilizarPanel(this);

        //titulo y filtro
        JPanel panelSuperior = new JPanel(new BorderLayout());
        TemaSteam.estilizarPanel(panelSuperior);

        JLabel lblTitulo = new JLabel("TIENDA DESTACADA", SwingConstants.LEFT);
        TemaSteam.estilizarTitulo(lblTitulo);

        JPanel panelFiltro = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        TemaSteam.estilizarPanel(panelFiltro);
        JLabel lblFiltro = new JLabel("Categoría: ");
        TemaSteam.estilizarLabel(lblFiltro);

        comboFiltroCategoria = new JComboBox<>(new String[] { "Todas", "JUEGO", "REDES" });
        TemaSteam.estilizarCombo(comboFiltroCategoria);
        comboFiltroCategoria.addActionListener(e -> actualizarTabla());

        panelFiltro.add(lblFiltro);
        panelFiltro.add(comboFiltroCategoria);

        panelSuperior.add(lblTitulo, BorderLayout.WEST);
        panelSuperior.add(panelFiltro, BorderLayout.EAST);
        add(panelSuperior, BorderLayout.NORTH);

        //Tabla central
        String[] columnas = { "Icono", "Nombre", "Categoría", "Peso (MB)", "Desarrollador", "Ventas", "Estado" };
        modelo = new DefaultTableModel(columnas, 0) {
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

        tabla = new JTable(modelo);
        tabla.setRowHeight(48);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scroll = new JScrollPane(tabla);
        TemaSteam.estilizarTabla(tabla, scroll);

        DefaultTableCellRenderer centrado = new DefaultTableCellRenderer();
        centrado.setHorizontalAlignment(SwingConstants.CENTER);
        tabla.getColumnModel().getColumn(2).setCellRenderer(centrado);
        tabla.getColumnModel().getColumn(3).setCellRenderer(centrado);
        tabla.getColumnModel().getColumn(5).setCellRenderer(centrado);

        tabla.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 1L;

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(SwingConstants.CENTER);
                setFont(getFont().deriveFont(Font.BOLD));

                if (!isSelected) {
                    c.setBackground(TemaSteam.FONDO_OSCURO);
                    if ("En biblioteca".equals(value)) {
                        c.setForeground(new Color(139, 195, 74)); // Verde claro legible sobre oscuro
                    } else {
                        c.setForeground(TemaSteam.AZUL_STEAM);
                    }
                }
                return c;
            }
        });

        add(scroll, BorderLayout.CENTER);

        //descarga y JProgressBar
        JPanel panelInferior = new JPanel(new BorderLayout(5, 8));
        TemaSteam.estilizarPanel(panelInferior);

        JPanel panelControles = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        TemaSteam.estilizarPanel(panelControles);

        btnDescargar = new JButton("⬇  Instalar / Descargar");
        TemaSteam.estilizarBoton(btnDescargar, TemaSteam.AZUL_STEAM);

        lblEstadoDescarga = new JLabel("Selecciona una aplicación para instalar");
        TemaSteam.estilizarLabel(lblEstadoDescarga);

        panelControles.add(btnDescargar);
        panelControles.add(lblEstadoDescarga);

        barraProgreso = new JProgressBar(0, 100);
        barraProgreso.setStringPainted(true);
        barraProgreso.setValue(0);
        barraProgreso.setBackground(TemaSteam.FONDO_OSCURO);
        barraProgreso.setForeground(TemaSteam.VERDE_STEAM);
        barraProgreso.setBorder(BorderFactory.createLineBorder(TemaSteam.BORDE_SUTIL));

        panelInferior.add(panelControles, BorderLayout.NORTH);
        panelInferior.add(barraProgreso, BorderLayout.SOUTH);

        add(panelInferior, BorderLayout.SOUTH);

        btnDescargar.addActionListener(e -> iniciarDescarga());

        actualizarTabla();
    }

    public void setOnDescargaCompletada(Runnable onDescargaCompletada) {
        this.onDescargaCompletada = onDescargaCompletada;
    }

    public void actualizarTabla() {
        modelo.setRowCount(0);
        juegosMostrados.clear();

        String filtro = (String) comboFiltroCategoria.getSelectedItem();

        for (Juego j : listaJuegos) {
            if ("Todas".equals(filtro) || j.getCategoria().name().equals(filtro)) {
                juegosMostrados.add(j);
                String nombreDev = (j.getDesarrollador() != null) ? j.getDesarrollador().getNombre() : "Desconocido";
                String estado = usuarioActual.getBiblioteca().contains(j) ? "En biblioteca" : "Disponible";

                modelo.addRow(new Object[] {
                        j.getIcono(),
                        j.getNombre(),
                        j.getCategoria(),
                        j.getPeso(),
                        nombreDev,
                        j.getVentas(),
                        estado
                });
            }
        }
    }

    private void iniciarDescarga() {
        if (descargando) {
            JOptionPane.showMessageDialog(this,
                    "Ya hay una descarga en curso. Espera a que termine.",
                    "Descarga activa",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int filaSeleccionada = tabla.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona un juego de la tabla para descargar.",
                    "Ningún juego seleccionado",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int filaModelo = tabla.convertRowIndexToModel(filaSeleccionada);
        Juego juegoSeleccionado = juegosMostrados.get(filaModelo);

        if (usuarioActual.getBiblioteca().contains(juegoSeleccionado)) {
            JOptionPane.showMessageDialog(this,
                    "Ya tienes \"" + juegoSeleccionado.getNombre() + "\" en tu biblioteca.",
                    "Juego ya adquirido",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        descargando = true;
        btnDescargar.setEnabled(false);
        barraProgreso.setValue(0);
        lblEstadoDescarga.setText("Descargando " + juegoSeleccionado.getNombre() + "...");

        long pausaMs = Math.max(15, Math.min(60, (long) (juegoSeleccionado.getPeso() / 10)));

        Thread hiloDescarga = new Thread(() -> {
            try {
                for (int progreso = 1; progreso <= 100; progreso++) {
                    Thread.sleep(pausaMs);
                    final int porcentajeActual = progreso;

                    SwingUtilities.invokeLater(() -> {
                        barraProgreso.setValue(porcentajeActual);
                        lblEstadoDescarga.setText("Descargando " + juegoSeleccionado.getNombre()
                                + " (" + porcentajeActual + "%)");
                    });
                }

                SwingUtilities.invokeLater(() -> {
                    usuarioActual.agregarJuegoBiblioteca(juegoSeleccionado);
                    juegoSeleccionado.setVentas(juegoSeleccionado.getVentas() + 1);

                    descargando = false;
                    btnDescargar.setEnabled(true);
                    lblEstadoDescarga.setText("Descarga completada: " + juegoSeleccionado.getNombre());

                    actualizarTabla();

                    if (onDescargaCompletada != null) {
                        onDescargaCompletada.run();
                    }

                    JOptionPane.showMessageDialog(PanelTienda.this,
                            "¡La descarga de \"" + juegoSeleccionado.getNombre()
                                    + "\" ha finalizado y se ha añadido a tu biblioteca!",
                            "Descarga finalizada",
                            JOptionPane.INFORMATION_MESSAGE);
                });

            } catch (InterruptedException ex) {
                SwingUtilities.invokeLater(() -> {
                    descargando = false;
                    btnDescargar.setEnabled(true);
                    lblEstadoDescarga.setText("Descarga cancelada.");
                });
            }
        });

        hiloDescarga.start();
    }
}
