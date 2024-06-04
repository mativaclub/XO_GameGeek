package XO_Game;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingWindow extends JFrame {
    private static final int WIDTH = 230;
    private static final int HEIGHT = 350;
    private int mode = 0;
    private int sizeX = 3;
    private int sizeY = 3;
    private int winLength = 3;
    private JButton btnStart;

    public SettingWindow(GameWindow gameWindow) { //Constructor where is given Game window with only ony button - start new game
        btnStart = new JButton("Start new game");

        setLocationRelativeTo(gameWindow); //Will locate our window related to game window -
        //left upper angle of our application will be in the center of gameWindow.
        setSize(WIDTH, HEIGHT); //Setting the sizes
        btnStart.addActionListener(new ActionListener() { //Here is an anonymous class with only one method.
            //Setting a Listener for the button start - it is an action that will be executed upon pressing the button

            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false); //For disappearing of the current setting window
                System.out.println(mode);
                gameWindow.startNewGame(mode, sizeX, sizeY, winLength); //calling method startNewGame from
                //gameWindow with static parameters of the game
            }
        });
        JPanel grid = new JPanel(new GridLayout(4, 1));
        grid.add(createGameModePanel());
        grid.add(createHorizontalFieldSlider("Please choose the size of the field: "));
        grid.add(createHorizontalWinnerSlider("Please choose the size to win: "));
        grid.add(btnStart); //Adding button on the graphic screen.
        this.add(grid); //We put grid to current object of JFrame

    }

    private JPanel createGameModePanel() {
        Label label = new Label("Please choose the game type");
        JRadioButton jRadioButton1 = new JRadioButton("Human against Computer");
        jRadioButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mode = 0;
            }
        });
        JRadioButton jRadioButton2 = new JRadioButton("Human against Human");
        jRadioButton2.addActionListener(e -> {
            mode = 1;
        });

        JPanel jPanel = new JPanel(new GridLayout(3, 1));
        ButtonGroup buttonGroup = new ButtonGroup(); //При нажатии одного сбрасывается другой, без него оба варианты активны
        buttonGroup.add(jRadioButton1);
        buttonGroup.add(jRadioButton2);
        jPanel.add(label);
        jPanel.add(jRadioButton1);
        jPanel.add(jRadioButton2);
        return jPanel;
    }

    private JPanel createHorizontalFieldSlider(String s) {
        int minPosition = 3;
        int maxPosition = 10;
        Label label = new Label(s + minPosition);
        JSlider jSlider = new JSlider(SwingConstants.HORIZONTAL, minPosition, maxPosition, 3);
        jSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int value = jSlider.getValue();
                label.setText(s + value);
                sizeX = value;
                sizeY = value;
            }
        });
        JPanel jPanel = new JPanel(new GridLayout(2, 1));
        jPanel.add(label);
        jPanel.add(jSlider);
        return jPanel;
    }

    private JPanel createHorizontalWinnerSlider(String s) {
        int minPosition = 3;
        int maxPosition = 10;
        Label label = new Label(s + minPosition);
        JSlider jSlider = new JSlider(SwingConstants.HORIZONTAL, minPosition, maxPosition, 3);
        jSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int value = jSlider.getValue();
                label.setText(s + value);
                winLength = value;
            }
        });
        JPanel jPanel = new JPanel(new GridLayout(2, 1));
        jPanel.add(label);
        jPanel.add(jSlider);
        return jPanel;
    }

}