package com.monitoreo.geolocalizacion.dto;

import com.monitoreo.geolocalizacion.enums.TipoAlerta;
import lombok.Data;

import java.io.Serializable;

@Data
/**
 * DTO de entrada para la creación o procesamiento de una alerta en el sistema de geolocalización.
 * Contiene la información necesaria para identificar el vehículo, el tipo de alerta y un mensaje descriptivo.
 */
public class AlertaInDTO implements Serializable {
    /**
     * Identificador del vehículo al que pertenece la alerta.
     */
    private Long idVehiculo;

    /**
     * Tipo de alerta generada (por ejemplo: exceso de velocidad, salida de zona, etc.).
     */
    private TipoAlerta tiopAlerta;

    /**
     * Mensaje descriptivo relacionado con la alerta.
     */
    private String mensaje;
}
