package gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import domain.Usuario;

public class IniciarSesion extends JDialog {

    private static final long serialVersionUID = 1L;
    private JTextField txtUsuario;
    private JPasswordField txtContrasena;
    private Usuario usuarioLogueado = null;
    private List<Usuario> listaUsuarios;

    public IniciarSesion(Frame parent, List<Usuario> listaUsuarios) {
        super(parent, "Iniciar sesión - SteamApp", true);
        this.listaUsuarios = listaUsuarios;

        setSize(420, 340);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(TemaSteam.FONDO_OSCURO);

        // Cabecera Steam
        JPanel panelCabecera = new JPanel(new GridLayout(2, 1, 0, 4));
        panelCabecera.setBackground(TemaSteam.FONDO_OSCURO);
        panelCabecera.setBorder(BorderFactory.createEmptyBorder(20, 20, 14, 20));

        JLabel lblLogo = new JLabel("STEAM APP STORE", SwingConstants.CENTER);
        lblLogo.setForeground(TemaSteam.AZUL_STEAM);
        lblLogo.setFont(new Font("SansSerif", Font.BOLD, 22));

        JLabel lblSub = new JLabel("INICIAR SESIÓN CON TU CUENTA", SwingConstants.CENTER);
        lblSub.setForeground(TemaSteam.TEXTO_CLARO);
        lblSub.setFont(new Font("SansSerif", Font.BOLD, 11));

        panelCabecera.add(lblLogo);
        panelCabecera.add(lblSub);
        add(panelCabecera, BorderLayout.NORTH);

        // Formulario central
        JPanel panelForm = new JPanel();
        panelForm.setLayout(new BoxLayout(panelForm, BoxLayout.Y_AXIS));
        panelForm.setBackground(TemaSteam.FONDO_PANEL);
        panelForm.setBorder(BorderFactory.createEmptyBorder(18, 35, 18, 35));

        JLabel lblUser = new JLabel("NOMBRE DE USUARIO");
        lblUser.setForeground(TemaSteam.AZUL_STEAM);
        lblUser.setFont(new Font("SansSerif", Font.BOLD, 11));
        lblUser.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtUsuario = new JTextField();
        txtUsuario.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtUsuario.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        txtUsuario.setPreferredSize(new Dimension(340, 34));
        txtUsuario.setAlignmentX(Component.LEFT_ALIGNMENT);
        TemaSteam.estilizarCampoTexto(txtUsuario);

        JLabel lblPass = new JLabel("CONTRASEÑA");
        lblPass.setForeground(TemaSteam.TEXTO_CLARO);
        lblPass.setFont(new Font("SansSerif", Font.BOLD, 11));
        lblPass.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtContrasena = new JPasswordField();
        txtContrasena.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtContrasena.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        txtContrasena.setPreferredSize(new Dimension(340, 34));
        txtContrasena.setAlignmentX(Component.LEFT_ALIGNMENT);
        TemaSteam.estilizarCampoTexto(txtContrasena);

        panelForm.add(lblUser);
        panelForm.add(Box.createRigidArea(new Dimension(0, 6)));
        panelForm.add(txtUsuario);
        panelForm.add(Box.createRigidArea(new Dimension(0, 14)));
        panelForm.add(lblPass);
        panelForm.add(Box.createRigidArea(new Dimension(0, 6)));
        panelForm.add(txtContrasena);

        add(panelForm, BorderLayout.CENTER);

        // Botones inferiores
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 14));
        panelBotones.setBackground(TemaSteam.FONDO_OSCURO);

        JButton btnLogin = new JButton("Iniciar sesión");
        TemaSteam.estilizarBoton(btnLogin, TemaSteam.AZUL_STEAM);
        btnLogin.addActionListener(e -> intentarLogin());

        JButton btnCancelar = new JButton("Salir");
        TemaSteam.estilizarBoton(btnCancelar, TemaSteam.FONDO_TARJETA);
        btnCancelar.addActionListener(e -> dispose());

        panelBotones.add(btnLogin);
        panelBotones.add(btnCancelar);
        add(panelBotones, BorderLayout.SOUTH);

        getRootPane().setDefaultButton(btnLogin);
    }

    private void intentarLogin() {
        String nombre = txtUsuario.getText().trim();
        String pass = new String(txtContrasena.getPassword());

        for (Usuario u : listaUsuarios) {
            if (u.getNombre().equalsIgnoreCase(nombre) && u.getContraseña().equals(pass)) {
                this.usuarioLogueado = u;
                dispose();
                return;
            }
        }

        JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.", "Error de acceso",
                JOptionPane.ERROR_MESSAGE);
    }

    public Usuario getUsuarioLogueado() {
        return usuarioLogueado;
    }
}