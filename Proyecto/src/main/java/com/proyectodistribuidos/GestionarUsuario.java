package com.proyectodistribuidos;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.StringTokenizer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GestionarUsuario {

    private ArrayList<UsuarioRegistrado> usuariosRegistrados = new ArrayList<>();
    private ArrayList<UsuarioRegistrado> usuariosAct = new ArrayList<>();
    PasswordHash controladorHash = new PasswordHash();

    /**
     * Método que guarda en un arraylist los usuarios autorizados
     */
    public void leerUsuarios() {
        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;
        // Abrir archivo de texto
        try {
            archivo = new File("usuarios.txt");
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);
            String linea;
            while ((linea = br.readLine()) != null) {
                StringTokenizer token = new StringTokenizer(linea, ",");
                UsuarioRegistrado usuarioAct = new UsuarioRegistrado();
                usuarioAct.setUsuario(token.nextToken());
                usuarioAct.setHash(token.nextToken());
                usuarioAct.setSalt(token.nextToken());
                usuariosRegistrados.add(usuarioAct);
            }
            br.close();
            fr.close();
        } catch (Exception e) {
            System.out.println("Error al leer el archivo de configuración");
        }
    }

    /**
     * Método que permite autenticar un usuario registrado.
     */
    public boolean autenticarUsuario(String usuario, String password) {
        if (!usuariosRegistrados.isEmpty()) {
            for (UsuarioRegistrado uActual : usuariosRegistrados) {
                if (uActual.getUsuario().equalsIgnoreCase(usuario)) {
                    try {
                        if (controladorHash.comprobarPassword(password, uActual.getSalt(), uActual.getHash()) == true) {
                            return true;
                        } else {
                            System.out.println("Contraseña incorrecta. ");
                            return false;
                        }
                    } catch (Exception e) {
                        System.out.println("Ocurrió un error en la seguridad de la contraseña.");
                    }

                }
            }
            System.out.println("El usuario ingresado no se ha encontrado en la BD. ");
            return false;
        }
        return false;
    }


    /**
     * Método que permite el registro de un usuario
     */
    public boolean registrarUsuario(String nombreUsuario, String passwordIngresada) {
        if (!existeUsuario(nombreUsuario)) {
            if (passwordIngresada.length() >= 8) {
                try {
                    String passwordHash = controladorHash.crearPasswordHash(passwordIngresada);
                    StringTokenizer token = new StringTokenizer(passwordHash, ": ");
                    token.nextToken();
                    String salt = token.nextToken();
                    String hash = token.nextToken();
                    registroUsuario(nombreUsuario, hash, salt);
                    return true;
                } catch (Exception e) {
                    System.out.println("Ocurrio un error en la seguridad de la contraseña.");
                }
            } else {
                System.out.println("La contraseña debe tener mín. 8 caracteres.");
                return false;
            }
        } else {
            System.out.println("Nombre de usuario existente. ");
            return false;
        }
        return true;
    }

    public void registroUsuario(String usuario, String hash, String salt) {
        UsuarioRegistrado uRegistrado = new UsuarioRegistrado(usuario, hash, salt);
        escribirUsuario(uRegistrado.toString());
    }

    /**
     * Método que escribe en el archivo de txt el usuario nuevo.
     */
    public void escribirUsuario(String usuario){
        String fileName = "usuarios.txt";
        try {
            FileWriter fstream = new FileWriter(fileName, true);
            BufferedWriter out = new BufferedWriter(fstream);
            out.write(usuario + "\n");
            out.close();
        } catch (IOException e) {
            System.out.println("Ha ocurrido un error. ");
            e.printStackTrace();
        }
    } 

     /**
     * Método que retorna un boolean cuando el nombre del usuario ya se encuentra en
     * la BD (archivo txt)
     */
    public boolean existeUsuario(String usuario) {
        if (usuariosRegistrados.isEmpty()) {
            return false;
        } else {
            for (UsuarioRegistrado usuarioRegistrado : usuariosRegistrados) {
                if (usuarioRegistrado.getUsuario().equalsIgnoreCase(usuario)) {
                    return true;
                }
            }
            return false;
        }
    }


}