package gui;

import domain.Juego;
import domain.Usuario;

import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VentanaPrincipal extends JFrame {
    /**
     
*/
  private static final long serialVersionUID = 1L;
  private Usuario usuario;
  private List<Juego> listaJuegos;

    public VentanaPrincipal(Usuario usuario, List<Juego> juegos) {
        this.usuario = usuario;
        this.listaJuegos = juegos;

        setTitle("App Store - " + usuario.getNombre());
        setSize(750, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Modelo de tabla para mostrar la tienda
        //Código generado con IAG
        String[] columnas = {"Icono", "Nombre", "Categoría", "Peso", "Desarrollador", "Ventas"};
        DefaultTableModel model = new DefaultTableModel(columnas, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                // Decimos que la primera columna es un Icono para que Swing dibuje la imagen
                return columnIndex == 0 ? Icon.class : Object.class;
            }

            //Fin código generado con IAG

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Rellenar la tabla con los datos del juego
        for (Juego j : listaJuegos) {
            model.addRow(new Object[]{
                j.getIcono(),
                j.getNombre(),
                j.getCategoria(),
                j.getPeso(),
                j.getDesarrollador(),
                j.getVentas()
            });
        }

        JTable tabla = new JTable(model);
        tabla.setRowHeight(48); // Dar altura suficiente a las filas para que la imagen quepa bien

        add(new JScrollPane(tabla), BorderLayout.CENTER);
    }
}