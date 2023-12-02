    package Projects;

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
            clientView.appendToOutput("Connecting to server on " + hostName + " at port " + portNumber);
            try {
                this.sock = new Socket(hostName, Integer.parseInt(portNumber));
                this.dis = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                this.strcliid = this.dis.readLine();
                clientView.appendToOutput("Client no." + strcliid + "...");
                connected = true;
                clientView.appendToOutput("Client[" + strcliid + "]: ");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }

        }
        public void receiveModel() throws IOException {
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
        }


        public void sendModel(String model) throws IOException {
            if (connected && sock != null && sock.isConnected()) {
                try {
                    funcID = "02";
                    this.dat = new PrintStream(this.sock.getOutputStream());
                    this.dis = new BufferedReader(new InputStreamReader(this.sock.getInputStream()));
                    this.dat.println(this.clientModel.encodeMsg(strcliid, funcID, model));
                    this.dat.flush();
                    this.clientView.appendToOutput(this.dis.readLine());
                    this.clientView.appendToOutput(this.dis.readLine());
                } catch (IOException e) {
                    clientView.appendToOutput("Error sending model: " + e.getMessage());
                }
            } else {
                clientView.appendToOutput("Not connected to the server.");
            }
        }

        public void disconnect() {
            try {
                if (connected && sock != null && sock.isConnected()) {
                    funcID = "01";
                    this.dat = new PrintStream(this.sock.getOutputStream());
                    this.dis = new BufferedReader(new InputStreamReader(this.sock.getInputStream()));

                    // Send disconnection message
                    dat.println(clientModel.encodeMsg(strcliid, funcID, "000"));
                    dat.flush();

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
                } else {
                    clientView.appendToOutput("Not connected to the server.");
                }
            } catch (IOException e) {
                clientView.appendToOutput("Error during disconnection: " + e.getMessage());
            }
        }



    }
