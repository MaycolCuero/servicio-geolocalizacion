package com.monitoreo.geolocalizacion.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * DTO utilizado para encapsular los datos de entrada relacionados con el servicio de geolocalización.
 * Contiene información sobre el vehículo, su ubicación actual, punto de partida y destino.
 */
@Setter
@Getter
public class ServicioGeolocalizacionInDTO implements Serializable {
    /**
     * -- GETTER --
     *  Obtiene el ID del vehículo.
     * -- SETTER --
     *  Establece el ID del vehículo.
     *
     */
    private Long idVehiculo;
    /**
     * -- GETTER --
     *  Obtiene el punto de partida del vehículo.
     * -- SETTER --
     *  Establece el punto de partida del vehículo.
     *
     */
    private PuntoReferencia puntoPartida;
    /**
     * -- GETTER --
     *  Obtiene el punto de llegada del vehículo.
     * -- SETTER --
     *  Establece el punto de llegada del vehículo.
     *
     */
    private PuntoReferencia puntoLlegada;
    /**
     * -- GETTER --
     *  Obtiene la ubicación actual del vehículo.
     * -- SETTER --
     *  Establece la ubicación actual del vehículo.
     *
     */
    private PuntoReferencia ubicacionActual;

}
