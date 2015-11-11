/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sumasecuencial;

/**
 *
 * @author TOSH
 */
public class Sumasecuencial {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        DatosArchivos arch1=new DatosArchivos("num10.txt");
        arch1.leeracchivo();
         DatosArchivos arch2=new DatosArchivos("num50.txt");
        arch2.leeracchivo();
         DatosArchivos arch3=new DatosArchivos("num100.txt");
        arch3.leeracchivo();
    }
    
}
