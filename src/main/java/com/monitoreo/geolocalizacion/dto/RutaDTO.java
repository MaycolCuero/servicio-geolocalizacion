package com.monitoreo.geolocalizacion.dto;

import com.monitoreo.geolocalizacion.enums.EstadoRutaEnum;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
@Setter
@Getter
public class RutaDTO implements Serializable {
    private Long idVehiculo;
    private Long idRuta;
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

    private EstadoRutaEnum estado;
}
