package app;

import modelo.*;
import servicio.Inventario;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Inventario inventario = new Inventario();

                //Captura de datos del Usuario
        System.out.println("===========================================");
        System.out.println("   INICIO DE SESIÓN - SISTEMA DE ALMACÉN   ");
        System.out.println("===========================================");
        System.out.print("Ingrese ID del usuario (ej. US-01): ");
        String idUsuario = scanner.nextLine();
        System.out.print("Ingrese su nombre: ");
        String nombreUsuario = scanner.nextLine();
        System.out.print("Ingrese su Rol (ej. RESPONSABLE_ALMACEN): ");
        String rolUsuario = scanner.nextLine();

// Se reemplaza la línea fija por este objeto dinámico:
Usuario usuarioActual = new Usuario(idUsuario, nombreUsuario, rolUsuario);
System.out.println("\n¡Bienvenido/a, " + usuarioActual.getNombre() + " (" + usuarioActual.getRol() + ")!");
// --------------------------------------------------
        //Inicio de programa, se muestra el menú principal y se solicita al usuario que seleccione una opción.

        int opcion = 0;

        do {
            System.out.println("\n===========================================");
            System.out.println("  SISTEMA DE GESTIÓN DE INVENTARIO (XP)   ");
            System.out.println("===========================================");
            System.out.println("1. Agregar Componente al Catálogo");
            System.out.println("2. Registrar Entrada de Stock");
            System.out.println("3. Registrar Salida de Stock");
            System.out.println("4. Consultar Existencias y Stock Actual");
            System.out.println("5. Consultar Alertas de Stock Mínimo");
            System.out.println("6. Consultar Kardex (Historial de Movimientos)");
            System.out.println("7. Salir");
            System.out.print("Seleccione una opción: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine());
                System.out.println();

                switch (opcion) {
                    case 1:
                        agregarProductoMenu(scanner, inventario);
                        break;
                    case 2:
                        registrarEntradaMenu(scanner, inventario, usuarioActual);
                        break;
                    case 3:
                        registrarSalidaMenu(scanner, inventario, usuarioActual);
                        break;
                    case 4:
                        consultarExistenciasMenu(inventario);
                        break;
                    case 5:
                        consultarAlertasMenu(inventario);
                        break;
                    case 6:
                        System.out.println("--- HISTORIAL DE MOVIMIENTOS (KARDEX) ---");
                        inventario.consultarKardex();
                        break;
                    case 7:
                        System.out.println("Saliendo del sistema... ¡Hasta luego!");
                        break;
                    default:
                        System.out.println("Opción no válida. Intente nuevamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Debe ingresar un número entero válido.");
            } catch (Exception e) {
                System.out.println("Error en la operación: " + e.getMessage());
            }

        } while (opcion != 7);

        scanner.close();
    }
    //Componentes de la "tienda"
    private static void agregarProductoMenu(Scanner scanner, Inventario inventario) {
    System.out.println("--- AGREGAR NUEVO COMPONENTE AL CATÁLOGO ---");
    System.out.println("1. Resistencia");
    System.out.println("2. Capacitor");
    System.out.println("3. Circuito Integrado");
    System.out.print("Seleccione el tipo de componente: ");
    
    int tipo = 0;
    try {
        tipo = Integer.parseInt(scanner.nextLine());
    } catch (NumberFormatException e) {
        System.out.println("Opción inválida.");
        return;
    }

    // Datos comunes a todos los productos (Clase Base Producto)
    System.out.print("SKU (Código único): ");
    String sku = scanner.nextLine();
    System.out.print("Nombre del componente: ");
    String nombre = scanner.nextLine();
    System.out.print("Especificación técnica: ");
    String especificacion = scanner.nextLine();
    System.out.print("Stock Mínimo para alertas: ");
    double stockMinimo = Double.parseDouble(scanner.nextLine());

    Producto p = null;

    switch (tipo) {
        case 1:
            // Datos para Resistencia
            System.out.print("Valor en Ohmios (Ω): ");
            double ohmios = Double.parseDouble(scanner.nextLine());
            System.out.print("Porcentaje de Tolerancia (ej. 5.0): ");
            double tolerancia = Double.parseDouble(scanner.nextLine());
            
            p = new Resistencia(sku, nombre, especificacion, stockMinimo, ohmios, tolerancia);
            break;

        case 2:
            // Datos para Capacitor
            System.out.print("Capacidad en Faradios/Microfaradios (ej. 100.0): ");
            double capacidad = Double.parseDouble(scanner.nextLine());
            System.out.print("Voltaje Máximo (ej. 16.0): ");
            double voltaje = Double.parseDouble(scanner.nextLine());

            p = new Capacitor(sku, nombre, especificacion, stockMinimo, capacidad, voltaje);
            break;

        case 3:
            // Datos para Circuito Integrado
            System.out.print("Número de Pines (ej. 8, 14, 28): ");
            int pines = Integer.parseInt(scanner.nextLine());
            System.out.print("Encapsulado (ej. DIP, SOIC): ");
            String encapsulado = scanner.nextLine();

            p = new CircuitoIntegrado(sku, nombre, especificacion, stockMinimo, pines, encapsulado);
            break;

        default:
            System.out.println("Tipo de componente desconocido.");
            return;
    }

    inventario.agregarProducto(p);
    System.out.println("¡" + nombre + " registrado exitosamente en el catálogo!");
}

    //Entrada de stock
    private static void registrarEntradaMenu(Scanner scanner, Inventario inventario, Usuario usuario) {
        System.out.println("--- REGISTRAR ENTRADA DE STOCK ---");
        Producto p = seleccionarProducto(scanner, inventario);
        if (p == null) return;

        System.out.print("Cantidad a ingresar: ");
        double cantidad = Double.parseDouble(scanner.nextLine());
        System.out.print("Motivo (ej. Compra a Proveedor): ");
        String motivo = scanner.nextLine();
        System.out.print("Número de documento / Factura: ");
        String doc = scanner.nextLine();

        inventario.registrarEntrada(p, cantidad, usuario, motivo, doc);
        System.out.println("¡Entrada registrada correctamente en el inventario!");
    }

    //Salida de stock
    private static void registrarSalidaMenu(Scanner scanner, Inventario inventario, Usuario usuario) {
        System.out.println("--- REGISTRAR SALIDA DE STOCK ---");
        Producto p = seleccionarProducto(scanner, inventario);
        if (p == null) return;

        System.out.print("Cantidad a retirar: ");
        double cantidad = Double.parseDouble(scanner.nextLine());
        System.out.print("Motivo (ej. Venta en mostrador): ");
        String motivo = scanner.nextLine();
        System.out.print("Número de Orden de Venta: ");
        String doc = scanner.nextLine();

        inventario.registrarSalida(p, cantidad, usuario, motivo, doc);
        System.out.println("¡Salida registrada correctamente!");
    }

    // Consultas de stock y alertas
    private static void consultarExistenciasMenu(Inventario inventario) {
        System.out.println("--- CONSULTA DE EXISTENCIAS EN ALMACÉN ---");
        List<Producto> productos = inventario.getProductos();
        if (productos.isEmpty()) {
            System.out.println("El catálogo está vacío. Agregue productos primero.");
            return;
        }

        for (Producto p : productos) {
            double stock = inventario.obtenerStockActual(p);
            System.out.println("SKU: " + p.getSku() + " | Nombre: " + p.getNombre() + 
                               " | Stock Actual: " + stock + " | Stock Mínimo: " + p.getStockMinimo());
        }
        System.out.println("-------------------------------------------");
        System.out.println("Total Stock Disponible en Almacén: " + inventario.getCantidadDisponible());
    }

    private static void consultarAlertasMenu(Inventario inventario) {
        System.out.println("--- ALERTAS DE STOCK MÍNIMO ---");
        List<Producto> alertas = inventario.obtenerAlertasStock();
        if (alertas.isEmpty()) {
            System.out.println("¡Excelente! No hay productos por debajo del stock mínimo.");
            return;
        }

        System.out.println("¡ATENCIÓN! Los siguientes productos requieren reabastecimiento:");
        for (Producto p : alertas) {
            double stock = inventario.obtenerStockActual(p);
            System.out.println("[ALERTA] SKU: " + p.getSku() + " - " + p.getNombre() + 
                               " (Stock Actual: " + stock + " <= Mínimo: " + p.getStockMinimo() + ")");
        }
    }

    private static Producto seleccionarProducto(Scanner scanner, Inventario inventario) {
        List<Producto> productos = inventario.getProductos();
        if (productos.isEmpty()) {
            System.out.println("No hay productos en el catálogo. Agregue uno primero.");
            return null;
        }

        System.out.println("Seleccione un producto:");
        for (int i = 0; i < productos.size(); i++) {
            System.out.println((i + 1) + ". " + productos.get(i).getNombre() + " (SKU: " + productos.get(i).getSku() + ")");
        }
        System.out.print("Opción: ");
        int index = Integer.parseInt(scanner.nextLine()) - 1;

        if (index >= 0 && index < productos.size()) {
            return productos.get(index);
        } else {
            System.out.println("Selección inválida.");
            return null;
        }
    }
}