package mercafacil.controlador;

import mercafacil.modelo.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Servicio para simular el proceso de cobro en una tienda.
 * Asigna clientes a cajeras y calcula el detalle de cada compra.
 */
public class SimulacionCobroService {

    /**
     * Ejecuta la simulación de cobro.
     * Cada cliente es atendido por una cajera (en orden rotativo).
     *
     * @param clientes Lista de clientes con productos.
     * @param cajeras Lista de cajeras disponibles.
     * @return Lista de compras simuladas, con detalles y tiempos.
     */
    public List<CompraSimulada> simular(List<Cliente> clientes, List<Cajera> cajeras) {
        List<CompraSimulada> resultados = new ArrayList<>();

        for (int i = 0; i < clientes.size(); i++) {
            Cliente cliente = clientes.get(i);
            Cajera cajera = cajeras.get(i % cajeras.size()); // Rotar entre cajeras

            CompraSimulada compra = new CompraSimulada(cliente, cajera);

            for (Producto producto : cliente.getProductos()) {
                int tiempo = producto.getCantidad() * cajera.getTiempoPorProducto();
                DetalleSimulacion detalle = new DetalleSimulacion(
                        producto.getNombre(),
                        producto.getCantidad(),
                        producto.getPrecioUnitario(),
                        tiempo
                );
                compra.agregarDetalle(detalle);
            }

            resultados.add(compra);
        }

        return resultados;
    }
}
