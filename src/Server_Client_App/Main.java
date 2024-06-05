package Server_Client_App;

public class Main {
    public static void main(String[] args) {

        ServerWindow serverWindow = new ServerWindow(); //We give object of server to the client and client can use send message methods
        new ClientGUI(serverWindow);
        new ClientGUI(serverWindow);
    }
}