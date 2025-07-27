package com.monitoreo.geolocalizacion.service;

import com.monitoreo.geolocalizacion.dto.ServicioGeolocalizacionInDTO;
import com.monitoreo.geolocalizacion.rest.ServicioGeolocalizacionRest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;

@SpringBootApplication
public class ServicioGeolocalizacionApplication implements ServicioGeolocalizacionRest {

	public static void main(String[] args) {
		SpringApplication.run(ServicioGeolocalizacionApplication.class, args);
	}

	/**
	 *
	 * @param datosIn Objeto {@link ServicioGeolocalizacionInDTO} que contiene la información de la ruta a registrar.
	 */
	@Override
	public ResponseEntity<String> registrarGeolocalizacion(ServicioGeolocalizacionInDTO datosIn) {
		System.out.println("Información por registrar...");
		return null;
	}

	/**
	 *
	 * @param datosIn Objeto {@link ServicioGeolocalizacionInDTO} que contiene los puntos de inicio y destino.
	 */
	@Override
	public void calcularRuta(ServicioGeolocalizacionInDTO datosIn) {

	}
}
