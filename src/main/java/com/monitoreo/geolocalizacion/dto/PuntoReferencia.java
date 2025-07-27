package com.monitoreo.geolocalizacion.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

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

}
