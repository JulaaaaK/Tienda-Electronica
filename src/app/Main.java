package app;

import modelo.*;
import servicio.Inventario;

public class Main {
    public static void main(String[] args) {
        System.out.println("==========================================================");
        System.out.println("  SISTEMA DE GESTIÓN DE INVENTARIO - COMPONENTES BJT/PASIVOS");
        System.out.println("==========================================================\n");

        // 1. Se llama Inventario (Caso de uso: Administrador / Almacén)
        Inventario inventario = new Inventario();
        Usuario operador = new Usuario("US-01", "Carlos Pérez", "Responsable de Almacén");

        // 2. Se llaman a los componentes 
        Producto res10k = new Resistencia("RES-10K", "Resistencia 10K Ohm", "1/4W 5%", 100.0, 10000.0, 5.0);
        Producto cap100u = new Capacitor("CAP-100U", "Capacitor Electrolítico", "Electrolítico Radial", 50.0, 100.0, 25.0);
        Producto ci555 = new CircuitoIntegrado("CI-555", "Temporizador NE555", " DIP-8 Monolítico", 30.0, "DIP", 8);

        // 3. Se registran los productos en el catálogo
        inventario.agregarProducto(res10k);
        inventario.agregarProducto(cap100u);
        inventario.agregarProducto(ci555);

        // 4.  Se Simula la Recepción de Mercancía (Entrada a Inventario)
        System.out.println("--- 1. REGISTRANDO RECEPCIÓN DE MERCANCÍA ---");
        inventario.registrarEntrada(res10k, 500.0, operador);
        inventario.registrarEntrada(cap100u, 30.0, operador); // Queda bajo del mínimo (30 < 50)
        inventario.registrarEntrada(ci555, 200.0, operador);

        // 5. Se Simula Despacho o Venta (Salida de Inventario)
        System.out.println("\n--- 2. REGISTRANDO DESPACHO / VENTA ---");
        inventario.registrarSalida(res10k, 450.0, operador); // Queda en 50, dispara alerta (50 <= 100)

        // 6. Se Consultan Alertas de Stock Mínimo (Caso de uso del Gerente)
        System.out.println("\n--- 3. CONSULTANDO ALERTAS DE STOCK MÍNIMO ---");
        for (Producto p : inventario.obtenerAlertasStock()) {
            System.out.println("⚠️ ALERTA: El producto '" + p.getNombre() + "' alcanzó el nivel mínimo de stock.");
            System.out.println("   Detalle: " + p.mostrarDetalle());
        }

        // 7. Se Consultan Existencias y Kardex
        System.out.println("\n--- 4. HISTORIAL DE MOVIMIENTOS (KARDEX) ---");
        inventario.consultarKardex();
    }
}