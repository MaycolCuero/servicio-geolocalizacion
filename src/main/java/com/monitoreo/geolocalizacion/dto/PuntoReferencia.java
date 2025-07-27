package com.monitoreo.geolocalizacion.dto;

import java.io.Serializable;

/**
 * Representa un punto geográfico con coordenadas de latitud y longitud.
 * Esta clase se utiliza para definir ubicaciones en el sistema de geolocalización.
 */
public class PuntoReferencia implements Serializable {
    private Long longitud;
    private Long latitud;

    /**
     * Obtiene la longitud geográfica del punto.
     *
     * @return Longitud en formato decimal.
     */
    public Long getLongitud() {
        return longitud;
    }

    /**
     * Establece la longitud geográfica del punto.
     *
     * @param longitud Valor decimal que representa la longitud.
     */
    public void setLongitud(Long longitud) {
        this.longitud = longitud;
    }

    /**
     * Obtiene la latitud geográfica del punto.
     *
     * @return Latitud en formato decimal.
     */
    public Long getLatitud() {
        return latitud;
    }

    /**
     * Establece la latitud geográfica del punto.
     *
     * @param latitud Valor decimal que representa la latitud.
     */
    public void setLatitud(Long latitud) {
        this.latitud = latitud;
    }
}
