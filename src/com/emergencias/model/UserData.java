package com.emergencias.model;

/**
 * Datos básicos del usuario que hace la llamada.
 * Aquí solo guardamos nombre y teléfono.
 */
public class UserData {

    private final String nombre;
    private final String telefono;

    public UserData(String nombre, String telefono) {
        this.nombre = nombre;
        this.telefono = telefono;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    @Override
    public String toString() {
        return nombre + " (" + telefono + ")";
    }
}
