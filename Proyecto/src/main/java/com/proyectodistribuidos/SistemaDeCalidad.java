package com.proyectodistribuidos;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import javax.naming.Context;

import org.zeromq.SocketType;
import org.zeromq.ZMQ;

public class SistemaDeCalidad {
    // private static ArrayList<UsuarioRegistrado> usuariosRegistrados = new
    // ArrayList<>();

    /**
     * Comunicación con CrearMonitor (Push - Pull)
     * **Actúa como Pull (receptor, solo se encarga de recibir)
     * Tiene un menú de opciones:
     * ** 1. Registrarse
     * ** 2. Iniciar sesión
     * ** 3. Salir
     */

    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);
        int opcion;
        boolean menu = true;
        GestionarUsuario controladorUsuario = new GestionarUsuario();
        controladorUsuario.leerUsuarios();

        do {
            System.out.println("\n\n Bienvenido al Sistema de Calidad");
            System.out.println("\t 1. Registrarse");
            System.out.println("\t 2. Iniciar sesión");
            System.out.println("\t 3. Salir");
            System.out.print("\t Ingrese la opción que desea realizar: ");
            opcion = scan.nextInt();
            System.out.println("\n");
            switch (opcion) {
                case 1:
                    System.out.println("\nRegistro\n");
                    System.out.print("\t Ingrese su nombre de usuario: ");
                    String nombreUsuario = scan.next();
                    System.out.println("");
                    System.out.print("\t Ingrese la contraseña (recuerde que debe tener min. 8 caracteres): ");
                    String passwordIngresada = scan.next();
                    if (controladorUsuario.registrarUsuario(nombreUsuario, passwordIngresada)) {
                        System.out.println("El registro se realizo correctamente. ");
                        menu = false;
                    } else {
                        System.exit(1);
                    }
                    break;
                case 2:
                    System.out.println("\nInicio sesión\n");
                    System.out.print("Ingrese su nombre de usuario: ");
                    String usuario = scan.next();
                    System.out.println("");
                    System.out.print("Ingrese su contraseña: ");
                    String password = scan.next();
                    if (controladorUsuario.autenticarUsuario(usuario, password) == true) {
                        System.out.println("Se ha ingresado existosamente!");
                        menu = false;
                    } else {
                        //System.out.println("Recuerde que solo los usuarios autorizados pueden acceder.");
                        System.exit(1);
                    }
                    break;
                case 3:
                    menu = false;
                    System.exit(0);
                    break;
                default:
                    System.out.println("\n\t\t La opción " + opcion + " no es válida :( \n");
                    System.out.print("\t\t\t Desea visualizar las opciones nuevamente? (S/N): ");
                    String respuesta = scan.next();
                    if ((respuesta.equals("S")) || (respuesta.equals("SI"))
                            || (respuesta.equals("Si") || (respuesta.equals("s")))) {
                        menu = true;
                    } else {
                        menu = false;
                        System.exit(1);
                    }
                    break;
            }
        } while (menu);

        /**
         * Contexto de canal de comunicación con el Monitor (CrearSensor)
         */
        try {
            ZMQ.Context context = ZMQ.context(1);
            ZMQ.Socket pull = context.socket(SocketType.PULL);
            pull.bind("tcp://*:5601");
            while ((!Thread.currentThread().isInterrupted())) {
                String string = pull.recvStr().trim();
                String parts[] = string.split(" ");
                String tipo = parts[0];
                float dato = Float.parseFloat(parts[1]);
                String tiempo = parts[2];
                Medida nuevaMedida = new Medida(dato,tipo);
                if(!nuevaMedida.verificarMedida())
                {
                    System.out.println(string);
                }

                try {
                    calcularYAgregarTiempoLlegadaMonitor(tiempo, tipo);
                } catch (Exception e) {
                    System.out
                            .println("Error al calcular el tiempo entre la salida del monitor y la entrada al sistema de calidad");
                }
            }

            pull.close();
            context.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            System.exit(1);
        }
        scan.close();
    }

    public void imprimirAlarma(String infoAlarma) {
        System.out.println(infoAlarma);
    }

    private static void calcularYAgregarTiempoLlegadaMonitor(String tiempoSensor, String tipo) {


        // Obtener el tiempo actual
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss.SSS");

        String fileName = "tiemposDeLlegadaMonitorSistemaCalidad" + tipo + ".txt";
        try {
            // Convertir el tiempo del sensor a Date
            Date fechaSensor = formatter.parse(tiempoSensor);
            String fechaNowString = formatter.format(new Date());
            Date fechaNow = formatter.parse(fechaNowString);
            // System.out.println(fechaNow.getTime());
            // System.out.println(fechaSensor.getTime());
            float diference = Math.abs(fechaNow.getTime() - fechaSensor.getTime());
            FileWriter fstream = new FileWriter(fileName, true);
            BufferedWriter out = new BufferedWriter(fstream);
            out.write(tipo + " " + diference + "\n");
            out.close();
        } catch (Exception e) {
            System.out.println("Ha ocurrido un error. ");
            e.printStackTrace();
        }
    }

}
