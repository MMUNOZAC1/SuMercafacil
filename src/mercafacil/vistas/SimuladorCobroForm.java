package mercafacil.vistas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mercafacil.controlador.ProductoService;
import mercafacil.controlador.SimulacionCobroService;
import mercafacil.modelo.*;

public class SimuladorCobroForm extends JFrame {

    private JComboBox<Cajera> comboCajeras;
    private JTextArea areaDetalle;
    private JButton btnPagar, btnGuardar;
    private List<Producto> productosSeleccionados;
    private List<JButton> botonesAgregar;
    private Connection connection;

    public SimuladorCobroForm(Connection connection) {
        this.connection = connection;
        this.productosSeleccionados = new ArrayList<>();

        setTitle("Mercafácil - Simulación de Cobro");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        JPanel panelProductos = new JPanel(new GridLayout(2, 3, 10, 10));
        botonesAgregar = new ArrayList<>();

        List<Producto> productosDisponibles = obtenerProductosDesdeBD();
        for (Producto producto : productosDisponibles) {
            JPanel panel = new JPanel(new BorderLayout());
            JLabel lblNombre = new JLabel(producto.getNombre(), SwingConstants.CENTER);
            JLabel lblPrecio = new JLabel("$" + producto.getPrecioUnitario(), SwingConstants.CENTER);
            JButton btnAgregar = new JButton("Agregar");
            btnAgregar.addActionListener(e -> agregarProducto(producto));

            panel.add(lblNombre, BorderLayout.NORTH);
            panel.add(lblPrecio, BorderLayout.CENTER);
            panel.add(btnAgregar, BorderLayout.SOUTH);
            panelProductos.add(panel);

            botonesAgregar.add(btnAgregar);
        }

        JPanel panelInferior = new JPanel(new BorderLayout());

        areaDetalle = new JTextArea(10, 30);
        areaDetalle.setEditable(false);
        JScrollPane scrollDetalle = new JScrollPane(areaDetalle);

        JPanel panelControles = new JPanel(new FlowLayout());

        comboCajeras = new JComboBox<>(crearCajerasDeEjemplo().toArray(new Cajera[0]));
        btnPagar = new JButton("Pagar");
        btnPagar.addActionListener(this::pagarSimulacion);

        btnGuardar = new JButton("Guardar reporte");
        btnGuardar.addActionListener(e -> JOptionPane.showMessageDialog(this, "Funcionalidad pendiente"));

        panelControles.add(new JLabel("Seleccione cajera:"));
        panelControles.add(comboCajeras);
        panelControles.add(btnPagar);
        panelControles.add(btnGuardar);

        panelInferior.add(scrollDetalle, BorderLayout.CENTER);
        panelInferior.add(panelControles, BorderLayout.SOUTH);

        add(panelProductos, BorderLayout.NORTH);
        add(panelInferior, BorderLayout.CENTER);
    }

    private void agregarProducto(Producto producto) {
        productosSeleccionados.add(producto);
        areaDetalle.append("- " + producto.getNombre() + ": $" + producto.getPrecioUnitario() + "\n");
    }

    private void pagarSimulacion(ActionEvent e) {
        if (productosSeleccionados.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe agregar al menos un producto.");
            return;
        }

        String nombreCliente = JOptionPane.showInputDialog(this, "Ingrese el nombre del cliente:");
        if (nombreCliente == null || nombreCliente.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nombre de cliente es obligatorio.");
            return;
        }

        List<Producto> productosAgrupados = agruparProductos(productosSeleccionados);
        Cliente cliente = new Cliente(nombreCliente, productosAgrupados);
        Cajera cajeraSeleccionada = (Cajera) comboCajeras.getSelectedItem();

        SimulacionCobroService servicio = new SimulacionCobroService();
        List<CompraSimulada> resultado = servicio.simular(
                Arrays.asList(cliente),
                Arrays.asList(cajeraSeleccionada)
        );

        CompraSimulada compra = resultado.get(0);
        areaDetalle.append("\n--- Cobro ---\n");
        areaDetalle.append("Cliente: " + nombreCliente + "\n");
        areaDetalle.append("Cajera: " + cajeraSeleccionada.getNombre() + "\n");
        for (DetalleSimulacion detalle : compra.getDetalles()) {
            areaDetalle.append(detalle.getNombreProducto() + ": $" + detalle.getPrecioUnitario()
                    + " x" + detalle.getCantidad() + " (" + detalle.getTiempoProcesamiento() + " ms)\n");
        }
        areaDetalle.append("Total: $" + compra.getTotalPagar() + "\n\n");

        productosSeleccionados.clear();
    }

    private List<Producto> agruparProductos(List<Producto> productos) {
        List<Producto> resultado = new ArrayList<>();
        for (Producto p : productos) {
            boolean agregado = false;
            for (Producto rp : resultado) {
                if (rp.getNombre().equals(p.getNombre())) {
                    rp.setCantidad(rp.getCantidad() + 1);
                    agregado = true;
                    break;
                }
            }
            if (!agregado) {
                resultado.add(new Producto(p.getNombre(), 1, p.getPrecioUnitario()));
            }
        }
        return resultado;
    }

    private List<Cajera> crearCajerasDeEjemplo() {
        Cajera c1 = new Cajera("Cajera 1", 1000);
        Cajera c2 = new Cajera("Cajera 2", 800);
        return Arrays.asList(c1, c2);
    }

    private List<Producto> obtenerProductosDesdeBD() {
        try {
            ProductoService productoService = new ProductoService(connection);
            return productoService.obtenerTodos();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar productos desde la base de datos.");
            return new ArrayList<>();
        }
    }
}
