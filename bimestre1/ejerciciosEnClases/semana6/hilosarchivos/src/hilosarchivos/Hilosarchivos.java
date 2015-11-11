/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hilosarchivos;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 22B
 */
public class Hilosarchivos  {

    public static void main(String[] args) {
        int numline = 0;
        int sumatotal = 0;
        Archivos[] archi = new Archivos[args.length];
        for (int i = 0; i < args.length; i++) {
            archi[i] = new Archivos(args[i]);
            archi[i].start();
        }
        for (int i = 0; i < archi.length; i++) {
            try {
                archi[i].join();
                numline=Integer.parseInt(archi[i].getLinea());
                sumatotal+=numline;
                System.out.println(args[i] + ": " + numline);
            } catch (InterruptedException ex) {
                Logger.getLogger(Hilosarchivos.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        System.out.println("La suma de Lineas es: "+sumatotal);
    }
}
