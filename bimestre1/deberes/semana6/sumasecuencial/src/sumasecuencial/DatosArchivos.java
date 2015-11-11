/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sumasecuencial;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 *
 * @author Luis Vivar
 */
public class DatosArchivos {
   private String nombreAr;
   private String lineas;
   private Integer suma;

    public DatosArchivos() {
    }

    public DatosArchivos(String nombreAr) {
        this.nombreAr = nombreAr;
    }

    public String getNombreAr() {
        return nombreAr;
    }

    public void setNombreAr(String nombreAr) {
        this.nombreAr = nombreAr;
    }
    public void leeracchivo() {
        long timeini;
        long timefin;
        long time;
        timeini = System.currentTimeMillis();
        try {
            FileReader fr = new FileReader(getNombreAr());
            BufferedReader br = new BufferedReader(fr);
            suma = 0;
            System.out.println("Los numeros son: ");
            while ((lineas = br.readLine()) != null) {
                System.out.print(" " + lineas);
                suma = suma + (Integer.parseInt(lineas));
            }
            System.out.println("\nLa Sumas Total es :" + suma);
        } catch (Exception e) {
        }
        timefin = System.currentTimeMillis();
        time = timefin - timeini;
        System.out.println("El tiempo de Ejecucion es :" + time);
    }

   
}
