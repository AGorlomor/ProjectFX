package Projects;

public class A32_TM_ServerModel {
    A32_TM_ServerController serverController;

    public A32_TM_ServerModel(A32_TM_ServerController serverController){
        this.serverController = serverController;

    }
    public String encodeMsg(String userID, String funcID, String data){
        return (userID + "#" + funcID + "#" + data);

    }
    public String getServerUserID(String data){
        return data.substring(0, data.indexOf("#"));
    }
    public String getServerFuncID(String data){
        int secondBreak = data.substring(data.indexOf("#")+1,data.length()).indexOf("#");
        String funchID = data.substring(data.indexOf("#")+1,secondBreak+2);
        return funchID;

    }
    public String getServerMsg(String data){
        String serverMsg = data.substring(data.substring(data.indexOf("#")+1,data.length()).indexOf("#")+3, data.length());
        return serverMsg;
    }
    public boolean isValidModel(String potModel){
        String model = potModel.replace(" ","");

        return false;
    }
    public boolean validateModel(){
        return false;
    }
}
