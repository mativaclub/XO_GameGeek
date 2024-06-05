package Server_Client_App;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ClientGUI extends JFrame {
    public static final int WIDTH = 400; //Client window size
    public static final int HEIGHT = 300;

    private ServerWindow server; //Connecting to server
    private boolean connected; //Check we are connected or no
    private String name; //save login name from whom the message was received

    private JTextArea log; //Lots of string of texts
    private JTextField tfIPAddress, tfPort, tfLogin, tfMessage; //Field to input a message
    private JPasswordField password;
    private JButton btnLogin, btnSend;
    private JPanel headerPanel;

    public ClientGUI(ServerWindow server){
        this.server = server;

        setSize(WIDTH, HEIGHT);
        setResizable(false); //We can't change the size of the window
        setTitle("Chat client");
        setLocation(server.getX() - 500, server.getY());

        createPanel();

        setVisible(true); //Set to visible our graphic window
    }

    public void answer(String text){ //Receive an answer from the server
        appendLog(text); //We add an answer to our panel
    }

    private void connectToServer() { //This method will work after we click on the login
        if (server.connectUser(this)){ //we ask server to connect us
            appendLog("Вы успешно подключились!\n"); //Successful connection
            headerPanel.setVisible(false); //Close the first window
            connected = true; //flag=connected
            name = tfLogin.getText(); //save name for visibility name in every message
            String log = server.readLog(); //Return to us the history of conversation
            if (log != null){
                appendLog(log); //add it in our text field
            }
        } else {
            appendLog("Подключение не удалось");
        }
    }

    public void disconnectFromServer() { //Method for disconnecting from server
        if (connected) {
            headerPanel.setVisible(true); //Turn on authorization panel
            connected = false;
            server.disconnectUser(this); //Let the server know that we are disconnected
            appendLog("Вы были отключены от сервера!");
        }
    }

    public void closeWindow() { //Method for disconnecting from server when closing the window
        if (connected) {
            server.disconnectUser(this); //Let the server know that we are disconnected
        }
    }

    public void message(){ //method of sending message - send message or enter
        if (connected){ //if we are connected
            String text = tfMessage.getText(); //we get a text from text field
            if (!text.equals("")){ //If text exists
                server.message(name + ": " + text); //server receive a message
                tfMessage.setText(""); //clear the text field
            }
        } else {
            appendLog("Нет подключения к серверу");
        }

    }

    private void appendLog(String text){
        log.append(text + "\n");
    }

    private void createPanel() { //creating widgets, panels and their orientation
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createLog());
        add(createFooter(), BorderLayout.SOUTH);
    }

    private Component createHeaderPanel(){ //Because the main class is Component
        headerPanel = new JPanel(new GridLayout(2, 3)); //creating objects
        tfIPAddress = new JTextField("127.0.0.1");
        tfPort = new JTextField("8189");
        tfLogin = new JTextField("Ivan Ivanovich");
        password = new JPasswordField("123456");
        btnLogin = new JButton("login");
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connectToServer();
            }
        });

        headerPanel.add(tfIPAddress); //adding objects to the panel
        headerPanel.add(tfPort);
        headerPanel.add(new JPanel()); //Empty panel for space between
        headerPanel.add(tfLogin);
        headerPanel.add(password);
        headerPanel.add(btnLogin);

        return headerPanel;
    }

    private Component createLog(){
        log = new JTextArea(); //Already inputted texts
        log.setEditable(false);
        return new JScrollPane(log); //For scrolling if there is no space
    }

    private Component createFooter() {
        JPanel panel = new JPanel(new BorderLayout());
        tfMessage = new JTextField(); //text field in the bottom part
        tfMessage.addKeyListener(new KeyAdapter() {//keyboard
            @Override
            public void keyTyped(KeyEvent e) { //listener of the keyboards
                if (e.getKeyChar() == '\n'){ //send message id meet that symbol - enter
                    message();
                }
            }
        });
        btnSend = new JButton("send");
        btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                message();
            } //the same thing as enter, when clicking on send
        });
        panel.add(tfMessage);
        panel.add(btnSend, BorderLayout.EAST);
        return panel;
    }

    @Override
    protected void processWindowEvent(WindowEvent e) { //work when smth happen to window
        if (e.getID() == WindowEvent.WINDOW_CLOSING){ //moment of window closing
            closeWindow();
        }
        super.processWindowEvent(e); //unichtojenie potoka okna
    }
}