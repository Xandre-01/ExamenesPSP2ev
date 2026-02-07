package ejercicio2;

import java.io.*;

public class ContadorLetras {
    public static void main(String[] args) {
        if (args.length < 2)
            return;

        char letraTarget = args[0].charAt(0);
        String nombreFichero = args[1];
        int contador = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(nombreFichero))) {
            int c;
            while ((c = br.read()) != -1) {
                if ((char) c == letraTarget) {
                    contador++;
                }
            }
            // Guardar resultado en un fichero específico para este proceso
            try (PrintWriter pw = new PrintWriter(new FileWriter("res_" + letraTarget + ".txt"))) {
                pw.print(contador);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}