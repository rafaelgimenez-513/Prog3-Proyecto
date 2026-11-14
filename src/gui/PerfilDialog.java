package gui;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import domain.Usuario;

public class PerfilDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    public PerfilDialog(Frame parent, Usuario usuario) {
        super(parent, "Perfil de Usuario", true);
        setSize(320, 220);
        setResizable(false);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        getContentPane().setBackground(TemaSteam.FONDO_OSCURO);

        JPanel panelInfo = new JPanel(new GridLayout(4, 1, 8, 8));
        panelInfo.setBackground(TemaSteam.FONDO_PANEL);
        panelInfo.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblTitulo = new JLabel("PERFIL DE STEAM", SwingConstants.CENTER);
        lblTitulo.setForeground(TemaSteam.AZUL_STEAM);
        lblTitulo.setFont(lblTitulo.getFont().deriveFont(15f));

        JLabel lblUser = new JLabel("Usuario: " + usuario.getNombre(), SwingConstants.CENTER);
        TemaSteam.estilizarLabel(lblUser);

        JLabel lblRol = new JLabel("Rol: " + usuario.getTipoUsuario(), SwingConstants.CENTER);
        TemaSteam.estilizarLabel(lblRol);

        JLabel lblApps = new JLabel("Apps en biblioteca: " + usuario.getBiblioteca().size(), SwingConstants.CENTER);
        TemaSteam.estilizarLabel(lblApps);

        panelInfo.add(lblTitulo);
        panelInfo.add(lblUser);
        panelInfo.add(lblRol);
        panelInfo.add(lblApps);
        add(panelInfo, BorderLayout.CENTER);

        JPanel panelSur = new JPanel();
        panelSur.setBackground(TemaSteam.FONDO_OSCURO);
        JButton btnCerrar = new JButton("Cerrar");
        TemaSteam.estilizarBoton(btnCerrar, TemaSteam.FONDO_TARJETA);
        btnCerrar.addActionListener(e -> dispose());
        panelSur.add(btnCerrar);
        add(panelSur, BorderLayout.SOUTH);
    }
}