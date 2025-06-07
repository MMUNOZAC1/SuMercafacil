package mercafacil;

import mercafacil.controlador.Conexion;
import mercafacil.vistas.LoginForm;

import javax.swing.*;
import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Conexión a la base de datos
                Connection connection = Conexion.getConexion();

                // Mostrar formulario de inicio de sesión
                LoginForm loginForm = new LoginForm(connection);
                loginForm.setVisible(true);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error conectando a la base de datos: " + e.getMessage());
            }
        });
    }
}

