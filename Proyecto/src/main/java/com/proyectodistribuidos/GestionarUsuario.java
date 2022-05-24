package com.proyectodistribuidos;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class GestionarUsuario{

    private ArrayList<UsuarioRegistrado> usuariosRegistrados = new ArrayList<>();
    PasswordHash controladorHash = new PasswordHash();

    public boolean registrarUsuario(String nombreUsuario, String passwordIngresada){
        if (!existeUsuario(nombreUsuario)){
            if (passwordIngresada.length() >= 8){
                try {
                    String passwordHash = controladorHash.crearPasswordHash(passwordIngresada);
                    StringTokenizer token = new StringTokenizer(passwordHash, ": ");
                    token.nextToken();
                    String salt = token.nextToken();
                    String hash = token.nextToken();
                    registroUsuario(nombreUsuario, hash, salt);
                    return true;
                }catch (Exception e){
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

    public boolean autenticarUsuario(){
        return true;
    }

    public void registroUsuario(String usuario, String hash, String salt){
        UsuarioRegistrado uRegistrado = new UsuarioRegistrado(usuario, hash, salt);
        //usuariosRegistrados.add(uRegistrado);
        //Acá se debe guardar el usuario en el archivo

    }

    /**
     * Método que retorna un boolean cuando el nombre del usuario ya se encuentra en la BD (archivo JSON)
     */
    public boolean existeUsuario(String usuario){
        if (usuariosRegistrados.isEmpty()){
            return false;
        } else {
            for (UsuarioRegistrado usuarioRegistrado : usuariosRegistrados) {
                if (usuarioRegistrado.getUsuario().equals(usuario)){
                    return true;
                }
            }
            return false;
        }
    }


    public void crearJSON (UsuarioRegistrado usuario){
        
    }

}