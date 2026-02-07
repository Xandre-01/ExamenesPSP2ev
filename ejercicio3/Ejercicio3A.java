package ejercicio3;

import java.util.LinkedList;
import java.util.Queue;

class Pedido {
    int mesa;
    String plato;
    int tiempoPreparacion;

    public Pedido(int mesa, String plato, int tiempo) {
        this.mesa = mesa;
        this.plato = plato;
        this.tiempoPreparacion = tiempo;
    }

    @Override
    public String toString() {
        return "Mesa " + mesa + " - " + plato + " (" + tiempoPreparacion + "s)";
    }
}

class Cocina {
    private final Queue<Pedido> cola = new LinkedList<>();
    private final int CAPACIDAD = 5;
    private int totalProducidos = 0;
    private final int MAX_PEDIDOS = 20;

    public synchronized boolean puedeProducir() {
        return totalProducidos < MAX_PEDIDOS;
    }

    public synchronized void producir(Pedido p) throws InterruptedException {
        while (cola.size() == CAPACIDAD) {
            wait();
        }
        cola.add(p);
        totalProducidos++;
        System.out.println("[CAMARERO] Pedido " + totalProducidos + " añadido: " + p);
        notifyAll();
    }

    public synchronized Pedido consumir() throws InterruptedException {
        while (cola.isEmpty() && totalProducidos < MAX_PEDIDOS) {
            wait();
        }
        if (cola.isEmpty() && totalProducidos >= MAX_PEDIDOS)
            return null;

        Pedido p = cola.poll();
        notifyAll();
        return p;
    }

    public synchronized boolean hayTrabajo() {
        return totalProducidos < MAX_PEDIDOS || !cola.isEmpty();
    }
}

public class Ejercicio3A {
    public static void main(String[] args) {
        Cocina cocina = new Cocina();

        Thread camarero = new Thread(() -> {
            try {
                while (true) {
                    synchronized (cocina) {
                        if (!cocina.puedeProducir())
                            break;
                    }
                    int tiempoPed = (int) (Math.random() * 3) + 1;
                    Pedido p = new Pedido((int) (Math.random() * 10) + 1, "Plato", tiempoPed);

                    cocina.producir(p);
                    Thread.sleep((long) (Math.random() * 2000) + 1000);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread cocinero = new Thread(() -> {
            try {
                while (cocina.hayTrabajo()) {
                    Pedido p = cocina.consumir();
                    if (p != null) {
                        System.out.println("[COCINERO] Empezando: " + p.plato);
                        Thread.sleep(p.tiempoPreparacion * 1000L);
                        System.out.println("[COCINERO] Terminado: " + p.plato);
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        camarero.start();
        cocinero.start();
    }
}