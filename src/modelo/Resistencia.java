package modelo;


public class Resistencia extends Producto {
    private double ohmios;
    private double tolerancia; // Ej: 5.0 para 5%

   
    public Resistencia(String sku, String nombre, String especificacion, double stockMinimo, double ohmios, double tolerancia) {
        super(sku, nombre, especificacion, stockMinimo);
        this.ohmios = ohmios;
        this.tolerancia = tolerancia;
    }

    @Override
    public String mostrarDetalle() {
        return String.format("[RESISTENCIA] SKU: %s | Nombre: %s | Valor: %.2f Ω | Tolerancia: %.1f%% | Espec: %s",
                getSku(), getNombre(), ohmios, tolerancia, getEspecificacion());
    }


    public double getOhmios() { return ohmios; }
    public void setOhmios(double ohmios) { this.ohmios = ohmios; }

    public double getTolerancia() { return tolerancia; }
    public void setTolerancia(double tolerancia) { this.tolerancia = tolerancia; }
}