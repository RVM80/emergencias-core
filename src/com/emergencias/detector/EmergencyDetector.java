package com.emergencias.detector;

import com.emergencias.model.EmergencyEvent;
import com.emergencias.model.UserData;

import java.util.Properties;
import java.util.Scanner;

/**
 * CORE 1: Detección / activación de la emergencia.
 * Simula un botón por consola: el usuario pulsa E.
 */
public class EmergencyDetector {

    private final int threshold;   // umbral mínimo de gravedad (para evitar falsos positivos)

    public EmergencyDetector(Properties cfg) {
        // Leemos el umbral de app.properties. Si no está, usamos 1.
        this.threshold = Integer.parseInt(cfg.getProperty("threshold", "1"));
    }

    /**
     * Detecta una posible emergencia.
     * Devuelve un EmergencyEvent o null si no se confirma.
     */
    public EmergencyEvent detectEvent(UserData user) {
        Scanner sc = new Scanner(System.in);

        System.out.println("=== DETECCIÓN DE EMERGENCIAS ===");
        System.out.print("Pulsa 'E' y Enter para activar emergencia (o solo Enter para salir): ");
        String tecla = sc.nextLine();

        if (!tecla.equalsIgnoreCase("E")) {
            System.out.println("No se activó ninguna emergencia.");
            return null;
        }

        // pequeña confirmación para evitar falsos positivos
        System.out.print("¿Confirmas la emergencia? (S/N): ");
        String confirma = sc.nextLine();
        if (!confirma.equalsIgnoreCase("S")) {
            System.out.println("Emergencia cancelada por el usuario.");
            return null;
        }

        System.out.print("Tipo de emergencia (Sanitaria, Accidente, etc.): ");
        String tipo = sc.nextLine();

        System.out.print("Gravedad (0 a 3): ");
        int gravedad;
        try {
            gravedad = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Valor de gravedad no válido. Usando 1 por defecto.");
            gravedad = 1;
        }

        // usamos un método separado para validar la gravedad (como pide la práctica)
        if (!validateSeverity(gravedad)) {
            System.out.println("Emergencia descartada: gravedad menor que el umbral (" + threshold + ").");
            return null;
        }

        System.out.print("Ubicación (texto, calle, referencia...): ");
        String ubicacion = sc.nextLine();

        // Creamos el modelo de evento
        return new EmergencyEvent(tipo, ubicacion, gravedad, user);
    }

    /**
     * Comprueba si la gravedad supera el umbral configurado.
     */
    private boolean validateSeverity(int gravedad) {
        return gravedad >= threshold;
    }
}
