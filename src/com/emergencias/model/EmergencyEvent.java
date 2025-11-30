package com.emergencias.model;

/**
 * Representa un evento de emergencia.
 * Es nuestro "modelo" principal.
 */
public class EmergencyEvent {

    private final String tipoEmergencia;
    private final String ubicacion;
    private final int gravedad;       // 0..3
    private final UserData usuario;   // quién está llamando

    public EmergencyEvent(String tipoEmergencia, String ubicacion, int gravedad, UserData usuario) {
        this.tipoEmergencia = tipoEmergencia;
        this.ubicacion = ubicacion;
        this.gravedad = gravedad;
        this.usuario = usuario;
    }

    public String getTipoEmergencia() {
        return tipoEmergencia;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public int getGravedad() {
        return gravedad;
    }

    public UserData getUsuario() {
        return usuario;
    }

    @Override
    public String toString() {
        return "EmergencyEvent{" +
                "tipoEmergencia='" + tipoEmergencia + '\'' +
                ", ubicacion='" + ubicacion + '\'' +
                ", gravedad=" + gravedad +
                ", usuario=" + usuario +
                '}';
    }
}
