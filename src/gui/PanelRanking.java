package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;

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

        setLayout(new BorderLayout());

        // Panel superior con titulo y filtro
        JPanel panelSuperior = new JPanel(new GridLayout(2, 1));

        JLabel titulo = new JLabel("Ranking de videojuegos",
                SwingConstants.CENTER);
        panelSuperior.add(titulo);

        filtroCategoria = new JComboBox<>(
                new String[]{"Todas", "JUEGO", "REDES"});

        panelSuperior.add(filtroCategoria);

        add(panelSuperior, BorderLayout.NORTH);

        // Crear la tabla
        String[] columnas = {"TOP", "Nombre", "Categoria", "Ventas"};

        modelo = new DefaultTableModel(columnas, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tabla = new JTable(modelo);

        // Personalizar la tabla
        tabla.setRowHeight(30);

        // IAG (herramienta: ChatGPT)
        // SIN CAMBIOS
        // Colorear los tres primeros puestos del ranking
        tabla.getColumnModel().getColumn(0).setCellRenderer(
                new DefaultTableCellRenderer() {

            private static final long serialVersionUID = 1L;

            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value,
                    boolean isSelected, boolean hasFocus,
                    int row, int column) {

                Component celda = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus,
                        row, column);

                setHorizontalAlignment(SwingConstants.CENTER);

                if (!isSelected) {
                    if (row == 0) {
                        celda.setBackground(new Color(255, 230, 150));
                    } else if (row == 1) {
                        celda.setBackground(new Color(220, 220, 220));
                    } else if (row == 2) {
                        celda.setBackground(new Color(225, 185, 155));
                    } else {
                        celda.setBackground(table.getBackground());
                    }
                }

                return celda;
            }
        });

        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // IAG (herramienta: ChatGPT)
        // SIN CAMBIOS
        // Evento de raton para mostrar los jugadores simultaneos
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
                                + " jugadores simultaneos.",
                                "Jugadores simultaneos",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });

        // Actualizar la tabla al cambiar de categoria
        filtroCategoria.addActionListener(e -> actualizarTabla());

        actualizarTabla();
    }

    public void actualizarTabla() {

        modelo.setRowCount(0);
        juegosMostrados.clear();

        // Crear una copia de la lista original
        List<Juego> juegosOrdenados = new ArrayList<>(listaJuegos);

        // Ordenar los videojuegos por ventas
        juegosOrdenados.sort((j1, j2) ->
                Integer.compare(j2.getVentas(), j1.getVentas()));

        String categoriaSeleccionada =
                (String) filtroCategoria.getSelectedItem();

        int posicion = 1;

        for (Juego juego : juegosOrdenados) {

            if (categoriaSeleccionada.equals("Todas") ||
                    juego.getCategoria().name().equals(categoriaSeleccionada)) {

                juegosMostrados.add(juego);

                modelo.addRow(new Object[]{
                        posicion,
                        juego.getNombre(),
                        juego.getCategoria(),
                        juego.getVentas()
                });

                posicion++;
            }
        }
    }
}
