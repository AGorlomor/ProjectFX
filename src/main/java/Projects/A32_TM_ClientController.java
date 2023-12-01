package Projects;

import javafx.application.Application;
import javafx.stage.Stage;
import java.net.*;
import java.io.*;

public class A32_TM_ClientController {
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
    static A32_TM_ClientView clientView;
    static A32_TM_ClientModel clientModel;
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
    public A32_TM_ClientController() {
        this.clientModel = new A32_TM_ClientModel();
        this.stage =new Stage();
    }
    public void startClientView() throws Exception {
        clientView = new A32_TM_ClientView(this);
        clientView.start(stage);


    }
    /**
     * Main method.
     * @param args Param arguments.
     */
    public static void main(String args[]) throws Exception {
        hostName = HOSTNAME;
        portNumber = PORT;

    }
    public void connectToServer(String hostName, String portNumber){
        this.hostName = hostName;
        this.portNumber = Integer.parseInt(portNumber);
        clientView.appendToOutput("Connecting with server on " + hostName + " at port " + portNumber);
        clientView.appendToOutput("Starting Server Thread on port " + portNumber);

        try {
            this.sock = new Socket(hostName, Integer.parseInt(portNumber));
            this.dis = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            this.strcliid = dis.readLine();
            clientView.appendToOutput("Client no." + strcliid + "...");
            connected = true;
            String consoleData;
            String serverData;
            ///DataInputStream inConsole = new DataInputStream(System.in);
            //BufferedReader inConsole = new BufferedReader(new InputStreamReader(System.in));
            clientView.appendToOutput("Client[" + strcliid + "]: ");
            /**
            consoleData = inConsole.readLine();

            while (!consoleData.equals("end")) {
                consoleData = strcliid + "#" + consoleData;
                dat.println(consoleData);
                dat.flush();
                serverData = dis.readLine();
                clientView.appendToOutput("Server: " + serverData);
                clientView.appendToOutput("Client[" + strcliid + "]: ");
                consoleData = inConsole.readLine();
            }

            consoleData = strcliid + "#" + consoleData;
            dat.println(consoleData);

            dat.flush();

            sock.close();
             **/
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

    }
    public void recieveModel() throws IOException {
        funcID = "03";
        this.dat = new PrintStream(this.sock.getOutputStream());
        this.dis = new BufferedReader(new InputStreamReader(this.sock.getInputStream()));
        dat.println(clientModel.encodeMsg(strcliid, funcID, "00"));
        dat.flush();
        model =dis.readLine();
        clientView.setmodelTM(clientModel.getServerMsg(model));
        clientView.appendToOutput("Server model:" + model);

    }
    public void sendModel(String model) throws IOException {
        funcID = "02";
        this.dat = new PrintStream(this.sock.getOutputStream());
        this.dis = new BufferedReader(new InputStreamReader(this.sock.getInputStream()));
        // encMassage = strcliid + "#02#" + model;
        dat.println(clientModel.encodeMsg(strcliid,funcID,model));
        dat.flush();
        clientView.appendToOutput(dis.readLine());
        clientView.appendToOutput(dis.readLine());
        clientView.appendToOutput(dis.readLine());
    }
    public void disconnect() {
        try {
            funcID = "01";
            this.dat = new PrintStream(this.sock.getOutputStream());
            this.dis = new BufferedReader(new InputStreamReader(this.sock.getInputStream()));

            // Send disconnection message
            //String encMessage = strcliid + "#01#000";
            dat.println(clientModel.encodeMsg(strcliid,funcID,"000"));
            dat.flush();

            // Read the response from the server
            String serverResponse = dis.readLine();
            clientView.appendToOutput(serverResponse);

            // Close the socket
            sock.close();
            clientView.appendToOutput("Disconnected from the server.");

            // Update local state for disconnection
            connected = false;
            strcliid = null; // Reset client ID or set it to a default value
            sock = null; // Set the socket to null or any appropriate value

        } catch (IOException e) {
            clientView.appendToOutput("Error during disconnection: " + e.getMessage());
        }
    }


}
