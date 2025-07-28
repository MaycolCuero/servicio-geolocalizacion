package com.monitoreo.geolocalizacion.rest;

import com.monitoreo.geolocalizacion.dto.ServicioGeolocalizacionInDTO;
import com.monitoreo.geolocalizacion.entidades.Coordinador;
import jakarta.websocket.server.PathParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    ResponseEntity<String> registrarGeolocalizacion(@RequestBody ServicioGeolocalizacionInDTO datosIn);
    /**
     * Calcula la ruta más corta entre dos puntos geográficos especificados en el DTO.
     *
     * @param datosIn Objeto {@link ServicioGeolocalizacionInDTO} que contiene los puntos de inicio y destino.
     */
    @PostMapping("/calcularRuta")
    void calcularRuta(@RequestBody ServicioGeolocalizacionInDTO datosIn);

    /**
     * Servicio encargado de obtener la información de las rutas registradas
     * por vehículo.
     *
     * @param idVehiculo identificador del vehículo
     * @return Coordinador información de la ruta
     */
    @ResponseBody
    @GetMapping("/obtenerInformacionRutaPorIdVehiculo")
    Coordinador obtenerInformacionRutaPorIdVehiculo(@RequestParam("idVehiculo") Long idVehiculo);

    /**
     * Método encargado de obtener el historial de rutas realizadas por
     * un vehículo
     * @param idVehiculo identificador del vehículo
     * @return  List<Coordinador> lista de coordenadas
     */
    @ResponseBody
    @GetMapping("/obtenerHistorialRutasPorIdVehiculo")
    List<Coordinador> obtenerHistorialRutasPorIdVehiculo(@RequestParam("idVehiculo") Long idVehiculo);
}
