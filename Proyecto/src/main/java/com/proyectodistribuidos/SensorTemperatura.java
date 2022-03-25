package com.proyectodistribuidos;
public class SensorTemperatura extends Sensor {

    public SensorTemperatura(String tipo, int tiempo, ArchivoConfiguracion archivoConfiguracion) {
        super(tipo, tiempo, archivoConfiguracion);
        super.setValorMinimo(68f);
        super.setValorMaximo(89f);
    }

}
    

