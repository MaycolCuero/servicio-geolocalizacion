package com.monitoreo.geolocalizacion.dto;

public class NodoRuta extends PuntoReferencia {
    public double g;
    public double h;
    public double f;
    public NodoRuta padre;

    public NodoRuta(double latitud, double longitud) {
        super(latitud, longitud);
    }
}
