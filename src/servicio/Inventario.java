package servicio;

import modelo.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class Inventario {

    private UUID id;
    private double cantidadDisponible;
    private double cantidadReservada;
    private List<Producto> productos;
    private Map<Producto, Double> stockPorProducto;
    private List<MovimientoStock> movimientos;

    public Inventario() {
        this.id = UUID.randomUUID();
        this.cantidadDisponible = 0;
        this.cantidadReservada = 0;
        this.productos = new ArrayList<>();
        this.stockPorProducto = new HashMap<>();
        this.movimientos = new ArrayList<>();
    }

    public UUID getId() {
        return id;
    }

    public double getCantidadDisponible() {
        return cantidadDisponible;
    }

    public double getCantidadReservada() {
        return cantidadReservada;
    }

    public void setCantidadReservada(double cantidadReservada) {
        this.cantidadReservada = cantidadReservada;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public List<MovimientoStock> getMovimientos() {
        return movimientos;
    }

    public void agregarProducto(Producto p) {
        if (!productos.contains(p)) {
            productos.add(p);
            stockPorProducto.put(p, 0.0);
        }
    }

    public double obtenerStockActual(Producto p) {
        return stockPorProducto.getOrDefault(p, 0.0);
    }

    public void registrarEntrada(Producto p, double cantidad, Usuario u) {
        registrarEntrada(p, cantidad, u, "Ingreso de mercancía", "");
    }

    public void registrarEntrada(Producto p, double cantidad, Usuario u,
                                 String motivo, String referenciaDocumento) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad a ingresar debe ser mayor a cero.");
        }

        agregarProducto(p);

        double stockActual = stockPorProducto.get(p);
        stockPorProducto.put(p, stockActual + cantidad);
        this.cantidadDisponible += cantidad;

        MovimientoStock movimiento = new MovimientoStock(
                MovimientoStock.TipoMovimiento.ENTRADA,
                cantidad,
                motivo,
                referenciaDocumento,
                p,
                u
        );
        movimientos.add(movimiento);
    }

    public void registrarSalida(Producto p, double cantidad, Usuario u) {
        registrarSalida(p, cantidad, u, "Salida por venta", "");
    }

    public void registrarSalida(Producto p, double cantidad, Usuario u,
                                String motivo, String referenciaDocumento) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad a retirar debe ser mayor a cero.");
        }

        double stockActual = stockPorProducto.getOrDefault(p, 0.0);
        if (stockActual < cantidad) {
            throw new IllegalStateException(
                    "Stock insuficiente para el producto " + p.getSku() +
                    ". Disponible: " + stockActual + ", solicitado: " + cantidad
            );
        }

        stockPorProducto.put(p, stockActual - cantidad);
        this.cantidadDisponible -= cantidad;

    
        MovimientoStock movimiento = new MovimientoStock(
                MovimientoStock.TipoMovimiento.SALIDA_VENTA,
                cantidad,
                motivo,
                referenciaDocumento,
                p,
                u
        );
        movimientos.add(movimiento);
    }

    public List<Producto> obtenerAlertasStock() {
        List<Producto> alertas = new ArrayList<>();
        for (Producto p : productos) {
            double stockActual = stockPorProducto.getOrDefault(p, 0.0);
            if (p.esBajoStock(stockActual)) {
                alertas.add(p);
            }
        }
        return alertas;
    }

    public void consultarKardex() {
        if (movimientos.isEmpty()) {
            System.out.println("No hay movimientos registrados.");
            return;
        }
        for (MovimientoStock m : movimientos) {
            System.out.println(m.mostrarDetalleKardex());
        }
    }
}
