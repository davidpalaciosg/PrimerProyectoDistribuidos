package com.proyectodistribuidos;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.StringTokenizer;
import java.io.*;

import org.zeromq.SocketType;
import org.zeromq.ZMQ;

public class CrearMonitor {
    /**
     * La clase mantiene una conexión con CrearSensor y con SistemaDeCalidad
     * Comunicación con CrearSensor (Publisher - Subscriber):
     * ** En esta, actúa como Subscriber (SUB)
     * Comunicación con SistemaDeCalidad (Push - Pull)
     * ** En esta actúa como Push (emisor, solo se encarga de enviar)
     * args:
     * [0]: tipo de monitor
     * [1] : dirección ip del publisher
     * [2] : dirección ip del pull (Sistema de calidad)
     */

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Error: Numero de argumentos incorrecto");
            System.exit(1);
        }
        String tipo = args[0].toLowerCase();
        String direccion = args[1];
        String direccionSC = args[2]; // dirección SistemaDeCalidad

        boolean exitoPubHealth = false;
        String tcp = "";
        String ipc = "";
        int puerto = 0;
        ZMQ.Context context = ZMQ.context(1);
        // Socket publisher hacia el Health Check
        ZMQ.Socket nuevoPublisherHealth = context.socket(SocketType.PUB);
        // Socket subscriber de sensor
        ZMQ.Socket subscriber = context.socket(SocketType.SUB);

        // Socket push hacia Sistema de Calidad
        ZMQ.Socket push = context.socket(SocketType.PUSH);
        boolean exitoPushCalidad = false;

        try {
            // Conectar a puertos por tipo

            if (args.length != 3) {
                System.out.println("Error: Número de argumentos incorrecto");
                System.exit(1);
            }            

            /**
             * Contexto de canal de comunicación con los sensores y el sistema de Calidad
             */
            try {

                /**
                 * Contexto de canal de comunicación con CrearSensor
                 */

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
                exitoPubHealth = true;

                // Socket push hacia Sistema de Calidad
                push.connect("tcp://" + direccionSC + ":5601");
                exitoPushCalidad = true;

            } catch (Exception e) {

                // Intentar abrir el publisher hacia el Health Check desde otro puerto

                if (!exitoPubHealth) {
                    puerto = 5585;
                    // Desde el puerto 5585 al 5600 se crean los publishers de Monitores hacia el
                    // HealthCheck
                    while (puerto <= 5600 && exitoPubHealth == false) {
                        try {
                            tcp = "tcp://*:" + puerto;
                            nuevoPublisherHealth.bind(tcp);
                            // nuevoPublisherHealth.bind(ipc);
                            exitoPubHealth = true;
                        } catch (Exception e1) {
                            puerto++;
                        }
                    }
                    /*
                    if (!exitoPushCalidad) {
                        puerto = 5601;
                        while (puerto <= 5610 && exitoPushCalidad == false) {
                            try {
                                push.connect("tcp://" + direccionSC + ":" + puerto);
                                exitoPushCalidad = true;
                            } catch (Exception e1) {
                                puerto++;
                            }
                        }
                    }
                    */
                }
            }
        } finally {

            /**
             * Contexto de canal de comunicación con el Sistema de calidad
             */

            while ((!Thread.currentThread().isInterrupted())) {
                String string = subscriber.recvStr().trim();
                System.out.println(string);

                //Escribe en el archivo de Medidas correctas y fuera de rango
                StringTokenizer token = new StringTokenizer(string, " ");
                String tipoA = token.nextToken();
                float dato = Float.valueOf(token.nextToken());
                if (dato >= 0) {
                    String fileName = "Medidas.txt";
                    try {
                        FileWriter fstream = new FileWriter(fileName, true);
                        BufferedWriter out = new BufferedWriter(fstream);
                        out.write(string + "\n");
                        out.close();
                    } catch (IOException e) {
                        System.out.println("Ha ocurrido un error. ");
                        e.printStackTrace();
                    }
                }
                //Genera alarma y envía al sistema de calidad
                Alarma alarma = alarmaGenerada(tipoA, dato);
                if (alarma.getTipo() != null) {
                    push.send(alarma.toString());
                }

                // Publicar hacia el Health Check
                if (exitoPubHealth == true) {
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
                    LocalDateTime now = LocalDateTime.now();
                    //System.out.println("Publicando hacia el Health Check mediante el puerto " + puerto);
                    long pid = ProcessHandle.current().pid();
                    String msg = pid + " " + tipo + " " + dtf.format(now).toString() + " " + direccion+" "+direccionSC;
                    //System.out.println("Publicando: " + msg);
                    nuevoPublisherHealth.send(msg, 0);
                }
            }

            subscriber.close();
            nuevoPublisherHealth.close();
            push.close();
            context.close();
        }
    }

    /**
     * Método para establecer las conexiones con CrearSensor
     */
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

    private static Alarma alarmaGenerada(String tipo, float dato) {
        Alarma alarmaG = new Alarma(null, -1000);
        if (tipo.equalsIgnoreCase("temperatura")) {
            if ((dato < 68) || (dato > 89)||dato<0) {// fuera de rango
                alarmaG = new Alarma(tipo, dato);
            }
        } else if (tipo.equalsIgnoreCase("oxigeno")) {
            if ((dato < 6) || (dato > 8)||dato<0) {// fuera de rango
                alarmaG = new Alarma(tipo, dato);
            }
        } else if (tipo.equalsIgnoreCase("ph")) {
            if ((dato < 2) || (dato > 11)||dato<0) {// fuera de rango
                alarmaG = new Alarma(tipo, dato);
            }
        }
        return alarmaG;
    }

}
