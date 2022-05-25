package com.proyectodistribuidos;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GestionarUsuario {

    private ArrayList<UsuarioRegistrado> usuariosRegistrados = new ArrayList<>();
    private ArrayList<UsuarioRegistrado> usuariosAct = new ArrayList<>();
    PasswordHash controladorHash = new PasswordHash();

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
                System.out.println("La contraseña debe tener min 8 caracteres.");
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
        escribirJSON(uRegistrado);
    }

    /**
     * Método que permite almacenar la info. de un usuario registrado en un archivo
     * JSON
     */
    public void escribirJSON(UsuarioRegistrado usuario) {
        try {
            usuariosAct.add(usuario);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Writer writer = Files.newBufferedWriter(Paths.get("usuarios.json"));
            gson.toJson(usuariosAct, writer);
            writer.close();
        } catch (IOException e) {
            System.out.println("Ocurrio un error en el archivo JSON.");
        }
    }

    /**
     * Métodos necesarios para la autenticación del usuario
     */

    public boolean autenticarUsuario(String usuario, String password) {
        ArrayList<UsuarioRegistrado> usuarios = leerJSON();
        if (!usuarios.isEmpty()) {
            for (UsuarioRegistrado uActual : usuarios) {
                if (uActual.getUsuario().equals(usuario)) {
                    try {
                        if (controladorHash.comprobarPassword(password, uActual.getSalt(), uActual.getHash()) == true) {
                            return true;
                        } else {
                            System.out.println("Contrasena incorrecta. ");
                            return false;
                        }
                    } catch (Exception e) {
                        System.out.println("Ocurrio un error en la seguridad de la contraseña.");
                    }

                } else {
                    System.out.println("El usuario ingresado no se ha encontrado en la BD. ");
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * Método que permite obtener los usuarios registrados
     */
    public ArrayList<UsuarioRegistrado> leerJSON() {

        ArrayList<UsuarioRegistrado> usuariosRegistrados = new ArrayList<>();
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Reader reader = Files.newBufferedReader(Paths.get("usuarios.json"));
            usuariosRegistrados = new ArrayList<UsuarioRegistrado>(
                    Arrays.asList(gson.fromJson(reader, UsuarioRegistrado[].class)));
            reader.close();
        } catch (IOException e) {
            System.out.println("Ocurrio un error en el archivo JSON.");
        }
        return usuariosRegistrados;
    }

    /**
     * Método que retorna un boolean cuando el nombre del usuario ya se encuentra en
     * la BD (archivo JSON)
     */
    public boolean existeUsuario(String usuario) {
        if (usuariosRegistrados.isEmpty()) {
            return false;
        } else {
            for (UsuarioRegistrado usuarioRegistrado : usuariosRegistrados) {
                if (usuarioRegistrado.getUsuario().equals(usuario)) {
                    return true;
                }
            }
            return false;
        }
    }

}