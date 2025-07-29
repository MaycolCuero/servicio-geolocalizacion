package com.monitoreo.geolocalizacion.rest;

import com.monitoreo.geolocalizacion.dto.NodoRuta;
import com.monitoreo.geolocalizacion.dto.PuntoReferencia;

import java.util.*;

public class CalculadorRutaAEstrella {
    /**
     * Calcula la ruta óptima usando A* entre dos coordenadas GPS.
     */
    public List<PuntoReferencia> calcularRuta(PuntoReferencia inicio, PuntoReferencia destino) {
        PriorityQueue<NodoRuta> abiertos = new PriorityQueue<>(Comparator.comparingDouble(n -> n.f));
        Set<NodoRuta> cerrados = new HashSet<>();

        NodoRuta nodoInicio = new NodoRuta(inicio.getLatitud(), inicio.getLongitud());
        NodoRuta nodoDestino = new NodoRuta(destino.getLatitud(), destino.getLongitud());

        nodoInicio.g = 0;
        nodoInicio.h = calcularHeuristica(nodoInicio, nodoDestino);
        nodoInicio.f = nodoInicio.g + nodoInicio.h;

        abiertos.add(nodoInicio);

        int contadorIteraciones = 0;
        int maxIteraciones = 10000;
        while (!abiertos.isEmpty() && contadorIteraciones++ < maxIteraciones) {
            NodoRuta actual = abiertos.poll();

            if (estanCerca(actual, nodoDestino)) {
                return reconstruirRuta(actual);
            }

            cerrados.add(actual);

            for (NodoRuta vecino : obtenerVecinosSimulados(actual)) {
                if (cerrados.contains(vecino)) continue;

                double costoTentativo = actual.g + calcularHeuristica(actual, vecino);

                if (!abiertos.contains(vecino) || costoTentativo < vecino.g) {
                    vecino.padre = actual;
                    vecino.g = costoTentativo;
                    vecino.h = calcularHeuristica(vecino, nodoDestino);
                    vecino.f = vecino.g + vecino.h;

                    abiertos.add(vecino);
                }
            }
        }

        System.out.println("No se pudo encontrar una ruta en el número máximo de iteraciones.");
        return null;
    }

    /**
     * Simula vecinos alrededor del nodo actual (ej: 8 direcciones a 0.001° distancia).
     */
    private List<NodoRuta> obtenerVecinosSimulados(NodoRuta nodo) {
        List<NodoRuta> vecinos = new ArrayList<>();
        double[] deltas = {-0.001, 0, 0.001};

        for (double dLat : deltas) {
            for (double dLon : deltas) {
                if (dLat == 0 && dLon == 0) continue;
                vecinos.add(new NodoRuta(
                        nodo.getLatitud() + dLat,
                        nodo.getLongitud() + dLon
                ));
            }
        }

        return vecinos;
    }

    /**
     * Usa la fórmula de Haversine para estimar la distancia entre dos puntos.
     */
    public double calcularHeuristica(PuntoReferencia a, PuntoReferencia b) {
        double R = 6371.0; // Radio de la Tierra en km
        double dLat = Math.toRadians(b.getLatitud() - a.getLatitud());
        double dLon = Math.toRadians(b.getLongitud() - a.getLongitud());
        double lat1 = Math.toRadians(a.getLatitud());
        double lat2 = Math.toRadians(b.getLatitud());

        double aVal = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(aVal), Math.sqrt(1-aVal));
        return R * c; // distancia en km
    }

    /**
     * Verifica si dos puntos están suficientemente cerca (por ejemplo, < 20 metros)
     */
    private boolean estanCerca(PuntoReferencia a, PuntoReferencia b) {
        return calcularHeuristica(a, b) < 0.02; // ~20 metros
    }

    /**
     * Reconstruye la ruta desde el nodo destino hasta el origen.
     */
    private List<PuntoReferencia> reconstruirRuta(NodoRuta nodoFinal) {
        List<PuntoReferencia> ruta = new ArrayList<>();
        NodoRuta actual = nodoFinal;
        while (actual != null) {
            ruta.add(0, new PuntoReferencia(actual.getLatitud(), actual.getLongitud()));
            actual = actual.padre;
        }
        return ruta;
    }
}
