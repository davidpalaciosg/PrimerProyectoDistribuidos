package com.proyectodistribuidos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.zeromq.SocketType;
import org.zeromq.ZMQ;

public class HealthCheck {

    // Lista que guarda los monitores registrados en el Health Check
    private static ArrayList<InfoMonitorHealthCheck> listaMonitores;
    private static int tiempoHilo;
    private static ZMQ.Context context;
    private static ZMQ.Socket subscriberMonitor;
    private static ZMQ.Socket pubReplica;
    private static String tcpReplica;
        

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Debe ingresar el tiempo de espera en ms para revisar el estado de los monitores");
            System.exit(1);
        }

        //ARGS[0] = tiempo de espera en ms
        tiempoHilo = Integer.parseInt(args[0]);

        context = ZMQ.context(1);
        // Socket subscriberMonitor de monitores
        subscriberMonitor = context.socket(SocketType.SUB);

        listaMonitores = new ArrayList<>();
        // Hilo que verifica si todos los monitores estan activos cada 3 segundos
        Runnable hilo = crearHilo();
        new Thread(hilo).start();

        pubReplica = context.socket(SocketType.PUB);

        try {
            // Conectar a todos los monitores que lleguen por parámetro
            System.out.println("Intentando conectar a monitores");

            // El health check se conecta a TODOS los monitores de localhost
            //System.out.println("Conectando a " + "localhost");
            suscribirseAMonitor("localhost", subscriberMonitor); // Conectarse a todos los monitores

            subscriberMonitor.subscribe("".getBytes());

            //Crear canal entre el HealthCheck y la réplica
            tcpReplica = "tcp://*:5610";
            pubReplica.bind(tcpReplica);
            

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());

        } finally {
            while ((!Thread.currentThread().isInterrupted())) {

                String string = subscriberMonitor.recvStr();

                // Obtener datos de monitor
                //System.out.println(string);
                String[] datos = string.split(" ");
                String pid = datos[0];
                String tipo = datos[1];
                String tiempo = datos[2];
                String ipSensor = datos[3];
                String ipSistemaDeCalidad = datos[4];


                // Si el monitor no está en la lista, lo agrega
                int index = getMonitorByPid(pid);
                if (index == -1) {
                    InfoMonitorHealthCheck info = new InfoMonitorHealthCheck(pid, tipo, tiempo, ipSensor, ipSistemaDeCalidad);
                    listaMonitores.add(info);
                    System.out.println("Se registró el monitor con PID " + pid + " al Health Check para el sensor " + ipSensor+ " y el Sistema de Calidad en " + ipSistemaDeCalidad);
                } else {
                    // Si el monitor está en la lista, actualiza su tiempo
                    listaMonitores.get(index).setFechaDeIngreso(tiempo);
                }
            }
        }

    }

    private static void suscribirseAMonitor(String direccion, ZMQ.Socket subscriberMonitor) {
        // Rango de 5585 a 5600
        for (int i = 5585; i <= 5600; i++) {
            String tcp = "tcp://" + direccion + ":" + i;
            subscriberMonitor.connect(tcp);
        }
    }
    
    private static ArrayList<String> getProcesosVivos() {
        // Solo vale la pena hacerlo si hay monitores registrados
        if (listaMonitores.size() > 0) {
            ArrayList<String> procesosVivos = new ArrayList<>();
            try {
                String line;
                Process p = Runtime.getRuntime().exec(System.getenv("windir") + "\\system32\\" + "tasklist.exe");
                BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
                // Ignorar las tres primeras lineas
                line = input.readLine();
                line = input.readLine();
                line = input.readLine();
                while ((line = input.readLine()) != null) {
                    // Partir la linea por uno o muchos espacios
                    String[] parts = line.split("\\s+");
                    // Obtener el PID del proceso activo
                    if(parts[0].equalsIgnoreCase("java.exe"))
                        procesosVivos.add(parts[1]);

                }
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            }
            return procesosVivos;
        }
        return null;
    }

    private static void estanVivosLosMonitores() {

        ArrayList<String> procesosVivos = getProcesosVivos();
        if (procesosVivos != null) {
            for (InfoMonitorHealthCheck info : listaMonitores) {
                // Si el proceso murió, crea un nuevo proceso
                if (info.isVivo()) {
                    if (!procesosVivos.contains(info.getPidMonitor())) {
                        System.out.println("El proceso " + info.getPidMonitor() + " murió");
                        // Enviar info del proceso muerto a la réplica
                        String msg = info.getIpSensor()+" "+info.getTipoMonitor()+" "+info.getIpSistemaDeCalidad();
                        System.out.println("Se envió al monitor réplica el mensaje: " + msg);
                        pubReplica.send(msg);
                        info.setVivo(false);
                    }
                }
            }
        }

    }

    private static int getMonitorByPid(String pid) {
        for (int i = 0; i < listaMonitores.size(); i++) {
            if (listaMonitores.get(i).getPidMonitor().equals(pid)) {
                return i;
            }
        }
        return -1;
    }

    private static Runnable crearHilo() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // Esto se ejecuta en segundo plano una única vez
                while (true) {
                    try {
                        // En él, hacemos que el hilo duerma
                        Thread.sleep(tiempoHilo);
                        // Y después realizamos las operaciones
                        // System.out.println("Verificando si los monitores están activos");
                        estanVivosLosMonitores();
                        // Así, se da la impresión de que se ejecuta cada cierto tiempo
                        // Thread.currentThread().interrupt();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        return runnable;
    }

}
