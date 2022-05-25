package com.proyectodistribuidos;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Alarma {

    private String tipo;
    private float dato;
    private Date horaDeProduccion;

    public Alarma(String tipo, float dato, Date horaDeProduccion) {
        this.tipo = tipo;
        this.dato = dato;
        this.horaDeProduccion = horaDeProduccion;
    }

    public Date getHoraDeProduccion() {
        return horaDeProduccion;
    }

    public void setHoraDeProduccion(Date horaDeProduccion) {
        this.horaDeProduccion = horaDeProduccion;
    }

    public Alarma() {

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
        // Obtener el tiempo actual
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss.SSS");
        
        String res = "";
        res += tipo + " " + dato+" "+formatter.format(this.horaDeProduccion);
        return res;
    }

}
