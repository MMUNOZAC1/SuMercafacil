package mercafacil.vistas;

import mercafacil.controlador.InventarioService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.util.List;

public class InventarioForm extends JFrame {
    private JTable tablaInventario;
    private DefaultTableModel modelo;
    private Connection connection;

    public InventarioForm(Connection connection) {
        this.connection = connection;

        setTitle("Inventario");
        setSize(600, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initUI();
        cargarInventario();
    }

    private void initUI() {
        modelo = new DefaultTableModel(new String[]{"ID", "Nombre", "Cantidad", "Precio"}, 0);
        tablaInventario = new JTable(modelo);
        JScrollPane scrollPane = new JScrollPane(tablaInventario);

        JButton btnAgregar = new JButton("Agregar Producto");
        JButton btnEliminar = new JButton("Eliminar Producto");

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnAgregar);
        panelBotones.add(btnEliminar);

        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(panelBotones, BorderLayout.SOUTH);

        // Acción: agregar producto
        btnAgregar.addActionListener(e -> {
            JTextField nombre = new JTextField();
            JTextField cantidad = new JTextField();
            JTextField precio = new JTextField();

            Object[] inputs = {
                    "Nombre:", nombre,
                    "Cantidad:", cantidad,
                    "Precio:", precio
            };

            int result = JOptionPane.showConfirmDialog(this, inputs, "Nuevo Producto", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    InventarioService servicio = new InventarioService(connection);
                    servicio.agregarProducto(nombre.getText(),
                            Integer.parseInt(cantidad.getText()),
                            Double.parseDouble(precio.getText()));
                    cargarInventario();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error al agregar: " + ex.getMessage());
                }
            }
        });

        // Acción: eliminar producto seleccionado
        btnEliminar.addActionListener(e -> {
            int fila = tablaInventario.getSelectedRow();
            if (fila != -1) {
                int id = Integer.parseInt(modelo.getValueAt(fila, 0).toString());
                try {
                    InventarioService servicio = new InventarioService(connection);
                    servicio.eliminarProducto(id);
                    cargarInventario();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error al eliminar: " + ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecciona un producto para eliminar.");
            }
        });
    }

    private void cargarInventario() {
        try {
            InventarioService servicio = new InventarioService(connection);
            List<String[]> datos = servicio.obtenerInventarioCompleto();

            modelo.setRowCount(0); // Limpiar
            for (String[] fila : datos) {
                modelo.addRow(fila);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar inventario: " + e.getMessage());
        }
    }
}
