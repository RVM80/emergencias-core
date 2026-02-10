package com.emergencias.loader;
import com.emergencias.model.CentroSalud;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class CentroSaludLoader {

    /*
    * Lee el fichero completo y con la librería Gson lo convierte directamente ArrayList
    * Esto permite almacenar todos los centros en memoria y usarlos luego en el programa.
    *
    *  */

    public static List<CentroSalud> loadFromFile(String fileName) throws IOException {
        String json = Files.readString(Path.of(fileName), StandardCharsets.UTF_8);
        Type listType = new TypeToken<List<CentroSalud>>() {}.getType();
        return new Gson().fromJson(json, listType);
    }
}
