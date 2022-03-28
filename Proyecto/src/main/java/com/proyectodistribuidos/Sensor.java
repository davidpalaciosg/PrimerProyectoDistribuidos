package com.proyectodistribuidos;
import java.util.ArrayList;

public abstract class Sensor {
    private String tipo;
    private int tiempo;
    private ArchivoConfiguracion archivoConfiguracion;
    ArrayList<Medida> medidas;

    private float valorMinimo;
    private float valorMaximo;

    

    public Sensor(String tipo, int tiempo, ArchivoConfiguracion archivoConfiguracion) {
        this.tipo = tipo;
        this.tiempo = tiempo;
        this.archivoConfiguracion = archivoConfiguracion;
        this.medidas = new ArrayList<Medida>();
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getTiempo() {
        return tiempo;
    }

    public void setTiempo(int tiempo) {
        this.tiempo = tiempo;
    }

    public ArchivoConfiguracion getArchivoConfiguracion() {
        return archivoConfiguracion;
    }

    public void setArchivoConfiguracion(ArchivoConfiguracion archivoConfiguracion) {
        this.archivoConfiguracion = archivoConfiguracion;
    }

    public ArrayList<Medida> getMedidas() {
        return medidas;
    }

    public void setMedidas(ArrayList<Medida> medidas) {
        this.medidas = medidas;
    }

    public float getValorMinimo() {
        return valorMinimo;
    }

    public void setValorMinimo(float valorMinimo) {
        this.valorMinimo = valorMinimo;
    }

    public float getValorMaximo() {
        return valorMaximo;
    }

    public void setValorMaximo(float valorMaximo) {
        this.valorMaximo = valorMaximo;
    }

    public String generarMedidas() {

        String res="";
        // Crear numero aleatorio de medidas entre 10 y 100
        int cantMedidas = (int) ((Math.random() * (100 - 10)) + 10);

        // Crear medidas válidas
        for (int i = 0; i < cantMedidas * archivoConfiguracion.getValoresCorrectos(); i++) {
            float dato = (float) (Math.random() * (valorMaximo - valorMinimo) + valorMinimo);
            Medida medida = new Medida(dato, tipo);
            // Insertar medida
            res +=medida.toString();
        }
        // Crear medidas erroneas
        for (int i = 0; i < cantMedidas * archivoConfiguracion.getErrores(); i++) {
            float dato = (float) (Math.random() * (0 - -25) + -25); // Valores negativos
            Medida medida = new Medida(dato, tipo);
            // Insertar medida
            res +=medida.toString();
        }
        // Crear medidas fuera de rango
        // Medidas antes del valor mínimo [1-valorMinimo]
        for (int i = 0; i < cantMedidas * archivoConfiguracion.getValoresFueraDeRango() / 2; i++) {
            float dato = (float) (Math.random() * (valorMinimo - 1) + 1);
            Medida medida = new Medida(dato, tipo);
            // Insertar medida
            res +=medida.toString();
        }
        // Medidas después del valor máximo [valorMaximo-valoraMaximo+25]
        for (int i = 0; i < cantMedidas * archivoConfiguracion.getValoresFueraDeRango() / 2; i++) {
            float dato = (float) (Math.random() * (valorMaximo + 25 - valorMaximo) + valorMaximo);
            Medida medida = new Medida(dato, tipo);
            // Insertar medida
            res +=medida.toString();
        }
        return res;
    }
}
