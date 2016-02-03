package filesync;

import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

class SyncServerThread extends Thread {

    /**
     * Declarar las variable necesarias para establecer la comunicación con el
     * cliente. Será necesario un socket de servidor y uno de cliente para
     * escuchar y recibir los mensajes.
     */
    private static DataInputStream in;
    private static DataOutputStream out;
    private static ServerSocket listenSocket;
    private static Socket clientSocket;

    private static InstructionFactory instFact = new InstructionFactory();
    private static SynchronisedFile file;
    private static String filename;
    private static Boolean newNegotiation = true;
    private static String action = "";
    private static int blockSize;
    private String inst;

    public SyncServerThread() {

        try {
            /**
             * Inicializar variables para establecer comunicación con el cliente
             */
            listenSocket = new ServerSocket(7800);
        } catch (IOException ex) {
            Logger.getLogger(SyncServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void run() {
        int i = 0;

        while (true) {
            try { // an echo server

                if (newNegotiation) {
                    newNegotiation = false;

                    /**
                     * Para una nueva negociación el servidor debe escuchar
                     * peticiones y aceptar las peticiones entrantes. Luego debe
                     * instanciar los objetos para poder leer y escribir en el
                     * stream.
                     */
                    System.out.println("Servidor Esperando por conexiones");
                    clientSocket = listenSocket.accept();
                    System.out.println("Conexion Recibida");

                    in = new DataInputStream(clientSocket.getInputStream());
                    out = new DataOutputStream(clientSocket.getOutputStream());

                    /**
                     * El primer mensaje que debe recibir es el nombre del
                     * archivo a sincronizar y almacenarlo en filename. Enviar
                     * acuso de recibo.
                     */
                    System.out.println("Esperando nombre del archivo");
                    filename = in.readUTF();
                    System.out.println("Nombre de archivo recibido : " + filename);

                    System.out.println("Enviando Acuso de recibo : " + filename);
                    out.writeUTF("OK"); // el acuso de recibo

                    /**
                     * Y esperar por el siguiente mensaje que es el tamaño del
                     * bloque y enviar el correspondiente acuso de recibo.
                     */
                    System.out.println("Esperando el tamaño de bloque");
                    blockSize = in.readInt();
                    System.out.println("Tamaño de bloque recibido: " + blockSize);
                    System.out.println("Envio acuse de recibo");
                    out.writeUTF("OK"); // el acuso de recibo

                    /**
                     * Luego debe inicializar el objeto SynchronisedFile con los
                     * datos recibidos:
                     */
                    file = new SynchronisedFile(filename, blockSize);

                    /**
                     * Debe recibir la accion a realizar "commit" o "update" Si
                     * la accion es válida entonces debe enviar un acuso de
                     * recibo "OK"
                     */
                    System.out.println("Esperando accion");
                    action = in.readUTF();
                    System.out.println("Accion Recibida" + action);
                    System.out.println("Envio Acuso de Recibo");
                    out.writeUTF("OK"); // el acuso de recibo

                } else {
                    /**
                     * Si la acción no es válida el servidor debe mostrar el
                     * mensaje respectivo y terminar la conexión.
                     *
                     */
                    /*como receptor o emiso */
                    switch (action) {
                        case "commit":
                            inst = in.readUTF();
                            actAsReceiver(inst);
                        case "update":
                        //actAsSender();
                        default:
                            clientSocket.close();

                    }
                }

                /**
                 * Contrario al cliente, si la acción es "commit" el servidor
                 * actua como Receptor "update" el servidor actua como Emisor
                 */
            } catch (EOFException e) {
                System.out.println("EOF: " + e.getMessage());
                System.exit(-1);
            } catch (IOException e) {
                System.out.println("readline: " + e.getMessage());
                System.exit(-1);
            }
        }
    }

    private static void actAsReceiver(String msg) {
        try {
            /*
             * El servidor recibe la instrucción la cual debe ser
             * desempaquetada antes de ser procesada.
             * metodo FromJSON de la clase InstructionFactory
             */
            Instruction receivedInst = instFact.FromJSON(msg);
            try {
                // The Server processes the instruction
                file.ProcessInstruction(receivedInst);
            } catch (IOException e) {
                System.out.println(e.getMessage());
                System.exit(-1); // just die at the first sign of trouble
            } catch (BlockUnavailableException e) {
                // The server does not have the bytes referred to by the block hash.
                try {
                    /**
                     * Si se lanza esta excepción quiere decir que el servidor
                     * no tiene los bytes a los que hace referencia el bloque
                     * hash recibido. Por lo tanto el servidor debe enviar una
                     * petición al cliente para que le sean enviados los bytes
                     * reales contenidos en el bloque.
                     */
                    System.out.println("Bloque no encontrado. Pidiendo bytes reales");
                    out.writeUTF("NEW");

                    /*
                     * El servidor recibe el nuevo bloque de bytes
                     * los cuales deben ser desempaquetados antes
                     * de ser procesados.
                     * Utilizar el metodo fromJSON de la clase
                     * InstructionFactory
                     */
                    String new_block;
                    System.out.println("Servidor esperando los bytes reales");
                    new_block = in.readUTF();

                    Instruction receivedInst2 = instFact.FromJSON(new_block);
                    file.ProcessInstruction(receivedInst2);
                } catch (IOException e1) {
                    System.out.println(e1.getMessage());
                    System.exit(-1);
                } catch (BlockUnavailableException e1) {
                    assert (false); // a NewBlockInstruction can never throw this exception
                }
            }

            /*
             * Como estamos usando un protocolo 
             * peticion-respuesta, se debe enviar un
             * acuso de recibo para indicar que 
             * el bloque fue recibido correctamente y que la 
             * siguiente instrucción puede ser enviada.
             */
            System.out.println("Enviando Acuso");
            out.writeUTF("OK");
            //finalise sync
            if (receivedInst.Type().equals("EndUpdate")) {//El receptor sabe q se finaliza y continua sin problema
                System.out.println("Sincronizacion Finalizada.");
                newNegotiation = true; // la sincronización se ha finalizado, entonces la siguiente será una nueva negociación de parametros
                action = "";
                blockSize = 0;
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            System.exit(-1);
        }
    }

    private static void actAsSender() {
//		// arguments supply hostname filename
//
        Instruction inst;
        String reply = "";
//		
        try {
            System.out.println("SyncServer: calling fromFile.CheckFileState()");
            file.CheckFileState();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            System.exit(-1);
        } catch (InterruptedException ex) {
            System.out.println(ex.getMessage());
            System.exit(-1);
        }
//		
//		// The server reads instructions to send to the client
        while ((inst = file.NextInstruction()) != null) {
//			/**
//			 * El servidor envia las instrucciones de sincronización 
//			 * hacia el cliente
//			 * 
//			 * Los mensajes deben ser empaquetados utilizando el 
//			 * método ToJSON() dentro de la clase Instruction
//			 */
            try {
                String msg = inst.ToJSON();
                System.out.println("Sending: " + msg);
                out.writeUTF(msg);
//
                reply = in.readUTF();
                System.out.println("Received: " + reply);
//
//				/** Si el servidor envia como respuesta "NEW", quiere
//				 * decir que existe un cambio en el archivo y por lo
//				 * tanto el cliente debe cambiar el CopyBlock por un 
//				 * NewBlock
//				 */ 
                if (reply.equals("NEW")) {
//					/*
//					 * El servidor cambia la instrucción CopyBlock por 
//					 * una NewBlock y lo envia.
//					 * El mensaje debe ser empaquetado antes de 
//					 * enviarse.
//					 */
                    Instruction upgraded = new NewBlockInstruction((CopyBlockInstruction) inst);
                    String msg2 = upgraded.ToJSON();
//					
//					/**
//					 * Enviar la la nueva instrucción al servidor 
//					 * y recibir el acuso de recibo
//					 */ 
                    System.err.println("Sending: " + msg2);
                    out.writeUTF(msg2);
                }
            } catch (UnknownHostException e) {
                System.out.println("Socket: " + e.getMessage());
                System.exit(-1);
            } catch (EOFException e) {
                System.out.println("EOF: " + e.getMessage());
                System.exit(-1);
            } catch (IOException e) {
                System.out.println("readline: " + e.getMessage());
                System.exit(-1);
            }
//
//			/**
//			 * Verificar que el acuso de recibo es OK y moverse a 
//			 * la siguiente instrucción
//			 */
            while (!reply.equals("OK")) {
                try {
                    System.out.println("Esperando confrnacion de cliente");
                    reply = in.readUTF();
                } catch (Exception e) {
                    System.out.println("readline: " + e.getMessage());
                    System.exit(-1);
                }
            }
			 System.out.println("OK received. Move to the next instruction.");
//			
//			//finalise sync
			if( inst.Type().equals("EndUpdate")  ) {
				System.out.println("Sync finalised.");
				newNegotiation = true;// se puede empezar una nueva negociación de parámetros.
				action = "";
				blockSize = 0;
				break;
			}
		}
        }
    }
