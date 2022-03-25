package com.proyectodistribuidos;
public class SensorPH extends Sensor {

    public SensorPH(String tipo, int tiempo, ArchivoConfiguracion archivoConfiguracion) {
        super(tipo, tiempo, archivoConfiguracion);
        super.setValorMinimo(6.0f);
        super.setValorMaximo(8.0f);
        
    }

    
    

}
