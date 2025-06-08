package mercafacil.controlador;

import mercafacil.modelo.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoService {
    private final Connection connection;

    public ProductoService(Connection connection) {
        this.connection = connection;
    }

    // ✅ Para SimuladorCobroForm - retorna objetos Producto
    public List<Producto> obtenerTodos() throws SQLException {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM producto";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Producto p = new Producto(
                        rs.getString("nombre"),
                        rs.getInt("stock"), // cantidad usada como stock
                        rs.getDouble("precio")
                );
                lista.add(p);
            }
        }

        return lista;
    }

    // ✅ Para ProductoForm - retorna datos en formato texto
    public List<String[]> obtenerTodosComoTexto() throws SQLException {
        List<String[]> lista = new ArrayList<>();
        String sql = "SELECT * FROM producto";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(new String[]{
                        String.valueOf(rs.getInt("id")),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        String.format("%.2f", rs.getDouble("precio")),
                        String.valueOf(rs.getInt("stock"))
                });
            }
        }

        return lista;
    }

    public void agregar(String nombre, String descripcion, double precio, int stock) throws SQLException {
        String sql = "INSERT INTO producto (nombre, descripcion, precio, stock) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nombre);
            stmt.setString(2, descripcion);
            stmt.setDouble(3, precio);
            stmt.setInt(4, stock);
            stmt.executeUpdate();
        }
    }

    public void actualizar(int id, String nombre, String descripcion, double precio, int stock) throws SQLException {
        String sql = "UPDATE producto SET nombre = ?, descripcion = ?, precio = ?, stock = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nombre);
            stmt.setString(2, descripcion);
            stmt.setDouble(3, precio);
            stmt.setInt(4, stock);
            stmt.setInt(5, id);
            stmt.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM producto WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
