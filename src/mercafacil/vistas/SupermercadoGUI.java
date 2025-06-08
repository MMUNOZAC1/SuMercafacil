package mercafacil.vistas;

import mercafacil.modelo.Usuario;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

public class SupermercadoGUI extends JFrame {
    private JLabel lblBienvenida;
    private JPanel panelBotones;
    private Connection connection;

    public SupermercadoGUI(Connection connection) {
        this.connection = connection;

        setTitle("Panel Principal - MercaFácil");
        setSize(500, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        lblBienvenida = new JLabel("Bienvenido al sistema", SwingConstants.CENTER);
        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 16));
        add(lblBienvenida, BorderLayout.NORTH);

        panelBotones = new JPanel(new FlowLayout());
        add(panelBotones, BorderLayout.CENTER);
    }

    public void ajustarInterfazPorRol(Usuario usuario) {
        lblBienvenida.setText("Bienvenido, " + usuario.getNombre() + " (" + usuario.getRol() + ")");

        panelBotones.removeAll(); // Limpiar por si se cambia de usuario

        JButton btnInventario = new JButton("Inventario");
        JButton btnGestionProductos = new JButton("Gestionar Productos");
        JButton btnGestionUsuarios = new JButton("Gestionar Usuarios");
        JButton btnSimularCompra = new JButton("Simular Compra");
        JButton btnSalir = new JButton("Cerrar Sesión");

        // Acciones
        btnInventario.addActionListener(e -> new InventarioForm(connection).setVisible(true));
        btnGestionProductos.addActionListener(e -> new ProductoForm(connection).setVisible(true));
        btnGestionUsuarios.addActionListener(e -> new UsuarioForm(connection).setVisible(true));
        btnSimularCompra.addActionListener(e -> new SimuladorCobroForm(connection).setVisible(true));
        btnSalir.addActionListener(e -> {
            dispose();
            new LoginForm(connection).setVisible(true);
        });

        // Botones comunes
        panelBotones.add(btnInventario);
        panelBotones.add(btnSimularCompra); // ¡Nuevo botón agregado!

        // Botones solo para gerente
        if (usuario.getRol().equalsIgnoreCase("gerente")) {
            panelBotones.add(btnGestionProductos);
            panelBotones.add(btnGestionUsuarios);
        }

        panelBotones.add(btnSalir);

        revalidate();
        repaint();
    }
}
