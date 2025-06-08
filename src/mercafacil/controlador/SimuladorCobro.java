package mercafacil.controlador;

import mercafacil.modelo.Cajera;
import mercafacil.modelo.Cliente;

import javax.swing.JTextArea;

/** Arranca la simulación de cobro para un cliente concreto. */
public class SimuladorCobro {

    /**
     * Crea un hilo Cajera y lo pone a trabajar.
     *
     * @param cliente       Cliente a atender
     * @param nombreCajera  Nombre que se mostrará para la cajera
     * @param areaReporte   Área de texto donde se irán escribiendo los eventos
     */
    public static void iniciarCobro(Cliente cliente, String nombreCajera, JTextArea areaReporte) {
        new Cajera(nombreCajera, cliente, areaReporte).start();
    }
}

