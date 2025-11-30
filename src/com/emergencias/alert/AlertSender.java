package com.emergencias.alert;

import com.emergencias.model.EmergencyEvent;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

/**
 * CORE 2: Notificación / alerta.
 * "Envía" la emergencia al 112 mostrando por consola
 * y guardando en un archivo de texto.
 */
public class AlertSender {

    private final String destino;   // normalmente "112"
    private final String logFile;   // archivo donde guardamos las alertas

    public AlertSender(Properties cfg) {
        this.destino = cfg.getProperty("destino", "112");
        this.logFile = cfg.getProperty("logFile", "alertas.log");
    }

    /**
     * Empaqueta y "envía" la alerta:
     * - imprime en consola
     * - la persiste en un archivo
     */
    public void sendAlert(EmergencyEvent event) {
        if (event == null) {
            System.out.println("No hay emergencia que enviar.");
            return;
        }

        System.out.println("===== ENVÍO DE ALERTA =====");
        System.out.println("Destino: " + destino);
        System.out.println("Datos enviados:");
        System.out.println("Tipo: " + event.getTipoEmergencia());
        System.out.println("Ubicación: " + event.getUbicacion());
        System.out.println("Gravedad: " + event.getGravedad());
        System.out.println("Usuario: " + event.getUsuario());

        // Guardamos la alerta en un archivo de texto
        try (FileWriter fw = new FileWriter(logFile, true)) {
            fw.write(event + System.lineSeparator());

        } catch (IOException e) {
            System.out.println("Error guardando la alerta en el archivo: " + e.getMessage());
        }
    }

    /**
     * Simula aviso a contactos personales (por ejemplo familiares).
     * Aquí solo imprimimos por consola.
     */
    public void notifyContacts(EmergencyEvent event, String... contactos) {
        if (event == null || contactos == null) {
            return;
        }

        System.out.println("===== AVISO A CONTACTOS PERSONALES =====");
        for (String contacto : contactos) {
            System.out.println("Avisando a " + contacto + " sobre: " + event.getTipoEmergencia()
                    + " en " + event.getUbicacion());
        }
    }
}
