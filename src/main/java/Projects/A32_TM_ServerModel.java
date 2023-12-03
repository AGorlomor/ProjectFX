package Projects;

import java.util.HashMap;

public class A32_TM_ServerModel {
    A32_TM_ServerController serverController;
    HashMap<String,String> hashMap;

    public A32_TM_ServerModel(A32_TM_ServerController serverController){
        this.serverController = serverController;

    }
    public String encodeMsg(String userID, String funcID, String data){
        return (userID + "#" + funcID + "#" + data);

    }
    public String getClientUserID(String data){
        return data.substring(0, data.indexOf("#"));
    }
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
    public String getServerMsg(String data){
        String serverMsg = data.substring(data.substring(data.indexOf("#")+1,data.length()).indexOf("#")+3, data.length());
        return serverMsg;
    }
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
