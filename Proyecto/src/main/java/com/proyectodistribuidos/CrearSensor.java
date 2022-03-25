package com.proyectodistribuidos;

public class CrearSensor {
    
    public static void main(String[] args) {

        System.out.println(args[0]);
        
        ArchivoConfiguracion archivoConfiguracion = new ArchivoConfiguracion((float)0.6,(float)0.3,(float)0.1);
        SensorTemperatura sensor = new SensorTemperatura("Temperatura", 10, archivoConfiguracion);
        
        System.out.println("Sensor creado: " + sensor.toString());
        
    }

    
}
