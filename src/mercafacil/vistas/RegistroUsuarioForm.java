package mercafacil.vistas;

import mercafacil.controlador.UsuarioDAO;
import mercafacil.modelo.Usuario;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

public class RegistroUsuarioForm extends JFrame {
    private JTextField nombreField, emailField;
    private JPasswordField passwordField;
    private JComboBox<String> rolCombo;
    private JButton registrarButton;

    public RegistroUsuarioForm(Connection connection) {
        setTitle("Registrar Usuario");
        setSize(300, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        nombreField = new JTextField();
        emailField = new JTextField();
        passwordField = new JPasswordField();
        rolCombo = new JComboBox<>(new String[] {"administrador", "gerente"});
        registrarButton = new JButton("Registrar");

        setLayout(new GridLayout(6, 1));
        add(new JLabel("Nombre:"));
        add(nombreField);
        add(new JLabel("Email:"));
        add(emailField);
        add(new JLabel("Contraseña:"));
        add(passwordField);
        add(new JLabel("Rol:"));
        add(rolCombo);
        add(registrarButton);

        registrarButton.addActionListener(e -> {
            try {
                Usuario usuario = new Usuario();
                usuario.setNombre(nombreField.getText());
                usuario.setEmail(emailField.getText());
                usuario.setContrasena(new String(passwordField.getPassword()));
                usuario.setRol((String) rolCombo.getSelectedItem());

                UsuarioDAO dao = new UsuarioDAO(connection);
                dao.crearUsuario(usuario);

                JOptionPane.showMessageDialog(this, "Usuario registrado exitosamente");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al registrar");
            }
        });
    }
}
