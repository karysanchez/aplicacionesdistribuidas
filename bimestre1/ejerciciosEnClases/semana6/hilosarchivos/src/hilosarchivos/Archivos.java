/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hilosarchivos;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 *
 * @author 22B
 */
public class Archivos extends Thread {

    private String nomArchivo;
    private String linea;

    public Archivos() {
    }

    public Archivos(String nomArchivo) {
        this.nomArchivo = nomArchivo;
    }

    public String getNomArchivo() {
        return nomArchivo;
    }

    public void setNomArchivo(String nomArchivo) {
        this.nomArchivo = nomArchivo;
    }

    public String getLinea() {
        return linea;
    }

    public void setLinea(String linea) {
        this.linea = linea;
    }
    //Implemento de Hilos

    @Override
    public void run() {
        int sumli = 0;
        try {
            FileReader fr = new FileReader(getNomArchivo());
            BufferedReader br = new BufferedReader(fr);
            while ((linea = br.readLine()) != null) {
                sumli++;
            }
            this.linea=String.valueOf(sumli);
        } catch (Exception e) {
        }
    }
}