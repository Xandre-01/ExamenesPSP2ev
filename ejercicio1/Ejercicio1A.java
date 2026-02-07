import java.io.*;
import java.util.ArrayList;
import java.util.List;

// Hilo encargado de procesar un fichero individual
class ProcesadorFichero extends Thread {
    private String nombreFichero;
    private int umbral;
    private long sumaParcial = 0;

    public ProcesadorFichero(String nombreFichero, int umbral) {
        this.nombreFichero = nombreFichero;
        this.umbral = umbral;
    }

    @Override
    public void run() {
        try (BufferedReader br = new BufferedReader(new FileReader(nombreFichero))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                int cantidad = Integer.parseInt(linea.trim());
                if (cantidad > umbral) {
                    sumaParcial += cantidad;
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error procesando " + nombreFichero + ": " + e.getMessage());
        }
    }

    public long getSumaParcial() {
        return sumaParcial;
    }
}

public class Ejercicio1A {
    /* * ARGUMENTOS: El programa recibe un número entero por línea de comandos (args[0]).
     * RESULTADOS: Se muestran por consola tras la ejecución de todos los hilos.
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Uso: java Ejercicio1A <umbral>");
            return;
        }

        int umbral = Integer.parseInt(args[0]);
        String[] ficheros = {"informatica.txt", "gerencia.txt", "contabilidad.txt", "comercio.txt"};
        List<ProcesadorFichero> hilos = new ArrayList<>();

        // Crear y lanzar hilos
        for (String f : ficheros) {
            ProcesadorFichero hilo = new ProcesadorFichero(f, umbral);
            hilo.start();
            hilos.add(hilo);
        }

        long sumaTotal = 0;
        // Esperar a que todos terminen (join) y recoger resultados
        for (ProcesadorFichero hilo : hilos) {
            try {
                hilo.join();
                sumaTotal += hilo.getSumaParcial();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("La suma total de cantidades superiores a " + umbral + " es: " + sumaTotal);
    }
}