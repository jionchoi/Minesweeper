import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;

import javax.swing.SwingUtilities;
import java.util.Random;

public class Minesweeper{
    private JFrame frame;
    private JPanel pane; //main JPanel
    private JPanel menuPane; //Menu Bar JPanel (timer, number of mines/flags)
    private JLayeredPane boardPane; //Game Board JPanel for the button and label array
    private Dimension frameSize = new Dimension(700, 700); //dimension for main JPanel
    private Dimension boardSize = new Dimension(400, 400); //dimension for the game JPanel
    private Dimension numPaneSize;
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
        frame.setMaximumSize(frameSize);
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
        boardPane = new JLayeredPane();
        boardPane.setPreferredSize(boardSize); //set the board size
        // boardPane.setBounds(0, 0, 300, 300);
       
        //set bound of the child panels because the layout of JLayeredPane is null
    
        buttonGrid = new JButton[dimension][dimension]; //button grid
        board = new int[dimension][dimension]; //Game board to keep track of numbers and mines
        
        //position of buttons and labels 
        int x = 0;
        int y = 0;

        //width and height of the buttons and labels
        int width = 50;
        int height = width;
        //while(mines != 0){}
        for(int i = 0; i < dimension; i++){
            for(int j = 0; j < dimension ; j++){
                //if the random number is 3, place a mines
                if(randNum(0,5) == 3){
                    JLabel example = new JLabel("Bomb!!", SwingConstants.CENTER);
                    example.setBounds(x, y, width, height);
                    boardPane.add(example, JLayeredPane.DEFAULT_LAYER);
                    board[i][j] = 1; //1 is mine 
                }
                else{
                    board[i][j] = 0; //0 is number
                    JLabel example = new JLabel("Safe");
                    example.setBounds(x, y, width, height);
                    boardPane.add(example, JLayeredPane.DEFAULT_LAYER);

                    // boardPane.setComponentZOrder(example, 0);
                }
                buttonGrid[i][j] = new JButton();
                // buttonGrid[i][j].setHorizontalAlignment(SwingConstants.VERTICAL);
                buttonGrid[i][j].addMouseListener(new MouseAdapter() {
                    @Override //Button Mouse Listener
                    public void mousePressed(MouseEvent e) {
                        JButton clicked = (JButton) e.getSource();

                        //if it's right click, flag/unflag
                        if(SwingUtilities.isRightMouseButton(e)){
                            //mark the button
                            if(isflagged(clicked)) 
                                unflag(clicked);
                            else 
                                flag(clicked);
                        }
                        else{
                            int row = btnPositionRow(clicked);
                            int col = btnPositionRow(clicked);
                            //if the clicked button is not flagged
                            if(!isflagged(clicked))
                                reveal(row, col, clicked); //remove the button and reveal the square
                        }
                    }
                });
                buttonGrid[i][j].setBounds(x, y, width, height);
                boardPane.add(buttonGrid[i][j], JLayeredPane.PALETTE_LAYER); //add the button to the board JPanel
                // boardPane.setComponentZOrder(buttonGrid[i][j], 1);
                x += 50; //increase x 
            }
            y += 50; //increase y
            x = 0; //set x to 0 because this is a new line
        }

        //add the Game Board Panel to the Main Panel
        pane.add(boardPane);
    }

    /* TO-DO
        - Organize the code (Maybe just two return value in one function using like vector or list)
    */
    //Find the coordinate(row) of the clicked button
    public int btnPositionRow(JButton btn){
        for(int i = 0; i < dimension; ++i){
            for(int j = 0; j < dimension; ++j){
                if(buttonGrid[i][j] == btn){
                    return i; //return row
                }
            }
        }
        return 0;
    }

    //Find the coordinate(column) of the clicked button
    public int btnPositionCol(JButton btn){
        for(int i = 0; i < dimension; ++i){
            for(int j = 0; j < dimension; ++j){
                if(buttonGrid[i][j] == btn){
                    return j; //return column
                }
            }
        }
        return 0;
    }

    //mark the button
    public void flag(JButton btn){
        //resize the image
        ImageIcon flag = new ImageIcon("./bin/images/red-flag.png"); // load the image to a imageIcon
        Image image = flag.getImage(); // transform it 
        Image newimg = image.getScaledInstance(30, 30,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
        flag = new ImageIcon(newimg);  // transform it back
        
        //System.out.println("marked");
        btn.setIcon(flag);
    }

    //unmark the button
    public void unflag(JButton btn){
        btn.setIcon(null);
    }

    //check if the button has an Icon or not
    public boolean isflagged(JButton btn){
        if(btn.getIcon() == null) return false;
        else return true;
    }

    //Remove the button and open the square
    public void reveal(int row, int col, JButton btn){
        btn.setVisible(false); //hide the button
    }


    public void setNumber(){

    }

    //Random number generator
    public int randNum(int min, int max){
        Random rand = new Random();
        return rand.nextInt(max) + min;
    }  
    
}
