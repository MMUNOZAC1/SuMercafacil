package mercafacil.controlador;

import mercafacil.modelo.Usuario;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.SQLException;

public class AutenticacionService {
    private UsuarioDAO usuarioDAO;

    public AutenticacionService(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    public Usuario autenticar(String email, String contrasena) throws SQLException {
        Usuario usuario = usuarioDAO.buscarPorEmail(email);
        if (usuario != null && BCrypt.checkpw(contrasena, usuario.getContrasena())) {
            return usuario;
        }
        return null;
    }
}
