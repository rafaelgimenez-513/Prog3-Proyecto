package gui;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import domain.Juego;

public class PanelAdministrador extends JPanel {

    private static final long serialVersionUID = 1L;

    private List<Juego> listaJuegos;
    private JTable tabla;
    private DefaultTableModel modelo;

    public PanelAdministrador(List<Juego> listaJuegos) {

        this.listaJuegos = listaJuegos;

        setLayout(new BorderLayout());

        // Titulo del panel
        JLabel titulo = new JLabel("Administracion de videojuegos",
                SwingConstants.CENTER);
        add(titulo, BorderLayout.NORTH);

        // Crear la tabla de videojuegos
        String[] columnas = {
                "Nombre", "Categoria", "Peso", "Desarrollador", "Ventas"
        };

        modelo = new DefaultTableModel(columnas, 0) {

            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tabla = new JTable(modelo);
        tabla.setRowHeight(30);

        // Permitir seleccionar solo un videojuego
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // Boton para eliminar un videojuego
        JButton btnEliminar = new JButton("Eliminar videojuego");
        add(btnEliminar, BorderLayout.SOUTH);

        btnEliminar.addActionListener(e -> eliminarJuego());

        // Mostrar los juegos al abrir el panel
        actualizarTabla();
    }

    // Actualizar la tabla con los videojuegos disponibles
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

    // Eliminar el videojuego seleccionado
    private void eliminarJuego() {

        int fila = tabla.getSelectedRow();

        // Comprobar que se ha seleccionado un videojuego
        if (fila == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Selecciona un videojuego para eliminar.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);

            return;
        }

        // Obtener el videojuego seleccionado
        int filaModelo = tabla.convertRowIndexToModel(fila);
        Juego juego = listaJuegos.get(filaModelo);

        // Pedir confirmacion antes de eliminar
        int respuesta = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro que quieres eliminar " + juego.getNombre() + "?",
                "Confirmar eliminacion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        // Eliminar solamente si el administrador confirma
        if (respuesta == JOptionPane.YES_OPTION) {

            listaJuegos.remove(juego);

            // Actualizar la tabla despues de eliminar
            actualizarTabla();

            JOptionPane.showMessageDialog(
                    this,
                    "Videojuego eliminado correctamente.",
                    "Eliminado",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
