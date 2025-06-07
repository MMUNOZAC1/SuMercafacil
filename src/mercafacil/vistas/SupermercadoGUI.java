package mercafacil.vistas;

import mercafacil.controlador.SimuladorCobro;
import mercafacil.modelo.Cliente;
import mercafacil.modelo.Producto;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class SupermercadoGUI extends JFrame {
    private DefaultListModel<String> carritoModel;
    private List<Producto> carrito;
    private JComboBox<String> cajeraCombo;
    private JTextArea areaReporte;

    public SupermercadoGUI() {
        super("Mercafacil - Simulación de Cobro");
        carrito = new ArrayList<>();
        carritoModel = new DefaultListModel<>();
        setLayout(new BorderLayout());
        add(crearPanelCatalogo(), BorderLayout.CENTER);
        add(crearPanelInferior(), BorderLayout.SOUTH);
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                long tiempoTotal = mercafacil.modelo.Cajera.getTiempoTotalGlobal();
                double totalGlobal = mercafacil.modelo.Cajera.getTotalGlobal();
                JOptionPane.showMessageDialog(null,
                        "Cobro total de la jornada:\nTiempo total: " + tiempoTotal + " ms\nTotal recaudado: $" + totalGlobal,
                        "Resumen Final", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    private JPanel crearPanelCatalogo() {
        JPanel panel = new JPanel(new GridLayout(2, 3, 10, 10));
        List<Producto> productos = List.of(
                new Producto("Leche", 3500, 1000),
                new Producto("Pan", 2500, 800),
                new Producto("Queso", 7800, 1200),
                new Producto("Huevos", 6900, 1100),
                new Producto("Jugo", 4500, 900),
                new Producto("Cafe", 5800, 1000)
        );
        for (Producto p : productos) {
            panel.add(crearTarjetaProducto(p));
        }
        return panel;
    }

    private JPanel crearTarjetaProducto(Producto producto) {
        JPanel card = new JPanel(new BorderLayout());
        JLabel nombre = new JLabel(producto.getNombre(), SwingConstants.CENTER);
        JLabel precio = new JLabel("$" + producto.getPrecio(), SwingConstants.CENTER);
        JLabel imagen = new JLabel();
        try {
            ImageIcon icon = new ImageIcon("resources/productos/" + producto.getNombre().toLowerCase() + ".png");
            imagen.setIcon(new ImageIcon(icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
        } catch (Exception e) {
            imagen.setText("[Sin imagen]");
        }
        JButton agregar = new JButton("Agregar");
        agregar.addActionListener((ActionEvent e) -> {
            carrito.add(producto);
            carritoModel.addElement(producto.getNombre() + " - $" + producto.getPrecio());
        });
        card.add(nombre, BorderLayout.NORTH);
        card.add(imagen, BorderLayout.CENTER);
        card.add(precio, BorderLayout.SOUTH);
        card.add(agregar, BorderLayout.EAST);
        card.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        return card;
    }

    private JPanel crearPanelInferior() {
        JPanel panel = new JPanel(new BorderLayout());
        JList<String> carritoList = new JList<>(carritoModel);
        JScrollPane scrollCarrito = new JScrollPane(carritoList);
        scrollCarrito.setPreferredSize(new Dimension(300, 150));
        panel.add(scrollCarrito, BorderLayout.WEST);

        areaReporte = new JTextArea(10, 40);
        areaReporte.setEditable(false);
        JScrollPane scrollReporte = new JScrollPane(areaReporte);
        JButton guardarReporte = new JButton("Guardar reporte");
        guardarReporte.addActionListener(e -> guardarReporteEnArchivo());
        JPanel panelCentro = new JPanel(new BorderLayout());
        panelCentro.add(scrollReporte, BorderLayout.CENTER);
        panelCentro.add(guardarReporte, BorderLayout.SOUTH);
        panel.add(panelCentro, BorderLayout.CENTER);

        JPanel panelDerecho = new JPanel(new GridLayout(3, 1));
        cajeraCombo = new JComboBox<>(new String[]{"Cajera 1", "Cajera 2", "Cajera 3"});
        JButton pagar = new JButton("Pagar");
        pagar.addActionListener(e -> {
            String nombreCliente = JOptionPane.showInputDialog("Ingrese su nombre:");
            if (nombreCliente == null || nombreCliente.isEmpty()) return;
            Cliente cliente = new Cliente(nombreCliente, new ArrayList<>(carrito));
            String cajera = (String) cajeraCombo.getSelectedItem();
            SimuladorCobro.iniciarCobro(cliente, cajera, areaReporte);
            carrito.clear();
            carritoModel.clear();
        });
        panelDerecho.add(new JLabel("Seleccione cajera:"));
        panelDerecho.add(cajeraCombo);
        panelDerecho.add(pagar);
        panel.add(panelDerecho, BorderLayout.EAST);
        return panel;
    }

    private void guardarReporteEnArchivo() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar reporte");
        int seleccion = fileChooser.showSaveDialog(this);
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            try {
                java.io.File archivo = fileChooser.getSelectedFile();
                if (!archivo.getName().endsWith(".txt")) {
                    archivo = new java.io.File(archivo.getAbsolutePath() + ".txt");
                }
                PrintWriter writer = new PrintWriter(archivo);
                writer.print(areaReporte.getText());
                writer.close();
                JOptionPane.showMessageDialog(this, "Reporte guardado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al guardar el reporte: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
