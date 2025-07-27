package com.monitoreo.geolocalizacion.dto;

import java.io.Serializable;

/**
 * DTO utilizado para encapsular los datos de entrada relacionados con el servicio de geolocalización.
 * Contiene información sobre el vehículo, su ubicación actual, punto de partida y destino.
 */
public class ServicioGeolocalizacionInDTO implements Serializable {
    private Long idVehiculo;
    private PuntoReferencia puntoPartida;
    private PuntoReferencia puntoLlegada;
    private PuntoReferencia ubicacionActual;

    /**
     * Obtiene el ID del vehículo.
     *
     * @return ID del vehículo.
     */
    public Long getIdVehiculo() {
        return idVehiculo;
    }

    /**
     * Establece el ID del vehículo.
     *
     * @param idVehiculo ID del vehículo.
     */
    public void setIdVehiculo(Long idVehiculo) {
        this.idVehiculo = idVehiculo;
    }

    /**
     * Obtiene el punto de partida del vehículo.
     *
     * @return Objeto {@link PuntoReferencia} correspondiente al punto de partida.
     */
    public PuntoReferencia getPuntoPartida() {
        return puntoPartida;
    }

    /**
     * Establece el punto de partida del vehículo.
     *
     * @param puntoPartida Objeto {@link PuntoReferencia} que representa el punto de partida.
     */
    public void setPuntoPartida(PuntoReferencia puntoPartida) {
        this.puntoPartida = puntoPartida;
    }

    /**
     * Obtiene el punto de llegada del vehículo.
     *
     * @return Objeto {@link PuntoReferencia} correspondiente al punto de llegada.
     */
    public PuntoReferencia getPuntoLlegada() {
        return puntoLlegada;
    }

    /**
     * Establece el punto de llegada del vehículo.
     *
     * @param puntoLlegada Objeto {@link PuntoReferencia} que representa el punto de llegada.
     */
    public void setPuntoLlegada(PuntoReferencia puntoLlegada) {
        this.puntoLlegada = puntoLlegada;
    }

    /**
     * Obtiene la ubicación actual del vehículo.
     *
     * @return Objeto {@link PuntoReferencia} que representa la ubicación actual.
     */
    public PuntoReferencia getUbicacionActual() {
        return ubicacionActual;
    }

    /**
     * Establece la ubicación actual del vehículo.
     *
     * @param ubicacionActual Objeto {@link PuntoReferencia} que representa la ubicación actual.
     */
    public void setUbicacionActual(PuntoReferencia ubicacionActual) {
        this.ubicacionActual = ubicacionActual;
    }
}
