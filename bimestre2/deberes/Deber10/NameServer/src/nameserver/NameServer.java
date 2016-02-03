/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nameserver;

import java.net.*;
import java.io.*;


public class NameServer {

  public static void main(String[] args) {
      Long tiempoInicio;
      Long tiempoFin;
      Long tiempo;
      
      tiempoInicio = System.currentTimeMillis();
    System.out.println("Nameserver started  :");
    try {
      ServerSocket listener = new ServerSocket(Symbols.serverPort);
      while (true) {
        Socket aClient = listener.accept();
        Handleclient cs = new Handleclient(aClient);
        new Thread(cs).start();
          try {
              Thread.sleep(20000);
          } catch (InterruptedException ex) {
              System.out.println("El hilo no se ha dormido" + ex);
          }
          
          tiempoFin = System.currentTimeMillis();
          tiempo = tiempoFin - tiempoInicio;
          
          System.out.println("El hilo ha comenzado en el tiempo de: "+tiempo);
          
          
          /*Como puedo observar al realizar la ejecucion con multihilos notamos una 
          gran diferencia al que se implemento en clase que era monohilo al momento de
          poner el sleep() por 20 segundos el hilo se duerme y esto implica que en el multihilo se ejecute 
          el primer hilo espera 20 segundos y empieza el otro, mientras en el otro implica el dopble 
          de tiempo de ejecucion y solo para un cliente por lo que no debe ser asi.*/
      }
    } catch (IOException e) {
      System.err.println("Server aborted  :" + e);
    }
  }
}
