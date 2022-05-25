package com.proyectodistribuidos;
public class UsuarioRegistrado {
    private String usuario;
    private String hash;
    private String salt;

    public UsuarioRegistrado(String usuario, String hash, String salt) {
        this.usuario = usuario;
        this.hash = hash;
        this.salt = salt;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getSalt (){
        return salt;
    }

    public void setSalt(String salt){
        this.salt = salt;
    }

    @Override
    public String toString() {
        return "name=" + usuario + 
                ", hash=" + hash + 
                ", salt=" + salt;
    }
    
}
