package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Icon;
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
import domain.Usuario;

public class PanelBiblioteca extends JPanel {

    private static final long serialVersionUID = 1L;

    private Usuario usuarioActual;
    private List<Juego> juegosBiblioteca;

    private JTable tabla;
    private DefaultTableModel modelo;
    private JLabel lblContador;
    private JButton btnEjecutar;
    private JButton btnDesinstalar;
    private Runnable onBibliotecaModificada;

    public PanelBiblioteca(Usuario usuarioActual) {
        this.usuarioActual = usuarioActual;
        this.juegosBiblioteca = new ArrayList<>();

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 18, 15, 18));
        TemaSteam.estilizarPanel(this);

        // 1. Cabecera
        JPanel panelSuperior = new JPanel(new BorderLayout());
        TemaSteam.estilizarPanel(panelSuperior);

        JLabel lblTitulo = new JLabel("MI BIBLIOTECA", SwingConstants.LEFT);
        TemaSteam.estilizarTitulo(lblTitulo);

        lblContador = new JLabel("0 aplicaciones instaladas", SwingConstants.RIGHT);
        TemaSteam.estilizarLabel(lblContador);

        panelSuperior.add(lblTitulo, BorderLayout.WEST);
        panelSuperior.add(lblContador, BorderLayout.EAST);
        add(panelSuperior, BorderLayout.NORTH);

        // 2. Tabla de juegos
        String[] columnas = { "Icono", "Nombre", "Categoría", "Peso (MB)", "Desarrollador" };
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

        add(scroll, BorderLayout.CENTER);

        // Botones juegar y desinstalar
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        TemaSteam.estilizarPanel(panelInferior);

        btnEjecutar = new JButton("▶  JUGAR");
        TemaSteam.estilizarBoton(btnEjecutar, TemaSteam.VERDE_STEAM);
        btnEjecutar.addActionListener(e -> ejecutarJuegoSeleccionado());

        btnDesinstalar = new JButton("🗑  Desinstalar");
        TemaSteam.estilizarBoton(btnDesinstalar, TemaSteam.ROJO_PELIGRO);
        btnDesinstalar.addActionListener(e -> desinstalarJuegoSeleccionado());

        panelInferior.add(btnEjecutar);
        panelInferior.add(btnDesinstalar);

        add(panelInferior, BorderLayout.SOUTH);

        actualizarTabla();
    }

    public void setOnBibliotecaModificada(Runnable onBibliotecaModificada) {
        this.onBibliotecaModificada = onBibliotecaModificada;
    }

    public void actualizarTabla() {
        modelo.setRowCount(0);
        juegosBiblioteca.clear();

        List<Juego> lista = usuarioActual.getBiblioteca();
        for (Juego j : lista) {
            juegosBiblioteca.add(j);
            String nombreDev = (j.getDesarrollador() != null) ? j.getDesarrollador().getNombre() : "Desconocido";

            modelo.addRow(new Object[] {
                    j.getIcono(),
                    j.getNombre(),
                    j.getCategoria(),
                    j.getPeso(),
                    nombreDev
            });
        }

        int total = juegosBiblioteca.size();
        lblContador.setText(total + (total == 1 ? " aplicación instalada" : " aplicaciones instaladas"));
    }

    private void ejecutarJuegoSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona una aplicación de tu biblioteca para iniciarla.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int filaModelo = tabla.convertRowIndexToModel(fila);
        Juego juego = juegosBiblioteca.get(filaModelo);

        juego.setJugadoresSimultaneos(juego.getJugadoresSimultaneos() + 1);

        JOptionPane.showMessageDialog(this,
                "Iniciando \"" + juego.getNombre() + "\"...\nJugadores activos ahora: " + juego.getJugadoresSimultaneos(),
                "Steam - Ejecutando",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void desinstalarJuegoSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona una aplicación de tu biblioteca para desinstalarla.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int filaModelo = tabla.convertRowIndexToModel(fila);
        Juego juego = juegosBiblioteca.get(filaModelo);

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Seguro que quieres desinstalar \"" + juego.getNombre() + "\" de tu biblioteca?",
                "Confirmar desinstalación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            usuarioActual.eliminarJuegoBiblioteca(juego);
            actualizarTabla();

            if (onBibliotecaModificada != null) {
                onBibliotecaModificada.run();
            }

            JOptionPane.showMessageDialog(this,
                    "\"" + juego.getNombre() + "\" ha sido desinstalado.",
                    "Desinstalado",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
}