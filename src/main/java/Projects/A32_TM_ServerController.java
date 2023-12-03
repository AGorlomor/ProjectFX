package Projects;

import javafx.stage.Stage;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

public class A32_TM_ServerController implements Runnable {
    static int nclient = 0, nclients = 0;
    static ServerSocket servsock;
    static int PORT = 3000;
    static int portNumber = 0;

    A32_TM_ServerModel serverModel;
    ProtocolSC protocol;
    static A32_TM_ServerView serverView;
    static Stage stage;
    String modelTM = "00000 01000 10010 11000";
    String tape = "000000000000000000000000000000";

    private static Map<Integer, Worked> workedMap = new HashMap<>();

    public A32_TM_ServerController() {
        this.stage = new Stage();
        this.serverModel = new A32_TM_ServerModel(this);
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
        for (; ; ) {
            try {
                Socket clientSocket = servsock.accept();
                nclient += 1;
                nclients += 1;
                serverView.appendToOutput("Connecting " + clientSocket.getInetAddress() + " at port " + clientSocket.getPort() + ".");
                Worked w = new Worked(clientSocket, nclient);
                workedMap.put(nclient, w);
                w.start();
            } catch (IOException ioe) {
                System.out.println(ioe);
            }
        }
    }

    class Worked extends Thread {
        Socket sock;
        int clientId;
        String strcliid = "";
        String funchID;
        String clientMsg;
        String data;
        PrintStream clientOut;
        BufferedReader in;

        public Worked(Socket s, int nclient) {
            this.sock = s;
            this.clientId = nclient;
            try {
                this.clientOut = new PrintStream(sock.getOutputStream());
                this.in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Broadcast a message to a specific client
        private void sendMessageToClient(int clientId, String message) {
            Worked client = workedMap.get(clientId);

            if (client != null) {
                client.clientOut.println(message);
            }
        }

        public void run() {
            try {
                clientOut.println(clientId);
                System.out.println("Thread for client " + clientId + ": " + Thread.currentThread().getId());
                System.out.println("Port for client " + clientId + ": " + sock.getPort());

                while (this.sock.isConnected()) {
                    this.data = in.readLine();
                    if (data == null) {
                        break;
                    }

                    int firstBreak = data.indexOf("#");
                    strcliid = data.substring(0, firstBreak);
                    int secondBreak = data.substring(firstBreak + 1, data.length()).indexOf("#");
                    funchID = data.substring(firstBreak + 1, secondBreak + 2);
                    clientMsg = data.substring(secondBreak + 3, data.length());
                    clientOut.println(Integer.parseInt(strcliid) + "Cli[" + strcliid + "]: " + data);
                    if (clientId != Integer.parseInt(strcliid)) {
                    } else {
                        switch (funchID) {
                            case "01":
                                sendMessageToClient(clientId, "Disconnecting " + this.sock.getInetAddress() + "!");
                                nclients -= 1;
                                sendMessageToClient(clientId, "Current client number: " + nclients);
                                this.sock.close();
                                break;
                            case "02":
                                if (serverModel.isValidModel(clientMsg)) {
                                    modelTM = clientMsg.replace(" ", "");
                                    clientOut.println(clientId + "String: \"" + data + "\" received. User: " + " Server model: " + modelTM);
                                    serverView.appendToOutput(modelTM);
                                    clientOut.flush();
                                } else {
                                    clientOut.println(clientId + "Not valid Model format, Model not set.");
                                }

                                break;
                            case "03":

                                clientOut.flush();
                                clientOut.println(modelTM);
                                clientOut.flush();
                                break;
                            case "04":
                                if (serverModel.isValidModel(clientMsg)) {
                                    clientOut.println(strcliid + "#04#11");
                                } else {
                                    clientOut.println(strcliid + "#04#00");
                                }
                            case "05":
                                clientOut.println(modelTM);

                            default:
                        }
                    }
                }
            } catch (IOException ioe) {
                System.out.println(ioe);
            } finally {
                clientOut.close();
                try {
                    in.close();
                    this.sock.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}