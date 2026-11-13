
package gui;

import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.BorderLayout;
import java.io.File;

import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import domain.Categoria;
import domain.Juego;
import domain.Usuario;

public class DialogSubirJuego extends JDialog {

    private static final long serialVersionUID = 1L;

    private JTextField txtNombre;
    private JTextField txtPeso;
    private JComboBox<Categoria> comboCategoria;
    private Icon iconoSeleccionado;
    private Usuario desarrollador;
    private Juego juegoCreado;
    private JLabel lblVistaPrevia;

    public DialogSubirJuego(Frame parent, Usuario desarrollador) {

        super(parent, "Subir videojuego", true);

        this.desarrollador = desarrollador;

        setSize(450, 300);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));

        panel.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        panel.add(txtNombre);

        panel.add(new JLabel("Peso (MB):"));
        txtPeso = new JTextField();
        panel.add(txtPeso);

        panel.add(new JLabel("Categoria:"));
        comboCategoria = new JComboBox<>(Categoria.values());
        panel.add(comboCategoria);

        panel.add(new JLabel("Icono:"));

        JPanel panelIcono = new JPanel();

        JButton btnSeleccionarIcono = new JButton("Seleccionar icono");
        lblVistaPrevia = new JLabel();

        btnSeleccionarIcono.addActionListener(e -> seleccionarIcono());

        panelIcono.add(btnSeleccionarIcono);
        panelIcono.add(lblVistaPrevia);

        panel.add(panelIcono);

        add(panel, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel();

        JButton btnSubir = new JButton("Subir juego");
        JButton btnCancelar = new JButton("Cancelar");

        btnSubir.addActionListener(e -> subirJuego());
        btnCancelar.addActionListener(e -> dispose());

        panelBotones.add(btnSubir);
        panelBotones.add(btnCancelar);

        add(panelBotones, BorderLayout.SOUTH);
    }

    private void seleccionarIcono() {

        JFileChooser selector = new JFileChooser(new File("resources/images"));

        FileNameExtensionFilter filtro = new FileNameExtensionFilter(
                "Imagenes (PNG, JPG, JPEG)", "png", "jpg", "jpeg"
        );

        selector.setFileFilter(filtro);

        int resultado = selector.showOpenDialog(this);

        if (resultado == JFileChooser.APPROVE_OPTION) {

            File archivo = selector.getSelectedFile();

            ImageIcon imagen = new ImageIcon(archivo.getAbsolutePath());

            if (imagen.getIconWidth() <= 0) {
                JOptionPane.showMessageDialog(this,
                        "No se ha podido cargar la imagen.");
                return;
            }

            Image imagenEscalada = imagen.getImage()
                    .getScaledInstance(40, 40, Image.SCALE_SMOOTH);

            iconoSeleccionado = new ImageIcon(imagenEscalada);

            lblVistaPrevia.setIcon(iconoSeleccionado);
        }
    }

    private void subirJuego() {

        String nombre = txtNombre.getText().trim();
        String textoPeso = txtPeso.getText().trim();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Introduce el nombre del juego.");
            return;
        }

        if (iconoSeleccionado == null) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona un icono.");
            return;
        }

        double peso;

        try {
            peso = Double.parseDouble(textoPeso);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "El peso debe ser un número válido.");
            return;
        }

        if (!Double.isFinite(peso) || peso <= 0) {
            JOptionPane.showMessageDialog(this,
                    "El peso debe ser mayor que 0.");
            return;
        }

        Categoria categoria = (Categoria) comboCategoria.getSelectedItem();

        juegoCreado = new Juego(
                iconoSeleccionado,
                nombre,
                peso,
                categoria,
                desarrollador,
                0,
                0
        );

        dispose();
    }

    public Juego getJuegoCreado() {
        return juegoCreado;
    }
}


