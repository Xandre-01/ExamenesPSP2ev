package ejercicio3;

import java.util.LinkedList;
import java.util.Queue;

class Trabajo {
    int id;
    String tipo;
    int tiempo;

    public Trabajo(int id, String tipo, int tiempo) {
        this.id = id;
        this.tipo = tipo;
        this.tiempo = tiempo;
    }
}

class Imprenta {
    private final Queue<Trabajo> cola = new LinkedList<>();
    private final int MAX_COLA = 8;
    private int producidos = 0;
    private final int LIMITE = 10;

    public synchronized void insertarTrabajo(Trabajo t) throws InterruptedException {
        while (cola.size() == MAX_COLA) {
            wait();
        }
        cola.add(t);
        producidos++;
        System.out.println("DISEÑADOR: Trabajo ID " + t.id + " añadido a la cola.");
        notifyAll();
    }

    public synchronized Trabajo recogerTrabajo() throws InterruptedException {
        while (cola.isEmpty() && producidos < LIMITE) {
            wait();
        }
        if (cola.isEmpty() && producidos >= LIMITE)
            return null;

        Trabajo t = cola.poll();
        notifyAll();
        return t;
    }

    public synchronized boolean hayTrabajo() {
        return producidos < LIMITE || !cola.isEmpty();
    }
}

public class Ejercicio3B {
    public static void main(String[] args) {
        Imprenta imprenta = new Imprenta();

        Thread disenador = new Thread(() -> {
            try {
                for (int i = 1; i <= 10; i++) {
                    Trabajo t = new Trabajo(i, "Folletos", (int) (Math.random() * 4 + 2));
                    imprenta.insertarTrabajo(t);
                    Thread.sleep((long) (Math.random() * 3000 + 2000));
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread impresora = new Thread(() -> {
            try {
                while (imprenta.hayTrabajo()) {
                    Trabajo t = imprenta.recogerTrabajo();
                    if (t != null) {
                        System.out.println("IMPRESORA: Iniciando impresion ID: " + t.id);
                        Thread.sleep(t.tiempo * 1000L);
                        System.out.println("IMPRESORA: Finalizada impresion ID: " + t.id);
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        disenador.start();
        impresora.start();
    }
}