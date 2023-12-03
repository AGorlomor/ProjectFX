package Projects;

import javafx.stage.Stage;

import java.net.*;
import java.io.*;
/**
 * A32_TM_ClientController is the controller class for the Turing Machine client application.
 * It manages the communication with the server, handles user input and events, and interacts
 * with the client model and view. The controller implements the Runnable interface to run
 * as a separate thread for handling server communication.
 *
 * This class is part of the Model-View-Controller (MVC) architecture, where it acts as
 * the controller that orchestrates the interaction between the model and the view.
 *
 * @author Alexey Rudoy
 * @version 3.6
 */
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
    String model = "";


    /**
     * Default constructor for A32_TM_ClientController class.
     * Initializes the client model and view and starts the view.
     *
     * @throws Exception If an error occurs during initialization.
     */
    public A32_TM_ClientController() throws Exception {
        this.clientModel = new A32_TM_ClientModel();
        this.stage = new Stage();
        clientView = new A32_TM_ClientView(this);
        clientView.start(this.stage);
        this.hostName = HOSTNAME;
        this.portNumber = PORT;
    }


    /**
     * Main method for the client application.
     *
     * @param args Command line arguments.
     * @throws Exception If an error occurs during execution.
     */
    public static void main() throws Exception {


    }
    /**
     * Connects to the server using the provided hostname and port number.
     * Starts a new thread to handle server communication.
     *
     * @param hostName   The hostname of the server.
     * @param portNumber The port number for communication.
     */
    public void connectToServer(String hostName, String portNumber) {

        this.hostName = hostName;
        this.portNumber = Integer.parseInt(portNumber);
        clientView.appendToOutput("Connecting to server on " + hostName + " at port " + portNumber);

        if (clientView != null) {
            new Thread(this).start();
        } else {
            System.err.println("clientView is null. Make sure it's properly initialized.");
        }


    }

    /**
     * Validates a Turing Machine model by sending a validation request to the server.
     *
     * @param potModel The potential Turing Machine model to be validated.
     * @return True if the model is valid; false otherwise.
     * @throws IOException If an I/O error occurs during validation.
     */

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
    /**
     * Sends a request to the server to receive the current Turing Machine model.
     *
     * @throws IOException If an I/O error occurs during the model retrieval.
     */
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

    /**
     * Sends a Turing Machine model to the server.
     *
     * @param model The Turing Machine model to be sent to the server.
     * @throws IOException If an I/O error occurs during the model sending.
     */
    public void sendModel(String model) throws IOException {
        funcID = "02";
        if (connected && sock != null && sock.isConnected()) {
            dat.println(clientModel.encodeMsg(strcliid, funcID, model));
        } else {
            clientView.appendToOutput("Not connected to the server.");
        }
    }
    /**
     * Disconnects from the server by sending a disconnection message.
     * Handles the cleanup and updates the client view accordingly.
     */
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


    /**
     * The run method for the separate thread handling server communication.
     * Listens for server responses and updates the client view based on the received data.
     */
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
                        strcliid = null;
                        sock = null;
                    case "02":
                        clientView.appendToOutput(dis.readLine());
                        //clientView.appendToOutput(dis.readLine());
                    case "03":
                        if (response != null) {

                            response = dis.readLine();
                            model = (response);

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
    /**
     * Retrieves the current Turing Machine model.
     *
     * @return The current Turing Machine model.
     */
    public String getModel(){
        return model;
    }

}
