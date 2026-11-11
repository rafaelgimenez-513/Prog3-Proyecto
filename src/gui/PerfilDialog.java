package gui;

import java.awt.Frame;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import domain.Usuario;

public class PerfilDialog extends JDialog {
    /**
     
     **/
  private static final long serialVersionUID = 1L;

    public PerfilDialog(Frame parent, Usuario usuario) {
        super(parent, "Perfil de Usuario", true);
        setSize(280, 180);
        setLocationRelativeTo(parent);
        setLayout(new GridLayout(4, 1, 10, 10));

        add(new JLabel("Usuario: " + usuario.getNombre(), SwingConstants.CENTER));
        add(new JLabel("Rol: " + usuario.getTipoUsuario(), SwingConstants.CENTER));
        add(new JLabel("Estado: Activo", SwingConstants.CENTER));

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        add(btnCerrar);
    }
}