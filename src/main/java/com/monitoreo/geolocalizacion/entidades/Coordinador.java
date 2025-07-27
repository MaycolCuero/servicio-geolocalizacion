package com.monitoreo.geolocalizacion.entidades;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
/**
 * Representa la información de geolocalización asociada a un vehículo.
 * Esta clase se almacena en Redis usando el hash "Cordinador".
 * Campos:
 * - idVehiculo: Identificador único del vehículo.
 * - latitud: Coordenada de latitud del vehículo.
 * - longitud: Coordenada de longitud del vehículo.
 * Se utilizan anotaciones de Lombok para generar automáticamente
 * constructores, métodos getter y setter, y el patrón Builder.
 */
@RedisHash("Cordinador")
public class Coordinador {
    @Id
    private String idVehiculo;

    private Double latitud;
    private Double longitud;
}
