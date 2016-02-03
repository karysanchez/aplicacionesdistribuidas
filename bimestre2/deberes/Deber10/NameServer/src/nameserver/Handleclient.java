package nameserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.StringTokenizer;

public class Handleclient extends Thread {

    private static NameTable table = new NameTable();


    
    private Socket theCliente;

    public Handleclient(Socket theClient) {

            this.theCliente = theClient;
        
            
    }

    public void run() {
        try {
            BufferedReader din = new BufferedReader(new InputStreamReader(theCliente.getInputStream()));
            PrintWriter pout = new PrintWriter(theCliente.getOutputStream());
            String getline = din.readLine();
            System.out.println(getline);

            StringTokenizer st = new StringTokenizer(getline);//separe por espacios 

            String tag = st.nextToken();   //se mueve
            if (tag.equals("search")) {
                int index = table.search(st.nextToken());//if es search se mueve el puntador
                System.out.println(index);
                if (index == -1) //  not  found
                {
                    pout.println(-1 + " " + "nullhost");
                } else {
                    pout.println(table.getPort(index) + " " + table.getHostName(index));
                }
            } else if (tag.equals("insert")) {
                String name = st.nextToken();
                String hostName = st.nextToken();
                int port = Integer.parseInt(st.nextToken());
                int retValue = table.insert(name, hostName, port);
                System.out.println(retValue);
                pout.println(retValue);
            }
            pout.flush();
        } catch (IOException e) {
            System.err.println(e);
        }finally{
            try {
                theCliente.close();
            } catch (Exception e) {
            }
        }

    }

}
