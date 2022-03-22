public class SensorOxigeno extends Sensor {
    
    public SensorOxigeno(String tipo, int tiempo, ArchivoConfiguracion archivoConfiguracion) {
        super(tipo, tiempo, archivoConfiguracion);
        super.setValorMinimo(2);
        super.setValorMaximo(11);
    }

}
    
