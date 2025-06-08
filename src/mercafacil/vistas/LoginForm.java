package mercafacil.vistas;

import mercafacil.modelo.Usuario;
import mercafacil.controlador.UsuarioDAO;
import mercafacil.controlador.AutenticacionService;
import mercafacil.vistas.SupermercadoGUI;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

public class LoginForm extends JFrame {
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnRegistro;
    private final Connection connection;

    public LoginForm(Connection connection) {
        this.connection = connection;

        setTitle("Login - MercaFácil");
        setSize(300, 220);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1, 5, 5));

        txtEmail = new JTextField();
        txtPassword = new JPasswordField();
        btnLogin = new JButton("Iniciar Sesión");
        btnRegistro = new JButton("Registrar Usuario");

        panel.add(new JLabel("Email:"));
        panel.add(txtEmail);
        panel.add(new JLabel("Contraseña:"));
        panel.add(txtPassword);
        panel.add(btnLogin);
        panel.add(btnRegistro);

        add(panel);

        // Acceso
        btnLogin.addActionListener(e -> {
            String email = txtEmail.getText();
            String pass = new String(txtPassword.getPassword());

            try {
                UsuarioDAO dao = new UsuarioDAO(connection);
                AutenticacionService auth = new AutenticacionService(dao);
                Usuario usuario = auth.autenticar(email, pass);

                if (usuario != null) {
                    abrirPrincipal(usuario);
                } else {
                    JOptionPane.showMessageDialog(this, "Credenciales inválidas");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al autenticar");
            }
        });

        // Redirigir a formulario de registro
        btnRegistro.addActionListener(e -> {
            dispose();
            new RegistroUsuarioForm(connection).setVisible(true);
        });
    }

    private void abrirPrincipal(Usuario usuario) {
        SupermercadoGUI gui = new SupermercadoGUI(connection);
        gui.ajustarInterfazPorRol(usuario);
        gui.setVisible(true);
        this.dispose();
    }
}
