package XO_Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Random;

public class Map extends JPanel { //JPanel is an element that we can put in any place of JFrame - widget container
    private static final Random RANDOM = new Random(); //For generating random values
    private static final int HUMAN_DOT = 1; //1 - steps were made by Human
    private static final int AI_DOT = 2; //2 - steps were made by Computer
    private static final int EMPTY_DOT = 0; //Empty places
    private static final int PADDING = 10; //Space between X or O and the borders of the square where they are located

    private int gameStateType; //In which state now is our game
    private static final int STATE_GAME = 0; //Game is currently running
    private static final int STATE_WIN_HUMAN = 1; //Human won the game
    private static final int STATE_WIN_AI = 2; //Computer won the game
    private static final int STATE_DRAW = 3; //Nobody win

    private static final String MSG_WIN_HUMAN = "Победил игрок!"; //Text for console
    private static final String MSG_WIN_AI = "Победил компьютер!";
    private static final String MSG_DRAW = "Ничья!";

    private int width, height, cellWidth, cellHeight; //Our sizes of cells by width and height
    private int mode, fieldSizeX, fieldSizeY, winLen; //mode - can change the game type - human x human or human x computer
    //fieldSizeX, fieldSizeY - qty of cells by width and height
    //winLen - how much should be enough for winning the game
    private int[][] field; //Our matrix field
    private boolean gameWork; //flag - game is running or not

