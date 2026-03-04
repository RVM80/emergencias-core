package com.emergencias.detector;

import com.emergencias.model.EmergencyEvent;
import com.emergencias.model.UserData;

import java.util.Properties;
import java.util.Scanner;

/**
 * CORE 1: Detección / activación de la emergencia.
 * - Manual: el usuario pulsa E y confirma.
 * - Automática: temporizador que dispara una emergencia simulada.
 */
public class EmergencyDetector {

    private final int threshold; // umbral mínimo (0..3)

    public EmergencyDetector(Properties cfg) {
        int t = 1;
        String raw = cfg.getProperty("threshold", "1");
        try {
            t = Integer.parseInt(raw);
        } catch (NumberFormatException ignored) {
            t = 1;
        }

        // Aseguramos rango 0..3
        if (t < 0) t = 0;
        if (t > 3) t = 3;

        this.threshold = t;
    }

    /**
     * Disparador MANUAL (por consola).
     * Devuelve un EmergencyEvent si se confirma y supera el umbral.
     */
    public EmergencyEvent detectEvent(UserData user, Scanner sc) {
        System.out.println("=== DETECCIÓN DE EMERGENCIAS (MANUAL) ===");
        System.out.print("Pulsa 'E' y Enter para activar emergencia (o solo Enter para salir): ");
        String tecla = sc.nextLine().trim();

        if (!tecla.equalsIgnoreCase("E")) {
            System.out.println("No se activó ninguna emergencia.");
            return null;
        }

        System.out.print("¿Confirmas la emergencia? (S/N): ");
        String confirma = sc.nextLine().trim();
        if (!confirma.equalsIgnoreCase("S")) {
            System.out.println("Emergencia cancelada por el usuario.");
            return null;
        }

        String tipo;
        while (true) {
            System.out.print("Tipo de emergencia (Sanitaria, Accidente, etc.): ");
            tipo = sc.nextLine().trim();
            if (!tipo.isEmpty()) break;
            System.out.println("El tipo no puede estar vacío.");
        }

        int gravedad = leerEnteroEnRango(sc, "Gravedad (0 a 3): ", 0, 3);

        if (!validateSeverity(gravedad)) {
            System.out.println("Emergencia descartada: gravedad menor que el umbral (" + threshold + ").");
            return null;
        }

        System.out.print("Municipio de la emergencia: ");
        String municipio = sc.nextLine().trim();

        System.out.print("Calle / referencia: ");
        String calle = sc.nextLine().trim();

        String ubicacion;
        if (municipio.isEmpty() && calle.isEmpty()) {
            ubicacion = "Ubicación no indicada";
        } else if (municipio.isEmpty()) {
            ubicacion = calle;
        } else if (calle.isEmpty()) {
            ubicacion = municipio;
        } else {
            ubicacion = municipio + ", " + calle;
        }

        return new EmergencyEvent(tipo, ubicacion, gravedad, user);
    }

    /**
     * Disparador AUTOMÁTICO (por temporizador).
     * Espera X segundos y crea una emergencia simulada.
     */
    public EmergencyEvent detectEventAutomatic(UserData user, int segundos) {
        System.out.println("=== DETECCIÓN DE EMERGENCIAS (AUTOMÁTICA) ===");
        System.out.println("Modo automático: esperando " + segundos + " segundos...");

        try {
            Thread.sleep(segundos * 1000L);
        } catch (InterruptedException e) {
            System.out.println("Temporizador interrumpido.");
            return null;
        }

        // Datos simulados
        String tipo = "Sanitaria";
        int gravedad = Math.max(threshold, 1); // asegura pasar el umbral
        String ubicacion = "Ubicación simulada (sin GPS real)";

        System.out.println("Activación automática detectada.");

        return new EmergencyEvent(tipo, ubicacion, gravedad, user);
    }

    // Valida la gravedad comparándola con el umbral
    private boolean validateSeverity(int gravedad) {
        return gravedad >= threshold;
    }

    // Lee un entero en un rango (evita errores de NumberFormatException)
    private int leerEnteroEnRango(Scanner sc, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine().trim();
            try {
                int v = Integer.parseInt(s);
                if (v < min || v > max) {
                    System.out.println("Introduce un número entre " + min + " y " + max + ".");
                    continue;
                }
                return v;
            } catch (NumberFormatException e) {
                System.out.println("Valor no válido. Escribe un número.");
            }
        }
    }
}