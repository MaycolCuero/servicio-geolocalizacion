package com.monitoreo.geolocalizacion.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "alerta")
@Data
@NoArgsConstructor
@AllArgsConstructor
/**
 * Entidad que representa una alerta generada para un vehículo.
 * Contiene información sobre el tipo de alerta, el mensaje asociado,
 * la fecha de creación y el vehículo al que está asociada.
 */
public class Alerta {

    /**
     * Identificador único de la alerta.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Vehículo al que está asociada la alerta.
     */
    @ManyToOne
    @JoinColumn(name = "vehiculo_id")
    private Vehiculo vehiculo;

    /**
     * Tipo de alerta (por ejemplo, velocidad, ubicación, etc.).
     */
    private String tipo;
    /**
     * Mensaje descriptivo de la alerta.
     */
    private String mensaje;
    /**
     * Fecha y hora en que se generó la alerta.
     */
    private LocalDateTime fecha;
}