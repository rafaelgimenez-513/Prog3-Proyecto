package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import domain.Juego;

public class PanelAdministrador extends JPanel {

    private static final long serialVersionUID = 1L;

    private List<Juego> listaJuegos;
    private JTable tabla;
    private DefaultTableModel modelo;

    public PanelAdministrador(List<Juego> listaJuegos) {
        this.listaJuegos = listaJuegos;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 18, 15, 18));
        TemaSteam.estilizarPanel(this);

        JLabel titulo = new JLabel("ADMINISTRACIÓN DEL CATÁLOGO", SwingConstants.LEFT);
        TemaSteam.estilizarTitulo(titulo);
        add(titulo, BorderLayout.NORTH);

        String[] columnas = { "Nombre", "Categoría", "Peso (MB)", "Desarrollador", "Ventas" };

        modelo = new DefaultTableModel(columnas, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tabla = new JTable(modelo);
        tabla.setRowHeight(36);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scroll = new JScrollPane(tabla);
        TemaSteam.estilizarTabla(tabla, scroll);

        DefaultTableCellRenderer centrado = new DefaultTableCellRenderer();
        centrado.setHorizontalAlignment(SwingConstants.CENTER);
        tabla.getColumnModel().getColumn(1).setCellRenderer(centrado);
        tabla.getColumnModel().getColumn(2).setCellRenderer(centrado);
        tabla.getColumnModel().getColumn(4).setCellRenderer(centrado);

        add(scroll, BorderLayout.CENTER);

        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.CENTER));
        TemaSteam.estilizarPanel(panelSur);

        JButton btnEliminar = new JButton("🗑  Eliminar videojuego seleccionado");
        TemaSteam.estilizarBoton(btnEliminar, TemaSteam.ROJO_PELIGRO);
        btnEliminar.addActionListener(e -> eliminarJuego());

        panelSur.add(btnEliminar);
        add(panelSur, BorderLayout.SOUTH);

        actualizarTabla();
    }

    public void actualizarTabla() {
        modelo.setRowCount(0);

        for (Juego juego : listaJuegos) {
            modelo.addRow(new Object[] {
                    juego.getNombre(),
                    juego.getCategoria(),
                    juego.getPeso(),
                    juego.getDesarrollador().getNombre(),
                    juego.getVentas()
            });
        }
    }

    private void eliminarJuego() {
        int fila = tabla.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Selecciona un videojuego para eliminar.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int filaModelo = tabla.convertRowIndexToModel(fila);
        Juego juego = listaJuegos.get(filaModelo);

        int respuesta = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro que quieres eliminar " + juego.getNombre() + "?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (respuesta == JOptionPane.YES_OPTION) {
            listaJuegos.remove(juego);
            actualizarTabla();

            JOptionPane.showMessageDialog(
                    this,
                    "Videojuego eliminado correctamente.",
                    "Eliminado",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
