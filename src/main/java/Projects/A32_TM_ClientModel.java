package Projects;

public class A32_TM_ClientModel {

    public A32_TM_ClientModel(){

    }
    public String encodeMsg(String userID, String funcID, String data){
        return (userID + "#" + funcID + "#" + data);

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



}
