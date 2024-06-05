package Server_Client_App;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class ServerWindow extends JFrame {
    public static final int WIDTH = 400;
    public static final int HEIGHT = 300;
    public static final String LOG_PATH = "src/server/log.txt"; //path to file with history, where we save it

    private List<ClientGUI> clientGUIList; //list of clients

    private JButton btnStart, btnStop;
    private JTextArea log;
    private boolean work; //flag that server is waiting for messages

    public ServerWindow(){
        clientGUIList = new ArrayList<>(); //initialization for empty list

        setDefaultCloseOperation(EXIT_ON_CLOSE); //upon clicking on x of the window everything will close
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setTitle("Chat server");
        setLocationRelativeTo(null);

        createPanel();

        setVisible(true);
    }

    public boolean connectUser(ClientGUI clientGUI){ //Client call from server to connect himself
        if (!work){
            return false;
        }
        clientGUIList.add(clientGUI); //if server is working, adding a client
        return true;
    }

    public void disconnectUser(ClientGUI clientGUI){ //Choosing which client to disconnect
        clientGUIList.remove(clientGUI); //Client is removed from the list
        if (clientGUI != null){
            clientGUI.disconnectFromServer(); //Telling client that he is disconnected and bring him start menu
        }
    }

    public void message(String text){
        if (!work){ //If the server is working
            return;
        }
        appendLog(text); //Add text
        answerAll(text); //Send to all clients the same answer - method below
        saveInLog(text); //Save the message to conversation history - method below
    }

    private void answerAll(String text){
        for (ClientGUI clientGUI: clientGUIList){
            clientGUI.answer(text);
        }
    }

    private void saveInLog(String text){
        try (FileWriter writer = new FileWriter(LOG_PATH, true)){ //open stream
            writer.write(text); //write the text
            writer.write("\n");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public String readLog(){ //reading data from file log - history of conversation
        StringBuilder stringBuilder = new StringBuilder();
        try (FileReader reader = new FileReader(LOG_PATH);){ //Read everything that is written there
            int c;
            while ((c = reader.read()) != -1){ //-1 is the end of file
                stringBuilder.append((char) c);
            }
            if (!stringBuilder.isEmpty()) {
                stringBuilder.delete(stringBuilder.length()-1, stringBuilder.length()); //Deleting last symbol \n
            }
            return stringBuilder.toString();
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private void appendLog(String text){
        log.append(text + "\n"); //Adding line break
    }

    private void createPanel() {
        log = new JTextArea();
        add(log);
        add(createButtons(), BorderLayout.SOUTH);
    }

    private Component createButtons() {
        JPanel panel = new JPanel(new GridLayout(1, 2));
        btnStart = new JButton("Start");
        btnStop = new JButton("Stop");

        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (work){
                    appendLog("Сервер уже был запущен");
                } else {
                    work = true;
                    appendLog("Сервер запущен!");
                }
            }
        });

        btnStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!work){
                    appendLog("Сервер уже был остановлен");
                } else {
                    work = false; //set working to false
                    while (!clientGUIList.isEmpty()){
                        disconnectUser(clientGUIList.get(clientGUIList.size()-1));
                    }
                    appendLog("Сервер остановлен!");
                }
            }
        });

        panel.add(btnStart);
        panel.add(btnStop);
        return panel;
    }
}