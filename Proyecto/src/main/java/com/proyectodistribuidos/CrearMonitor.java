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
            // Socket conectado al puerto
            // Crea la conexión

            String tcp = "tcp://" + direccion;
            subscriber.connect(tcp);

            String ipc = "ipc://" + tipo;
            subscriber.bind(ipc);

            tipo+=" ";
            System.out.println("Intentando conectar...");
            

            //Subscribir
            subscriber.subscribe("".getBytes());
            

            while (true) {
                String string = subscriber.recvStr();
                System.out.println(string);
            }
            //context.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            System.exit(1);
        }

    }

}
