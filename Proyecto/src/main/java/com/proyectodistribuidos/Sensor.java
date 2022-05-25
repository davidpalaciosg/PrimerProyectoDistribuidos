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

    public String generarMedida(){
        String res = "";

        float probaCorrectos = archivoConfiguracion.getValoresCorrectos();
        float probaFueraDeRango = archivoConfiguracion.getValoresFueraDeRango();
        //float probaErrores =  archivoConfiguracion.getErrores();

        float proba = (float) (Math.random()); //Función genera número aleatorio entre 0.0 - 1.0

        if (proba>=0 && proba <= probaCorrectos ) {
            //Medida correcta
            float dato = (float) (Math.random() * (valorMaximo - valorMinimo) + valorMinimo);
            Medida medida = new Medida(dato, tipo);
            // Insertar medida
            res += medida.toString();

            
        } else if (proba >probaCorrectos && proba <= (probaCorrectos + probaFueraDeRango)) {
            //Medida fuera de rango
            float dato = (float) (Math.random() * (valorMaximo + 25 - valorMaximo) + valorMaximo);//No sé cómo generar uno
            Medida medida = new Medida(dato, tipo);
            // Insertar medida
            res += medida.toString();
        } else {
            //Medida con error
            float dato = (float) (Math.random() * (0 - -25) + -25); // Valores negativos
            Medida medida = new Medida(dato, tipo);
            // Insertar medida
            res += medida.toString();
        }
        return res;

    }

    /*
    public String generarMedidas() {

        String res = "\n";
        // Crear numero aleatorio de medidas entre 10 y 100
        int cantMedidas = (int) ((Math.random() * (100 - 10)) + 10);

        float correctos = cantMedidas * archivoConfiguracion.getValoresCorrectos();
        float fueraDeRango = cantMedidas * archivoConfiguracion.getValoresFueraDeRango();
        float errores = cantMedidas * archivoConfiguracion.getErrores();

        res += "Medidas generadas: " + cantMedidas + "\n";
        res += "Correctas: " + (int) correctos + " -> " + String.format("%.2f",archivoConfiguracion.getValoresCorrectos() * 100) + "%\n";
        res += "Erroneas: " + (int) errores + " -> " + String.format("%.2f",archivoConfiguracion.getErrores() * 100) + "%\n";
        res += "Fuera de rango: " + (int) fueraDeRango + " -> " + String.format("%.2f",archivoConfiguracion.getValoresFueraDeRango() * 100) + "%\n";
        res += "\n";
        // Crear medidas válidas
        //res+="Correctas:"+"\n";
        for (int i = 0; i < (int) correctos; i++) {
            float dato = (float) (Math.random() * (valorMaximo - valorMinimo) + valorMinimo);
            Medida medida = new Medida(dato, tipo);
            // Insertar medida
            res += medida.toString();
        }
        res+="\n";
        //res+="Erroneas: "+"\n";
        // Crear medidas erroneas
        for (int i = 0; i < (int) errores; i++) {
            float dato = (float) (Math.random() * (0 - -25) + -25); // Valores negativos
            Medida medida = new Medida(dato, tipo);
            // Insertar medida
            res += medida.toString();
        }
        // Crear medidas fuera de rango
        // Medidas antes del valor mínimo [1-valorMinimo]
        res+="\n";
        //res+="Fuera de rango: "+"\n";
        for (int i = 0; i < (int) fueraDeRango / 2; i++) {
            float dato = (float) (Math.random() * (valorMinimo - 1) + 1);
            Medida medida = new Medida(dato, tipo);
            // Insertar medida
            res += medida.toString();
        }
        // Medidas después del valor máximo [valorMaximo-valoraMaximo+25]
        for (int i = 0; i < (int) fueraDeRango / 2; i++) {
            float dato = (float) (Math.random() * (valorMaximo + 25 - valorMaximo) + valorMaximo);
            Medida medida = new Medida(dato, tipo);
            // Insertar medida
            res += medida.toString();
        }
        res+="\n";
        return res;
    }
    */
}
