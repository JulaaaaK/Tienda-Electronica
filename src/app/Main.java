package app;

import modelo.*;
import servicio.Inventario;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    // Color ANSI para consola
    public static final String RESET = "\u001B[0m";
    public static final String ROJO = "\u001B[31m";
    public static final String VERDE = "\u001B[32m";
    public static final String AMARILLO = "\u001B[33m";
    public static final String AZUL = "\u001B[34m";
    public static final String PURPURA = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String BLANCO_NEGRITA = "\u001B[1;37m";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Inventario inventario = new Inventario();

        //Stock Predeterminado
        cargarDatosPrueba(inventario);

        // Usuarios
        List<Usuario> usuariosSistema = obtenerUsuariosPredefinidos();

        // Seleccionar usuario inicial
        Usuario usuarioActual = seleccionarUsuario(scanner, usuariosSistema);

        int opcion = 0;

        do {
            mostrarEncabezadoMenu(usuarioActual);
            System.out.println(CYAN + "1." + RESET + " Agregar Componente al Catálogo " + obtenerEtiquetaPermiso(usuarioActual, "ADMINISTRADOR"));
            System.out.println(CYAN + "2." + RESET + " Registrar Entrada de Stock (Recepción) " + obtenerEtiquetaPermiso(usuarioActual, "RESPONSABLE_COMPRAS", "RESPONSABLE_ALMACEN", "ADMINISTRADOR"));
            System.out.println(CYAN + "3." + RESET + " Registrar Salida de Stock (Despacho / Venta) " + obtenerEtiquetaPermiso(usuarioActual, "VENDEDOR", "RESPONSABLE_ALMACEN", "ADMINISTRADOR"));
            System.out.println(CYAN + "4." + RESET + " Consultar Existencias en Almacén " + obtenerEtiquetaPermiso(usuarioActual, "TODOS"));
            System.out.println(CYAN + "5." + RESET + " Consultar Alertas de Stock Mínimo " + obtenerEtiquetaPermiso(usuarioActual, "GERENTE", "RESPONSABLE_ALMACEN", "ADMINISTRADOR"));
            System.out.println(CYAN + "6." + RESET + " Consultar Kardex (Historial de Movimientos) " + obtenerEtiquetaPermiso(usuarioActual, "GERENTE", "RESPONSABLE_ALMACEN", "ADMINISTRADOR"));
            System.out.println(CYAN + "7." + RESET + " Cambiar de Usuario / Cerrar Sesión");
            System.out.println(CYAN + "8." + RESET + " Salir del Sistema");
            System.out.print(BLANCO_NEGRITA + "Seleccione una opción: " + RESET);

            try {
                opcion = Integer.parseInt(scanner.nextLine());
                System.out.println();

                switch (opcion) {    //Permisos
                    case 1:
                        if (validarPermiso(usuarioActual, "ADMINISTRADOR")) {
                            agregarProductoMenu(scanner, inventario);
                        }
                        break;
                    case 2:
                        if (validarPermiso(usuarioActual, "RESPONSABLE_COMPRAS", "RESPONSABLE_ALMACEN", "ADMINISTRADOR")) {
                            registrarEntradaMenu(scanner, inventario, usuarioActual);
                        }
                        break;
                    case 3:
                        if (validarPermiso(usuarioActual, "VENDEDOR", "RESPONSABLE_ALMACEN", "ADMINISTRADOR")) {
                            registrarSalidaMenu(scanner, inventario, usuarioActual);
                        }
                        break;
                    case 4:
                        consultarExistenciasMenu(inventario);
                        break;
                    case 5:
                        if (validarPermiso(usuarioActual, "GERENTE", "RESPONSABLE_ALMACEN", "ADMINISTRADOR")) {
                            consultarAlertasMenu(inventario);
                        }
                        break;
                    case 6:
                        if (validarPermiso(usuarioActual, "GERENTE", "RESPONSABLE_ALMACEN", "ADMINISTRADOR")) {
                            imprimirTitulo("HISTORIAL DE MOVIMIENTOS (KARDEX DE AUDITORÍA)");
                            inventario.consultarKardex();
                        }
                        break;
                    case 7:
                        usuarioActual = seleccionarUsuario(scanner, usuariosSistema);
                        break;
                    case 8:
                        System.out.println(VERDE + "Saliendo del sistema... ¡Gracias por usar la plataforma de almacén!" + RESET);
                        break;
                    default:
                        imprimirError("Opción no válida. Intente nuevamente.");
                }
            } catch (NumberFormatException e) {
                imprimirError("Debe ingresar un número entero válido.");
            } catch (Exception e) {
                imprimirError("Ocurrió un error en la operación: " + e.getMessage());
            }

        } while (opcion != 8);

        scanner.close();
    }

    // Memoria Inicial
    private static void cargarDatosPrueba(Inventario inventario) {
        Usuario adminSystem = new Usuario("US-00", "Sistema Base", "ADMINISTRADOR");

        // Componentes iniciales
        Producto res1 = new Resistencia("RES-10K", "Resistencia 10K 1/4W", "Película de Carbón 5%", 10.0, 10000.0, 5.0);
        Producto cap1 = new Capacitor("CAP-100U", "Capacitor Electrolítico 100uF", "Aluminio Radial", 5.0, 100.0, 25.0);
        Producto ic1 = new CircuitoIntegrado("IC-NE555", "Circuito Integrado NE555", "Temporizador de Precisión", 15.0, 8, "DIP-8");

        inventario.agregarProducto(res1);
        inventario.agregarProducto(cap1);
        inventario.agregarProducto(ic1);

        // Cargar stock inicial 
        inventario.registrarEntrada(res1, 50.0, adminSystem, "Stock Inicial de Apertura", "FAC-INI-01");
        inventario.registrarEntrada(cap1, 3.0, adminSystem, "Stock Inicial de Apertura", "FAC-INI-01"); // Generará alerta stock mínimo
        inventario.registrarEntrada(ic1, 25.0, adminSystem, "Stock Inicial de Apertura", "FAC-INI-01");
    }

    // -Usuarios
    private static List<Usuario> obtenerUsuariosPredefinidos() {
        List<Usuario> usuarios = new ArrayList<>();
        usuarios.add(new Usuario("US-01", "Carlos Gómez", "ADMINISTRADOR"));
        usuarios.add(new Usuario("US-02", "Ana Martínez", "VENDEDOR"));
        usuarios.add(new Usuario("US-03", "Luis Hernández", "RESPONSABLE_COMPRAS"));
        usuarios.add(new Usuario("US-04", "Elena Torres", "RESPONSABLE_ALMACEN"));
        usuarios.add(new Usuario("US-05", "Roberto Silva", "GERENTE"));
        return usuarios;
    }

    private static Usuario seleccionarUsuario(Scanner scanner, List<Usuario> usuarios) {
        imprimirTitulo("INICIO DE SESIÓN - SELECCIÓN DE ROL DE USUARIO");
        for (int i = 0; i < usuarios.size(); i++) {
            Usuario u = usuarios.get(i);
            System.out.println(CYAN + (i + 1) + ". " + RESET + u.getNombre() + " (" + PURPURA + "Rol: " + u.getRol() + RESET + ")");
        }
        System.out.print(BLANCO_NEGRITA + "Seleccione el usuario con el que ingresará: " + RESET);
        
        int idx = -1;
        try {
            idx = Integer.parseInt(scanner.nextLine()) - 1;
        } catch (Exception e) {
            idx = -1;
        }

        if (idx >= 0 && idx < usuarios.size()) {
            Usuario seleccionado = usuarios.get(idx);
            System.out.println(VERDE + "\n¡Sesión iniciada correctamente como: " + seleccionado.getNombre() + " [" + seleccionado.getRol() + "]!" + RESET);
            return seleccionado;
        } else {
            imprimirError("Selección inválida. Asignando usuario Administrador por defecto.");
            return usuarios.get(0);
        }
    }

    // Manejo de permisos
    private static boolean validarPermiso(Usuario usuario, String... rolesPermitidos) {
        for (String rol : rolesPermitidos) {
            if (usuario.getRol().equalsIgnoreCase(rol)) {
                return true;
            }
        }
        imprimirError("ACCESO DENEGADO: El usuario con rol [" + usuario.getRol() + "] no tiene permisos para esta acción.");
        return false;
    }

    private static String obtenerEtiquetaPermiso(Usuario usuario, String... rolesPermitidos) {
        if (rolesPermitidos[0].equals("TODOS")) return "";
        for (String rol : rolesPermitidos) {
            if (usuario.getRol().equalsIgnoreCase(rol)) {
                return VERDE + "[Permitido]" + RESET;
            }
        }
        return ROJO + "[Bloqueado]" + RESET;
    }

    // Menus de Operaciones
    private static void agregarProductoMenu(Scanner scanner, Inventario inventario) {
        imprimirTitulo("AGREGAR NUEVO COMPONENTE AL CATÁLOGO (SOLO ADMIN)");
        System.out.println("1. Resistencia | 2. Capacitor | 3. Circuito Integrado");
        System.out.print("Seleccione el tipo de componente: ");
        
        int tipo = 0;
        try {
            tipo = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            imprimirError("Opción de tipo inválida.");
            return;
        }

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
                System.out.print("Valor en Ohmios (Ω): ");
                double ohmios = Double.parseDouble(scanner.nextLine());
                System.out.print("Porcentaje de Tolerancia (ej. 5.0): ");
                double tolerancia = Double.parseDouble(scanner.nextLine());
                p = new Resistencia(sku, nombre, especificacion, stockMinimo, ohmios, tolerancia);
                break;

            case 2:
                System.out.print("Capacidad en Faradios/Microfaradios: ");
                double capacidad = Double.parseDouble(scanner.nextLine());
                System.out.print("Voltaje Máximo (V): ");
                double voltaje = Double.parseDouble(scanner.nextLine());
                p = new Capacitor(sku, nombre, especificacion, stockMinimo, capacidad, voltaje);
                break;

            case 3:
                System.out.print("Número de Pines: ");
                int pines = Integer.parseInt(scanner.nextLine());
                System.out.print("Encapsulado (ej. DIP, SOIC): ");
                String encapsulado = scanner.nextLine();
                p = new CircuitoIntegrado(sku, nombre, especificacion, stockMinimo, pines, encapsulado);
                break;

            default:
                imprimirError("Tipo de componente desconocido.");
                return;
        }

        inventario.agregarProducto(p);
        System.out.println(VERDE + "¡" + nombre + " registrado exitosamente en el catálogo!" + RESET);
    }

    private static void registrarEntradaMenu(Scanner scanner, Inventario inventario, Usuario usuario) {
        imprimirTitulo("REGISTRAR RECEPCIÓN / ENTRADA DE STOCK");
        Producto p = seleccionarProducto(scanner, inventario);
        if (p == null) return;

        System.out.print("Cantidad a ingresar: ");
        double cantidad = Double.parseDouble(scanner.nextLine());
        System.out.print("Motivo / Proveedor: ");
        String motivo = scanner.nextLine();
        System.out.print("Número de Factura / Orden de Compra: ");
        String doc = scanner.nextLine();

        inventario.registrarEntrada(p, cantidad, usuario, motivo, doc);
        System.out.println(VERDE + "¡Entrada de " + cantidad + " unidades asignada exitosamente!" + RESET);
    }

    private static void registrarSalidaMenu(Scanner scanner, Inventario inventario, Usuario usuario) {
        imprimirTitulo("REGISTRAR DESPACHO / VENTA DE STOCK");
        Producto p = seleccionarProducto(scanner, inventario);
        if (p == null) return;

        System.out.print("Cantidad a retirar: ");
        double cantidad = Double.parseDouble(scanner.nextLine());
        System.out.print("Motivo / Cliente: ");
        String motivo = scanner.nextLine();
        System.out.print("Número de Pedido de Venta: ");
        String doc = scanner.nextLine();

        inventario.registrarSalida(p, cantidad, usuario, motivo, doc);
        System.out.println(VERDE + "¡Salida de " + cantidad + " unidades registrada correctamente!" + RESET);
    }

    private static void consultarExistenciasMenu(Inventario inventario) {
        imprimirTitulo("CONSULTA DE EXISTENCIAS EN ALMACÉN");
        List<Producto> productos = inventario.getProductos();
        if (productos.isEmpty()) {
            System.out.println(AMARILLO + "El catálogo está vacío." + RESET);
            return;
        }

        for (Producto p : productos) {
            double stock = inventario.obtenerStockActual(p);
            String estadoStock = p.esBajoStock(stock) ? ROJO + " [STOCK BAJO!]" + RESET : VERDE + " [OK]" + RESET;
            System.out.println(p.mostrarDetalle() + " | " + BLANCO_NEGRITA + "Stock Actual: " + stock + RESET + " | Min: " + p.getStockMinimo() + estadoStock);
        }
        System.out.println(AZUL + "----------------------------------------------------------------------------------" + RESET);
        System.out.println(BLANCO_NEGRITA + "Total Unidades Disponibles en Almacén: " + CYAN + inventario.getCantidadDisponible() + RESET);
    }

    private static void consultarAlertasMenu(Inventario inventario) {
        imprimirTitulo("ALERTAS DE STOCK MÍNIMO (CONTROL GERENCIAL)");
        List<Producto> alertas = inventario.obtenerAlertasStock();
        if (alertas.isEmpty()) {
            System.out.println(VERDE + "¡Excelente! No hay productos por debajo del stock mínimo exigido." + RESET);
            return;
        }

        System.out.println(AMARILLO + "¡ATENCIÓN! Los siguientes productos requieren reabastecimiento urgente:" + RESET);
        for (Producto p : alertas) {
            double stock = inventario.obtenerStockActual(p);
            System.out.println(ROJO + "[ALERTA]" + RESET + " SKU: " + p.getSku() + " - " + p.getNombre() + 
                               " (" + BLANCO_NEGRITA + "Stock Actual: " + stock + RESET + " <= Mínimo Exigido: " + p.getStockMinimo() + ")");
        }
    }

    private static Producto seleccionarProducto(Scanner scanner, Inventario inventario) {
        List<Producto> productos = inventario.getProductos();
        if (productos.isEmpty()) {
            imprimirError("No hay productos registrados en el catálogo.");
            return null;
        }

        System.out.println("Seleccione un componente:");
        for (int i = 0; i < productos.size(); i++) {
            System.out.println(CYAN + (i + 1) + ". " + RESET + productos.get(i).getNombre() + " (SKU: " + productos.get(i).getSku() + ")");
        }
        System.out.print(BLANCO_NEGRITA + "Opción: " + RESET);
        int index = Integer.parseInt(scanner.nextLine()) - 1;

        if (index >= 0 && index < productos.size()) {
            return productos.get(index);
        } else {
            imprimirError("Selección inválida.");
            return null;
        }
    }

    // Consola
    private static void mostrarEncabezadoMenu(Usuario u) {
        System.out.println();
        System.out.println(AZUL + "==========================================================================" + RESET);
        System.out.println(BLANCO_NEGRITA + "   SISTEMA DE GESTIÓN DE INVENTARIO - COMPONENTES ELECTRÓNICOS (XP)   " + RESET);
        System.out.println(AZUL + "==========================================================================" + RESET);
        System.out.println("Usuario Activo: " + VERDE + u.getNombre() + RESET + " | Rol: " + PURPURA + u.getRol() + RESET);
        System.out.println(AZUL + "--------------------------------------------------------------------------" + RESET);
    }

    private static void imprimirTitulo(String titulo) {
        System.out.println(PURPURA + "\n>>> " + titulo + " <<<" + RESET);
    }

    private static void imprimirError(String mensaje) {
        System.out.println(ROJO + "ERROR: " + mensaje + RESET);
    }
}
