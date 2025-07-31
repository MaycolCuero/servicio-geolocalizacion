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
public class ServicioGeolocalizacionInDTO extends  RutaDTO {
    /**
     * -- GETTER --
     *  Obtiene el ID del vehículo.
     * -- SETTER --
     *  Establece el ID del vehículo.
     *
     */
    private Long idVehiculo;


}
