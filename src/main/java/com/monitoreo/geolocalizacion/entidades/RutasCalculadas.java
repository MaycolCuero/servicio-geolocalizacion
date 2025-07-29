package com.monitoreo.geolocalizacion.entidades;

import com.monitoreo.geolocalizacion.dto.PuntoReferencia;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@RedisHash("RutasCalculadas")
public class RutasCalculadas {
    @Id
    private String idVehiculo;
    private PuntoReferencia puntoPartida;
    private PuntoReferencia puntoLlegada;
    private List<PuntoReferencia> rutaCorta;
}
