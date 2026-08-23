package modelo;

import java.util.UUID;


public abstract class Producto {
    
    private UUID id;
    private String sku;
    private String nombre;
    private String especificacion;
    private double stockMinimo;
    private boolean activo;

    
    public Producto() {
        this.id = UUID.randomUUID();
        this.activo = true;
    }

   
    public Producto(String sku, String nombre, String especificacion, double stockMinimo) {
        this.id = UUID.randomUUID();
        this.sku = sku;
        this.nombre = nombre;
        this.especificacion = especificacion;
        this.stockMinimo = stockMinimo;
        this.activo = true;
    }

   
    public boolean esBajoStock(double stockActual) {
        return stockActual <= this.stockMinimo;
    }

    
    public abstract String mostrarDetalle();

    
    public UUID getId() {
        return id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEspecificacion() {
        return especificacion;
    }

    public void setEspecificacion(String especificacion) {
        this.especificacion = especificacion;
    }

    public double getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(double stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}