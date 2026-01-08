package com.emergencias.alert;

import com.emergencias.model.EmergencyEvent;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Properties;

public class AlertSender {

    private final String destino;
    private final String logFile;

    public AlertSender(Properties cfg) {
        String d = cfg.getProperty("destino", "112");
        this.destino = (d == null || d.trim().isEmpty()) ? "112" : d.trim();

        String lf = cfg.getProperty("logFile", "alertas.log");
        this.logFile = (lf == null || lf.trim().isEmpty()) ? "alertas.log" : lf.trim();
    }

    public void sendAlert(EmergencyEvent event) {
        if (event == null) {
            System.out.println("No hay emergencia que enviar.");
            return;
        }

        System.out.println("===== ENVÍO DE ALERTA =====");
        System.out.println("Destino: " + destino);
        System.out.println("Tipo: " + event.getTipoEmergencia());
        System.out.println("Ubicación: " + event.getUbicacion());
        System.out.println("Gravedad: " + event.getGravedad());
        System.out.println("Usuario: " + event.getUsuario());

        String linea = LocalDateTime.now()
                + " | destino=" + destino
                + " | tipo=" + event.getTipoEmergencia()
                + " | ubicacion=" + event.getUbicacion()
                + " | gravedad=" + event.getGravedad()
                + " | usuario=" + event.getUsuario();

        try (FileWriter fw = new FileWriter(logFile, true)) {
            fw.write(linea + System.lineSeparator());
        } catch (IOException e) {
            System.out.println("No se pudo guardar la alerta, pero la app continúa.");
        }
    }

    public void notifyContacts(EmergencyEvent event, String... contactos) {
        if (event == null || contactos == null || contactos.length == 0) {
            return;
        }

        System.out.println("===== AVISO A CONTACTOS =====");
        for (String c : contactos) {
            if (c == null || c.trim().isEmpty()) continue;
            System.out.println("Avisando a " + c + " sobre " + event.getTipoEmergencia()
                    + " en " + event.getUbicacion());
        }
    }
}
