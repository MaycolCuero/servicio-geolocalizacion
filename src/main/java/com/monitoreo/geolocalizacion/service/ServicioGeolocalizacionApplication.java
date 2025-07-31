package com.monitoreo.geolocalizacion.service;

import com.monitoreo.geolocalizacion.dto.HistorialRutaInDTO;
import com.monitoreo.geolocalizacion.dto.PuntoReferencia;
import com.monitoreo.geolocalizacion.dto.RutaDTO;
import com.monitoreo.geolocalizacion.dto.ServicioGeolocalizacionInDTO;
import com.monitoreo.geolocalizacion.entidades.Coordinador;
import com.monitoreo.geolocalizacion.entities.Coordenada;
import com.monitoreo.geolocalizacion.entities.Ruta;
import com.monitoreo.geolocalizacion.entities.Vehiculo;
import com.monitoreo.geolocalizacion.enums.EstadoRutaEnum;
import com.monitoreo.geolocalizacion.enums.TipoCoordenadaEnum;
import com.monitoreo.geolocalizacion.rest.CalculadorRutaAEstrella;
import com.monitoreo.geolocalizacion.rest.ServicioGeolocalizacionRest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.http.ResponseEntity;

import java.sql.Timestamp;
import java.time.LocalDateTime;
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
	 * Atributo encargado de instancias el entityManager
	 */
	@PersistenceContext
	private EntityManager entityManager;

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
			return ResponseEntity.status(500)
					.body("Error al guardar la información: " + e.getMessage());
		}
		return ResponseEntity.status(201).body("Información guardada");
	}

	/**
	 * Calcula la ruta entre el punto de partida y el destino.
	 * (Método pendiente de implementación)
	 *
	 * @param datosIn Objeto {@link ServicioGeolocalizacionInDTO} que contiene los puntos de inicio y destino.
	 */
	@Override
	public ResponseEntity<String>  servicioRuteo(ServicioGeolocalizacionInDTO datosIn) {
		// Se valida si ya exíste un registro para esa coordenada con otro vehículo
		String keyPuntoReferencia = this.administrarCoordinador.crearkeyReferencia(datosIn);
		String keyReferenciaGuardada = this.administrarCoordinador.obtenerKeyReferencia(keyPuntoReferencia);
		if(keyReferenciaGuardada != null && !keyReferenciaGuardada.equals(datosIn.getIdVehiculo().toString()))
			return ResponseEntity.status(409).body("Vehículo " + datosIn.getIdVehiculo() + " no se puede procesar tu solicitud porque ya existe un vehiculo en esta ruta");
		// se calcula la ruta más corta entre dos puntos
		CalculadorRutaAEstrella calcularRutaEstrella = new CalculadorRutaAEstrella();
		List<PuntoReferencia> rutaMasCorta = calcularRutaEstrella.calcularRuta(datosIn.getPuntoPartida(), datosIn.getPuntoLlegada());
		this.administrarCoordinador.guardarRutasCalculadas(datosIn, rutaMasCorta);
		return ResponseEntity.status(201).body("Registro guardado");
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

	public void guardarRuta(RutaDTO datosIn) {
		Ruta ruta;
		if(datosIn.getIdRuta() == null)
			ruta = new Ruta();
		else
			ruta = this.entityManager.find(Ruta.class, datosIn.getIdRuta());
		Vehiculo vehiculo = new Vehiculo();
		vehiculo.setId(datosIn.getIdVehiculo());
		ruta.setVehiculo(vehiculo);
		ruta.setEstado(datosIn.getEstado().name());
		if(EstadoRutaEnum.FINALIZADO.equals(datosIn.getEstado()))
			ruta.setFechaFin(LocalDateTime.now());
		if(EstadoRutaEnum.INICIADO.equals(datosIn.getEstado()))
			ruta.setFechaInicio(LocalDateTime.now());
		this.entityManager.merge(ruta);
		if(datosIn.getPuntoPartida() != null)
			this.guardarCoordenada(ruta, datosIn.getPuntoPartida(), TipoCoordenadaEnum.PUNTO_PARTIDA.name());
		else if(datosIn.getPuntoLlegada() != null)
			this.guardarCoordenada(ruta, datosIn.getPuntoLlegada(), TipoCoordenadaEnum.PUNTO_LLEGADA.name());
	}

	private void guardarCoordenada(Ruta ruta,PuntoReferencia puntoReferencia, String tipoCoordenada) {
		Coordenada coordenada = new Coordenada();
		coordenada.setRuta(ruta);
		coordenada.setLatitud(puntoReferencia.getLatitud());
		coordenada.setLongitud(puntoReferencia.getLongitud());
		this.entityManager.persist(coordenada);
	}

	public void guardarUbicacionActual(ServicioGeolocalizacionInDTO datosIn) {
		Coordenada coordenada = new Coordenada();
		Vehiculo vehiculo = new Vehiculo();
		vehiculo.setId(datosIn.getIdVehiculo());
		coordenada.setVehiculo(vehiculo);
		coordenada.setLatitud(datosIn.getUbicacionActual().getLatitud());
		coordenada.setLongitud(datosIn.getUbicacionActual().getLongitud());
		coordenada.setTimestamp(LocalDateTime.now());
		this.entityManager.persist(coordenada);
	}

	public void guardarAlerta(RutaDTO datosIn) {

	}
}
