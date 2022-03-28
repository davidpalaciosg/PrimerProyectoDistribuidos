package com.proyectodistribuidos;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class CrearSensor {

    /*
     * Args:
     * -[0]: tipo de sensor
     * -[1]: tiempo de creacion de medidas
     * -[2]: ubicacion archivo de configuración
     */
    public static void main(String[] args) {

        if (args.length != 3) {
            System.out.println("Error: Numero de argumentos incorrecto");
            System.exit(1);
        }
        try {
            String tipo = args[0];
            int tiempo = Integer.parseInt(args[1]);
            String ubicacionArchivo = args[2];

            // Crear archivo de configuración
            ArchivoConfiguracion archivo = creaArchivoConfiguracion(ubicacionArchivo);
            Sensor sensor = crearSensor(tipo, tiempo, archivo);

            System.out.println("Sensor creado");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            System.exit(1);
        }

    }

    // Crear archivo de configuración
    public static ArchivoConfiguracion creaArchivoConfiguracion(String filename) {
        // Abrir archivo de texto
        try {
            FileReader fr = new FileReader(filename);
            BufferedReader br = new BufferedReader(fr);
            String linea;
            String[] datos;
            float[] datosFloat = new float[3];

            linea = br.readLine();
            datos = linea.split(",");
            for (int i = 0; i < datos.length; i++) {
                datosFloat[i] = Float.parseFloat(datos[1]);
                i++;
            }
            br.close();
            fr.close();
            return new ArchivoConfiguracion(datosFloat[0], datosFloat[1], datosFloat[2]);
        } catch (Exception e) {
            System.out.println("Error al leer el archivo de configuración");
            System.exit(1);
            return null;
        }
    }

    public static Sensor crearSensor(String tipo, int tiempo, ArchivoConfiguracion archivo) {
        tipo = tipo.toLowerCase();
        if (tipo.equalsIgnoreCase("temperatura")) {
            return new SensorTemperatura(tipo, tiempo, archivo);
        } else if (tipo.equalsIgnoreCase("oxigeno")) {
            return new SensorOxigeno(tipo, tiempo, archivo);
        } else if (tipo.equalsIgnoreCase("ph")) {
            return new SensorPH(tipo, tiempo, archivo);
        } else {
            System.out.println("Error: Tipo de sensor no reconocido");
            System.exit(1);
            return null;
        }
    }

    // TODO Publicar medida de Sensor hacia Monitor
    public boolean publicarMedida(ArrayList<String> medidasString) {
        return false;
    }
}
