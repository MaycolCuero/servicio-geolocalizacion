package com.monitoreo.geolocalizacion.rest;

import com.monitoreo.geolocalizacion.dto.ServicioGeolocalizacionInDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Clase encargada de definir los servicios
 * dispuestos a ser consumidos
 */
@RestController
@RequestMapping("/api/coordinates")
public interface ServicioGeolocalizacionRest {

    /**
     * Registra una nueva ruta de geolocalización basada en los datos proporcionados.
     *
     * @param datosIn Objeto {@link ServicioGeolocalizacionInDTO} que contiene la información de la ruta a registrar.
     * @return {@link ResponseEntity} con un mensaje indicando el resultado del registro.
     */
    @PostMapping("/registrarGeolocalizacion")
    public ResponseEntity<String> registrarGeolocalizacion(@RequestBody ServicioGeolocalizacionInDTO datosIn);

    /**
     * Calcula la ruta más corta entre dos puntos geográficos especificados en el DTO.
     *
     * @param datosIn Objeto {@link ServicioGeolocalizacionInDTO} que contiene los puntos de inicio y destino.
     */
    @PostMapping("/calcularRuta")
    public void calcularRuta(@RequestBody ServicioGeolocalizacionInDTO datosIn);
}
