/**
 * Created by CIA on 08/10/2017.
 */
public class ChatProtocol {

    String serverIpPort = "localhost:4444";

    public String handleUserMessage(String message, String ip){
        String pCommand = message.substring(0, 4);

        if (pCommand.equals("JOIN")) {
            return handleJoin(message);
        }
        else if (pCommand.equals("DATA")) {
            return handleData(message, ip);
        }
        else if (pCommand.equals("IMAV")) {
            return  handleIMAV(message);
        }
        else if (pCommand.equals("QUIT")) {
            return  handleQuit(message);
        }
        else {
            return "Error";
        }
    }

    private String handleJoin(String message){

        if (message.length() > 3) {
            String[] jCommands = message.split(" ");
            String userName = jCommands[1].replace(",", "");
            String ipPort = jCommands[2];

            if (!ipPort.equals(serverIpPort)){
                return "J_ER 419: Wrong IP:PORT";
            }

            for (ServerThread user : ChatServer.users) {

                if (user.getUsername().equals(userName)){
                    return "J_ER 420: Username exists.";
                }

            }

            if (userName.length() > 12){
                return "J_ER 418: Username too long.";
            }

            if (!userName.matches("^[a-zA-Z0-9_+-]*$")) {
                return "J_ER 417: Username characters invalid";
            }

            return "J_OK" + userName;

        }

        return "J_ER 499: No inpur";
    }

    private String handleData(String message, String ip) {
        String messageToSend = message.substring(4, message.length());
        String[] jCommands = message.split(" ");
        String userName = jCommands[1].replace(":", "");

        for (ServerThread user : ChatServer.users) {
            if (user.getUsername().equals(userName)) {
                if (user.getSocket().getInetAddress().toString().equals(ip)) {
                    return "DATA" + messageToSend;
                }
                else {
                    return "J_ER 411: IP mismatch";
                }
            }
        }
        return "J_ER 410: User not found";
    }

    private String handleIMAV(String message) {
        return "";
    }
    private String handleQuit(String message) {
        return "";
    }

}
