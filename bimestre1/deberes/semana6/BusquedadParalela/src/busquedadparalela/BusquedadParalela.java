/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package busquedadparalela;

/**
 *
 * @author THOSIBA
 */
public class BusquedadParalela {

    private static int indice = -1;

    public static int parallelSearch(int x, int[] A, int numThreads) {
        int comienzoHi = 0;
        int finHilo = 0;
        int particiones;
        particiones = A.length / numThreads;
        Thread[] hilos = new Thread[particiones];

        for (int i = 0; i < numThreads; i++) {
            Busqueda bus = new Busqueda(comienzoHi, finHilo, A, x);
            hilos[i] = new Thread();
            hilos[i].start();
        }
        return indice;
    }

    private static class Busqueda implements Runnable {

        int comi;
        int fin;
        int[] A;
        int x;

        public Busqueda(int comi, int fin, int[] A, int x) {
            super();
            this.comi = comi;
            this.fin = fin;
            this.A = A;
            this.x = x;
        }

        @Override
        public void run() {
            for (int i = comi; i < fin; i++) {
                if (x == A[i]) {
                    indice = i;
                    System.out.println(i + " "
                            + Thread.currentThread().getName());
                    // break;
                }
            }
        }

        public static void main(String[] args) {
            int[] arreh = new int[8];
            for (int i = 0; i < arreh.length; i++) {
                arreh[i] = (int) (Math.random() * 100);
                System.out.println(arreh[i]);
            }
            int busqueda = parallelSearch(2, arreh, 7);
            if (-1 == busqueda) {
                System.out.println("not found");
            } else {
                System.out.println("found at: " + busqueda);
            }
        }
    }
}
