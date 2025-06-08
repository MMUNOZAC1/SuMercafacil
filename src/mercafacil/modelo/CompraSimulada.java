package mercafacil.modelo;

import java.util.ArrayList;
import java.util.List;

public class CompraSimulada {
    private Cliente cliente;
    private Cajera cajera;
    private List<DetalleSimulacion> detalles;
    private int tiempoTotal;
    private double totalPagar;

    public CompraSimulada(Cliente cliente, Cajera cajera) {
        this.cliente = cliente;
        this.cajera = cajera;
        this.detalles = new ArrayList<>();
    }

    public void agregarDetalle(DetalleSimulacion detalle) {
        detalles.add(detalle);
        tiempoTotal += detalle.getTiempoProcesamiento();
        totalPagar += detalle.getCantidad() * detalle.getPrecioUnitario();
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Cajera getCajera() {
        return cajera;
    }

    public List<DetalleSimulacion> getDetalles() {
        return detalles;
    }

    public int getTiempoTotal() {
        return tiempoTotal;
    }

    public double getTotalPagar() {
        return totalPagar;
    }
}
