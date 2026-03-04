package com.emergencias.controller;

import com.emergencias.alert.AlertSender;
import com.emergencias.detector.EmergencyDetector;
import com.emergencias.model.EmergencyEvent;
import com.emergencias.model.UserData;

import java.util.Properties;
import java.util.Scanner;

/**
 * Controlador principal (Controller).
 * Orquesta:
 * - CORE 1: EmergencyDetector (manual / automático)
 * - CORE 2: AlertSender (envío + guardado en archivo + aviso a contactos)
 */
public class EmergencyManager {

    private final EmergencyDetector detector;
    private final AlertSender sender;
    private final UserData user;
    private final Scanner sc;

    public EmergencyManager(Properties cfg) {
        this.detector = new EmergencyDetector(cfg);
        this.sender = new AlertSender(cfg);

        this.user = new UserData(
                cfg.getProperty("usuario.nombre", "Usuario Demo"),
                cfg.getProperty("usuario.telefono", "000000000")
        );

        this.sc = new Scanner(System.in);
    }

    public void startSystem() {
        System.out.println("=== SISTEMA DE EMERGENCIAS (CORE) ===");
        System.out.println("1) Activación MANUAL (teclado)");
        System.out.println("2) Activación AUTOMÁTICA (temporizador)");
        System.out.print("Elige opción (1/2): ");
        String opcion = sc.nextLine().trim();

        EmergencyEvent event;

        try {
            if (opcion.equals("2")) {
                System.out.print("Segundos de espera para el modo automático: ");
                String raw = sc.nextLine().trim();

                int segundos = 5;
                try {
                    segundos = Integer.parseInt(raw);
                } catch (NumberFormatException ignored) {
                    System.out.println("Valor no válido. Uso 5 segundos por defecto.");
                    segundos = 5;
                }

                if (segundos < 1) {
                    System.out.println("Mínimo 1 segundo. Ajusto a 1.");
                    segundos = 1;
                }

                event = detector.detectEventAutomatic(user, segundos);

            } else {
                // por defecto manual (si mete 1 o cualquier cosa)
                event = detector.detectEvent(user, sc);
            }

        } catch (Exception e) {
            System.out.println("Error durante la detección de la emergencia: " + e.getMessage());
            return;
        }

        if (event == null) {
            System.out.println("Sistema finalizado sin emergencias.");
            return;
        }

        // CORE 2: enviar alerta (consola + archivo)
        try {
            sender.sendAlert(event);
        } catch (Exception e) {
            System.out.println("Error enviando la alerta: " + e.getMessage());
        }

        // Simulación de aviso a contactos
        try {
            sender.notifyContacts(event, user.getTelefono());
        } catch (Exception e) {
            System.out.println("No se pudieron notificar los contactos: " + e.getMessage());
        }
    }
}