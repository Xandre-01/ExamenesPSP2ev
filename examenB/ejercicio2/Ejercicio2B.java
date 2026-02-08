package ejercicio2;

import java.io.*;
import java.util.*;

public class Ejercicio2B {
    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("Indique el fichero de texto como argumento.");
            return;
        }

        String fic = args[0];
        Random r = new Random();
        List<Process> procs = new ArrayList<>();
        List<Character> letras = new ArrayList<>();

        System.out.println("--- INICIANDO BÚSQUEDA ALEATORIA ---");
        for (int i = 0; i < 3; i++) {
            char minus = (char) ('a' + r.nextInt(26));
            letras.add(minus);

            // Aquí mostramos por pantalla qué letra se está buscando
            System.out.println("Proceso " + (i + 1) + ": Buscando la letra '" + minus + "'...");

            ProcessBuilder pb = new ProcessBuilder("java", "ejercicio2.ContadorMinusculas", String.valueOf(minus), fic);
            procs.add(pb.start());
        }

        for (Process p : procs)
            p.waitFor();

        System.out.println("--- RECOPILANDO RESULTADOS ---");
        int total = 0;
        for (char l : letras) {
            File f = new File("res_" + l + ".txt");
            if (f.exists()) {
                try (BufferedReader br = new BufferedReader(new FileReader(f))) {
                    int parcial = Integer.parseInt(br.readLine());
                    System.out.println("Letra '" + l + "': Encontradas " + parcial + " veces.");
                    total += parcial;
                }
            }
        }
        System.out.println("Suma total de minúsculas: " + total);
    }
}