package com.monitoreo.geolocalizacion.rest;

import com.monitoreo.geolocalizacion.dto.AlertaInDTO;
import com.monitoreo.geolocalizacion.dto.RutaDTO;
import com.monitoreo.geolocalizacion.dto.ServicioGeolocalizacionInDTO;
import com.monitoreo.geolocalizacion.dto.VehiculoDTO;
import com.monitoreo.geolocalizacion.entities.Coordinador;
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
    @PostMapping("/servicioRuteo")
    ResponseEntity<String> servicioRuteo(@RequestBody ServicioGeolocalizacionInDTO datosIn);

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

    /**
     * {@inheritDoc}
     * Implementación del método definido en {@link ServicioGeolocalizacionRest}.
     * Este método guarda o actualiza la información de una ruta y su respectiva coordenada asociada.
     *
     * @param datosIn DTO con los datos de la ruta, incluyendo punto de partida, llegada y ubicación actual.
     * @return ID de la ruta guardada.
     */
    @ResponseBody
    @PostMapping("/guardarRuta")
    Long guardarRuta(@RequestBody RutaDTO datosIn);

    /**
     * Método encargado de guardar las alertas enviadas por los vehículos
     * @param datosIn DTO con los datos necesarios para registrar una alerta
     */
    @PostMapping("/guardarAlerta")
    void guardarAlerta(@RequestBody AlertaInDTO datosIn);

    /**
     * Método ecargado de almacenar la información de un vehiculo.
     * @param datosIn DTO con los datos necesarios para crear el registro de un
     *                vehículo
     * @return Long identificador del registro creado
     */
    @PostMapping("/guardarVehiculo")
    Long guardarVehiculo(@RequestBody VehiculoDTO datosIn);
}
