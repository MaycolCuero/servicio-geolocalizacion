package com.monitoreo.geolocalizacion.service;

import com.monitoreo.geolocalizacion.dto.ServicioGeolocalizacionInDTO;
import com.monitoreo.geolocalizacion.entidades.Coordinador;
import com.monitoreo.geolocalizacion.rest.ServicioGeolocalizacionRest;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.http.ResponseEntity;

import java.util.List;

@EnableRedisRepositories(basePackages = "com.monitoreo.geolocalizacion.conexiones")
@RequiredArgsConstructor
/**
 * Clase principal que inicia la aplicación de geolocalización y expone servicios REST para registrar y consultar rutas.
 */
@SpringBootApplication
public class ServicioGeolocalizacionApplication implements ServicioGeolocalizacionRest {
	/**
	 * Atributo que determina la instancia de la clase AdministrarCoordinador
	 */
	private final AdministrarCoordinador administrarCoordinador;

	/**
	 * Método princial encargado de iniciar el proyecto
	 * @param args arreglo de parámetros
	 */
	public static void main(String[] args) {
		SpringApplication.run(ServicioGeolocalizacionApplication.class, args);
	}
	/**
	 * Registra la geolocalización actual de un vehículo.
	 * Si no se proporciona ubicación actual, se toma el punto de partida.
	 *
	 * @param datosIn Objeto {@link ServicioGeolocalizacionInDTO} que contiene la información de la ruta a registrar.
	 * @return Respuesta HTTP con estado OK o error interno.
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
	 * Calcula la ruta entre el punto de partida y el destino.
	 * (Método pendiente de implementación)
	 *
	 * @param datosIn Objeto {@link ServicioGeolocalizacionInDTO} que contiene los puntos de inicio y destino.
	 */
	@Override
	public void calcularRuta(ServicioGeolocalizacionInDTO datosIn) {

	}

	/**
	 *
	 * @param idVehiculo identificador del vehículo
	 * @return Coordinador información de la última ubicación
	 */
	@Override
	public Coordinador obtenerInformacionRutaPorIdVehiculo(Long idVehiculo) {
		return administrarCoordinador.obtenerCoordenadasPorIdVehiculo(idVehiculo.toString());
	}

	/**
	 * Obtiene el historial completo de coordenadas registradas para un vehículo.
	 *
	 * @param idVehiculo Identificador único del vehículo.
	 * @return Lista de coordenadas históricas.
	 */
	@Override
	public List<Coordinador> obtenerHistorialRutasPorIdVehiculo(Long idVehiculo) {
		return this.administrarCoordinador.obtenerHistorialCoordenadas(idVehiculo.toString());
	}
}
