package mercafacil.controlador;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private static final String URL = "jdbc:postgresql://localhost:5432/MercaFacil";
    private static final String USUARIO = "mmunozac";
    private static final String CONTRASENA = "IUdigital2025";

    public static Connection getConexion() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CONTRASENA);
    }
}
