package com.proyectodistribuidos;

public class InfoMonitorHealthCheck {
    private String pidMonitor;
    private String tipoMonitor;
    private String ipSensor;
    private String ipSistemaDeCalidad;
    private String fechaDeIngreso;
    private boolean vivo;
    public InfoMonitorHealthCheck(String pidMonitor, String tipoMonitor, String fechaDeIngreso, String ipSensor, String ipSistemaDeCalidad) {
        this.pidMonitor = pidMonitor;
        this.tipoMonitor = tipoMonitor;
        this.fechaDeIngreso = fechaDeIngreso;
        this.vivo = true;
        this.ipSensor = ipSensor;
        this.ipSistemaDeCalidad = ipSistemaDeCalidad;
    }
    
    public String getIpSistemaDeCalidad() {
        return ipSistemaDeCalidad;
    }

    public void setIpSistemaDeCalidad(String ipSistemaDeCalidad) {
        this.ipSistemaDeCalidad = ipSistemaDeCalidad;
    }

    public String getIpSensor() {
        return ipSensor;
    }
    public void setIpSensor(String ipSensor) {
        this.ipSensor = ipSensor;
    }
    public String getPidMonitor() {
        return pidMonitor;
    }
    public void setPidMonitor(String pidMonitor) {
        this.pidMonitor = pidMonitor;
    }
    public String getTipoMonitor() {
        return tipoMonitor;
    }
    public boolean isVivo() {
        return vivo;
    }
    public void setVivo(boolean vivo) {
        this.vivo = vivo;
    }
    public void setTipoMonitor(String tipoMonitor) {
        this.tipoMonitor = tipoMonitor;
    }
    public String getFechaDeIngreso() {
        return fechaDeIngreso;
    }
    public void setFechaDeIngreso(String fechaDeIngreso) {
        this.fechaDeIngreso = fechaDeIngreso;
    }
    
}

