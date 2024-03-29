package com.proyectodistribuidos;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Medida {
    private float dato;
    private String tipo;
    private Date horaDeProduccion;

    public Medida(float dato, String tipo) {
        this.dato = dato;
        this.tipo = tipo;
        this.horaDeProduccion = new Date();
    }

    public float getDato() {
        return dato;
    }

    public void setDato(int dato) {
        this.dato = dato;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Date getHoraDeProduccion() {
        return horaDeProduccion;
    }

    public void setHoraDeProduccion(Date horaDeProduccion) {
        this.horaDeProduccion = horaDeProduccion;
    }

    @Override
    public String toString() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss.SSS");
        String res = "";
        if (tipo.equalsIgnoreCase("temperatura")) {
            res += "Temperatura: " + dato + " F " + format.format(horaDeProduccion).toString() + "\n";
        } else if (tipo.equalsIgnoreCase("ph")) {
            res += "PH: " + dato + " PH " + format.format(horaDeProduccion).toString() + "\n";
        } else if (tipo.equalsIgnoreCase("oxigeno")) {
            res += "Oxigeno: " + dato + " Mg/L " + format.format(horaDeProduccion).toString() + "\n";
        }
        return res;
    }

    public boolean verificarMedida() {
        if (tipo.equalsIgnoreCase("temperatura")) {
            if (!((int)dato >= 68 && (int)dato >= 89)) {// fuera de rango
                return false;
            }
        } else if (tipo.equalsIgnoreCase("ph")) {
            if (!((int)dato >= 2 && (int)dato <= 11)) {// fuera de rango
                return false;
            }
        } else if (tipo.equalsIgnoreCase("oxigeno")) {
            if (!((int)dato >= 6 && (int)dato <= 8)) {// fuera de rango
                return false;
            }
        }
        return true;
    }

}
