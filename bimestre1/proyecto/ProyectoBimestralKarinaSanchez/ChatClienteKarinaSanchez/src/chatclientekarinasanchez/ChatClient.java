/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclientekarinasanchez;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


/**
 *
 * @author TOSH
 */
public class ChatClient {

    public static void main(String[] args) throws Exception {
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        try (DatagramSocket clientSocket = new DatagramSocket()) {
            InetAddress IPAddress = InetAddress.getByName("localhost");
            byte[] sendData = new byte[65508];

            System.out.println("SOY EL CLIENTE: ");
            System.out.println("Ingrese su nombre por favor: ");
            String clientUsername = inFromUser.readLine();

            System.out.println("Ahora puede empezar a enviar mensajes...");

            while (true) {
                System.out.print("Yo: ");
                String clientSentence = clientUsername + ": " + inFromUser.readLine();
                sendData = clientSentence.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
                clientSocket.send(sendPacket);
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

}
class ReciboHilo implements Runnable {

    DatagramSocket sok = null;
    BufferedReader reci = null;

    public ReciboHilo(DatagramSocket sok) {
        this.sok = sok;
    }

    @Override
    public void run() {
        try {
            BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
            byte[] receiveData = new byte[65508];
            String mensaj = null;
            while ((mensaj = inFromUser.readLine()) != null) {
                System.out.println("Esperando respuesta desde el servidor...");
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                sok.receive(receivePacket);
                String serverSentence = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println(serverSentence);
            }

        } catch (Exception e) {
        }
    }
}
