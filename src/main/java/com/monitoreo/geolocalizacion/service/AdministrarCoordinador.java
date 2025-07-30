package com.monitoreo.geolocalizacion.service;

import com.monitoreo.geolocalizacion.conexiones.RepositorioCoordinador;
import com.monitoreo.geolocalizacion.conexiones.RepositorioRutaCorta;
import com.monitoreo.geolocalizacion.dto.PuntoReferencia;
import com.monitoreo.geolocalizacion.dto.ServicioGeolocalizacionInDTO;
import com.monitoreo.geolocalizacion.entidades.Coordinador;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.monitoreo.geolocalizacion.entidades.RutasCalculadas;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 * Clase de servicio que gestiona las coordenadas de ubicación de vehículos.
 * Permite guardar y recuperar información de localización usando Redis.
 */
@Service
@RequiredArgsConstructor
public class AdministrarCoordinador {
    /**
     * Repositorio para realizar operaciones CRUD sobre la entidad Coordinador en Redis.
     */
    private final RepositorioCoordinador repositorio;

    private final RepositorioRutaCorta respositorioRutaCorta;
    /**
     * Plantilla para realizar operaciones directas sobre Redis en formato String.
     */
    @Autowired
    private StringRedisTemplate redisTemplate;
    /**
     * Utilidad para convertir objetos Java a JSON y viceversa.
     */
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Guarda la coordenada actual del vehículo tanto en el repositorio como en una estructura
     * de conjunto ordenado (Sorted Set) en Redis.
     *
     * @param datosIn Objeto que contiene latitud y longitud de la ubicación actual.
     * @param idVehiculo Identificador único del vehículo.
     */
    public void guardarCoordenadaEnRedis(PuntoReferencia datosIn, String idVehiculo ) {
        Coordinador coordinador = Coordinador.builder()
                .idVehiculo(idVehiculo)
                .longitud(datosIn.getLongitud())
                .latitud(datosIn.getLatitud())
                .build();
        /*
            Se utiliza Redis Sorted Set para almacenar los registros de forma que no
            permita datos repetidos
         */
        try {
            String jsonValue = objectMapper.writeValueAsString(coordinador);
            double idscore = coordinador.getLatitud()+coordinador.getLatitud();
            // Se guarda la información de las rutas sin repeticiones
            redisTemplate.opsForZSet().add("veh:" + idVehiculo + ":coords", jsonValue, idscore);
            // Se declara el TTL (Time to live) de 5 minutos
            redisTemplate.expire("veh:" + idVehiculo + ":coords", 5, TimeUnit.MINUTES);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Recupera la última coordenada registrada del vehículo desde Redis.
     * @param idVehiculo Identificador del vehículo.
     * @return Coordenada más reciente almacenada en Redis o null si no se encuentra.
     */
    public Coordinador obtenerCoordenadasPorIdVehiculo(String idVehiculo) {
        return repositorio.findById(idVehiculo).orElse(null) ;
    }

    /**
     * Recupera todas las coordenadas registradas de un vehículo desde el conjunto ordenado de Redis.
     *
     * @param idVehiculo Identificador del vehículo.
     * @return Lista de coordenadas históricas asociadas al vehículo.
     */
    public List<Coordinador> obtenerHistorialCoordenadas(String idVehiculo) {
        Set<String> jsonValues = redisTemplate.opsForZSet()
            .range("veh:" + idVehiculo + ":coords", 0, -1);

        if (jsonValues == null) return Collections.emptyList();

        return jsonValues.stream()
            .map(json -> {
                try {
                    return objectMapper.readValue(json, Coordinador.class);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                    return null;
                }
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    public void guardarRutasCalculadas(ServicioGeolocalizacionInDTO datosIn, List<PuntoReferencia> rutaCorta) {
        RutasCalculadas ruta = new RutasCalculadas();
        ruta.setIdVehiculo(datosIn.getIdVehiculo().toString());
        ruta.setPuntoPartida(datosIn.getPuntoPartida());
        ruta.setPuntoLlegada(datosIn.getPuntoLlegada());
        ruta.setRutaCorta(rutaCorta);
        respositorioRutaCorta.save(ruta);
        try{
            String jsonValue = objectMapper.writeValueAsString(ruta);
            redisTemplate.opsForSet().add(datosIn.getIdVehiculo().toString(),jsonValue);
            String keyPorReferencia = this.crearkeyReferencia(datosIn);
            redisTemplate.opsForSet().add(keyPorReferencia, datosIn.getIdVehiculo().toString());
        } catch (JsonProcessingException e){

        }

    }

    public String crearkeyReferencia(ServicioGeolocalizacionInDTO datosIn) {
        return String.valueOf(datosIn.getPuntoPartida().getLatitud()) +
                datosIn.getPuntoPartida().getLongitud() +
                datosIn.getPuntoLlegada().getLatitud() +
                datosIn.getPuntoLlegada().getLongitud();
    }

    public String obtenerKeyReferencia(String idKeyReferencia) {
        Set<String> setKey = this.redisTemplate.opsForSet().members(idKeyReferencia);
        if(setKey.isEmpty()) return null;
        return setKey.stream().findFirst().orElse(null);
    }
}
