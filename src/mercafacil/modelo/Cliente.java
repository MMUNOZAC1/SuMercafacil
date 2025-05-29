package mercafacil.modelo;

import java.util.List;

public class Cliente {
    private String nombre;
    private List<Producto> carrito;

    public Cliente(String nombre, List<Producto> carrito) {
        this.nombre = nombre;
        this.carrito = carrito;
    }

    public String getNombre() {
        return nombre;
    }

    public List<Producto> getCarrito() {
        return carrito;
    }
}
