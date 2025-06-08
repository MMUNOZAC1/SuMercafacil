package mercafacil.modelo;

public class DetalleSimulacion {
    private String nombreProducto;
    private int cantidad;
    private double precioUnitario;
    private int tiempoProcesamiento;

    public DetalleSimulacion(String nombreProducto, int cantidad, double precioUnitario, int tiempoProcesamiento) {
        this.nombreProducto = nombreProducto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.tiempoProcesamiento = tiempoProcesamiento;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public int getTiempoProcesamiento() {
        return tiempoProcesamiento;
    }
}
