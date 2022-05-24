package com.proyectodistribuidos;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

public class SistemaDeCalidad {
    // private static ArrayList<UsuarioRegistrado> usuariosRegistrados = new
    // ArrayList<>();

    /*
     * Menú de opciones:
     * 1. Registrase
     * 2. Iniciar sesión
     */

    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);
        int opcion;
        boolean menu = true;
        GestionarUsuario controladorUsuario = new GestionarUsuario();

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
                    }
                    menu = false;
                    break;
                case 2:
                    System.out.println("\nInicio sesion\n");
                    System.out.print("Ingrese su nombre de usuario: ");
                    String usuario = scan.next();
                    System.out.println("");
                    System.out.print("Ingrese su contraseña: ");
                    String password = scan.next();
                    if (controladorUsuario.autenticarUsuario(usuario, password) == true) {
                        System.out.println("Se ha ingresado existosamente!");
                    }
                    menu = false;
                    break;
                case 3:
                    menu = false;
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
                    }
                    break;
            }
        } while (menu);

    }

    /*
     * public static boolean iniciarSesion(String usuario, String password) {
     * for (UsuarioRegistrado usuarioRegistrado : usuariosRegistrados) {
     * if (usuarioRegistrado.getUsuario().equals(usuario) &&
     * usuarioRegistrado.getPassword().equals(password)) {
     * return true;
     * }
     * }
     * return false;
     * }
     */

    public void imprimirAlarma(String infoAlarma) {
        System.out.println(infoAlarma);
    }
}
