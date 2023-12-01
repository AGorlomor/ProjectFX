package Projects;

import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetAddress;

public class A32_TM_ServerController implements Runnable {
    static int nclient = 0, nclients = 0;
    static ServerSocket servsock;
    static int PORT = 3000;
    static int portNumber = 0;

    A32_TM_ServerModel serverModel;
    ProtocolSC protocol;
    static A32_TM_ServerView serverView;
    static Stage stage;
    String modelTM = "";

    public A32_TM_ServerController() {
        this.stage = new Stage();
        this.serverModel = new A32_TM_ServerModel();
    }

    public static void startView() throws Exception {
        serverView = new A32_TM_ServerView();
        serverView.start(stage);
    }

    public static void main(String args[]) throws Exception {
        int portNumber;
        startView();
        if (args == null || args.length < 1) {
            portNumber = PORT;
        } else {
            portNumber = Integer.parseInt(args[0]);
        }
    }

    public void startServer(String port) {
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

    public void run() {
        for (;;) {
            try {
                Socket clientSocket = servsock.accept();
                nclient += 1;
                nclients += 1;
                serverView.appendToOutput("Connecting " + clientSocket.getInetAddress() + " at port " + clientSocket.getPort() + ".");
                Worked w = new Worked(clientSocket, nclient);
                w.start();
            } catch (IOException ioe) {
                System.out.println(ioe);
            }
        }
    }

    class Worked extends Thread {
        Socket sock;
        int clientid, firstBreak, secondBreak;
        String strcliid, funchID, clientMsg;
        String data;

        public Worked(Socket s, int nclient) {
            this.sock = s;
            this.clientid = nclient;
        }

        public void run() {

            try {
                // Create separate instances of PrintStream and BufferedReader for each client
                PrintStream out = new PrintStream(this.sock.getOutputStream());
                BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));

                out.println(clientid);

                data = in.readLine();
                firstBreak = data.indexOf("#");
                strcliid = data.substring(0, firstBreak);
                secondBreak = data.substring(firstBreak + 1, data.length()).indexOf("#");
                funchID = data.substring(firstBreak + 1, secondBreak + 2);
                clientMsg = data.substring(secondBreak + 3, data.length());

                while (sock.isConnected()) {
                    // Create a new PrintStream for each client iteration
                    PrintStream clientOut = new PrintStream(this.sock.getOutputStream());
                    clientOut.println("Cli[" + strcliid + "]: " + data);

                    switch (funchID) {
                        case "01":
                            clientOut.println("Disconnecting " + sock.getInetAddress() + "!");
                            nclients -= 1;
                            clientOut.println("Current client number: " + nclients);
                            if (nclients == 0) {
                                sock.close();
                            }
                            break;
                        case "02":
                            modelTM = clientMsg;
                            clientOut.println("String: \"" + data + "\" received. User: " + strcliid + " funcID: " + funchID + " MSG: " + clientMsg + " Server model: " + modelTM);
                            clientOut.flush();
                            break;
                        case "03":
                            clientOut.println(strcliid + "#" + funchID + "#" + modelTM);
                            clientOut.flush();
                            break;
                        default:
                    }

                    this.data = in.readLine();
                    firstBreak = data.indexOf("#");
                    strcliid = data.substring(0, firstBreak);
                    secondBreak = data.substring(firstBreak + 1, data.length()).indexOf("#");
                    funchID = data.substring(firstBreak + 1, secondBreak + 2);
                    clientMsg = data.substring(secondBreak + 3, data.length());
                }
            } catch (IOException ioe) {
                System.out.println(ioe);
            }
        }

        public void updateModel() {
            // Your updateModel code
        }
    }
    public void dropAllConnections() {
        try {
            serverView.appendToOutput("Dropping all client connections.");

            // Iterate through the connected clients and close their sockets
            for (Thread thread : Thread.getAllStackTraces().keySet()) {
                if (thread instanceof Worked) {
                    Worked workedThread = (Worked) thread;

                    // Synchronize the block to avoid potential concurrent modification
                    synchronized (workedThread) {
                        Socket clientSocket = workedThread.sock;

                        // Close the socket and interrupt the thread
                        clientSocket.close();
                        workedThread.interrupt();

                        serverView.appendToOutput("Disconnected client: " + clientSocket.getInetAddress() +
                                " at port " + clientSocket.getPort());
                    }
                }
            }

            // Close the server socket
            servsock.close();
            serverView.appendToOutput("Server socket closed.");
        } catch (IOException e) {
            serverView.appendToOutput("Error while dropping connections: " + e.toString());
        }
    }


}
