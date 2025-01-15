import java.awt.*;
import java.awt.event.*;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
import javax.sound.sampled.*;

public class SpaceInvaders extends JPanel implements ActionListener, KeyListener {

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

    //ship
    int shipWidth = tileSize*2;
    int shipHeight = tileSize;
    int shipX = tileSize*columns/2 - tileSize;
    int shipY = boardHeight - tileSize * 2;
    int shipVelocityX = tileSize;
    Block ship;

    //aliens
    ArrayList<Block> alienArray;
    int alienWidth = tileSize*2;
    int alienHeight = tileSize;
    int alienX = tileSize;
    int alienY = tileSize;

    int alienRows = 2;
    int alienColumns = 3;
    int alienCount = 0; // number of aliens killed
    int alienVelocityX = 1; //alien moving speed

    //bullets
    ArrayList<Block> bulletArray;
    int bulletWidth = tileSize/8;
    int bulletHeight = tileSize/2;
    int bulletVelocityY = -10; //speed of the bullet


    // Flags for movement and shooting
    boolean moveLeft = false;
    boolean moveRight = false;
    boolean shooting = false;




    Timer gameLoop; //this is for our game to update the graphics every time we shoot a bullet or move the ship sideways
    int score = 0;
    boolean gameOver = false;
    SpaceInvaders(){
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.black);
        setFocusable(true);
        addKeyListener(this);

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
        alienArray = new ArrayList<Block>();
        bulletArray = new ArrayList<Block>();

        gameLoop = new Timer(1000/60, this); //this refers to the spaceInvaders class
        createAliens();
        gameLoop.start();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }


    //DRAW ON THE GAME WINDOW
    public void draw(Graphics g) {
        //ship
        g.drawImage(ship.img, ship.x, ship.y, ship.width, ship.height, null);

        //alien. there are several alien so we need to go through the array.
        for (int i = 0; i < alienArray.size(); i++){
            Block alien =  alienArray.get(i);   //here we are creating a random alien
            if(alien.alive){      //here we check if the alien is alive, ergo, not killed by the ship.
                g.drawImage(alien.img, alien.x, alien.y, alien.width, alien.height, null); //here we draw the alien on the board
            }
        }

        //bullets
        g.setColor(Color.WHITE);
        for (int i = 0; i < bulletArray.size(); i++){
            Block bullet = bulletArray.get(i);
            if (!bullet.used) {
                g.drawRect(bullet.x, bullet.y, bullet.width, bullet.height);
            }
        }

        //SCORE
        g.setColor(Color.green);
        g.setFont(new Font("ariel", Font.PLAIN, 32));
        if (gameOver) {
            g.drawString("Game over: " + String.valueOf(score), 10,35); //this will write out game over and your score
            g.drawString("Please press any ", 10, 70 );
            g.drawString("key to restart the game", 10, 100);
        }
        else {
            g.drawString(String.valueOf(score), 10, 35); //this will write out your score
        }
    }

    public void move(){  // this function will handle of the movements for the aliens and the bullets
        //ALIENS
        for (int i = 0; i < alienArray.size(); i++){
            Block alien = alienArray.get(i);
            if (alien.alive){
                alien.x += alienVelocityX;

                //if alien touches the sides
                if (alien.x + alien.width >= boardWidth || alien.x <= 0){
                    alienVelocityX *= -1;
                    alien.x += alienVelocityX*2;

                    //move all aliens down one row
                    for (int j = 0; j < alienArray.size(); j++){
                        alienArray.get(j).y += alienHeight;
                    }
                }

                if (alien.y >= ship.y) {
                    gameOver = true;
                }
            }
        }

        //BULLETS
        for (int i = 0; i < bulletArray.size(); i++){
            Block bullet = bulletArray.get(i);
            bullet.y += bulletVelocityY;

            //bullet collision with aliens
            for (int j = 0; j < alienArray.size(); j++){
                Block alien = alienArray.get(j);
                if (!bullet.used && alien.alive && detectCollision(bullet, alien)){
                    bullet.used = true;
                    alien.alive = false;
                    alienCount--;
                    score += 100;
                }
            }
        }

        //CLEAR BULLETS

        //here we check if we have bullets in our array AND those bullets have been used OR those bullets are past the screen. We should delete them.
        // We do this for memory purposes
        while (bulletArray.size() > 0 && (bulletArray.get(0).used || bulletArray.get(0).y < 0)){
            bulletArray.remove(0);
        }

        //next level
        if (alienCount == 0){
            //bonus points
            score += alienColumns * alienRows * 100;

            //increase the number of aliens columns and rows by 1
            alienColumns = Math.min(alienColumns + 1, columns/2 - 2); //cap column  at 16/2 - 2 = 6
            alienRows = Math.min(alienRows + 1, rows - 6); //cap rows at 16 - 6 = 10

            alienArray.clear();
            bulletArray.clear();

            //increase alien speed
            alienVelocityX += alienVelocityX > 0 ? 1 : -1; //mantain direction while increasing speed
            createAliens();
        }

        // Continuous movement
        if (moveLeft && ship.x - shipVelocityX >= 0) {
            ship.x -= shipVelocityX;
        }
        if (moveRight && ship.x + ship.width + shipVelocityX <= boardWidth) {
            ship.x += shipVelocityX;
        }

        // Continuous shooting
        if (shooting) {
            fireBullet();
        }



    }


    //better shooting
    public void fireBullet() {
        if (bulletArray.size() < 3) { // Limit number of bullets on screen
            Block bullet = new Block(ship.x + shipWidth * 15 / 32, ship.y, bulletWidth, bulletHeight, null);
            bulletArray.add(bullet);
        }
    }



    //CREATE ALIENS
    public void createAliens(){
        Random random = new Random(); // this will create a random number, that will choose a random alien pic
        for (int r = 0; r < alienRows; r++){
            for ( int c = 0; c < alienColumns; c++){
                int randomImgIndex = random.nextInt(alienImgArray.size());
                Block alien = new Block(
                        alienX + c*alienWidth,
                        alienY + r*alienHeight,
                        alienWidth,
                        alienHeight,
                        alienImgArray.get(randomImgIndex)
                );
                alienArray.add(alien);
            }
        }
        alienCount = alienArray.size();
    }

    public boolean detectCollision(Block a, Block b){
        return a.x < b.x  + b.width &&    //  a's top left corner doesn't reach  b's top  right corner
                a.x + a.width > b.x &&    //  a's top right corner passes  b's top  left corner
                a.y < b.y + b.height &&   //  a's top left corner doesn't reach  b's bottom  left corner
                a.y + a.height > b.y;     //  a's bottom left corner passes b's top  right corner
    }

    public void playSound(String soundFile) {
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(getClass().getResource(soundFile));
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver) {
            gameLoop.stop();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (gameOver) {
            ship.x = shipX;
            alienArray.clear();
            bulletArray.clear();
            score = 0;
            alienVelocityX = 1;
            alienColumns = 3;
            alienRows = 2;
            gameOver = false;
            createAliens();
            gameLoop.start();
        } else {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT -> moveLeft = true;
                case KeyEvent.VK_RIGHT -> moveRight = true;
                case KeyEvent.VK_SPACE -> fireBullet();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT -> moveLeft = false;
            case KeyEvent.VK_RIGHT -> moveRight = false;
            //case KeyEvent.VK_SPACE -> shooting = false;
        }
    }


}
