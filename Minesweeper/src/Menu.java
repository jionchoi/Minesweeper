import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;


public class Menu{
    private JFrame frame;
    private JPanel pane;
    private Dimension size = new Dimension(1080, 720);

    private String selected;
    //Constructor: Create the Menu Frame
    public Menu(){
        frame = new JFrame();

        pane = new JPanel();
        pane.setBorder(new EmptyBorder(50, 50, 50, 50));
        pane.setLayout(null);

        frame.add(pane, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Minesweeper");
        frame.pack();
        frame.setVisible(true);

        JLabel diffLabel = new JLabel("Choose Difficulty:");
        diffLabel.setFont(new Font("Serief", Font.BOLD, 20));
        diffLabel.setBounds(100, 30, 300, 30);
        pane.add(diffLabel);

        //grid selector
        String[] gridSelector = {"Easy", "Medium", "Hard"};
        JComboBox<String> jComboBox = new JComboBox<>(gridSelector);
        jComboBox.setBounds(280, 30, 100, 30);
        selected = String.valueOf(jComboBox.getSelectedItem());

//        //get the changed level if the user changed
//        jComboBox.addActionListener(new ItemListener(){
//            @Override
//            public void itemStateChanged(ItemEvent e) {
//                if(e.getStateChange() == ItemEvent.SELECTED){
//                    selected = String.valueOf(jComboBox.getSelectedItem());
//                }
//            }
//        });

        pane.add(jComboBox);

        /*
        TO-DO
        - Finish the preview
        */

        //if the selected item/level is custom grid, display the custom grid setting
        switch(selected){
            //grid preview
            case "Easy":

                break;
            case "Medium":
                break;
            case "Hard":
                break;
        }

        JButton startBtn = new JButton("Start!");
        startBtn.setBounds(125, 380, 250, 50);
        pane.add(startBtn);
        startBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //set the menu frame invisible and start the game
                frame.setVisible(false);
                Minesweeper game = new Minesweeper(selected);
                // System.out.println(selected);
            }
        });

        frame.setSize(500, 500);
        frame.setMaximumSize(size);
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
