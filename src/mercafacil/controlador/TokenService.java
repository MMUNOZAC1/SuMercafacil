package mercafacil.controlador;

import mercafacil.modelo.Usuario;

public class TokenService {
    public static String generarToken(Usuario usuario) {
        return usuario.getEmail() + ":" + usuario.getRol();
    }

    public static Usuario verificarToken(String token, UsuarioDAO dao) throws Exception {
        String[] partes = token.split(":");
        if (partes.length == 2) {
            String email = partes[0];
            Usuario usuario = dao.buscarPorEmail(email);
            if (usuario != null && usuario.getRol().equals(partes[1])) {
                return usuario;
            }
        }
        throw new SecurityException("Token inválido");
    }
}