package mercafacil.controlador;

import mercafacil.modelo.Cajera;  // ✅ Agregado
import mercafacil.modelo.Cliente;

import javax.swing.*;

public class SimuladorCobro {
    public static void iniciarCobro(Cliente cliente, String nombreCajera, JTextArea areaReporte) {
        new Cajera(nombreCajera, cliente, areaReporte).start();
    }
}
