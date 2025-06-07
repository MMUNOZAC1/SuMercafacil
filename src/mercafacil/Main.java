package mercafacil;

import mercafacil.controlador.Conexion;
import mercafacil.vistas.LoginForm;
import mercafacil.vistas.RegistroUsuarioForm;

import javax.swing.*;
import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                Connection connection = Conexion.getConexion();
                new LoginForm(connection).setVisible(true);
                // new RegistroUsuarioForm(connection).setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error conectando a la base de datos: " + e.getMessage());
            }
        });
    }
}
