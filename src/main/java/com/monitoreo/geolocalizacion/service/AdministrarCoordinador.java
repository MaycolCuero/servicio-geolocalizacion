package com.monitoreo.geolocalizacion.service;

import com.monitoreo.geolocalizacion.conexiones.RepositorioCoordinador;
import com.monitoreo.geolocalizacion.dto.PuntoReferencia;
import com.monitoreo.geolocalizacion.entidades.Coordinador;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


/**
 * Servicio encargado de administrar la información de coordenadas asociadas a vehículos.
 * Este servicio interactúa con Redis a través del repositorio correspondiente para
 * guardar y recuperar información de geolocalización.
 */
@Service
@RequiredArgsConstructor
public class AdministrarCoordinador {

    /**
     * Repositorio para realizar operaciones CRUD sobre la entidad Coordinador en Redis.
     */
    private final RepositorioCoordinador repositorio;

    /**
     * Guarda en Redis la información de ubicación (coordenadas) de un vehículo.
     *
     * @param datosIn   Objeto que contiene la latitud y longitud del vehículo.
     * @param idVehiculo Identificador único del vehículo.
     */
    public void guardarCoordenadaEnRedis(PuntoReferencia datosIn, String idVehiculo ) {
        Coordinador coordinador = Coordinador.builder()
                .idVehiculo(idVehiculo)
                .longitud(datosIn.getLongitud())
                .latitud(datosIn.getLatitud())
                .fechaRegistro(LocalDateTime.now())
                .build();
        repositorio.save(coordinador);
    }

    /**
     * Obtiene las coordenadas almacenadas en Redis asociadas a un vehículo, mediante su ID.
     *
     * @param idVehiculo Identificador único del vehículo.
     * @return Instancia de Coordinador con la información encontrada, o null si no existe.
     */
    public Coordinador obtenerCoordenadasPorIdVehiculo(String idVehiculo) {
        return repositorio.findById(idVehiculo).orElse(null) ;
    }
}
