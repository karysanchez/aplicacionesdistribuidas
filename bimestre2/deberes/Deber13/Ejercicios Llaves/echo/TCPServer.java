/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.net.*;
import java.io.*;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.SecureRandom;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.TrustManagerFactory;

public class TCPServer {

    private static void setupServerKeystore() throws GeneralSecurityException, IOException {
        KeyStore serverKeyStore = KeyStore.getInstance("JKS");
        serverKeyStore.load(new FileInputStream("server.private"), passphrase.toCharArray());
    }

    private static void  setupClientKeyStore() throws GeneralSecurityException, IOException {
        KeyStore clientKeyStore = KeyStore.getInstance("JKS");
        clientKeyStore.load(new FileInputStream("client.private"), "public".toCharArray());
    }

    private static void setupSSLContext() throws GeneralSecurityException, IOException {
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        KeyStore clientKeyStore = null;
        tmf.init(clientKeyStore);

        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(serverKeyStore, passphrase.toCharArray());
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(),
                tmf.getTrustManagers(), secureRandom);
    }


    public static void main(String args[]) throws GeneralSecurityException {
        try {
            setupClientKeyStore();
            setupServerKeystore();
            setupSSLContext();
            int serverPort = 7899;
            SSLServerSocketFactory sf = sslContext.getServerSocketFactory();
            SSLServerSocket ss = (SSLServerSocket) sf.createServerSocket(serverPort);        
             ss.setNeedClientAuth(true);
            ServerSocket listenSocket = new ServerSocket(serverPort);
            int i = 0; 
            SecureRandom secureRandom = new SecureRandom();
            secureRandom.nextInt();
            while (true) {
                System.out.println("Server listening for a connection");
                Socket clientSocket = listenSocket.accept();
                i++;
                System.out.println("Received connection " + i);
                Connection c = new Connection(clientSocket);
            }
        } catch (IOException e) {
            System.out.println("Listen socket:" + e.getMessage());
        }
    }
}
