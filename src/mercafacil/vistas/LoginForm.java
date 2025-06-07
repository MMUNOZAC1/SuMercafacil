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

    public LoginForm(Connection connection) {
        setTitle("Iniciar Sesión");
        setSize(300, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        emailField = new JTextField();
        passwordField = new JPasswordField();
        loginButton = new JButton("Entrar");

        setLayout(new GridLayout(4, 1));
        add(new JLabel("Email:"));
        add(emailField);
        add(new JLabel("Contraseña:"));
        add(passwordField);
        add(loginButton);

        loginButton.addActionListener(e -> {
            try {
                UsuarioDAO dao = new UsuarioDAO(connection);
                AutenticacionService auth = new AutenticacionService(dao);
                Usuario user = auth.autenticar(emailField.getText(), new String(passwordField.getPassword()));
                if (user != null) {
                    JOptionPane.showMessageDialog(this, "Bienvenido " + user.getNombre() + " (" + user.getRol() + ")");
                } else {
                    JOptionPane.showMessageDialog(this, "Credenciales inválidas");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al autenticar");
            }
        });
    }
}