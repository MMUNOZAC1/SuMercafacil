package mercafacil.controlador;

import mercafacil.modelo.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioService {
    private final Connection connection;

    public UsuarioService(Connection connection) {
        this.connection = connection;
    }

    public List<Usuario> obtenerTodos() throws SQLException {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Usuario u = new Usuario(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("email"),
                        rs.getString("contrasena"),
                        rs.getString("rol")
                );
                lista.add(u);
            }
        }

        return lista;
    }

    public void agregar(Usuario usuario) throws SQLException {
        UsuarioDAO dao = new UsuarioDAO(connection);
        dao.crearUsuario(usuario);
    }

    public void actualizar(Usuario usuario) throws SQLException {
        String sql = "UPDATE usuarios SET nombre = ?, rol = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getRol());
            stmt.setInt(3, usuario.getId());
            stmt.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
