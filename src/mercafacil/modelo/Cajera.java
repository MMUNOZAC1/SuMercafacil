package mercafacil.modelo;

import javax.swing.*;

public class Cajera extends Thread {
    private String nombre;
    private Cliente cliente;
    private JTextArea areaReporte;

    private static long tiempoTotalGlobal = 0;
    private static double totalGlobal = 0;

    public Cajera(String nombre, Cliente cliente, JTextArea areaReporte) {
        this.nombre = nombre;
        this.cliente = cliente;
        this.areaReporte = areaReporte;
    }

    public static synchronized void agregarTiempo(long tiempo) {
        tiempoTotalGlobal += tiempo;
    }

    public static synchronized void agregarTotal(double total) {
        totalGlobal += total;
    }

    public static long getTiempoTotalGlobal() {
        return tiempoTotalGlobal;
    }

    public static double getTotalGlobal() {
        return totalGlobal;
    }

    @Override
    public void run() {
        long tiempoInicio = System.currentTimeMillis();
        StringBuilder reporte = new StringBuilder();

        reporte.append("Cajera: ").append(nombre).append("\n");
        reporte.append("Cliente: ").append(cliente.getNombre()).append("\n\n");

        double total = 0;
        for (Producto producto : cliente.getCarrito()) {
            try {
                Thread.sleep(producto.getTiempoProceso());
                total += producto.getPrecio();

                reporte.append("- ").append(producto.getNombre())
                        .append(": $").append(producto.getPrecio())
                        .append(" (").append(producto.getTiempoProceso()).append(" ms)\n");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long tiempoFin = System.currentTimeMillis();
        long duracion = tiempoFin - tiempoInicio;

        reporte.append("\nTotal: $").append(total);
        reporte.append("\nTiempo total: ").append(duracion).append(" ms\n");

        agregarTiempo(duracion);
        agregarTotal(total);

        reporte.append("------------------------------------------------------\n");
        reporte.append("Total acumulado global: $").append(getTotalGlobal()).append("\n");
        reporte.append("Tiempo acumulado total: ").append(getTiempoTotalGlobal()).append(" ms\n");
        reporte.append("======================================================\n\n");

        SwingUtilities.invokeLater(() -> areaReporte.append(reporte.toString()));
    }
}
