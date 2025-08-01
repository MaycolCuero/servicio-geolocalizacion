package com.monitoreo.geolocalizacion.dto;

import lombok.Data;
import java.io.Serializable;

@Data
public class VehiculoDTO implements Serializable {

    private Long id;

    private String nombre;
    private String marca;
    private String modelo;
    private Long idTipoVehiculo;
}