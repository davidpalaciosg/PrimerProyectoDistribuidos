package com.proyectodistribuidos;

public class Alarma {

    private String tipo;
    private float dato;

    public Alarma(String tipo, float dato) {
        this.tipo = tipo;
        this.dato = dato;
    }

    public Alarma(){

    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public float getDato() {
        return dato;
    }

    public void setDato(float dato) {
        this.dato = dato;
    }

    @Override
    public String toString() {
        String res = "";
        res += tipo + ": " + dato;
        return res;
    }

}
