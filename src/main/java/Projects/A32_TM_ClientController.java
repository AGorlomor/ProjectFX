package Projects;

import javafx.stage.Stage;

import java.net.*;
import java.io.*;

public class A32_TM_ClientController implements Runnable {
    /**
     * Default port.
     */
    static int PORT = 3000;

    /**
     * Number of port.
     */
    static int portNumber = 0;

    /**
     * Default hostname.
     */
    static String HOSTNAME = "localhost";

    /**
     * Variable for hostname.
     */
    static String hostName = "";
    A32_TM_ClientView clientView;
    A32_TM_ClientModel clientModel;
    static Stage stage;
    ProtocolSC protocol;
    boolean connected = false;
    String strcliid;
    Socket sock;
    BufferedReader dis;
    PrintStream dat;
    String funcID;
    String model;


    /**
     * Default constructor.
     */
    public A32_TM_ClientController() throws Exception {
        this.clientModel = new A32_TM_ClientModel();
        this.stage = new Stage();
        clientView = new A32_TM_ClientView(this);
        clientView.start(this.stage);
    }


    /**
     * Main method.
     *
     * @param args Param arguments.
     */
    public static void main(String args[]) throws Exception {
        hostName = HOSTNAME;
        portNumber = PORT;

    }

    public void connectToServer(String hostName, String portNumber) {

        this.hostName = hostName;
        this.portNumber = Integer.parseInt(portNumber);
        clientView.appendToOutput("Connecting to server on " + hostName + " at port " + portNumber);

        // Ensure clientView is not null before starting the thread
        if (clientView != null) {
            new Thread(this).start();
        } else {
            System.err.println("clientView is null. Make sure it's properly initialized.");
        }


    }

    public boolean validateModel(String potModel) throws IOException {
        funcID = "04";
        //clientView.appendToOutput(dis.readLine());
        // Send validation request to the server
        dat.println(clientModel.encodeMsg(this.strcliid, funcID, potModel));
        dat.flush();

        // Read the response from the server
        String response = dis.readLine();

        // Check if the response is valid
        if (clientModel.getServerFuncID(response).equals(funcID)) {
            String serverMsg = clientModel.getServerMsg(response);
            if ("11".equals(serverMsg)) {
                clientView.appendToOutput("Model validation successful");
                return true;
            } else {
                clientView.appendToOutput("Model validation failed. Server response: " + serverMsg);
            }
        } else {
            clientView.appendToOutput("Invalid server response. Expected funcID: " + funcID);
        }

        return false;
    }

    public void receiveModel() throws IOException {
        funcID = "03";
        if (connected && sock != null && sock.isConnected()) {
            this.dat.println(clientModel.encodeMsg(strcliid, funcID, "00"));

        } else {
            clientView.appendToOutput("Not connected to the server.");
        }
        /**
        funcID = "03";
        this.dat = new PrintStream(this.sock.getOutputStream());
        this.dis = new BufferedReader(new InputStreamReader(this.sock.getInputStream()));
        dat.println(clientModel.encodeMsg(strcliid, funcID, "00"));
        dat.flush();

        // Read the response from the server
        String response = dis.readLine();

        if (response != null) {
            response = dis.readLine();
            model = (response);
            clientView.setmodelTM(model);
            clientView.appendToOutput("Server model: " + model);
        } else {
            clientView.appendToOutput("Error receiving model.");
        }
         **/
    }


    public void sendModel(String model) throws IOException {
        funcID = "02";
        if (connected && sock != null && sock.isConnected()) {
            dat.println(clientModel.encodeMsg(strcliid, funcID, model));
        } else {
            clientView.appendToOutput("Not connected to the server.");
        }
    }

    public void disconnect() {

            if (connected && sock != null && sock.isConnected()) {
                funcID = "01";
                // Send disconnection message
                dat.println(clientModel.encodeMsg(strcliid, funcID, "000"));
                dat.flush();


            } else {
                clientView.appendToOutput("Not connected to the server.");
            }
    }



    @Override
    public void run() {

        try {

            this.sock = new Socket(hostName, portNumber);
            this.dis = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            this.strcliid = this.dis.readLine();
            clientView.appendToOutput("Client no." + strcliid + "...");

            connected = true;


            while (sock.isConnected()) {
                this.dat = new PrintStream(this.sock.getOutputStream());
                this.dis = new BufferedReader(new InputStreamReader(this.sock.getInputStream()));
                String response = dis.readLine();

                // Check if response is null
                if (response == null) {
                    // Handle the situation where the response is null
                    break;
                }

                String serverFunc = clientModel.getServerFuncID(response);

                switch (serverFunc) {
                    case "01":
                        // Read the response from the server
                        String serverResponse = this.dis.readLine();
                        this.clientView.appendToOutput(serverResponse);

                        // Close the socket
                        sock.close();
                        this.clientView.appendToOutput("Disconnected from the server.");

                        // Update local state for disconnection
                        connected = false;
                        strcliid = null; // Reset client ID or set it to a default value
                        sock = null; // Set the socket to null or any appropriate value
                    case "02":
                        clientView.appendToOutput(dis.readLine());
                        //clientView.appendToOutput(dis.readLine());
                    case "03":
                        if (response != null) {
                            /**
                            response = dis.readLine();
                            model = (response);
                             **/
                            clientView.setmodelTM(model);
                            clientView.modelTextField.setText(model);
                            clientView.appendToOutput("Server model: " + model);
                        } else {
                            clientView.appendToOutput("Error receiving model.");
                        }
                    case "05":
                        model = dis.readLine();
                    case "06":


                    default:
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public String getModel(){
        return model;
    }

}
