package mercafacil.controlador;

import mercafacil.modelo.Usuario;
import java.sql.*;
import org.mindrot.jbcrypt.BCrypt;

public class UsuarioDAO {
    private Connection connection;

    public UsuarioDAO(Connection connection) {
        this.connection = connection;
    }

    // Crear nuevo usuario con contraseña encriptada
    public void crearUsuario(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuarios (nombre, email, contrasena, rol) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            String hashed = BCrypt.hashpw(usuario.getContrasena(), BCrypt.gensalt());
            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, hashed); // Guardamos la contraseña encriptada
            stmt.setString(4, usuario.getRol());
            stmt.executeUpdate();
        }
    }

    // Buscar usuario por email (para login)
    public Usuario buscarPorEmail(String email) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Usuario(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("email"),
                        rs.getString("contrasena"), // Muy importante
                        rs.getString("rol")
                );
            }
        }
        return null;
    }
}
