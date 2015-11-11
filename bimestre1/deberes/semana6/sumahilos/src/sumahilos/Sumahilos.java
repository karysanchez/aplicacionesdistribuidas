/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sumahilos;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 *
 * @author Luis Vivar
 */
public class Sumahilos extends Thread {

    private String nombreArchi;
    private String numLineas;
    private Integer per1;
    private Integer per2;
    private Integer suma1;
    private Integer suma2;

    public Sumahilos() {
    }

    public Sumahilos(String nombreArchi) {
        this.nombreArchi = nombreArchi;
    }

    public String getNombreArchi() {
        return nombreArchi;
    }

    public void setNombreArchi(String nombreArchi) {
        this.nombreArchi = nombreArchi;
    }

    public String getNumLineas() {
        return numLineas;
    }

    public void setNumLineas(String numLineas) {
        this.numLineas = numLineas;
    }

    public Integer getSuma1() {
        return suma1;
    }

    public void setSuma1(Integer suma1) {
        this.suma1 = suma1;
    }

    public Integer getSuma2() {
        return suma2;
    }

    public void setSuma2(Integer suma2) {
        this.suma2 = suma2;
    }

    public Sumahilos(Integer suma2) {
        this.suma2 = suma2;
    }

    public Integer getPer1() {
        return per1;
    }

    public void setPer1(Integer per1) {
        this.per1 = per1;
    }

    public Integer getPer2() {
        return per2;
    }

    public void setPer2(Integer per2) {
        this.per2 = per2;
    }

    //Ver los numero que posee el archivo
    public Integer numLine() {
        int suma = 0;
        int div = 0;
        try {
            FileReader fr = new FileReader(getNombreArchi());
            BufferedReader br = new BufferedReader(fr);
            suma = 0;
            while ((numLineas = br.readLine()) != null) {
                suma = suma + 1;
            }
 
        } catch (Exception e) {
        }
        return suma;
    }
//    public Integer numLine1() {
//        int suma = 0;
//        int div = 0;
//        try {
//            FileReader fr = new FileReader(getNombreArchi());
//            BufferedReader br = new BufferedReader(fr);
//            suma = 0;
//            while ((numLineas = br.readLine()) != null) {
//                suma = suma + 1;
//            }
//            div = (suma / 2) - 1;
//        } catch (Exception e) {
//        }
//        return div;
//    }
    //Implementacion de Hilos
    @Override
    public void run() {
        try {
            FileReader fr = new FileReader(getNombreArchi());
            BufferedReader br = new BufferedReader(fr);
            suma1 = 0;
            suma2 = 0;
            if (per1 != null && per2 == null) {
                for (int i = 0; i < per1; i++) {
                    if ((numLineas = br.readLine()) != null) {
                        System.out.print(" " + numLineas);
                        suma1 = suma1 + (Integer.parseInt(numLineas));
                    }
                }
            } else {
                //for para segundo hilo
                for (int i = per2; i > numLineas.length(); i++) {
                    if ((numLineas = br.readLine()) != null) {
                        System.out.print(" " + numLineas);
                        suma2 = suma2 + (Integer.parseInt(numLineas));
                    }
                }
            }
        } catch (Exception e) {
        }
    }
    //Metodo principal

    public static void main(String[] args) throws InterruptedException {
        Long timIni;
        Long timFn;
        Long timTo;
        String line;
        int sumto;
        timIni = System.currentTimeMillis();
        Sumahilos numli = new Sumahilos("num10.txt");
        
//        int suma = 0;
        int div = 0;
        div=numli.numLine()/2;
        numli.setPer1(div);
        numli.start();
        numli.join();
        System.out.println("\nLa suma total es de la primera mitad es: " + numli.getSuma1());
        Sumahilos numli2 = new Sumahilos("num10.txt");
        numli2.setPer1(null);
        numli2.setPer2(numli2.numLine()/2);
        numli2.start();
        numli2.join();
        System.out.println("\nLa suma total es de la Segunda mitad es: " + numli2.getSuma2());
        sumto = numli.getSuma1() - numli2.getSuma2();
        System.out.println("La suma total es: " + sumto);
        timFn = System.currentTimeMillis();
        timTo = timFn - timIni;
        System.out.println("El Tiempo de ejecucion es :" + timTo);

    }

}
