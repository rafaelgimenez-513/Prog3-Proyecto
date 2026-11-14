package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

public class TemaSteam {

    // Paleta de colores inspirada en Steam (Clase generada con IAG)
    public static final Color FONDO_OSCURO = new Color(23, 26, 33);
    public static final Color FONDO_PANEL = new Color(27, 40, 56);
    public static final Color FONDO_TARJETA = new Color(42, 71, 94);
    public static final Color AZUL_STEAM = new Color(26, 159, 255);
    public static final Color VERDE_STEAM = new Color(92, 126, 16);
    public static final Color ROJO_PELIGRO = new Color(180, 50, 50);
    public static final Color TEXTO_CLARO = new Color(199, 213, 224);
    public static final Color TEXTO_BLANCO = Color.WHITE;
    public static final Color BORDE_SUTIL = new Color(55, 85, 110);

    public static void estilizarPanel(JPanel panel) {
        panel.setBackground(FONDO_PANEL);
    }

    public static void estilizarTitulo(JLabel label) {
        label.setForeground(TEXTO_BLANCO);
        label.setFont(new Font("SansSerif", Font.BOLD, 18));
    }

    public static void estilizarLabel(JLabel label) {
        label.setForeground(TEXTO_CLARO);
        label.setFont(new Font("SansSerif", Font.PLAIN, 13));
    }

    public static void estilizarBoton(JButton btn, Color fondo) {
        btn.setBackground(fondo);
        btn.setForeground(TEXTO_BLANCO);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
    }

    public static void estilizarCampoTexto(JTextField campo) {
        campo.setBackground(FONDO_OSCURO);
        campo.setForeground(TEXTO_BLANCO);
        campo.setCaretColor(TEXTO_BLANCO);
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDE_SUTIL),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)));
    }

    public static void estilizarCombo(JComboBox<?> combo) {
        combo.setBackground(FONDO_TARJETA);
        combo.setForeground(TEXTO_BLANCO);
    }

    public static void estilizarTabla(JTable tabla, JScrollPane scroll) {
        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.getTableHeader().setResizingAllowed(false);
        
        if (tabla.getColumnCount() > 0 && "Icono".equals(tabla.getColumnName(0))) {
            TableColumn colIcono = tabla.getColumnModel().getColumn(0);
            colIcono.setMinWidth(48);
            colIcono.setMaxWidth(48);
            colIcono.setPreferredWidth(48);
        }

        tabla.setBackground(FONDO_OSCURO);
        tabla.setForeground(TEXTO_CLARO);
        tabla.setSelectionBackground(FONDO_TARJETA);
        tabla.setSelectionForeground(TEXTO_BLANCO);
        tabla.setGridColor(new Color(35, 50, 68));
        tabla.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JTableHeader header = tabla.getTableHeader();
        header.setBackground(FONDO_TARJETA);
        header.setForeground(TEXTO_BLANCO);
        header.setFont(new Font("SansSerif", Font.BOLD, 13));
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 1L;

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);
                lbl.setBackground(FONDO_TARJETA);
                lbl.setForeground(TEXTO_BLANCO);
                lbl.setHorizontalAlignment(SwingConstants.CENTER);
                lbl.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, BORDE_SUTIL));
                return lbl;
            }
        });

        if (scroll != null) {
            scroll.getViewport().setBackground(FONDO_OSCURO);
            scroll.setBorder(BorderFactory.createLineBorder(BORDE_SUTIL));
        }
    }
}