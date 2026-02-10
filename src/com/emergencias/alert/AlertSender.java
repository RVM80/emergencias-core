package com.emergencias.alert;

import com.emergencias.loader.CentroSaludLoader;
import com.emergencias.model.CentroSalud;
import com.emergencias.model.EmergencyEvent;

import java.io.FileWriter;
import java.io.IOException;
import java.text.Normalizer;
import java.util.List;
import java.util.Properties;

/**
 * CORE 2: Notificación / alerta.
 * "Envía" la emergencia al 112 mostrando por consola
 * y guardando en un archivo de texto.
 */
public class AlertSender {

    private final String destino;
    private final String logFile;
    private final String centrosJson;

    private List<CentroSalud> centrosCache; // carga 1 vez

    public AlertSender(Properties cfg) {
        this.destino = cfg.getProperty("destino", "112");
        this.logFile = cfg.getProperty("logFile", "alertas.log");
        this.centrosJson = cfg.getProperty("centrosSaludJson", "centros_salud.json");
    }

    public void sendAlert(EmergencyEvent event) {
        if (event == null) {
            System.out.println("No hay emergencia que enviar.");
            return;
        }

        // Mostrar centro sanitario según municipio (sin volver a preguntar)
        CentroSalud centro = findCentroPorMunicipioDelEvento(event);
        if (centro != null) {
            printCentro(centro);
        } else {
            System.out.println("===== CENTRO SANITARIO =====");
            System.out.println("No se encontró un centro para ese municipio.");
        }

        System.out.println("===== ENVÍO DE ALERTA =====");
        System.out.println("Destino: " + destino);
        System.out.println("Datos enviados:");
        System.out.println("Tipo: " + event.getTipoEmergencia());
        System.out.println("Ubicación: " + event.getUbicacion());
        System.out.println("Gravedad: " + event.getGravedad());
        System.out.println("Usuario: " + event.getUsuario());

        try (FileWriter fw = new FileWriter(logFile, true)) {
            fw.write(event + System.lineSeparator());
        } catch (IOException e) {
            System.out.println("Error guardando la alerta en el archivo: " + e.getMessage());
        }
    }

    public void notifyContacts(EmergencyEvent event, String... contactos) {
        if (event == null || contactos == null) return;

        System.out.println("===== AVISO A CONTACTOS PERSONALES =====");
        for (String contacto : contactos) {
            if (contacto == null || contacto.trim().isEmpty()) continue;
            System.out.println("Avisando a " + contacto + " sobre: " + event.getTipoEmergencia()
                    + " en " + event.getUbicacion());
        }
    }

    private CentroSalud findCentroPorMunicipioDelEvento(EmergencyEvent event) {
        ensureCentrosLoaded();
        if (centrosCache == null || centrosCache.isEmpty()) return null;

        String ubicacion = event.getUbicacion();
        if (ubicacion == null || ubicacion.trim().isEmpty()) return null;

        String municipio = ubicacion.split(",", 2)[0].trim();
        if (municipio.isEmpty()) return null;

        String target = norm(municipio);

        for (CentroSalud c : centrosCache) {
            if (c == null) continue;
            String m = c.getMunicipio();
            if (m != null && norm(m).equals(target)) {
                return c;
            }
        }
        return null;
    }

    private void ensureCentrosLoaded() {
        if (centrosCache != null) return;
        try {
            centrosCache = CentroSaludLoader.loadFromFile(centrosJson);
        } catch (IOException e) {
            centrosCache = null;
        }
    }

    private void printCentro(CentroSalud c) {
        System.out.println("===== CENTRO SANITARIO =====");
        System.out.println("Código: " + c.getCodigo());
        System.out.println("Nombre: " + c.getNombre());
        System.out.println("Dirección: " + c.getDireccion());
        System.out.println("C.P.: " + c.getCp());
        System.out.println("Municipio: " + c.getMunicipio());
        System.out.println("Teléfono: " + c.getTelefono());
        System.out.println("Email: " + c.getEmail());
        System.out.println("URL: " + c.getUrlReal());
        System.out.println("Latitud: " + c.getLatitud());
        System.out.println("Longitud: " + c.getLongitud());
    }

    private String norm(String s) {
        String t = s.trim().toLowerCase();
        t = Normalizer.normalize(t, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
        return t.replaceAll("\\s+", " ");
    }
}
