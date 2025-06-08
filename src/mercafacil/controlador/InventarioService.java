package mercafacil.controlador;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

public class InventarioService {
    private final Connection connection;

    // Constructor necesario para inicializar el servicio con la conexión
    public InventarioService(Connection connection) {
        this.connection = connection;
    }

    // Obtener todo el inventario con ID, nombre, cantidad y precio
    public List<String[]> obtenerInventarioCompleto() throws SQLException {
        List<String[]> inventario = new ArrayList<>();
        String sql = "SELECT * FROM inventario";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                inventario.add(new String[]{
                        String.valueOf(rs.getInt("id")),
                        rs.getString("nombre"),
                        String.valueOf(rs.getInt("cantidad")),
                        String.format("%.2f", rs.getDouble("precio"))
                });
            }
        }

        return inventario;
    }

    // Agregar producto al inventario
    public void agregarProducto(String nombre, int cantidad, double precio) throws SQLException {
        String sql = "INSERT INTO inventario (nombre, cantidad, precio) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nombre);
            stmt.setInt(2, cantidad);
            stmt.setDouble(3, precio);
            stmt.executeUpdate();
        }
    }

    // Eliminar producto por ID
    public void eliminarProducto(int id) throws SQLException {
        String sql = "DELETE FROM inventario WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
