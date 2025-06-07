package mercafacil.vistas;

import mercafacil.controlador.SimuladorCobro;
import mercafacil.modelo.Cliente;
import mercafacil.modelo.Producto;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SimuladorCobroForm extends JFrame {
    private JComboBox<String> clienteCombo;
    private JTextField cajeraField;
    private JTextArea areaReporte;
    private JButton iniciarButton;

    private List<Cliente> clientes;

    public SimuladorCobroForm() {
        setTitle("Simulación de Cobro - Supermercado");
        setSize(600, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Clientes predefinidos con productos simulados
        clientes = generarClientesDePrueba();

        clienteCombo = new JComboBox<>();
        for (Cliente cliente : clientes) {
            clienteCombo.addItem(cliente.getNombre());
        }

        cajeraField = new JTextField();
        areaReporte = new JTextArea();
        areaReporte.setEditable(false);
        areaReporte.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(areaReporte);
        iniciarButton = new JButton("Iniciar Cobro");

        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new GridLayout(3, 2));
        topPanel.add(new JLabel("Nombre Cajera:"));
        topPanel.add(cajeraField);
        topPanel.add(new JLabel("Seleccionar Cliente:"));
        topPanel.add(clienteCombo);
        topPanel.add(new JLabel(""));
        topPanel.add(iniciarButton);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        iniciarButton.addActionListener(e -> {
            String nombreCajera = cajeraField.getText().trim();
            int clienteIndex = clienteCombo.getSelectedIndex();

            if (nombreCajera.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingrese el nombre de la cajera.");
                return;
            }

            Cliente cliente = clientes.get(clienteIndex);
            SimuladorCobro.iniciarCobro(cliente, nombreCajera, areaReporte);
        });
    }

    private List<Cliente> generarClientesDePrueba() {
        Producto arroz = new Producto("Arroz", 3500, 500);
        Producto leche = new Producto("Leche", 2800, 300);
        Producto pan = new Producto("Pan", 2000, 200);
        Producto huevos = new Producto("Huevos", 4500, 600);

        List<Cliente> lista = new ArrayList<>();
        lista.add(new Cliente("Carlos", Arrays.asList(arroz, pan, huevos)));
        lista.add(new Cliente("Laura", Arrays.asList(leche, leche, pan)));
        lista.add(new Cliente("Andrés", Arrays.asList(huevos, arroz)));
        return lista;
    }
}
