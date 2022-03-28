package com.proyectodistribuidos;

import org.zeromq.SocketType;
import org.zeromq.ZMQ;
import org.zeromq.ZContext;

public class CrearMonitor {
    /*
     * args:
     * [0]: tipo de monitor
     * [1] : direccion ip:puerto del publisher
     * 
     */
    public static void main(String[] args) {

        if(args.length != 2) {
            System.out.println("Error: Numero de argumentos incorrecto");
            System.exit(1);
        }
        String tipo = args[0].toLowerCase();
        String direccion = args[1];

        try {
            ZContext context = new ZContext();
            // Socket SUB
            ZMQ.Socket subscriber = context.createSocket(SocketType.SUB);
            // Socket conectado al puerto
            // Crea la conexión
            subscriber.connect("tpc://localhost:5556");
           // subscriber.connect("ipc://" + tipo);
           tipo+=" ";
            System.out.println("Canal creado: "+tipo);
            //Suscribirse
            subscriber.subscribe(tipo.getBytes(ZMQ.CHARSET));

            while(!Thread.currentThread().isInterrupted())
			{
                String string = subscriber.recvStr(0).trim();
                System.out.println(string);
            }
            context.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            System.exit(1);
        }

    }

}
