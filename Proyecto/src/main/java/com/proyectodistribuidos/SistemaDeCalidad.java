package com.proyectodistribuidos;
import java.util.ArrayList;

public class SistemaDeCalidad {
    private ArrayList<UsuarioRegistrado> usuariosRegistrados;

    public SistemaDeCalidad() {
        this.usuariosRegistrados = new ArrayList<UsuarioRegistrado>();
    }

    public boolean iniciarSesion(String usuario, String password) {
        for (UsuarioRegistrado usuarioRegistrado : usuariosRegistrados) {
            if (usuarioRegistrado.getUsuario().equals(usuario) && usuarioRegistrado.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }
    public void imprimirAlarma(String infoAlarma)
    {
        System.out.println(infoAlarma);
    }
    //TODO Leer de archivo de texto de usuarios
    public ArrayList<UsuarioRegistrado> leerUsuariosRegistrados(String filename) {
        return null;
    }
    //TODO Obtener alarmas de monitores
    public String obtenerAlarmas(){
        return null;
    }

    
}
