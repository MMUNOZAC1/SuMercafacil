package mercafacil.vistas;

import mercafacil.controlador.ProductoService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.util.List;

public class ProductoForm extends JFrame {
    private JTable tabla;
    private DefaultTableModel modelo;
    private final Connection connection;

    public ProductoForm(Connection connection) {
        this.connection = connection;

        setTitle("Gestión de Productos");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initUI();
        cargarProductos();
    }

    private void initUI() {
        modelo = new DefaultTableModel(new String[]{"ID", "Nombre", "Descripción", "Precio", "Stock"}, 0);
        tabla = new JTable(modelo);
        JScrollPane scrollPane = new JScrollPane(tabla);

        JButton btnAgregar = new JButton("Agregar");
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);

        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(panelBotones, BorderLayout.SOUTH);

        btnAgregar.addActionListener(e -> abrirFormulario(null));
        btnEditar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila != -1) {
                String[] datos = new String[5];
                for (int i = 0; i < 5; i++) {
                    datos[i] = modelo.getValueAt(fila, i).toString();
                }
                abrirFormulario(datos);
            } else {
                JOptionPane.showMessageDialog(this, "Selecciona un producto para editar.");
            }
        });

        btnEliminar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila != -1) {
                int id = Integer.parseInt(modelo.getValueAt(fila, 0).toString());
                try {
                    new ProductoService(connection).eliminar(id);
                    cargarProductos();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error al eliminar: " + ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecciona un producto para eliminar.");
            }
        });
    }

    private void abrirFormulario(String[] datos) {
        JTextField nombre = new JTextField();
        JTextField descripcion = new JTextField();
        JTextField precio = new JTextField();
        JTextField stock = new JTextField();

        if (datos != null) {
            nombre.setText(datos[1]);
            descripcion.setText(datos[2]);
            precio.setText(datos[3]);
            stock.setText(datos[4]);
        }

        Object[] campos = {
                "Nombre:", nombre,
                "Descripción:", descripcion,
                "Precio:", precio,
                "Stock:", stock
        };

        int res = JOptionPane.showConfirmDialog(this, campos,
                datos == null ? "Nuevo Producto" : "Editar Producto", JOptionPane.OK_CANCEL_OPTION);

        if (res == JOptionPane.OK_OPTION) {
            try {
                ProductoService servicio = new ProductoService(connection);
                if (datos == null) {
                    servicio.agregar(nombre.getText(), descripcion.getText(),
                            Double.parseDouble(precio.getText()), Integer.parseInt(stock.getText()));
                } else {
                    servicio.actualizar(Integer.parseInt(datos[0]), nombre.getText(), descripcion.getText(),
                            Double.parseDouble(precio.getText()), Integer.parseInt(stock.getText()));
                }
                cargarProductos();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }

    private void cargarProductos() {
        try {
            ProductoService servicio = new ProductoService(connection);
            List<String[]> productos = servicio.obtenerTodosComoTexto(); // ✅ Método corregido
            modelo.setRowCount(0);
            for (String[] fila : productos) {
                modelo.addRow(fila);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar productos: " + e.getMessage());
        }
    }
}
