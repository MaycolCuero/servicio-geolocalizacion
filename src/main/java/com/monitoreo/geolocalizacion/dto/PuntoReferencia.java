package com.monitoreo.geolocalizacion.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

/**
 * Representa un punto geográfico con coordenadas de latitud y longitud.
 * Esta clase se utiliza para definir ubicaciones en el sistema de geolocalización.
 */
@Setter
@Getter
public class PuntoReferencia implements Serializable {
    /**
     * -- GETTER --
     *  Obtiene la longitud geográfica del punto.
     * -- SETTER --
     *  Establece la longitud geográfica del punto.
     *
     */
    private Double longitud;
    /**
     * -- GETTER --
     *  Obtiene la latitud geográfica del punto.
     * -- SETTER --
     *  Establece la latitud geográfica del punto.
     *
     */
    private Double latitud;

    public PuntoReferencia() {

    }

    public PuntoReferencia(Double longitud,Double latitud ) {
        this.longitud = longitud;
        this.latitud = latitud;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PuntoReferencia)) return false;
        PuntoReferencia p = (PuntoReferencia) o;
        return Double.compare(p.latitud, latitud) == 0 &&
                Double.compare(p.longitud, longitud) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitud, longitud);
    }
}
