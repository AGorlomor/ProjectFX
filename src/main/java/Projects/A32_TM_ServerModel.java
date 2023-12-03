package Projects;

import java.util.HashMap;
/**
 * A32_TM_ServerModel represents the data model for the Turing Machine server application.
 * It encapsulates the functionality related to encoding and decoding messages, extracting
 * information from messages, and validating Turing Machine models.
 *
 * This class is part of the Model-View-Controller (MVC) architecture, where it acts as
 * the model that holds and manages data used by the server.
 *
 * @author Alexey Rudoy
 * @version 1.7
 */
public class A32_TM_ServerModel {
    A32_TM_ServerController serverController;
    /**
     * Constructor for A32_TM_ServerModel class. Initializes the server controller.
     *
     * @param serverController The server controller associated with this model.
     */
    public A32_TM_ServerModel(A32_TM_ServerController serverController){
        this.serverController = serverController;

    }
    /**
     * Encodes a message using the specified user ID, function ID, and data.
     *
     * @param userID The user ID for the message.
     * @param funcID The function ID for the message.
     * @param data   The data to be encoded in the message.
     * @return The encoded message as a string.
     */
    public String encodeMsg(String userID, String funcID, String data){
        return (userID + "#" + funcID + "#" + data);

    }
    /**
     * Retrieves the client user ID from the given data string.
     *
     * @param data The data string containing the user ID.
     * @return The client user ID.
     */
    public String getClientUserID(String data){
        return data.substring(0, data.indexOf("#"));
    }
    /**
     * Retrieves the client function ID from the given data string.
     *
     * @param data The data string containing the function ID.
     * @return The client function ID.
     */
    public String getClientFuncID(String data){
        int firstBreak = data.indexOf("#");
        if (firstBreak != -1) {
            int secondBreak = data.indexOf("#", firstBreak + 1);
            if (secondBreak != -1) {
                return data.substring(firstBreak + 1, secondBreak + 1);
            }
        }
        return null;

    }
    /**
     * Extracts the server message from the given data string.
     *
     * @param data The data string containing the server message.
     * @return The server message.
     */
    public String getServerMsg(String data){
        String serverMsg = data.substring(data.substring(data.indexOf("#")+1,data.length()).indexOf("#")+3, data.length());
        return serverMsg;
    }
    /**
     * Validates a potential Turing Machine model by checking its length and character contents.
     *
     * @param potModel The potential Turing Machine model to be validated.
     * @return True if the model is valid; false otherwise.
     */
    public boolean isValidModel(String potModel){
        String model = potModel.replace(" ","");
        if(model.length()%5 !=0){
            return false;
        }
        for(int i = 0; i <model.length(); i++){

            if (model.charAt(i) != '0' && model.charAt(i) != '1'){
                return false;
            }
        }
        return true;
    }
}
