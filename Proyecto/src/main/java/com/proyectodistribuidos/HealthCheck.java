package com.proyectodistribuidos;

import org.zeromq.SocketType;
import org.zeromq.ZMQ;

public class HealthCheck {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Debe ingresar al menos la dirección IP de un servidor de monitores");
            System.exit(1);
        }

        ZMQ.Context context = ZMQ.context(1);
        // Socket subscriber de monitores
        ZMQ.Socket subscriber = context.socket(SocketType.SUB);

        try {
            // Conectar a todos los monitores que lleguen por parámetro
            System.out.println("Intentando conectar a monitores");
            for (int i = 0; i < args.length; i++) {
                if(isIPV4(args[i])) //Si es una dirección IP bien escrita
                {
                    System.out.println("Conectando a " + args[i]);
                    suscribirseAMonitor(args[i], subscriber); //Conectarse a todos los monitores
                }
            }
            subscriber.subscribe("".getBytes());


        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());

        }
        finally{
            while ((!Thread.currentThread().isInterrupted())) {
                String string = subscriber.recvStr();
                System.out.println(string);

                // Enviar info a Health Check
            }
    
        }

    }

    private static void suscribirseAMonitor(String direccion, ZMQ.Socket subscriber) {
        // Rango de 5585 a 5600
        for (int i = 5585; i <= 5600; i++) {
            String tcp = "tcp://" + direccion + ":" + i;
            subscriber.connect(tcp);
        }
    }

    

    private static boolean isIPV4(String ip) {
        String IPV4_PATTERN = "^(([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])(\\.(?!$)|$)){4}$";
        if(ip.matches(IPV4_PATTERN) || ip.equals("localhost"))
        {
            return true;
        }
        return false;
    }

}
