package modelo;


public class Capacitor extends Producto {
    private double capacitancia; // En Faradios o Microfaradios
    private double voltajeMaximo;

    public Capacitor(String sku, String nombre, String especificacion, double stockMinimo, double capacitancia, double voltajeMaximo) {
        super(sku, nombre, especificacion, stockMinimo);
        this.capacitancia = capacitancia;
        this.voltajeMaximo = voltajeMaximo;
    }

    @Override
    public String mostrarDetalle() {
        return String.format("[CAPACITOR] SKU: %s | Nombre: %s | Cap: %.2f uF | Voltaje Máx: %.1fV | Espec: %s",
                getSku(), getNombre(), capacitancia, voltajeMaximo, getEspecificacion());
    }

    // Getters y Setters
    public double getCapacitancia() { return capacitancia; }
    public void setCapacitancia(double capacitancia) { this.capacitancia = capacitancia; }

    public double getVoltajeMaximo() { return voltajeMaximo; }
    public void setVoltajeMaximo(double voltajeMaximo) { this.voltajeMaximo = voltajeMaximo; }
}