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

    private final int threshold;

    public EmergencyDetector(Properties cfg) {
        int t = 1;
        String raw = cfg.getProperty("threshold", "1");
        try {
            t = Integer.parseInt(raw);
        } catch (NumberFormatException ignored) {
            t = 1;
        }
        if (t < 0) t = 0;
        if (t > 3) t = 3;
        this.threshold = t;
    }

    public EmergencyEvent detectEvent(UserData user, Scanner sc) {
        System.out.println("=== DETECCIÓN DE EMERGENCIAS ===");
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

    private boolean validateSeverity(int gravedad) {
        return gravedad >= threshold;
    }

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
