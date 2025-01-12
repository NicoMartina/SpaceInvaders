import javax.swing.*;
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        // 1. Creation of the window where the game is played
        //here we set the size variables of the windows
        int tileSize = 32;
        int rows = 16;
        int columns = 16;
        int boardWidth = tileSize * columns;
        int boardHeight = tileSize * rows;

        //window creation
        JFrame frame = new JFrame("Space Invaders"); //create window
        frame.setSize(boardWidth, boardHeight); //here we set the window
        //frame.setVisible(true); //we set the window so its visible
        frame.setLocationRelativeTo(null); //we center the window
        frame.setResizable(false); //user cannot change the size by dragging the sides
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //we exit the game by pressing the cross


        SpaceInvaders spaceInvaders = new SpaceInvaders();
        frame.add(spaceInvaders);
        frame.pack();
        frame.setVisible(true);

    }
}