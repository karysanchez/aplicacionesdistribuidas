/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;

class Connection extends Thread {

  DataInputStream in;
  DataOutputStream out;
  Socket clientSocket;

  public Connection(Socket aClientSocket) {
    try {
      clientSocket = aClientSocket;
      in = new DataInputStream(clientSocket.getInputStream());
      out = new DataOutputStream(clientSocket.getOutputStream());
      this.start();
    } catch (IOException e) {
      System.out.println("Connection:" + e.getMessage());
    }
  }

  public void run() {
    try { // an echo server
      System.out.println("server reading data");
      String data = in.readUTF(); // read a line of data from the stream
      System.out.println("server writing data");
      out.writeUTF(data);
    } catch (EOFException e) {
      System.out.println("EOF:" + e.getMessage());
    } catch (IOException e) {
      System.out.println("readline:" + e.getMessage());
    } finally {
      try {
        clientSocket.close();
      } catch (IOException e) {/*close failed*/

      }
    }
  }
}
