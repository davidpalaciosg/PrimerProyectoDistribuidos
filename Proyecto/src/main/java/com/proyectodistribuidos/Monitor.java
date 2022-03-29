package com.proyectodistribuidos;

import java.util.ArrayList;

public class Monitor {
    private String tipo;
    private ArrayList<Alarma> alarmas;

    public Monitor(String tipo) {
        this.tipo = tipo;
        this.alarmas = new ArrayList<Alarma>();
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public ArrayList<Alarma> getAlarmas() {
        return alarmas;
    }

    public void setAlarmas(ArrayList<Alarma> alarmas) {
        this.alarmas = alarmas;
    }

    //TODO implementar método para agregar alarma
    public String crearAlarmas()
    {
        return "";
    }


    
    



    
}
