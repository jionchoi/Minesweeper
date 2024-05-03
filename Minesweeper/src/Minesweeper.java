import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.SwingUtilities;
import java.util.Random;

public class Minesweeper{
    private JFrame frame;
    private JPanel pane; //main JPanel
    private JPanel boardPane; //Game Board JPanel for the button array
    private JPanel menuPane; //Menu Bar JPanel (timer, number of mines/flags)
    private Dimension paneSize = new Dimension(700, 700); //dimension for main JPanel
    private Dimension boardSize = new Dimension(400, 400); //dimension for the game JPanel
    private int[][] board;
    private int mines;
    private static int dimension; //dimension of the game board
    private JButton[][] buttonGrid;

    /* TO-DO
        - Finish the Custom Grid
        - Finish the calculation of mines in custon grid
    */
    public Minesweeper(String diff){
        frame = new JFrame();

        pane = new JPanel();
        pane.setBorder(new EmptyBorder(50, 50, 50, 50));
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS)); //sorts panels horizontally 

        // frame.add(pane, BorderLayout.CENTER);
        frame.add(pane);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Minesweeper Game");
        

        switch(diff){
            case "Easy":
                mines = 10;
                dimension = 10;
                //set a new frame size based on the level
                frame.setSize(500, 500);
                break;

            case "Medium":
                mines = 40;
                dimension = 18;
                break;

            case "Hard":
                mines = 99;
                dimension = 25;
                break;

            case "Custom Grid":
                
                break;
        }
        
        //display menu bar (timer, number of mines)
        menuBar();

        //Initiallize the game board
        createBoard(dimension);

        //Set the numbers around the mine
        setNumber();

        //Final setup for the main Frame
        frame.setResizable(false);
        frame.setMaximumSize(paneSize);
        frame.pack();
        frame.setVisible(true);
    }

    //Menu bar on top of the board (timer, number of mines)
    public void menuBar(){
        menuPane = new JPanel();
        pane.add(menuPane);

        //JLabel for number of mines/flags
        JLabel numOfMines = new JLabel(String.valueOf(mines));
        menuPane.add(numOfMines);
    }

    //Initiallizing the board
    public void createBoard(int dimension){
        boardPane = new JPanel();
        boardPane.setLayout(new GridLayout(dimension, dimension));
        // boardPane.setBounds(0, 0, 300, 300);
        boardPane.setPreferredSize(boardSize); //set the board size

        buttonGrid = new JButton[dimension][dimension]; //button grid
        board = new int[dimension][dimension]; //Game board to keep track of numbers and mines
        
        /* TO-DO
            - Organize the code (Duplicated button grid)
        */
        //while(mines != 0){}
        for(int i = 0; i < dimension; i++){
            for(int j = 0; j < dimension ; j++){
                //if the random number is 3, place a mines
                if(randNum(0,5) == 3){
                    board[i][j] = 1; //1 is mine 
                }
                else{
                    board[i][j] = 0; //0 is number
                }

                buttonGrid[i][j] = new JButton();
                buttonGrid[i][j].addMouseListener(new MouseAdapter() {
                    @Override //Button Mouse Listener
                    public void mousePressed(MouseEvent e) {
                        //if it's right click, flag/unflag
                        if(SwingUtilities.isRightMouseButton(e)){
                            flag(buttonGrid);
                        }
                        else{
                            System.out.println("left");
                            reveal(); //remove the button and reveal the square
                        }
                    }
                });
                boardPane.add(buttonGrid[i][j]); //add the button to the board JPanel
            }
        }

        //add the Game Board Panel to the Main Panel
        pane.add(boardPane);
    }

    //Random number generator
    public int randNum(int min, int max){
        Random rand = new Random();
        return rand.nextInt(max) + min;
    }  

    public void setNumber(){

    }

    public void flag(JButton[][] button){
        ImageIcon flag = new ImageIcon("red-flag.png");
        System.out.println(button);
    }

    public void unflag(){

    }

    public boolean isflag(){
        
        return true;
    }
    //Remove the button and open the square
    public void reveal(){

    }
}
