package com.proyectodistribuidos;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

public class SistemaDeCalidad {
    private static ArrayList<UsuarioRegistrado> usuariosRegistrados = new ArrayList<>();

    /*
     * Menú de opciones:
     * 1. Registrase
     * 2. Iniciar sesión
     */

    public static void main(String[] args) {
        
        Scanner scan = new Scanner(System.in);
        int opcion;
        GestionarUsuario controladorUsuario = new GestionarUsuario();
        

        System.out.println("Bienvenido al Sistema de Calidad");
        System.out.println("Menu de opciones:");
        System.out.println("1. Registrase");
        System.out.println("2. Iniciar sesion");
        System.out.println("3. Salir");
        System.out.print("Ingrese la opcion que desea realizar: ");
        
        opcion = scan.nextInt();
        System.out.println("\n");
        do{
            switch (opcion){
                case 1: 
                    System.out.println("\nRegistro\n");
                    System.out.print("Ingrese su nombre de usuario: ");
                    String nombreUsuario = scan.next();
                    System.out.println("");
                    System.out.print("Ingrese la contraseña (recuerde que debe tener min. 8 caracteres): ");
                    String passwordIngresada = scan.next();
                    if (controladorUsuario.registrarUsuario(nombreUsuario, passwordIngresada)){
                        System.out.println("El registro se realizo correctamente. ");
                    }                    
                case 2:
                    
                case 3:
                    System.exit(0);
                default:
                    System.out.println("La opcion ingresada no es valida.");
            }
        } while ((opcion == 1) || (opcion == 2));
        

    }

    /*public static boolean iniciarSesion(String usuario, String password) {
        for (UsuarioRegistrado usuarioRegistrado : usuariosRegistrados) {
            if (usuarioRegistrado.getUsuario().equals(usuario) && usuarioRegistrado.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }*/
  


    public void imprimirAlarma(String infoAlarma)
    {
        System.out.println(infoAlarma);
    } 
}
