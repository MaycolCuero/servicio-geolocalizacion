package com.monitoreo.geolocalizacion.service;

import com.monitoreo.geolocalizacion.dto.AlertaInDTO;
import com.monitoreo.geolocalizacion.dto.PuntoReferencia;
import com.monitoreo.geolocalizacion.dto.RutaDTO;
import com.monitoreo.geolocalizacion.dto.ServicioGeolocalizacionInDTO;
import com.monitoreo.geolocalizacion.entidades.Coordinador;
import com.monitoreo.geolocalizacion.entities.Alerta;
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
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@EnableRedisRepositories(basePackages = "com.monitoreo.geolocalizacion.conexiones")
@RequiredArgsConstructor
/**
 * Clase principal que inicia la aplicación de geolocalización y expone servicios REST para registrar y consultar rutas.
 */
@EntityScan(basePackages = "com.monitoreo.geolocalizacion.entities")
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
	 * Método principal encargado de iniciar el proyecto
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

	/**
	 * {@inheritDoc}
	 * Implementación del método definido en {@link ServicioGeolocalizacionRest}.
	 * Este método guarda o actualiza la información de una ruta y su respectiva coordenada asociada.
	 *
	 * @param datosIn DTO con los datos de la ruta, incluyendo punto de partida, llegada y ubicación actual.
	 * @return ID de la ruta guardada.
	 */
	@Transactional
	public Long guardarRuta(RutaDTO datosIn) {
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
		// Se guarda o actualiza la información de la ruta
		if(datosIn.getIdRuta() == null)
			this.entityManager.persist(ruta);
		else
			this.entityManager.merge(ruta);
		// Se guarda la información de la coordenada
		if(EstadoRutaEnum.INICIADO.equals(datosIn.getEstado()))
			this.guardarCoordenada(ruta, datosIn.getPuntoPartida(), TipoCoordenadaEnum.PUNTO_PARTIDA.name());
		else if(EstadoRutaEnum.FINALIZADO.equals(datosIn.getEstado()))
			this.guardarCoordenada(ruta, datosIn.getPuntoLlegada(), TipoCoordenadaEnum.PUNTO_LLEGADA.name());
		else if(EstadoRutaEnum.EN_CURSO.equals(datosIn.getEstado()))
			this.guardarCoordenada(ruta, datosIn.getUbicacionActual(), TipoCoordenadaEnum.EN_CAMINO.name());
		else
			this.guardarCoordenada(ruta, datosIn.getUbicacionActual(), TipoCoordenadaEnum.EN_ESPERA.name());
		return ruta.getId();
	}

	/**
	 * Guarda una coordenada asociada a una ruta específica.
	 *
	 * @param ruta Instancia de la ruta a la que pertenece la coordenada.
	 * @param puntoReferencia Objeto con los datos de latitud y longitud.
	 * @param tipoCoordenada Tipo de coordenada (PUNTO_PARTIDA, PUNTO_LLEGADA, etc.).
	 */
	@Transactional
	private void guardarCoordenada(Ruta ruta,PuntoReferencia puntoReferencia, String tipoCoordenada) {
		Coordenada coordenada = new Coordenada();
		coordenada.setRuta(ruta);
		coordenada.setLatitud(puntoReferencia.getLatitud());
		coordenada.setLongitud(puntoReferencia.getLongitud());
		coordenada.setTipoCoordenada(tipoCoordenada);
		this.entityManager.persist(coordenada);
	}

    /**
     * Guarda una alerta relacionada con una ruta específica.
     * (Método pendiente de implementación).
     *
     * @param datosIn Objeto {@link RutaDTO} con la información necesaria para generar una alerta.
     */
	@Transactional
    public void guardarAlerta(AlertaInDTO datosIn) {
		Vehiculo vehiculo = new Vehiculo();
		vehiculo.setId(datosIn.getIdVehiculo());
		Alerta alerta = new Alerta();
		alerta.setVehiculo(vehiculo);
		alerta.setTipo(datosIn.getTiopAlerta().name());
		alerta.setMensaje(datosIn.getMensaje());
		alerta.setFecha(LocalDateTime.now());
		this.entityManager.persist(alerta);
    }
}
