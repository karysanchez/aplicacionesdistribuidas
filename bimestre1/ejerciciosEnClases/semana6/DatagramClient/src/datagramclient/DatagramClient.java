package datagramclient;

/**
 * un EchoClient que se lee datos del usuario, lo envia al servidor. El servidor
 * recibe los datagramas y regresa el mismo datagrama y el cliente lo imprime.
 * Se conecta solo al localhost.
 * 
*/
import java.net.*;
import java.io.*;

public class DatagramClient {

    String hostname;

    public DatagramClient() {
    }

    public DatagramClient(String hostname) {
        this.hostname = hostname;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public static void main(String[] args) {
        DatagramClient dt = new DatagramClient();
        dt.setHostname("192.168.0.104");
        int port = 2018;
        int len = 1024;
        DatagramPacket sPacket, rPacket;
        try {
            InetAddress ia = InetAddress.getByName(dt.getHostname());
            DatagramSocket datasocket = new DatagramSocket();
            BufferedReader stdinp = new BufferedReader(
                    new InputStreamReader(System.in));

            while (true) {
                try {
                    System.out.println("Ingrese un mensaje:");
                    String echoline = stdinp.readLine();
                    if (echoline.equals("salir")) {
                        break;
                    }
                    byte[] buffer = new byte[echoline.length()];
                    buffer = echoline.getBytes();
                    sPacket = new DatagramPacket(buffer, buffer.length, ia, port);
                    System.out.println("Enviando mensaje al servidor...");
                    datasocket.send(sPacket);

                    byte[] rbuffer = new byte[len];
                    rPacket = new DatagramPacket(rbuffer, rbuffer.length);
                    System.out.println("Esperando respuesta del servidor...");
                    datasocket.receive(rPacket);

                    String retstring = new String(rPacket.getData(), 0, rPacket.getLength());
                    System.out.println("El servidor devolvió el siguiente mensaje:");
                    System.out.println(retstring);

                } catch (IOException e) {
                    System.err.println(e);
                }
            }  //  while

        } catch (UnknownHostException e) {
            System.err.println(e);
        } catch (SocketException se) {
            System.err.println(se);
        }
    }//end main
}
