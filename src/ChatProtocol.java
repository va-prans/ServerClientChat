/**
 * Created by CIA on 08/10/2017.
 */
public class ChatProtocol {

    String serverIpPort = "localhost:4444";

    public String handleUserMessage(String message){

        String pCommand = message.substring(0, 4);
        System.out.println(pCommand);

        return null;
    }

    private String handleJoin(String message){

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




        return userName;
    }

}
