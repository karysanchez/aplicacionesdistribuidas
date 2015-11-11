/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ipadd;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 *
 * @author 22B
 */
public class Ipcomputador {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws UnknownHostException {
        InetAddress com=InetAddress.getLocalHost();
        System.out.println("IP Computadora: "+com.getHostAddress());
        InetAddress comp=InetAddress.getByName("www.epn.edu.ec");
        System.out.println("URL: "+comp.getHostAddress());
        
    }
    
}
