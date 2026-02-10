package com.emergencias.main;

import com.emergencias.controller.EmergencyManager;
import com.emergencias.loader.CentroSaludLoader;
import com.emergencias.model.CentroSalud;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class Main {

    public static void main(String[] args) {

        Properties cfg = new Properties();

        try (FileInputStream fis = new FileInputStream("app.properties")) {
            cfg.load(fis);
            System.out.println("Configuración cargada desde app.properties");
        } catch (IOException e) {
            System.out.println("No se pudo leer app.properties. Se usarán valores por defecto.");
        }

        String jsonFile = cfg.getProperty("centrosSaludJson", "centros_salud.json");

        try {
            List<CentroSalud> centros = CentroSaludLoader.loadFromFile(jsonFile);
            System.out.println("Centros de salud cargados: " + centros.size());
        } catch (IOException e) {
            System.out.println("No se pudo leer el JSON: " + jsonFile);
        }

        EmergencyManager manager = new EmergencyManager(cfg);
        manager.startSystem();
    }
}
