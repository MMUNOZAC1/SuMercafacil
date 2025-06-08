package mercafacil.modelo;

import javax.swing.JTextArea;

public class Cajera extends Thread {
    private String nombre;
    private int tiempoPorProducto;
    private Cliente cliente;
    private JTextArea areaReporte;

    // Constructor para simulaciones sin JTextArea
    public Cajera(String nombre, int tiempoPorProducto) {
        this.nombre = nombre;
        this.tiempoPorProducto = tiempoPorProducto;
    }

    // Constructor completo para SimuladorCobro
    public Cajera(String nombre, Cliente cliente, JTextArea areaReporte) {
        this.nombre = nombre;
        this.cliente = cliente;
        this.areaReporte = areaReporte;
        this.tiempoPorProducto = 1; // valor por defecto
    }

    public String getNombre() {
        return nombre;
    }

    public int getTiempoPorProducto() {
        return tiempoPorProducto;
    }

    @Override
    public void run() {
        if (areaReporte != null && cliente != null) {
            areaReporte.append("Cajera " + nombre + " comienza a atender a " + cliente.getNombre() + "\n");

            int tiempoTotal = 0;
            for (Producto p : cliente.getProductos()) {
                int tiempo = p.getCantidad() * tiempoPorProducto * 1000;
                tiempoTotal += tiempo;

                try {
                    Thread.sleep(tiempo);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                areaReporte.append("Procesado " + p.getNombre() + " x" + p.getCantidad() + "\n");
            }

            areaReporte.append("Cajera " + nombre + " terminó en " + (tiempoTotal / 1000) + " segundos\n\n");
        }
    }

    @Override
    public String toString() {
        return nombre;
    }
}
