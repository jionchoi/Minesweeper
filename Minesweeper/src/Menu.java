import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;


public class Menu{
    private JFrame frame;
    private JPanel pane;
    private Dimension size = new Dimension(1080, 720);
    private Dimension frameSize = new Dimension(500, 500);
    private String selected = "Easy"; //Default level is easy
    //Constructor: Create the Menu Frame
    public Menu(){
        frame = new JFrame();

        Image icon = Toolkit.getDefaultToolkit().getImage("Minesweeper/bin/images/mine.png");    
        frame.setIconImage(icon);   
        
        pane = new JPanel();
        pane.setBorder(new EmptyBorder(50, 50, 50, 50));
        pane.setLayout(null);

        frame.add(pane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Minesweeper");
        

        // frame.add(pane, BorderLayout.CENTER);
        
        JLabel diffLabel = new JLabel("Choose Difficulty:");
        diffLabel.setFont(new Font("Serief", Font.BOLD, 20));
        diffLabel.setBounds(100, 30, 300, 30);
        pane.add(diffLabel);

        //grid selector
        String[] gridSelector = {"Easy", "Medium"};
        JComboBox<String> jComboBox = new JComboBox<>(gridSelector);

        jComboBox.setBounds(280, 30, 100, 30);

        //get the changed level if the user changed
        jComboBox.addItemListener(new ItemListener(){
            public void itemStateChanged(ItemEvent e){
                selected = (String)e.getItem();
                showPrev(selected);
                
            }
        });

        pane.add(jComboBox);

        

        JButton startBtn = new JButton("Start!");
        startBtn.setBounds(125, 380, 250, 50);
        pane.add(startBtn);
        startBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                new Minesweeper(selected); //Start the game
            }
        });

        frame.setPreferredSize(frameSize);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }

    //Show the preview of the game
    public void showPrev(String level){
        /*
        TO-DO
        - Finish the preview
        */
        // //if the selected item/level is custom grid, display the custom grid setting
        // switch(selected){
        //     //grid preview
        //     case "Easy":

        //         break;
        //     case "Medium":
        //         break;
        //     case "Hard":
        //         break;
        // }
    }

    public static void main(String[] args) {
        //launch the program
        try{   
            new Menu();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}

