package modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;


public class MovimientoStock {

    // Tipos de movimiento que puede tener un producto en el inventario.
    public enum TipoMovimiento {
        ENTRADA,
        SALIDA_VENTA,
        AJUSTE
    }

    private final UUID id;
    private final LocalDateTime fecha;
    private final TipoMovimiento tipo;
    private final double cantidad;
    private final String motivo;
    private final String referenciaDocumento;
    private final Producto producto;
    private final Usuario usuario;

    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    //Constructor del Movimiento de Stock
    public MovimientoStock(TipoMovimiento tipo, double cantidad, String motivo,
                            String referenciaDocumento, Producto producto, Usuario usuario) {
        this.id = UUID.randomUUID();
        this.fecha = LocalDateTime.now();
        this.tipo = tipo;
        this.cantidad = cantidad;
        this.motivo = motivo;
        this.referenciaDocumento = referenciaDocumento;
        this.producto = producto;
        this.usuario = usuario;
    }

    // --- Getters ---

    public UUID getId() {
        return id;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public TipoMovimiento getTipo() {
        return tipo;
    }

    public double getCantidad() {
        return cantidad;
    }

    public String getMotivo() {
        return motivo;
    }

    public String getReferenciaDocumento() {
        return referenciaDocumento;
    }

    public Producto getProducto() {
        return producto;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    
    public String mostrarDetalleKardex() {
        return String.format(
                "[%s] %-13s | Cant: %8.2f | Doc: %-10s | Motivo: %-25s | Usuario: %-20s | %s",
                fecha.format(FORMATO_FECHA),
                tipo,
                cantidad,
                referenciaDocumento,
                motivo,
                usuario.getNombre(),
                producto.mostrarDetalle()
        );
    }
}
