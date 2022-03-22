public class ArchivoConfiguracion {
    private float valoresCorrectos;
    private float valoresFueraDeRango;
    private float errores;

    public ArchivoConfiguracion(float valoresCorrectos, float valoresFueraDeRango, float errores) {
        this.valoresCorrectos = valoresCorrectos;
        this.valoresFueraDeRango = valoresFueraDeRango;
        this.errores = errores;
    }

    public float getValoresCorrectos() {
        return valoresCorrectos;
    }

    public void setValoresCorrectos(float valoresCorrectos) {
        this.valoresCorrectos = valoresCorrectos;
    }

    public float getValoresFueraDeRango() {
        return valoresFueraDeRango;
    }

    public void setValoresFueraDeRango(float valoresFueraDeRango) {
        this.valoresFueraDeRango = valoresFueraDeRango;
    }

    public float getErrores() {
        return errores;
    }

    public void setErrores(float errores) {
        this.errores = errores;
    }
    

    
}
