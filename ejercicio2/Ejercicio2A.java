package ejercicio2;

import java.io.*;
import java.util.*;

public class Ejercicio2A {
    /*
     * * ARGUMENTOS: Nombre del fichero de lectura (args[0]).
     * [cite_start]RESULTADOS: Suma total de letras aleatorias encontradas por
     * procesos hijos. [cite: 18, 21, 22]
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Uso: java ejercicio2.Ejercicio2A <fichero>");
            return;
        }

        String ficheroLectura = args[0];
        Random rnd = new Random();
        List<Process> procesos = new ArrayList<>();
        List<Character> letrasGeneradas = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            char letraAleatoria = (char) ('A' + rnd.nextInt(26));
            letrasGeneradas.add(letraAleatoria);

            // CAMBIO CLAVE: Se añade el nombre del paquete al invocar al hijo
            ProcessBuilder pb = new ProcessBuilder("java", "ejercicio2.ContadorLetras",
                    String.valueOf(letraAleatoria), ficheroLectura);
            try {
                procesos.add(pb.start());
                System.out.println("Lanzado proceso para letra: " + letraAleatoria);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (Process p : procesos) {
            try {
                p.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        int granTotal = 0;
        for (char letra : letrasGeneradas) {
            // Los hijos guardan el archivo con el prefijo res_ [cite: 20]
            File f = new File("res_" + letra + ".txt");
            if (f.exists()) {
                try (BufferedReader br = new BufferedReader(new FileReader(f))) {
                    granTotal += Integer.parseInt(br.readLine());
                } catch (IOException | NumberFormatException e) {
                    System.err.println("Error leyendo resultado de " + letra);
                }
            } else {
                System.err.println("No se pudo leer el resultado de " + letra);
            }
        }
        System.out.println("Suma total de letras encontradas: " + granTotal);
    }
}