package com.emergencias.main;

import com.emergencias.controller.EmergencyManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Punto de entrada de la aplicación.
 * Carga la configuración y arranca el EmergencyManager.
 */
public class Main {

    public static void main(String[] args) {
        Properties cfg = new Properties();

        // Cargar desde resources (classpath)
        //FIX. Como el archivo app.properties no cargaba correctamente, se ha movido a la carpeta src y se ha utilizado getResourceAsStream para cargarlo.
        try (InputStream is = Main.class.getClassLoader().getResourceAsStream("app.properties")) {
            if (is != null) {
                cfg.load(is);
                System.out.println("Configuración cargada desde app.properties");
            } else {
                System.out.println("No se encontró app.properties. Se usarán valores por defecto.");
            }
        } catch (IOException e) {
            System.out.println("No se pudo leer app.properties. Se usarán valores por defecto.");
        }

        EmergencyManager manager = new EmergencyManager(cfg);
        manager.startSystem();
    }
}
