package com.emergencias.main;

import com.emergencias.controller.EmergencyManager;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Punto de entrada de la aplicación.
 * Carga la configuración y arranca el EmergencyManager.
 */
public class Main {

    public static void main(String[] args) {

        Properties cfg = new Properties();

        // Intentamos cargar app.properties desde el directorio de trabajo
        try (FileInputStream fis = new FileInputStream("app.properties")) {
            cfg.load(fis);
            System.out.println("Configuración cargada desde app.properties");
        } catch (IOException e) {
            System.out.println("No se pudo leer app.properties. Se usarán valores por defecto.");
        }

        // Creamos el controlador principal y arrancamos el sistema
        EmergencyManager manager = new EmergencyManager(cfg);
        manager.startSystem();
    }
}
