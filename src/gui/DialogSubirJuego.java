
package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
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

        setSize(520, 350);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().setBackground(TemaSteam.FONDO_OSCURO);

        JLabel lblTitulo = new JLabel("SUBIR VIDEOJUEGO", SwingConstants.CENTER);
        TemaSteam.estilizarTitulo(lblTitulo);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));

        add(lblTitulo, BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridLayout(4, 2, 12, 12));
        TemaSteam.estilizarPanel(panel);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblNombre = new JLabel("Nombre:");
        TemaSteam.estilizarLabel(lblNombre);
        panel.add(lblNombre);

        txtNombre = new JTextField();
        TemaSteam.estilizarCampoTexto(txtNombre);
        panel.add(txtNombre);

        JLabel lblPeso = new JLabel("Peso (MB):");
        TemaSteam.estilizarLabel(lblPeso);
        panel.add(lblPeso);

        txtPeso = new JTextField();
        TemaSteam.estilizarCampoTexto(txtPeso);
        panel.add(txtPeso);

        JLabel lblCategoria = new JLabel("Categoria:");
        TemaSteam.estilizarLabel(lblCategoria);
        panel.add(lblCategoria);

        comboCategoria = new JComboBox<>(Categoria.values());
        TemaSteam.estilizarCombo(comboCategoria);
        panel.add(comboCategoria);

        JLabel lblIcono = new JLabel("Icono:");
        TemaSteam.estilizarLabel(lblIcono);
        panel.add(lblIcono);

        JPanel panelIcono = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        TemaSteam.estilizarPanel(panelIcono);

        JButton btnSeleccionarIcono = new JButton("Seleccionar icono");
        TemaSteam.estilizarBoton(btnSeleccionarIcono, TemaSteam.FONDO_TARJETA);

        lblVistaPrevia = new JLabel();
        lblVistaPrevia.setPreferredSize(new Dimension(44, 44));

        btnSeleccionarIcono.addActionListener(e -> seleccionarIcono());

        panelIcono.add(btnSeleccionarIcono);
        panelIcono.add(lblVistaPrevia);

        panel.add(panelIcono);

        add(panel, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel();
        TemaSteam.estilizarPanel(panelBotones);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));

        JButton btnSubir = new JButton("Subir juego");
        TemaSteam.estilizarBoton(btnSubir, TemaSteam.AZUL_STEAM);

        JButton btnCancelar = new JButton("Cancelar");
        TemaSteam.estilizarBoton(btnCancelar, TemaSteam.ROJO_PELIGRO);

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
                    "El peso debe ser un numero valido.");
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
