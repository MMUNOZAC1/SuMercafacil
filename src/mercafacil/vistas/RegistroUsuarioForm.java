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
    private JButton registrarButton, volverLoginButton;
    private Connection connection;

    public RegistroUsuarioForm(Connection connection) {
        this.connection = connection;

        setTitle("Registrar Usuario");
        setSize(300, 320);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        nombreField = new JTextField();
        emailField = new JTextField();
        passwordField = new JPasswordField();
        rolCombo = new JComboBox<>(new String[]{"administrador", "gerente"});
        registrarButton = new JButton("Registrar");
        volverLoginButton = new JButton("Volver al Login");

        setLayout(new GridLayout(8, 1, 5, 5));

        add(new JLabel("Nombre:"));
        add(nombreField);
        add(new JLabel("Email:"));
        add(emailField);
        add(new JLabel("Contraseña:"));
        add(passwordField);
        add(new JLabel("Rol:"));
        add(rolCombo);
        add(registrarButton);
        add(volverLoginButton);

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
                dispose();
                new LoginForm(connection).setVisible(true);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al registrar usuario");
            }
        });

        volverLoginButton.addActionListener(e -> {
            dispose();
            new LoginForm(connection).setVisible(true);
        });
    }
}
