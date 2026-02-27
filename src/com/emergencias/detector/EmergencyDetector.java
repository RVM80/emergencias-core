package com.emergencias.detector;

import com.emergencias.model.EmergencyEvent;
import com.emergencias.model.UserData;

import java.util.Properties;
import java.util.Scanner;

public class EmergencyDetector {

    private final int threshold;

    public EmergencyDetector(Properties cfg) {
        //Fix; t = 1 es redundante porque ya le damos un defaultValue en el try.
        int t;
        try {
            t = Integer.parseInt(cfg.getProperty("threshold", "1"));
        } catch (NumberFormatException ignored) {
            t = 1;
        }
        if (t < 0) t = 0;
        if (t > 3) t = 3;
        this.threshold = t;
    }

    public EmergencyEvent detectEvent(UserData user, Scanner sc) {

        System.out.println("=== DETECCIÓN DE EMERGENCIAS ===");
        System.out.print("Pulsa 'E' y Enter para activar emergencia (o Enter para salir): ");
        String tecla = sc.nextLine().trim();

        if (!tecla.equalsIgnoreCase("E")) {
            System.out.println("No se activó ninguna emergencia.");
            return null;
        }

        System.out.print("¿Confirmas la emergencia? (S/N): ");
        String confirma = sc.nextLine().trim();
        if (!confirma.equalsIgnoreCase("S")) {
            System.out.println("Emergencia cancelada.");
            return null;
        }

        System.out.print("Tipo de emergencia: ");
        String tipo = sc.nextLine().trim();
        if (tipo.isEmpty()) tipo = "Desconocida";

        int gravedad = leerGravedad(sc);

        if (gravedad < threshold) {
            System.out.println("Emergencia descartada por baja gravedad.");
            return null;
        }

        System.out.print("Ubicación: ");
        String ubicacion = sc.nextLine().trim();
        if (ubicacion.isEmpty()) ubicacion = "No indicada";

        return new EmergencyEvent(tipo, ubicacion, gravedad, user);
    }

    private int leerGravedad(Scanner sc) {
        while (true) {
            System.out.print("Gravedad (0 a 3): ");
            try {
                int g = Integer.parseInt(sc.nextLine());
                if (g < 0 || g > 3) {
                    System.out.println("Debe estar entre 0 y 3.");
                    continue;
                }
                return g;
            } catch (NumberFormatException e) {
                System.out.println("Introduce un número válido.");
            }
        }
    }
}

