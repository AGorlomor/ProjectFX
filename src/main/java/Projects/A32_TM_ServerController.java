package Projects;

import javafx.stage.Stage;
import java.io.*;
import java.net.*;

public class A32_TM_ServerController implements Runnable {
    /**
     * Socket variable.
     */
    Socket sock;

    /**
     * Variables for number clients.
     */
    static int nclient = 0, nclients = 0;

    /**
     * Server socket.
     */
    static ServerSocket servsock;

    /**
     * Default port.
     */
    static int PORT = 3000;

    /**
     * Number of port.
     */
    static int portNumber = 0;

    /**
     * Default constructor.
     */
    A32_TM_ServerModel serverModel;
    ProtocolSC protocol;
    static A32_TM_ServerView serverView;
    static Stage stage;
    String modelTM = "";
    public A32_TM_ServerController(){
        this.stage = new Stage();
        this.serverModel = new A32_TM_ServerModel();


    }
    public static void startView() throws Exception {
        serverView = new A32_TM_ServerView();
        serverView.start(stage);
    }

    /**
     * Main method.
     * @param args Param arguments.
     */
    public static void main(String args[]) throws Exception {
        int portNumber;
        startView();
        if (args == null || args.length < 1) {
            // If no arguments are provided, use the default port
            portNumber = PORT;
        } else {
            // Use the provided port from the command line argument
            portNumber = Integer.parseInt(args[0]);
        }



    }
    public void startServer(String port){
        portNumber = Integer.parseInt(port);
        serverView.appendToOutput("Starting Server Thread");
        try {
            servsock = new ServerSocket(portNumber);
            Thread servDaemon = new Thread(new A32_TM_ServerController());
            servDaemon.start();
            serverView.appendToOutput("Server running on " + InetAddress.getLocalHost() + " at port " + portNumber + "!");
        } catch (Exception e) {
            serverView.appendToOutput("Error: " + e.toString());
        }

    }



    /**
     * Run method.
     */
    public void run() {
        for (;;) {
            try {
                sock = servsock.accept();
                nclient += 1;
                nclients += 1;
                serverView.appendToOutput("Connecting " + sock.getInetAddress() + " at port " + sock.getPort() + ".");
            } catch (IOException ioe) {
                System.out.println(ioe);
            }
            Worked w = new Worked(sock, nclient);
            w.start();
        }
    }




    /**
     * Inner class for Theads.
     * @author sousap
     *
     */
    class Worked extends Thread {

        /**
         * Socket variable.
         */
        Socket sock;

        /**
         * Integers for client and positions.
         */
        int clientid, firstBreak, secondBreak;

        /**
         * String for data.
         */
        String strcliid, funchID, clientMsg;

        /**
         * Default constructor.
         * @param s Socket
         * @param nclient Number of client.
         */
        public Worked(Socket s, int nclient) {
            this.sock = s;
            this.clientid = nclient;
        }

        /**
         * Run method.
         */
        public void run() {
            String data;
            PrintStream out = null;
            BufferedReader in;
            try {
                out = new PrintStream(sock.getOutputStream());
                in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                out.println(clientid);


                data = in.readLine();
                firstBreak = data.indexOf("#");
                strcliid = data.substring(0, firstBreak);
                secondBreak = data.substring(firstBreak+1,data.length()).indexOf("#");
                funchID = data.substring(firstBreak+1,secondBreak+2);
                clientMsg = data.substring(secondBreak+3, data.length());
                while (sock.isConnected()) {
                    serverView.appendToOutput("Cli[" + strcliid + "]: " + data);
                    serverView.appendToOutput("funcID:" + funchID);
                    serverView.appendToOutput("MSG:" + clientMsg);
                    switch (funchID){
                        case "01":
                            serverView.appendToOutput("Disconecting " + sock.getInetAddress() + "!");
                            nclients -= 1;
                            serverView.appendToOutput("Current client number: " + nclients);
                            if (nclients == 0) {

                                sock.close();
                                //System.exit(0);
                            }
                            break;
                        case "02":
                            modelTM = clientMsg;
                            out.println("String: \"" + data + "\" received.\n User: " +strcliid+" funcID: " + funchID + " MSG: " + clientMsg + "\n Server model: " + modelTM);
                            out.flush();
                            break;
                        case "03":
                            out.println(strcliid + "#" + funchID + "#" +modelTM);
                            out.flush();
                            break;
                        default:
                    }

                    data = in.readLine();
                    firstBreak = data.indexOf("#");
                    strcliid = data.substring(0, firstBreak);
                    secondBreak = data.substring(firstBreak+1,data.length()).indexOf("#");
                    funchID = data.substring(firstBreak+1,secondBreak+2);
                    clientMsg = data.substring(secondBreak+3, data.length());
                }


            } catch (IOException ioe) {
                System.out.println(ioe);
            }
        }
        public void updateModel(){

        }
    }
}