    public Map() { //Constructor
        setBackground(Color.WHITE);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) { //Only thing is connecting to MouseListener- after crating
                //map we only can click by the mouse in order to make any actions
                if (gameWork) { //if the game is running we will be processing the clicks and do what is needed
                    update(e); //what will be done after mouse clicks - put X or O or congrats winner , etc.
                }
            }
        });
    }

    private void initMap() { //Initializing our array with necessary sizes or when we want to start a new game
        field = new int[fieldSizeY][fieldSizeX];
    }

    void startNewGame(int mode, int sizeX, int sizeY, int winLen) { //method that is called in gameWindow, where we write sizes
        this.mode = mode;
        this.fieldSizeX = sizeX;
        this.fieldSizeY = sizeY;
        this.winLen = winLen;

        initMap(); //Calling init method
        gameWork = true; //flag - game is running
        gameStateType = STATE_GAME; //Game state - is running

        repaint(); //Перерисовка нашего окна - repainting of our window
    }

    private void update(MouseEvent mouseEvent) { //Will be done as soon as we click the mouse
        int x = mouseEvent.getX() / cellWidth; //Getting the places of mouse click and dividing by the width of cell
        int y = mouseEvent.getY() / cellHeight; //Getting the places of mouse click and dividing by the height of cell
        if (!isValidCell(x, y) || !isEmptyCell(x, y)) { //Check if the click was made in the game field or in occupied cell
            return; //Nothing will be done in that case
        }
        field[y][x] = HUMAN_DOT; //If the click was done in the right place, we save the result
        if (checkEndGame(HUMAN_DOT, STATE_WIN_HUMAN)) { //checking if the step became winning or not
            return; //If yes, finish
        }
        aiTurn(); //If not, then next step is after computer
        repaint(); //repainting the look of the field
        checkEndGame(AI_DOT, STATE_WIN_AI); //Checking who win the game
    }

    private void testBoard(){
        for (int i = 0; i < 3; i++) {
            System.out.println(Arrays.toString(field[i]));
        }
        System.out.println();
    }

    private boolean isValidCell(int x, int y) { //Checking if we get in the correct place
        return x >= 0 && x < fieldSizeX && y >= 0 && y < fieldSizeY;
    }

    private boolean isEmptyCell(int x, int y) { //Checking if we get in an empty cell
        return field[y][x] == EMPTY_DOT;
    }

    private void aiTurn() { //Method of computer steps - he is searching randomly an empty cell and make a step there
        int x, y;
        do {
            x = RANDOM.nextInt(fieldSizeX);
            y = RANDOM.nextInt(fieldSizeY);
        } while (!isEmptyCell(x, y));
        field[y][x] = AI_DOT;
    }

    private boolean isMapFull() { //Checking that our field is completely full - there is no steps
        for (int i = 0; i < fieldSizeY; i++) {
            for (int j = 0; j < fieldSizeX; j++) {
                if (field[i][j] == EMPTY_DOT) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkEndGame(int dot, int gameOverType) { //Checking the end of the game with another method checkWin
        if (checkWin(dot)) { //If there is a winner - game over and return
            this.gameStateType = gameOverType;
            repaint(); //Repainting the field
            return true;
        } else if (isMapFull()) { //If there is no winner we need to check if there are no empty cells to make a step
            this.gameStateType = STATE_DRAW;
            repaint();
            return true;
        }
        return false;
    }

    private boolean checkWin(int dot){ //Additional method for checking the winner
        for (int i = 0; i < fieldSizeX; i++) { //Going through array and checking dots - human or computer, with checkLine
            for (int j = 0; j < fieldSizeY; j++) {
                if (checkLine(i, j, 1, 0, winLen, dot)) return true;
                if (checkLine(i, j, 1, 1, winLen, dot)) return true;
                if (checkLine(i, j, 0, 1, winLen, dot)) return true;
                if (checkLine(i, j, 1, -1, winLen, dot)) return true;
            }
        }
        return false;
    }

    private boolean checkLine(int x, int y, int vx, int vy, int len, int dot){ //Method for checking 3 correct dots to win
        int far_x = x + (len - 1) * vx;
        int far_y = y + (len - 1) * vy;
        if (!isValidCell(far_x, far_y)){
            return false;
        }
        for (int i = 0; i < len; i++) {
            if (field[y + i * vy][x + i * vx] != dot){
                return false;
            }
        }
        return true;
    }

    @Override
    protected void paintComponent(Graphics g) { //Is working when we use our method repaint
        super.paintComponent(g);
        if (gameWork) {
            render(g); //Returning our method render
        }
    }

    private void render(Graphics g) { //For painting our field
        width = getWidth(); //sizes
        height = getHeight();
        cellWidth = width / fieldSizeX; //cells
        cellHeight = height / fieldSizeY;

        g.setColor(Color.BLACK); //color for painting
        for (int h = 0; h < fieldSizeX; h++) { //painting vertical lines of the game field
            int y = h * cellHeight;
            g.drawLine(0, y, width, y);
        }
        for (int w = 0; w < fieldSizeX; w++) { //painting horizontal lines of the game field
            int x = w * cellWidth;
            g.drawLine(x, 0, x, height);
        }

        for (int y = 0; y < fieldSizeY; y++) { //Going throw our array to paint X or O
            for (int x = 0; x < fieldSizeX; x++) {
                if (field[y][x] == EMPTY_DOT){ //If nobody make a step in the cell, paint nothing
                    continue;
                }
                if (field[y][x] == HUMAN_DOT) { //If there is a human, draw X
                    g.drawLine(x * cellWidth + PADDING, y * cellHeight + PADDING, //One line of X
                            (x + 1) * cellWidth - PADDING, (y + 1) * cellHeight - PADDING);
                    g.drawLine(x * cellWidth + PADDING, (y + 1) * cellHeight - PADDING,//Second line of X
                            (x + 1) * cellWidth - PADDING, y * cellHeight + PADDING);
                } else if (field[y][x] == AI_DOT) { //If there is a computer, draw O
                    g.drawOval(x * cellWidth + PADDING, y * cellHeight + PADDING,
                            cellWidth - PADDING * 2, cellHeight - PADDING * 2);
                } else {
                    throw new RuntimeException("unchecked value " + field[y][x] +
                            " in cell: x=" + x + " y=" + y);
                }
            }
        }
        if (gameStateType != STATE_GAME){ //If the game is stopped, need to write final message
            showMessage(g);
        }
    }

    private void showMessage(Graphics g) { //Final message about the winner
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, getHeight() / 2, getWidth(), 70);
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Times new roman", Font.BOLD, 48));
        switch (gameStateType){
            case STATE_DRAW -> g.drawString(MSG_DRAW, 180, getHeight() / 2 + 60);
            case STATE_WIN_HUMAN -> g.drawString(MSG_WIN_HUMAN, 20, getHeight() / 2 + 60);
            case STATE_WIN_AI -> g.drawString(MSG_WIN_AI, 70, getHeight() / 2 + 60);
            default -> throw new RuntimeException("Unchecked gameOverState: " + gameStateType);
        }
        gameWork = false;
    }
}