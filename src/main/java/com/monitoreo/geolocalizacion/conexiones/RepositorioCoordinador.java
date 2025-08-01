package com.monitoreo.geolocalizacion.conexiones;

import com.monitoreo.geolocalizacion.entities.Coordinador;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Interfaz encargada de definir las operaciones principales
 * CRUD para redis.
 */
@Repository
public interface RepositorioCoordinador  extends CrudRepository<Coordinador, String> {
}
