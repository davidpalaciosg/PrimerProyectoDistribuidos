package com.proyectodistribuidos;

import java.util.StringTokenizer;
import java.io.*;

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
        

        try {
            ZMQ.Context context =  ZMQ.context(1);
            // Socket SUB
            ZMQ.Socket subscriber = context.socket(SocketType.SUB);
            
            //Conectar a puertos por tipo
            System.out.println("Intentando conectar...");
            conectarSocket(direccion, tipo, subscriber);

            //Subscribir por tipo
            subscriber.subscribe("".getBytes());
            
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
                
                

            }
            context.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            System.exit(1);
        }

    }

    private static void conectarSocket(String direccion, String tipo, ZMQ.Socket subscriber)
    {
        if(tipo.equalsIgnoreCase("temperatura"))
        {
            subscriber.connect("tcp://localhost:5555");
            //Rango de 5555 a 5565
            /*for (int i = 5555; i <= 5565; i++) {
                String tcp = "tcp://" + direccion + ":" + i;
                subscriber.connect(tcp);
            }*/
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
