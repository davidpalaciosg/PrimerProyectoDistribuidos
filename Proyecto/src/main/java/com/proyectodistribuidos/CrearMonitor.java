package com.proyectodistribuidos;

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
            System.out.println("Error: Número de argumentos incorrecto");
            System.exit(1);
        }
        String tipo = args[0].toLowerCase();
        String direccionCS = args[1]; //dirección CrearSensor
        String direccionSC = args[2]; //dirección SistemaDeCalidad
        
        /**
         * Contexto de canal de comunicación con los sensores y el sistema de Calidad
         */
        try {
            ZMQ.Context context =  ZMQ.context(1);
            
            /**
            * Contexto de canal de comunicación con CrearSensor
            */
            // Socket SUB (conexión con sensores)
            ZMQ.Socket subscriber = context.socket(SocketType.SUB);
            
            //Conectar a puertos por tipo
            System.out.println("Intentando conectar...");
            conectarSocket(direccionCS, tipo, subscriber);

            //Subscribir por tipo
            subscriber.subscribe("".getBytes());
            
            /**
            * Contexto de canal de comunicación con el Sistema de calidad
            */
            ZMQ.Socket push = context.socket(SocketType.PUSH);
            push.connect("tcp://" + direccionSC + ":5601");

            while ((!Thread.currentThread().isInterrupted())) {
                String string = subscriber.recvStr().trim();
                System.out.println(string);
                StringTokenizer token = new StringTokenizer(string, " ");
                String tipoA = token.nextToken();
                float dato = Float.valueOf(token.nextToken());
                if (dato >= 0){
                    String fileName = "Medidas.txt";  
                    try{
                        FileWriter fstream = new FileWriter(fileName,true);
                        BufferedWriter out = new BufferedWriter(fstream);
                        out.write(string + "\n");
                        out.close();
                    }
                    catch (IOException e){
                        System.out.println("Ha ocurrido un error. ");
                        e.printStackTrace();
                    }
                }
                
                push.send("Conexiónnnnnn xd");

            }
            context.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Método para establecer las conexiones con CrearSensor
     */

    private static void conectarSocket(String direccion, String tipo, ZMQ.Socket subscriber)
    {
        if(tipo.equalsIgnoreCase("temperatura"))
        {
            //Rango de 5555 a 5565
            for (int i = 5555; i <= 5565; i++) {
                String tcp = "tcp://" + direccion + ":" + i;
                subscriber.connect(tcp);
            }
        }
        else if(tipo.equalsIgnoreCase("ph"))
        {
            //Rango de 5566 a 5576
            for (int i = 5566; i <= 5576; i++) {
                String tcp = "tcp://" + direccion + ":" + i;
                subscriber.connect(tcp);
            }
        }
        else if(tipo.equalsIgnoreCase("oxigeno"))
        {
            //Rango de 5577 a 5587
            for (int i = 5577; i <= 5587; i++) {
                String tcp = "tcp://" + direccion + ":" + i;
                subscriber.connect(tcp);
            }
        }
    }    
}
