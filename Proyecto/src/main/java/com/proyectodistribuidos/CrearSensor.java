package com.proyectodistribuidos;

import java.io.BufferedReader;
import java.io.FileReader;

import org.zeromq.SocketType;
import org.zeromq.ZMQ;

public class CrearSensor {

    private static Sensor sensor;

    /*
     * Args:
     * -[0]: tipo de sensor
     * -[1]: tiempo de creación de medidas
     * -[2]: ubicación archivo de configuración
     */

    public static void main(String[] args) throws InterruptedException {

        String tipo = "";
        int tiempo = 0;
        String ubicacionArchivo = "";
        String tcp = "";
        ZMQ.Context nuevoContext = ZMQ.context(1);
        ZMQ.Socket nuevoPublisher = nuevoContext.socket(SocketType.PUB);
        boolean exito = false;

        if (args.length != 3) {
            System.out.println("Error: Numero de argumentos incorrecto");
            System.exit(1);
        }
        try {
            tipo = args[0].toLowerCase();
            tiempo = Integer.parseInt(args[1]);
            ubicacionArchivo = args[2];

            // Crear archivo de configuración
            ArchivoConfiguracion archivo = creaArchivoConfiguracion(ubicacionArchivo);
            sensor = crearSensor(tipo, tiempo, archivo);
            System.out.println("Sensor de " + tipo + " creado");

            // Conectar sockets
            if (tipo.equals("temperatura")) {
                tcp = "tcp://*:5555";
            } else if (tipo.equals("ph")) {
                tcp = "tcp://*:5566";
            } else if (tipo.equals("oxigeno")) {
                tcp = "tcp://*:5577";
            }

            // Crear publisher de tópico (tipo de sensor)
            nuevoPublisher.bind(tcp);
            String ipc = "ipc://" + tipo;
            nuevoPublisher.bind(ipc);
            exito = true;

        } catch (Exception e) {

            int puerto;

            if (tipo.equalsIgnoreCase("temperatura")) {
                puerto = 5555;
                while (puerto <= 5565 && exito == false) {
                    try {
                        tcp = "tcp://localhost:" + puerto;
                        //tcp = "tcp://*:" + puerto;
                        nuevoPublisher.bind(tcp);
                        exito = true;
                    } catch (Exception e1) {
                        puerto++;
                    }

                }
            } else if (tipo.equalsIgnoreCase("ph")) {
                puerto = 5566;
                while (puerto <= 5576 && exito == false) {
                    try {
                        tcp = "tcp://*:" + puerto;
                        nuevoPublisher.bind(tcp);
                        exito = true;
                    } catch (Exception e1) {
                        puerto++;
                    }

                }
            } else if (tipo.equalsIgnoreCase("oxigeno")) {
                puerto = 5577;
                while (puerto <= 5587 && exito == false) {
                    try {
                        tcp = "tcp://*:" + puerto;
                        nuevoPublisher.bind(tcp);
                        exito = true;
                    } catch (Exception e1) {
                        puerto++;
                    }

                }
            }
            if (exito == false) {
                System.out.println("Error: Todos los puertos del sensor " + tipo + " estan ocupados");
                System.exit(1);
            }
        } finally {
            if (exito == true) {
                System.out.println("Generando medidas de " + tipo + "..." + "\n");
                while (!Thread.currentThread().isInterrupted()) {
                    // Generar lista de medidas
                    String message = sensor.generarMedida();
                    Thread.sleep(sensor.getTiempo()); // Set the message time period
                    nuevoPublisher.send(message, 0);
                    System.out.println(message);
                }
                nuevoContext.close();
            }
        }

    }

    // Crear archivo de configuración
    public static ArchivoConfiguracion creaArchivoConfiguracion(String filename) {
        // Abrir archivo de texto
        try {
            FileReader fr = new FileReader(filename);
            BufferedReader br = new BufferedReader(fr);
            String linea;

            float[] datosFloat = new float[3];

            for (int i = 0; i < 3; i++) {
                linea = br.readLine();
                datosFloat[i] = Float.parseFloat(linea);
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

}
