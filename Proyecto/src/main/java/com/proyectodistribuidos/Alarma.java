package com.proyectodistribuidos;

public class Alarma {

    private String tipo;
    private int dato;

    public Alarma(String tipo, int dato) {
        this.tipo = tipo;
        this.dato = dato;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getDato() {
        return dato;
    }

    public void setDato(int dato) {
        this.dato = dato;
    }

    @Override
    public String toString() {
        String res = "";
        res += tipo + ": " + dato;
        return res;
    }

}
