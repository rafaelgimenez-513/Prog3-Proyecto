package gui;

import domain.Usuario;

import java.awt.Frame;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class IniciarSesion extends JDialog {
    /**
     
     */
  private static final long serialVersionUID = 1L;
  private JTextField txtUsuario;
  private JPasswordField txtContrasena;
  private Usuario usuarioLogueado = null;
  private List<Usuario> listaUsuarios;

    public IniciarSesion(Frame parent, List<Usuario> listaUsuarios) {
        super(parent, "Inicio de Sesión", true);
        this.listaUsuarios = listaUsuarios;

        setLayout(new GridLayout(3, 2, 10, 10));
        setSize(320, 150);
        setLocationRelativeTo(null);

        add(new JLabel(" Usuario:"));
        txtUsuario = new JTextField();
        add(txtUsuario);

        add(new JLabel(" Contraseña:"));
        txtContrasena = new JPasswordField();
        add(txtContrasena);

        JButton btnLogin = new JButton("Iniciar Sesión");
        btnLogin.addActionListener(e -> intentarLogin());

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());

        add(btnLogin);
        add(btnCancelar);
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

        JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos", "Error", JOptionPane.ERROR_MESSAGE);
    }

    public Usuario getUsuarioLogueado() {
        return usuarioLogueado;
    }
}