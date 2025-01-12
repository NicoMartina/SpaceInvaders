import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class SpaceInvaders extends JPanel {
    class Block {
        int x;
        int y;
        int width;
        int height;
        Image img;
        boolean alive = true; //for the aliens
        boolean used = false; //for the bullets

        Block(int x, int y, int width, int height, Image img){
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.img = img;
        }
    }


    //JPanel allow us to add graphics to our window

    //here we set the variables for the game board
    int tileSize = 32;
    int rows = 16;
    int columns = 16;
    int boardWidth = tileSize * columns;
    int boardHeight = tileSize * rows;
    Image shipImg;
    Image alienImg;
    Image alienCyanImg;
    Image alienMagentaImg;
    Image alienYellowImg;

    ArrayList<Image> alienImgArray;

    //for the ship
    int shipWidth = tileSize*2;
    int shipHeight = tileSize;
    int shipX = tileSize*columns/2 - tileSize;
    int shipY = boardHeight - tileSize * 2;
    Block ship;
    SpaceInvaders(){
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.black);

        //load images
        shipImg = new ImageIcon(getClass().getResource("./ship.png")).getImage();
        alienImg = new ImageIcon(getClass().getResource("./alien.png")).getImage();
        alienCyanImg = new ImageIcon(getClass().getResource("./alien-cyan.png")).getImage();
        alienMagentaImg = new ImageIcon(getClass().getResource("./alien-magenta.png")).getImage();
        alienYellowImg = new ImageIcon(getClass().getResource("./alien-yellow.png")).getImage();

        alienImgArray = new ArrayList<Image>();
        alienImgArray.add(alienImg);
        alienImgArray.add(alienCyanImg);
        alienImgArray.add(alienMagentaImg);
        alienImgArray.add(alienYellowImg);

        ship = new Block(shipX, shipY, shipWidth, shipHeight, shipImg);
        

    }

}
