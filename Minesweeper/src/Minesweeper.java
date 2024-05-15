import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.SwingUtilities;
import javax.swing.Timer;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class Minesweeper{
    private JFrame frame;

    private JPanel pane; //main JPanel
    private JPanel menuPane; //Menu Bar JPanel (timer, number of mines/flags)
    private JLayeredPane boardPane; //Game Board JPanel for the button and label array

    private Dimension frameSize = new Dimension(700, 700); //dimension for main JPanel
    private Dimension boardSize = new Dimension(400, 400); //dimension for the game JPanel

    private int[][] board;
    private int mines;
    private static int dimension; //dimension of the game board
    private JButton[][] buttonGrid;
    private JButton playAgain; //PlayAgain button on the menu bar
    private JLabel numOfMines;
    private int X = 0; //X position of JButtons and JLabels
    private int Y = X;
    private int width = 40; //Width of JButtons and JLabels
    private int height = width;
    private int numberCell; //Number of cell which is not a mine
    private String level;
    private boolean[][] vis; //visited cells

    private Timer timer;
    private int elapsed;
    private JLabel seconds;

    public Minesweeper(String diff){
        level = diff;
        frame = new JFrame();

        pane = new JPanel();
        pane.setBorder(new EmptyBorder(50, 50, 50, 50));
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS)); //sorts panels horizontally 

        frame.add(pane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Minesweeper");
        
        //Set Difficulty
        setDiff(diff);

        Image icon = Toolkit.getDefaultToolkit().getImage("Minesweeper/bin/images/mine.png"); //for Window OS
        frame.setIconImage(icon);    

        numberCell = (dimension * dimension);

        //display menu bar (timer, number of mines)
        menuBar();

        //Initiallize the game board
        createBoard(dimension);

        //Start the timer when the game starts
        timer = new Timer(1000,new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                elapsed += 1000;
                updateTimer();
            }
        });
        timer.start();

        vis = new boolean[dimension][dimension];
        //Final setup for the main Frame
        frame.setResizable(false);
        frame.setMaximumSize(frameSize);
        frame.pack();
        frame.setVisible(true);
    }

    //Set Difficulty of the game
    public void setDiff(String level){
        switch(level){
            case "Easy":
                mines = 10;
                dimension = 10;
                //set a new frame size based on the level
                frame.setSize(500, 500);
                break;

            case "Medium":
                mines = 40;
                dimension = 16;
                boardSize = new Dimension(640, 640); //increase the board size 
                frame.setSize(800,800);
                break;
        }
    }

    //Update the timer
    private void updateTimer(){
        int timeSecond = (elapsed % 60000) / 1000;
        String time = String.format("%02d", timeSecond);
        seconds.setText(time);
    }

    //Update the number of remaining Mines
    private void updateMine(Boolean increase){
        if(increase)
            mines++;
        else
            mines--;

        String mineNum = String.format("%02d", mines);
        numOfMines.setText(mineNum);
    }

    //Menu bar on top of the board (timer, number of mines, restart)
    public void menuBar(){
        int hgap;
        if(level.equals("Easy"))
            hgap = 60;
        else
            hgap = 120;

        menuPane = new JPanel();
        menuPane.setLayout(new FlowLayout(FlowLayout.CENTER, hgap, 10));    

        seconds = new JLabel("00");
        seconds.setFont(new Font("Arial", Font.PLAIN, 30));
        seconds.setForeground(Color.red);

        menuPane.add(seconds);

        //Play Again Button
        playAgain = new JButton();
        ImageIcon face = resizeIcon("Minesweeper/bin/images/happiness.png", 40, 40);
        playAgain.setIcon(face);

        playAgain.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restart();
            }
        });
        
        menuPane.add(playAgain);

        //JLabel for number of mines/flags
        numOfMines = new JLabel(String.valueOf(mines));
        numOfMines.setFont(new Font("Arial", Font.PLAIN, 30));
        numOfMines.setForeground(Color.red);

        menuPane.add(numOfMines);

        
        pane.add(menuPane);
    }

    //Initiallizing the board
    public void createBoard(int dimension){
        boardPane = new JLayeredPane();
        boardPane.setPreferredSize(boardSize); //set the board size
        boardPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        buttonGrid = new JButton[dimension][dimension]; //button grid
        board = new int[dimension][dimension]; //Game board to keep track of numbers and mines
        
        //Create a integer board with numbers
        setNumber();

        for(int i = 0; i < dimension; i++){
            for(int j = 0; j < dimension ; j++){
                //Add Labels and Buttons to the Panel
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
        ImageIcon mine = resizeIcon("Minesweeper/bin/images/mine.png", 30, 30);

        JLabel Mine;
        if(board[row][col] == 1000){ //if it's bomb
            Mine = new JLabel(mine, SwingConstants.CENTER);
            
        }
        else if(board[row][col] == 0){  //if it's 0/empty
            Mine = new JLabel("",SwingConstants.CENTER);
        }
        else
            Mine = changeColor(String.valueOf(board[row][col]));
            
        Mine.setBounds(X, Y, width, height); //set bound of the child panels because the layout of JLayeredPane is null
        boardPane.add(Mine, JLayeredPane.DEFAULT_LAYER);
            
        return false;
    }

    //Change the color of the text based on its number
    public JLabel changeColor(String num){
        JLabel mine = new JLabel(num, SwingConstants.CENTER);
        switch(num){
            case "1":
                mine.setForeground(Color.blue);
                break;
            case "2":
                mine.setForeground(new Color(26, 102, 46));
                break;
            case "3":
                mine.setForeground(Color.red);
                break;
            case "4":
                mine.setForeground(Color.magenta);
                break;
        }

        return mine;
    }

    //Create buttons on the board 
    public void setButton(int row, int col){
        buttonGrid[row][col] = new JButton();
        // buttonGrid[i][j].setHorizontalAlignment(SwingConstants.VERTICAL);
        buttonGrid[row][col].addMouseListener(new MouseAdapter() {
            @Override //Button Mouse Listener
            public void mousePressed(MouseEvent e) {
                JButton clicked = (JButton) e.getSource();

                //if the button has been disabled, nothing will happen
                if(!clicked.isEnabled()) return;

                //if it's right click, flag/unflag
                if(SwingUtilities.isRightMouseButton(e)){
                    //mark the button
                    if(isflagged(clicked)){
                        unflag(clicked);
                        updateMine(true);
                    }
                    else {
                        flag(clicked);
                        updateMine(false);
                    }
                }
                else{
                    int row = btnPositionRow(clicked);
                    int col = btnPositionCol(clicked);

                    //if the clicked button is not flagged
                    if(!isflagged(clicked))
                        click(row, col, clicked); //remove the button and click the square
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
        ImageIcon flag = resizeIcon("Minesweeper/bin/images/red-flag.png", 30, 30);
        
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
    public void click(int row, int col, JButton btn){
        //if there is no more remaining button, player wins
        if(numberCell == 0) gameEnd(true);

        int clicked = board[row][col];
        //if the clicked cell is 1000, game over
        if(clicked == 1000){
            gameEnd(false);
            btn.setVisible(false);
        }
        else //open cells 
            openCell(row, col, btn);
    }

    //Change the Icon of the JButton on the menu bar, end game
    public void gameEnd(boolean win){
        ImageIcon face;

        if(win){
            face = resizeIcon("Minesweeper/bin/images/smile.png", 40, 40);
        }
        else{
            face = resizeIcon("Minesweeper/bin/images/hate.png", 40, 40);
        }

        playAgain.setIcon(face);
        
        //Stop the timer
        timer.stop();
        //Disable buttons and remove flags to display the mine
        disableButtons();
        removeFlags();
    }

    //Disable remaining buttons
    public void disableButtons(){
        //loop through the button grid
        for(int i = 0; i < dimension; ++i){
            for(int j = 0; j < dimension; ++j){
                if(buttonGrid[i][j].isVisible()){
                    buttonGrid[i][j].setEnabled(false);
                }
            }
        }
    }

    //If user wins the game, remove the flag and display mines
    public void removeFlags(){
        //loop through the button grid
        for(int i = 0; i < dimension; ++i){
            for(int j = 0; j < dimension; ++j){
                JButton btn = buttonGrid[i][j];
                if(isflagged(btn)){
                    unflag(btn); //unflag the button
                }
            }
        }
    }

    //reset and restart the game
    public void restart(){
        frame.setVisible(false);

        new Minesweeper(level);
    }

    /* TO-DO
        - Number of Mines are wrong. Fix the loop (Use randomly generated i and j until newMineNum is 0)
        - Organize the code. Too messy
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

    //Temporary class for indices of cells 
    static class Cell{
        int row;
        int col;
    Cell(int row, int col){
        this.row = row;
        this.col = col;
        }
    }   

    //Open Cells. If the selected cell is 0, check surroundings for more 0s using Queue
    public void openCell(int row, int col, JButton btn){
        int selected = board[row][col];

        //Check if the selected cell is 0
        if(selected == 0){
            breadthSearchCell(row, col);
        }
        else{
            btn.setVisible(false); //display the label
            vis[row][col] = true;
            //Reduce the number of cell
            numberCell--;
        }

        //if the number of cell is equal to the number of mines, player win
        if(numberCell == mines) gameEnd(true);
    }

    /* TO-DO
        - Fix counting (Number of Remaning Cells)
    */
    //Breadth Search Algorithm
    public void breadthSearchCell(int row, int col){
        Queue<Cell> cells = new LinkedList<>();

        //Enqueue the Starting tile into the queue
        cells.add(new Cell(row, col));
        vis[row][col] = true;
        int test = 0;

        //Until we reach "non-0 cell", while the queue is not empty
        while(!cells.isEmpty()){
            //Dequeue the cell to be current cell / Display the label
            Cell current = cells.peek();
            
            int x = current.row;
            int y = current.col;

            cells.remove();
            JButton btn = buttonGrid[x][y];
            btn.setVisible(false);
            test++;
            //Reduce the number of cell
            numberCell--;

            //if the current cell is non-zero, don't check the adjacent cell
            if(board[x][y] == 0){
                //Direction Vectors (down, up, right, left, right-bottom corner, left-bottom corner, right-top corner, left-top corner)
                int[] dr = {0, 0, 1, -1, 1, 1, -1, -1};
                int[] dc = {1, -1, 0, 0, 1, -1, 1, -1};

                //Check the neighbour cells
                for(int i = 0; i < dr.length; ++i){
                    int nearX = x + dr[i];
                    int nearY = y + dc[i];

                    if(checkCell(nearX, nearY)){
                        cells.add(new Cell(nearX, nearY));
                        vis[nearX][nearY] = true;
                    }
                }
            }
        }
    } 

    //Check if the cell is valid
    public boolean checkCell(int row, int col){
        //if the cell lies out of bounds, false
        if(row < 0 || col < 0 || row > dimension - 1 || col > dimension - 1) return false;

        //if the cell is already visited, false
        if(vis[row][col]) return false;

        return true;
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
    
    //Resize the image
    public ImageIcon resizeIcon(String fileName, int width, int height){
        //resize the image
        ImageIcon flag = new ImageIcon(fileName); // load the image to a imageIcon
        Image image = flag.getImage(); // transform it 
        Image newimg = image.getScaledInstance(width, height,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  

        return new ImageIcon(newimg);  // transform it back
    }
}
