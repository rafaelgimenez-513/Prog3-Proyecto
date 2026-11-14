package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import domain.Juego;

public class PanelRanking extends JPanel {

    private static final long serialVersionUID = 1L;

    private List<Juego> listaJuegos;
    private List<Juego> juegosMostrados = new ArrayList<>();

    private JTable tabla;
    private DefaultTableModel modelo;
    private JComboBox<String> filtroCategoria;

    public PanelRanking(List<Juego> listaJuegos) {
        this.listaJuegos = listaJuegos;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 18, 15, 18));
        TemaSteam.estilizarPanel(this);

        //titulo y filtro
        JPanel panelSuperior = new JPanel(new BorderLayout());
        TemaSteam.estilizarPanel(panelSuperior);

        JLabel titulo = new JLabel("LO MÁS VENDIDO (TOP RANKING)", SwingConstants.LEFT);
        TemaSteam.estilizarTitulo(titulo);

        JPanel panelFiltro = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        TemaSteam.estilizarPanel(panelFiltro);
        JLabel lblCat = new JLabel("Categoría: ");
        TemaSteam.estilizarLabel(lblCat);

        filtroCategoria = new JComboBox<>(new String[] { "Todas", "JUEGO", "REDES" });
        TemaSteam.estilizarCombo(filtroCategoria);

        panelFiltro.add(lblCat);
        panelFiltro.add(filtroCategoria);

        panelSuperior.add(titulo, BorderLayout.WEST);
        panelSuperior.add(panelFiltro, BorderLayout.EAST);

        add(panelSuperior, BorderLayout.NORTH);

        //tabla
        String[] columnas = { "TOP", "Nombre", "Categoría", "Ventas" };

        modelo = new DefaultTableModel(columnas, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tabla = new JTable(modelo);
        tabla.setRowHeight(36);

        JScrollPane scroll = new JScrollPane(tabla);
        TemaSteam.estilizarTabla(tabla, scroll);

        //Oro, Plata, Bronce de los 3 primeross
        tabla.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 1L;

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {

                Component celda = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                setHorizontalAlignment(SwingConstants.CENTER);
                setFont(getFont().deriveFont(Font.BOLD));

                if (!isSelected) {
                    if (row == 0) {
                        celda.setBackground(new Color(212, 175, 55));
                        celda.setForeground(Color.BLACK);
                    } else if (row == 1) {
                        celda.setBackground(new Color(176, 184, 192));
                        celda.setForeground(Color.BLACK);
                    } else if (row == 2) {
                        celda.setBackground(new Color(184, 115, 51));
                        celda.setForeground(Color.WHITE);
                    } else {
                        celda.setBackground(TemaSteam.FONDO_OSCURO);
                        celda.setForeground(TemaSteam.TEXTO_CLARO);
                    }
                }
                return celda;
            }
        });

        DefaultTableCellRenderer centrado = new DefaultTableCellRenderer();
        centrado.setHorizontalAlignment(SwingConstants.CENTER);
        tabla.getColumnModel().getColumn(2).setCellRenderer(centrado);
        tabla.getColumnModel().getColumn(3).setCellRenderer(centrado);

        add(scroll, BorderLayout.CENTER);

        //doble click mostrar jugadores simultaneos
        tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int fila = tabla.rowAtPoint(e.getPoint());
                    if (fila >= 0 && fila < juegosMostrados.size()) {
                        Juego juego = juegosMostrados.get(fila);
                        JOptionPane.showMessageDialog(
                                PanelRanking.this,
                                juego.getNombre() + " tiene "
                                        + juego.getJugadoresSimultaneos()
                                        + " jugadores simultáneos.",
                                "Jugadores simultáneos",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });

        filtroCategoria.addActionListener(e -> actualizarTabla());
        actualizarTabla();
    }

    public void actualizarTabla() {
        modelo.setRowCount(0);
        juegosMostrados.clear();

        List<Juego> juegosOrdenados = new ArrayList<>(listaJuegos);
        juegosOrdenados.sort((j1, j2) -> Integer.compare(j2.getVentas(), j1.getVentas()));

        String categoriaSeleccionada = (String) filtroCategoria.getSelectedItem();
        int posicion = 1;

        for (Juego juego : juegosOrdenados) {
            if (categoriaSeleccionada.equals("Todas") ||
                    juego.getCategoria().name().equals(categoriaSeleccionada)) {

                juegosMostrados.add(juego);
                modelo.addRow(new Object[] {
                        "#" + posicion,
                        juego.getNombre(),
                        juego.getCategoria(),
                        juego.getVentas()
                });
                posicion++;
            }
        }
    }
}