package mercafacil.vistas;

import mercafacil.controlador.AutenticacionService;
import mercafacil.controlador.UsuarioDAO;
import mercafacil.modelo.Usuario;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

public class LoginForm extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

    public LoginForm(Connection connection) {
        setTitle("Iniciar Sesión");
        setSize(300, 220);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar ventana

        emailField = new JTextField();
        passwordField = new JPasswordField();
        loginButton = new JButton("Entrar");
        registerButton = new JButton("Regístrate");

        setLayout(new GridLayout(5, 1, 5, 5));
        add(new JLabel("Email:"));
        add(emailField);
        add(new JLabel("Contraseña:"));
        add(passwordField);
        add(loginButton);
        add(registerButton);

        loginButton.addActionListener(e -> {
            try {
                UsuarioDAO dao = new UsuarioDAO(connection);
                AutenticacionService auth = new AutenticacionService(dao);
                Usuario user = auth.autenticar(emailField.getText(), new String(passwordField.getPassword()));
                if (user != null) {
                    JOptionPane.showMessageDialog(this, "Bienvenido " + user.getNombre() + " (" + user.getRol() + ")");
                    // Aquí podrías abrir el menú principal u otra ventana
                } else {
                    JOptionPane.showMessageDialog(this, "Credenciales inválidas");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al autenticar");
            }
        });

        registerButton.addActionListener(e -> {
            dispose(); // Cierra el login
            new RegistroUsuarioForm(connection).setVisible(true); // Abre el formulario de registro
        });
    }
}
