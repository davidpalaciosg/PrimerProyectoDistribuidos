package com.proyectodistribuidos;

import org.zeromq.SocketType;
import org.zeromq.ZMQ;

public class CrearMonitor {
    /*
     * args:
     * [0]: tipo de monitor
     * [1] : dirección ip del publisher
     * 
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Error: Numero de argumentos incorrecto");
            System.exit(1);
        }
        String tipo = args[0].toLowerCase();
        String direccion = args[1];
        boolean exito = false;
        String tcp = "";
        String ipc = "";
        int puerto=0;
        ZMQ.Context context = ZMQ.context(1);
        // Socket publisher hacia el Health Check
        ZMQ.Socket nuevoPublisherHealth = context.socket(SocketType.PUB);
        // Socket subscriber de sensor
        ZMQ.Socket subscriber = context.socket(SocketType.SUB);

        try {
            // Conectar a puertos por tipo
            System.out.println("Intentando conectar...");
            conectarSocket(direccion, tipo, subscriber);

            // Subscribir por tipo
            subscriber.subscribe("".getBytes());

            // Socket publisher hacia el Health Check
            tcp = "tcp://*:5585";
            nuevoPublisherHealth.bind(tcp);
            ipc = "ipc://" + tipo;
            nuevoPublisherHealth.bind(ipc);
            exito = true;

        } catch (Exception e) {

            // Intentar abrir el publisher hacia el Health Check desde otro puerto
            
            puerto = 5585;
            //Desde el puerto 5585 al 5600 se crean los publishers de Monitores hacia el HealthCheck
            while (puerto <= 5600 && exito == false) {
                try {
                    tcp = "tcp://*:" + puerto;
                    nuevoPublisherHealth.bind(tcp);
                    //nuevoPublisherHealth.bind(ipc);
                    exito = true;
                } catch (Exception e1) {
                    puerto++;
                }
            }
        } finally {
            while ((!Thread.currentThread().isInterrupted())) {
                String string = subscriber.recvStr();
                System.out.println(string);
                //Publicar hacia el Health Check
                if(exito==true)
                {
                    System.out.println("Publicando hacia el Health Check mediante el puerto "+puerto);
                    nuevoPublisherHealth.send(string, 0);
                }

                // Enviar info a Health Check
            }
            subscriber.close();
            nuevoPublisherHealth.close();
            context.close();
        }

    }

    private static void conectarSocket(String direccion, String tipo, ZMQ.Socket subscriber) {
        if (tipo.equalsIgnoreCase("temperatura")) {
            // Rango de 5555 a 5565
            for (int i = 5555; i <= 5565; i++) {
                String tcp = "tcp://" + direccion + ":" + i;
                subscriber.connect(tcp);
            }
        } else if (tipo.equalsIgnoreCase("ph")) {
            // Rango de 5566 a 5576
            for (int i = 5566; i <= 5576; i++) {
                String tcp = "tcp://" + direccion + ":" + i;
                subscriber.connect(tcp);
            }
        } else if (tipo.equalsIgnoreCase("oxigeno")) {
            // Rango de 5577 a 5587
            for (int i = 5577; i <= 5587; i++) {
                String tcp = "tcp://" + direccion + ":" + i;
                subscriber.connect(tcp);
            }
        }
    }

}
