package com.company;

//Libraries
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;
public class Board extends JPanel implements ActionListener {
    // Defining the Dimensions
    // these two determine the size of the board.
    private final int B_WIDTH = 300;
    private final int B_HEIGHT = 300;
    // this defines the size of the apple
    private final int DOT_SIZE = 10;
    // the maximum number of possible dots on the board
    private final int ALL_DOTS = 900;
    // Calculates the random position of the apple
    private final int RAND_POS = 29;
    // the speed of the game
    private final int DELAY = 140;

    // Array that stores the coordinates of the snake
    private final int x[] = new int[ALL_DOTS];
    private final int y[] = new int[ALL_DOTS];

    // Elements of the game
    private int dots;
    private int apple_x;
    private int apple_y;

    // Direction Values
    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inGame = true;

    private Timer timer;
    private Image ball;
    private Image apple;
    private Image head;


    public Board() {
        initialBoard();
    }

    private void initialBoard() {
        addKeyListener(new TAdapter()); // check wina button rah yemchi
        setBackground(Color.BLACK);
        setFocusable(true);

        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        loadImages();
        initGame();
    }

    // this gets us the images for the game
    private void loadImages() {

        ImageIcon iib = new ImageIcon("src/resources/dot.png");
        ball = iib.getImage();

        ImageIcon iia = new ImageIcon("src/resources/apple.png");
        apple = iia.getImage();

        ImageIcon iih = new ImageIcon("src/resources/head.png");
        head = iih.getImage();
    }

    // Creating the snake 'HOORAAAY!!'
    private void initGame() {
        dots = 3;
        for (int z = 0; z < dots; z++) {
            x[z] = 50 - z * 10;
            y[z] = 50;
        }
        locateApple();
        timer = new Timer(DELAY, this);
        timer.start();
    }

    // Check Apple
    private void checkApple() {
        // if the apple collides with the apple we add a joint in the snake
        if ((x[0] == apple_x) && (y[0] == apple_y)) {

            dots++;

            // new Apple appears in a new random location
            locateApple();
        }
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }

    private void doDrawing(Graphics g) {
        if (inGame) {

            g.drawImage(apple, apple_x, apple_y, this);

            for (int z = 0; z < dots; z++) {
                if (z == 0) {
                    g.drawImage(head, x[z], y[z], this);
                } else {
                    g.drawImage(ball, x[z], y[z], this);
                }
            }

            Toolkit.getDefaultToolkit().sync();

        } else {
            gameOver(g);
        }
    }

    private void gameOver(Graphics g) {
        String message = "Game Over";
        Font small = new Font ("Roboto", Font.BOLD, 14);
        FontMetrics metrics = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(message, (B_WIDTH - metrics.stringWidth(message)) / 2, B_HEIGHT / 2);
    }

// -------------- KEY ALGORITHM -------------
    // Movement class
    private void move() {

        // The parts follow the head (hehe not accurate description )
        for (int z = dots; z > 0; z--) {
            x[z] = x[(z - 1)];
            y[z] = y[(z - 1)];
        }

        // The head moves to the left
        if (leftDirection) {
            x[0] -= DOT_SIZE;
        }

        // the head moves to the right
        if (rightDirection) {
            x[0] += DOT_SIZE;
        }

        // the head moves upwards
        if (upDirection) {
            y[0] -= DOT_SIZE;
        }

        // the head moves Downers
        if (downDirection) {
            y[0] += DOT_SIZE;
        }
    }

    // Check Collision
    private void checkCollision() {

        // If the snake hits one of its joints with its head the game is over.
        for (int z = dots; z > 0; z--) {

            if ((z > 4) && (x[0] == x[z]) && (y[0] == y[z])) {
                inGame = false;
            }
        }

        // The game ends if the snake touvhes the board
        if (y[0] >= B_HEIGHT) {
            inGame = false;
        }

        if (y[0] < 0) {
            inGame = false;
        }

        if (x[0] >= B_WIDTH) {
            inGame = false;
        }

        if (x[0] < 0) {
            inGame = false;
        }

        if (!inGame) {
            timer.stop();
        }
    }

    // Apple location  -- I used the Random method so it appears in a different location everytime
    private void locateApple() {

        int r = (int) (Math.random() * RAND_POS);
        apple_x = ((r * DOT_SIZE));

        r = (int) (Math.random() * RAND_POS);
        apple_y = ((r * DOT_SIZE));
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (inGame) {

            checkApple();
            checkCollision();
            move();
        }

        repaint();
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            if ((key == KeyEvent.VK_LEFT) && (!rightDirection)) {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_RIGHT) && (!leftDirection)) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_UP) && (!downDirection)) {
                upDirection = true;
                rightDirection = false;
                leftDirection = false;
            }

            if ((key == KeyEvent.VK_DOWN) && (!upDirection)) {
                downDirection = true;
                rightDirection = false;
                leftDirection = false;
            }
        }
    }
}



