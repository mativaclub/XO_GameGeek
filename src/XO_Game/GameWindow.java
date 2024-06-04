package XO_Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameWindow extends JFrame {
    private static final int WIDTH = 555; //Length and width of the window
    private static final int HEIGHT = 507;

    private JButton btnStart, btnExit; //Buttons to start and to end the game
    private SettingWindow settingWindow;
    private Map map;

    public GameWindow(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null); //Screen will be in the center - not related to any position

        setTitle("TicTacToe"); //Name of the game
        setResizable(false); //Can't change the size
        btnStart = new JButton("New Game"); //Setting the text of the button
        btnExit = new JButton("Exit"); //Setting the text of the button
        settingWindow = new SettingWindow(this); //Creating settingWindow which is linked to itself - to gameWindow
        map = new Map(); //Creating map

        btnExit.addActionListener(new ActionListener() { //Connecting Listener for exit button
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settingWindow.setVisible(true);
            } //Will appear settingWindow
        });

        JPanel panBottom = new JPanel(new GridLayout(1, 2)); //1 row and 2 columns, as a grid, adding buttons
        panBottom.add(btnStart); //to the left side of the grid.
        panBottom.add(btnExit); //to the right side of the grid.

        add(panBottom, BorderLayout.SOUTH); //Adding this panel to our graphic window at the bottom part
        add(map); //In the center will be added map

        setVisible(true); //SettingWindow will ba visible right away after our gameWindow is created
    }

    public void startNewGame(int mode, int sizeX, int sizeY, int winLen){ //Method from SettingWindow
        map.startNewGame(mode, sizeX, sizeY, winLen); //providing sizes in the map in the same method.
    }
}