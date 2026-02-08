package ejercicio1;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

class SumadorFichero extends Thread {
    private String nombre;
    private int umbral;
    private long sumaParcial = 0;

    public SumadorFichero(String nombre, int umbral) {
        this.nombre = nombre;
        this.umbral = umbral;
    }

    @Override
    public void run() {
        File f = new File(nombre);
        if (!f.exists()) {
            System.err.println("Archivo no encontrado: " + nombre);
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty())
                    continue;

                int n = Integer.parseInt(linea);
                if (n < umbral) {
                    sumaParcial += n;
                }
            }
        } catch (Exception e) {
            System.err.println("Error procesando " + nombre);
        }
    }

    public long getSuma() {
        return sumaParcial;
    }
}

public class Ejercicio1B {
    public static void main(String[] args) throws InterruptedException {
        if (args.length < 1)
            return;

        int umbral = Integer.parseInt(args[0]);

        // Verifica que estos nombres coincidan con tus archivos .txt
        String[] archivos = {
                "ejercicio1/informatica.txt",
                "ejercicio1/gerencia.txt",
                "ejercicio1/contabilidad.txt",
                "ejercicio1/comercio.txt"
        };

        List<SumadorFichero> hilos = new ArrayList<>();

        for (String f : archivos) {
            SumadorFichero h = new SumadorFichero(f, umbral);
            h.start();
            hilos.add(h);
        }

        long total = 0;
        for (SumadorFichero h : hilos) {
            h.join();
            total += h.getSuma();
        }
        System.out.println("Suma total (inferiores a " + umbral + "): " + total);
    }
}