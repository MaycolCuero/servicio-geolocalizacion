package com.monitoreo.geolocalizacion.conexiones;

import com.monitoreo.geolocalizacion.entidades.Coordinador;
import org.springframework.data.repository.CrudRepository;

/**
 * Interfaz encargada de definir las operaciones principales
 * CRUD para redis.
 */
public interface RepositorioCoordinador  extends CrudRepository<Coordinador, String> {
}
