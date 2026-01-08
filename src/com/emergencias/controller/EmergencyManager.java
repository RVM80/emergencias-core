package com.emergencias.controller;

import com.emergencias.alert.AlertSender;
import com.emergencias.detector.EmergencyDetector;
import com.emergencias.model.EmergencyEvent;
import com.emergencias.model.UserData;

import java.util.Properties;
import java.util.Scanner;

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
        EmergencyEvent event;
        try {
            event = detector.detectEvent(user, sc);
        } catch (Exception e) {
            System.out.println("Error durante la detección de la emergencia.");
            return;
        }

        if (event == null) {
            System.out.println("Sistema finalizado sin emergencias.");
            return;
        }

        try {
            sender.sendAlert(event);
        } catch (Exception e) {
            System.out.println("Error enviando la alerta.");
        }

        try {
            sender.notifyContacts(event, user.getTelefono());
        } catch (Exception e) {
            System.out.println("No se pudieron notificar los contactos.");
        }
    }
}
