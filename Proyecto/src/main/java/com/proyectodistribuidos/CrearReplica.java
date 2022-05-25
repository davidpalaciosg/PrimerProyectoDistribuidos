package com.proyectodistribuidos;

import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;
import java.io.*;

import org.zeromq.SocketType;
import org.zeromq.ZMQ;

public class CrearReplica {
    /**
     * La clase mantiene una conexión con el Healthcheck
     * Comunicación con Healthcheck (Push - Pull)
     * En esta actúa como Pull (Receptor, solo se encarga de recibir)
     * 
     * Comunicación con CrearSensor (Publisher - Subscriber):
     * En esta, actúa como Subscriber (SUB)
     * 
     * Comunicación con SistemaDeCalidad (Push - Pull)
     * En esta actúa como Push (emisor, solo se encarga de enviar)
     *
     * 
     */

    // Conexiones
    private static ZMQ.Context context;
    private static ZMQ.Socket nuevoSubHealthCheck;
    private static ZMQ.Socket subscriber;
    private static ZMQ.Socket pushCalidad;

    // Variables del HealthCheck
    private static String tcpHealthCheck = "";
    private static String ipSensor = "";
    private static String tipoMonitor = "";
    private static String ipSistemaDeCalidad = "";

    // Lista que contiene los sensores conectados
    private static ArrayList<String> sensoresConectados;

    public static void main(String[] args) {

        context = ZMQ.context(1);
        sensoresConectados = new ArrayList<String>();

        try {
            // Conectar a puertos por tipo

            // Socket PULL desde el Health Check
            nuevoSubHealthCheck = context.socket(SocketType.SUB);
            // Socket subscriber de sensor
            subscriber = context.socket(SocketType.SUB);
            // Socket push hacia Sistema de Calidad
            pushCalidad = context.socket(SocketType.PUSH);

            /**
             * Contexto de canal de comunicación con los sensores y el sistema de Calidad
             */
            try {

                System.out.println("Inciando Monitor Replica");
                System.out.println("Esperando mensaje de Health Check");
                // Socket publisher hacia el Health Check
                tcpHealthCheck = "tcp://*:5610";
                nuevoSubHealthCheck.connect(tcpHealthCheck);
                nuevoSubHealthCheck.subscribe("".getBytes());

                // Socket push hacia Sistema de Calidad
                pushCalidad.connect("tcp://" + ipSistemaDeCalidad + ":5601");

            } catch (Exception e) {

                // Intentar abrir el publisher hacia el Health Check desde otro puerto

                /*
                 * if (!exitoPushCalidad) {
                 * puerto = 5601;
                 * while (puerto <= 5610 && exitoPushCalidad == false) {
                 * try {
                 * push.connect("tcp://" + direccionSC + ":" + puerto);
                 * exitoPushCalidad = true;
                 * } catch (Exception e1) {
                 * puerto++;
                 * }
                 * }
                 * }
                 */

            }
        } finally {

            /**
             * Contexto de canal de comunicación con el Sistema de calidad
             */
            // Socket push hacia Sistema de Calidad
            pushCalidad.connect("tcp://" + ipSistemaDeCalidad + ":5601");

            while ((!Thread.currentThread().isInterrupted())) {

                String msgHealtcheck = nuevoSubHealthCheck.recvStr();
                // Conectar a un nuevo monitor
                if (!msgHealtcheck.isEmpty()) {
                    System.out.println("Mensaje recibido: " + msgHealtcheck);
                    String[] parts = msgHealtcheck.split(" ");
                    ipSensor = parts[0];
                    tipoMonitor = parts[1];
                    ipSistemaDeCalidad = parts[2];

                    // Conexión con los sensores
                    int indexTipo = getSensorConectado(tipoMonitor);
                    // Si no ha sido conectado se conecta
                    if (indexTipo == -1) {
                        System.out.println("Intentando conectar a " + ipSensor + ": " + tipoMonitor);
                        conectarSocket(ipSensor, tipoMonitor, subscriber);
                        sensoresConectados.add(tipoMonitor);
                        //Hilos que reciben los mensajes de los sensores
                        Runnable hilo = crearHilo(ipSensor, tipoMonitor, subscriber);
                        new Thread(hilo).start();
                    }
                }

            }

            subscriber.close();
            nuevoSubHealthCheck.close();
            pushCalidad.close();
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
        Date date = new Date();
        Alarma alarmaG=null;
        StringTokenizer token = new StringTokenizer(tipo, ": ");
        String tipoS = token.nextToken();
        if (tipoS.equalsIgnoreCase("temperatura")) {
            if (!(dato>=68 && dato>=89)) {// fuera de rango
                alarmaG = new Alarma(tipo, dato,date);
            }
        } else if (tipoS.equalsIgnoreCase("oxigeno")) {
            if (!(dato>=6 && dato>=8)) {// fuera de rango
                alarmaG = new Alarma(tipo, dato,date);
            }
        } else if (tipoS.equalsIgnoreCase("ph")) {
            if (!(dato>=2 &&dato>=11)) {// fuera de rango
                alarmaG = new Alarma(tipo, dato,date);
            }
        }
        return alarmaG;
    }

    private static int getSensorConectado(String tipo) {
        for (int i = 0; i < sensoresConectados.size(); i++) {
            if (sensoresConectados.get(i).equalsIgnoreCase(tipo)) {
                return i;
            }
        }
        return -1;
    }

    private static Runnable crearHilo(String ipSensor, String tipo, ZMQ.Socket subscriber) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // Esto se ejecuta en segundo plano una única vez
                while (true) {
                    try {
                        // En él, hacemos que el hilo duerma
                        Thread.sleep(1000);
                        // Y después realizamos las operaciones

                        subscriber.subscribe("".getBytes());
                        String string = subscriber.recvStr().trim();
                        System.out.println(string);

                        // Escribe en el archivo de Medidas correctas y fuera de rango
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
                        // Genera alarma y envía al sistema de calidad
                        Alarma alarma = alarmaGenerada(tipoA, dato);
                        if (alarma!= null) {
                            pushCalidad.send(alarma.toString());
                        }

                        // Así, se da la impresión de que se ejecuta cada cierto tiempo
                        // Thread.currentThread().interrupt();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        return runnable;
    }

}
