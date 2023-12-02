package Projects;

public class A32_TM_ClientModel {

    public A32_TM_ClientModel(){

    }
    public String encodeMsg(String userID, String funcID, String data){
        return (userID + "#" + funcID + "#" + data);

    }
    public String getServerUserID(String data){
        return data.substring(0, data.indexOf("#"));
    }
    public String getServerFuncID(String data) {
        int firstBreak = data.indexOf('#');
        if (firstBreak != -1) {
            int secondBreak = data.indexOf('#', firstBreak + 1);
            if (secondBreak != -1) {
                return data.substring(firstBreak + 1, secondBreak);
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

        return false;
    }

}
