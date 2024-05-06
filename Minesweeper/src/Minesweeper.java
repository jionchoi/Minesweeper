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
    private int X = 0;
    private int Y = X;
    private int width = 40;
    private int height = width;

    /* TO-DO
        - Finish the Custom Grid
        - Finish the calculation of mines in custon grid
    */
    public Minesweeper(String diff){
        frame = new JFrame();

        pane = new JPanel();
        pane.setBorder(new EmptyBorder(50, 50, 50, 50));
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS)); //sorts panels horizontally 

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

        buttonGrid = new JButton[dimension][dimension]; //button grid
        board = new int[dimension][dimension]; //Game board to keep track of numbers and mines
        setNumber();

        for(int i = 0; i < dimension; ++i){
            for(int j = 0; j < dimension; ++j){
                System.out.print(board[i][j] + ",");
            }
            System.out.println("");
        }
        for(int i = 0; i < dimension; i++){
            for(int j = 0; j < dimension ; j++){
                setLabel(i, j);
                setButton(i, j);
                
                X += width; //increase x 
            }
            Y += width; //increase y
            X = 0; //set x to 0 because this is a new line
        }
        //add the Game Board Panel to the Main Panel
        pane.add(boardPane);
    }


    //Create labels on the board
    public boolean setLabel(int row, int col){
        //resize the image
        ImageIcon mine = new ImageIcon("Minesweeper/bin/images/mine.png"); // load the image to a imageIcon
        Image image = mine.getImage(); // transform it 
        Image newimg = image.getScaledInstance(30, 30,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
        mine = new ImageIcon(newimg);  // transform it back

        JLabel Mine;
        if(board[row][col] == 1000){ //if it's bomb
            Mine = new JLabel(mine, SwingConstants.CENTER);
        }
        // else if(board[row][col] == 0){  //if it's 0/empty
        //     Mine = new JLabel("",SwingConstants.CENTER);
        // }
        else
            Mine = new JLabel(String.valueOf(board[row][col]), SwingConstants.CENTER);
            
        Mine.setBounds(X, Y, width, height); //set bound of the child panels because the layout of JLayeredPane is null
        boardPane.add(Mine, JLayeredPane.DEFAULT_LAYER);
            
        return false;
    }

    //Create buttons on the board 
    public void setButton(int row, int col){
        buttonGrid[row][col] = new JButton();
        // buttonGrid[i][j].setHorizontalAlignment(SwingConstants.VERTICAL);
        buttonGrid[row][col].addMouseListener(new MouseAdapter() {
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
                    int col = btnPositionCol(clicked);

                    //if the clicked button is not flagged
                    if(!isflagged(clicked))
                        reveal(row, col, clicked); //remove the button and reveal the square
                }
            }
        });
        buttonGrid[row][col].setBounds(X, Y, width, height);
        boardPane.add(buttonGrid[row][col], JLayeredPane.PALETTE_LAYER); //add the button to the board JPanel
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
        ImageIcon flag = new ImageIcon("Minesweeper/bin/images/red-flag.png"); // load the image to a imageIcon
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
        int clicked = board[row][col];
        //if the clicked cell is 1000, game over
        if(clicked == 1000){
            gameEnd(false);
            btn.setVisible(false);
        }
        else //open cells 
            openEmpty(row, col, btn);
    }

    public void gameEnd(boolean win){
        if(win)
            System.out.println("You Won!");
        else
            System.out.println("Game Over");
    }

    /* TO-DO
        - Number of Mines are wrong. Fix the loop
    */
    //Create mines on the board
    public void setMines(){
        int newMineNum = mines;

        //loop through until all mines are created
        for(int i = 0; i < dimension; ++i){
            for(int j = 0; j < dimension; ++j){
                if(newMineNum == 0) return;
                if(randNum(0,10) == 3){
                    if(board[i][j] != 1000){
                        board[i][j] = 1000; //mines are indicated as "1000"
                        newMineNum--;
                    }
                }
            }
        }
    }

    /*
        Check how many mines are nearby.
        Algorithm: Check row - 1, row, row + 1; Check col - 1, col, col + 1. Therefore, starting from row - 1, loop through each cells
        if row - 1/ col - 1 is negative, that means row / col is 0. So don't check the previous row / col
        if row + 1 / col + 1 is greater than the dimension, that means row / col is at the dimension. So don't check the next row / col
    */
    public int checkMines(int row, int col){
        int rowMin = row - 1;
        int colMin = col - 1;
        int rowMax = row + 1;
        int colMax = col + 1;

        int numOfMines = 0;
        if(rowMin < 0) rowMin = row;
        if(colMin < 0) colMin = col;
        if(rowMax == dimension) rowMax = row;
        if(colMax == dimension) colMax = col;

        for(int i = rowMin; i < rowMax + 1; ++i){
            for(int j = colMin; j < colMax +1; ++j){
                if(board[i][j] == 1000){
                    numOfMines++;
                }
            }
        }
        
        return numOfMines;
    }

    public void openEmpty(int row, int col, JButton btn){
        int rowMin = row - 1;
        int colMin = col - 1;
        int rowMax = row + 1;
        int colMax = col + 1;

        if(rowMin < 0) rowMin = row;
        if(colMin < 0) colMin = col;
        if(rowMax == dimension) rowMax = row;
        if(colMax == dimension) colMax = col;

            for(int i = rowMin; i < rowMax + 1; ++i){
                for(int j = colMin; j < colMax + 1; ++j){
                    if(checkMines(i, j) == 0){
                        btn = buttonGrid[i][j];
                        openEmpty(i, j, btn);
                    }
                }
            }
           
            btn.setVisible(false);
    }

    //Initialize the board (number) 
    public void setNumber(){
        setMines(); //create mines first
        //loop through the board
        for(int i = 0; i < dimension; ++i){
            for(int j = 0; j < dimension; ++j){
                //if the cell does have 1000, which is not mine, set numbers based on the mines nearby
                if(board[i][j] != 1000){
                    board[i][j] = checkMines(i, j); //check how many mines are there
                }
            }
        }
    }

    //Random number generator
    public int randNum(int min, int max){
        Random rand = new Random();
        return rand.nextInt(max) + min;
    }  
    
}
