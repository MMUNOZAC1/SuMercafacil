package mercafacil.vistas;

import mercafacil.controlador.UsuarioService;
import mercafacil.modelo.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.util.List;

public class UsuarioForm extends JFrame {
    private JTable tabla;
    private DefaultTableModel modelo;
    private final Connection connection;

    public UsuarioForm(Connection connection) {
        this.connection = connection;

        setTitle("Gestión de Usuarios");
        setSize(600, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initUI();
        cargarUsuarios();
    }

    private void initUI() {
        modelo = new DefaultTableModel(new String[]{"ID", "Nombre", "Email", "Rol"}, 0);
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
                Usuario u = new Usuario(
                        Integer.parseInt(modelo.getValueAt(fila, 0).toString()),
                        modelo.getValueAt(fila, 1).toString(),
                        modelo.getValueAt(fila, 2).toString(),
                        "", // No editamos contraseña aquí
                        modelo.getValueAt(fila, 3).toString()
                );
                abrirFormulario(u);
            } else {
                JOptionPane.showMessageDialog(this, "Selecciona un usuario para editar.");
            }
        });

        btnEliminar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila != -1) {
                int id = Integer.parseInt(modelo.getValueAt(fila, 0).toString());
                try {
                    new UsuarioService(connection).eliminar(id);
                    cargarUsuarios();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error al eliminar: " + ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecciona un usuario para eliminar.");
            }
        });
    }

    private void abrirFormulario(Usuario usuario) {
        JTextField nombre = new JTextField();
        JTextField email = new JTextField();
        JPasswordField pass = new JPasswordField();
        JComboBox<String> rol = new JComboBox<>(new String[]{"administrador", "gerente"});

        if (usuario != null) {
            nombre.setText(usuario.getNombre());
            email.setText(usuario.getEmail());
            email.setEnabled(false); // no editable
            rol.setSelectedItem(usuario.getRol());
        }

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Nombre:"));
        panel.add(nombre);
        panel.add(new JLabel("Email:"));
        panel.add(email);
        if (usuario == null) {
            panel.add(new JLabel("Contraseña:"));
            panel.add(pass);
        }
        panel.add(new JLabel("Rol:"));
        panel.add(rol);

        int res = JOptionPane.showConfirmDialog(this, panel,
                usuario == null ? "Nuevo Usuario" : "Editar Usuario",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (res == JOptionPane.OK_OPTION) {
            try {
                UsuarioService servicio = new UsuarioService(connection);
                if (usuario == null) {
                    Usuario nuevo = new Usuario(
                            nombre.getText(),
                            email.getText(),
                            new String(pass.getPassword()),
                            rol.getSelectedItem().toString()
                    );
                    servicio.agregar(nuevo);
                } else {
                    usuario.setNombre(nombre.getText());
                    usuario.setRol(rol.getSelectedItem().toString());
                    servicio.actualizar(usuario);
                }
                cargarUsuarios();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }

    private void cargarUsuarios() {
        try {
            UsuarioService servicio = new UsuarioService(connection);
            List<Usuario> usuarios = servicio.obtenerTodos();
            modelo.setRowCount(0);
            for (Usuario u : usuarios) {
                modelo.addRow(new Object[]{
                        u.getId(),
                        u.getNombre(),
                        u.getEmail(),
                        u.getRol()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar usuarios: " + e.getMessage());
        }
    }
}
