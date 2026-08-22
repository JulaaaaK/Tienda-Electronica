package modelo;


public class CircuitoIntegrado extends Producto {
    private String encapsulado;
    private int numeroPines;

    public CircuitoIntegrado(String sku, String nombre, String especificacion, double stockMinimo, String encapsulado, int numeroPines) {
        super(sku, nombre, especificacion, stockMinimo);
        this.encapsulado = encapsulado;
        this.numeroPines = numeroPines;
    }

    @Override
    public String mostrarDetalle() {
        return String.format("[CIRCUITO INTEGRADO] SKU: %s | Nombre: %s | Encapsulado: %s | Pines: %d | Espec: %s",
                getSku(), getNombre(), encapsulado, numeroPines, getEspecificacion());
    }

    public String getEncapsulado() { return encapsulado; }
    public void setEncapsulado(String encapsulado) { this.encapsulado = encapsulado; }

    public int getNumeroPines() { return numeroPines; }
    public void setNumeroPines(int numeroPines) { this.numeroPines = numeroPines; }
}