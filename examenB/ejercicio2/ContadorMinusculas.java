package ejercicio2;

import java.io.*;

public class ContadorMinusculas {
    public static void main(String[] args) throws IOException {
        char letra = args[0].charAt(0);
        String ruta = args[1];
        int cont = 0;
        try (FileReader fr = new FileReader(ruta)) {
            int c;
            while ((c = fr.read()) != -1) {
                if ((char) c == letra)
                    cont++;
            }
        }
        try (PrintWriter pw = new PrintWriter(new FileWriter("res_" + letra + ".txt"))) {
            pw.print(cont);
        }
    }
}