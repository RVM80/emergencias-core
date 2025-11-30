package com.emergencias.controller;

import com.emergencias.alert.AlertSender;
import com.emergencias.detector.EmergencyDetector;
import com.emergencias.model.EmergencyEvent;
import com.emergencias.model.UserData;

import java.util.Properties;

/**
 * Controlador principal (Controller).
 * Se encarga de orquestar Core 1 (detector) y Core 2 (alertas).
 */
public class EmergencyManager {

    private final EmergencyDetector detector;
    private final AlertSender sender;
    private final UserData user;

    public EmergencyManager(Properties cfg) {
        //inicializamos
        this.detector = new EmergencyDetector(cfg);
        this.sender = new AlertSender(cfg);

        //datos básicos del usuario que vienen de app.properties
        this.user = new UserData(
                cfg.getProperty("usuario.nombre", "Usuario Demo"),
                cfg.getProperty("usuario.telefono", "000000000")
        );
    }

    /**
     * Método que arranca el sistema
     * 1. Detecta la emergencia.
     * 2. Si hay evento, envía alerta y avisa contactos.
     */
    public void startSystem() {
        EmergencyEvent event = detector.detectEvent(user);
        sender.sendAlert(event);

        // ejemplo simple de notificación a contactos:
        if (event != null) {
            sender.notifyContacts(event, user.getTelefono());
        }
    }
}
