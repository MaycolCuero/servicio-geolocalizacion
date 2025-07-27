package com.monitoreo.geolocalizacion.service;

import com.monitoreo.geolocalizacion.dto.ServicioGeolocalizacionInDTO;
import com.monitoreo.geolocalizacion.rest.ServicioGeolocalizacionRest;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.http.ResponseEntity;

@EnableRedisRepositories(basePackages = "com.monitoreo.geolocalizacion.conexiones")
@RequiredArgsConstructor
@SpringBootApplication
public class ServicioGeolocalizacionApplication implements ServicioGeolocalizacionRest {

	private final AdministrarCoordinador administrarCoordinador;

	public static void main(String[] args) {
		SpringApplication.run(ServicioGeolocalizacionApplication.class, args);
	}

	/**
	 *
	 * @param datosIn Objeto {@link ServicioGeolocalizacionInDTO} que contiene la información de la ruta a registrar.
	 */
	@Override
	public ResponseEntity<String> registrarGeolocalizacion(ServicioGeolocalizacionInDTO datosIn) {
		try{
			if(datosIn.getUbicacionActual() == null)
				this.administrarCoordinador.guardarCoordenadaEnRedis(datosIn.getPuntoPartida(), datosIn.getIdVehiculo().toString());
			else
				this.administrarCoordinador.guardarCoordenadaEnRedis(datosIn.getUbicacionActual(), datosIn.getIdVehiculo().toString());
		} catch (Exception e) {
			ResponseEntity.status(500)
					.body("Error al guardar la inforación {} " + e.getMessage());
		}
		return ResponseEntity.ok("Información guardad") ;
	}

	/**
	 *
	 * @param datosIn Objeto {@link ServicioGeolocalizacionInDTO} que contiene los puntos de inicio y destino.
	 */
	@Override
	public void calcularRuta(ServicioGeolocalizacionInDTO datosIn) {

	}
}
