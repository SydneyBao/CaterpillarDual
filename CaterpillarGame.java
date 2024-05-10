/***
 *
 * Sydney Bao
 * 12/12/2022
 * CIS 255
 * Professor Moussalem
 * Caterpillar Duel
 * file: CaterpillarGame.java
 *
 * This program detects how each player and number square are positioned in relation to each other. The program detects if a player has collected a number square, and if the next point of the number square or snake is valid.
 * The point is only valid if it is unoccupied by either player and on the board. Additionally, the program senses the user input and passes in the corresponding direction into the setDirection method.
 */

import  java.awt.*;
import  java.awt.event.*;

public class CaterpillarGame extends Frame {
    // setup board dimensions
    final static int BoardWidth = 60;
    final static int BoardHeight = 40;
    final static int SegmentSize = 10;
    // setup the two Caterpillars in their starting positions
    private Caterpillar playerOne = new Caterpillar (Color.blue, new Point(20, 10));
    private Caterpillar playerTwo = new Caterpillar (Color.red, new Point(20, 30));
    private int numSquare = 0;
    private static int frameWidth = (BoardWidth + 1) * SegmentSize;
    private static int frameHeight = (BoardHeight) * SegmentSize;
    private static double numSquareX;
    private static double numSquareY;
    private static int playerOneScore = 0;
    private static int playerTwoScore = 0;
    private static int playerOneCounter = 20;
    private static int playerTwoCounter = 20;
    private static boolean gameOver = false;

    //Constructor
    public CaterpillarGame() {
        setSize((BoardWidth + 1) * SegmentSize, BoardHeight * SegmentSize + 30);
        setTitle("Caterpillar Game");
        addKeyListener(new KeyReader());
    }

    //Main method to initiate program
    public static void main(String [] args) {
        CaterpillarGame world = new CaterpillarGame( );
        world.setVisible(true);
        world.newNumberSquare();
        world.run( );
    }

    Point numSquarePoint = new Point((int)numSquareX, (int)numSquareY);

    //Generates a new number square, incrementing by one and spawning at a random position
    public void newNumberSquare() {
        boolean unique = false;
        numSquare += 1;

        while (unique == false) {
            double x = Math.random() * (frameWidth - 50 + 1) + 50;
            double y = Math.random() * (frameHeight - 50 + 1) + 50;
            Point randomPoint = new Point((int)x, (int)y);

            if (playerOne.inPosition(randomPoint) == false && playerTwo.inPosition(randomPoint) == false) {
                numSquareX = x;
                numSquareY = y;
                numSquarePoint = new Point((int)numSquareX, (int)numSquareY);
                unique = true;
            }
        }
    }

    //Displays number square, player scores, and "game over" text on the screen
    public void paint(Graphics g) {
        g.drawString(Integer.toString(numSquare), (int) numSquareX, (int)numSquareY);
        g.drawString("Player One Score: " + playerOneScore, 50, 410);
        g.drawString("Player Two Score: " + playerTwoScore, 450, 410);
        playerOne.paint(g);
        playerTwo.paint(g);

        if (gameOver == true) {
            g.setColor(Color.RED);
            float f = 60.0f;
            g.setFont(g.getFont().deriveFont(f));
            g.drawString("Game Over", 150, 250);
        }
    }

    //Players move on screen by passing in "this" or the Caterpillar Game running
    public void movePieces() {
        playerOne.move(this);
        playerTwo.move(this);
    }

    //Start the game by constantly moving the snakes and repainting the snakes to their corresponding positions
    public void run() { // now start the game
        while (true) {
            movePieces();
            repaint();

            try {
                Thread.sleep(100); // create  animation illusion
            } catch (Exception e) {  } // must be in try-catch
        }
    }

    //Checks if the next position is on the board or occupied by either of the snakes
    public boolean canMove(Point np) {
        // get x, y coordinate
        int x = np.x;
        int y = np.y;
        squareScore(np);

        // test playing board boundaries
        if ((x <= 0) || (y <= 0)) return false;
        if ((x >= BoardWidth) || (y >= BoardHeight)) return false;

        // test caterpillars: can’t move through self or other Caterpillar
        if (playerOne.inPosition(np)) return false;
        if (playerTwo.inPosition(np)) return false;

        // ok, safe square
        return true;
    }

    //Checks if player has collected number square. If so, a new number is generated and the corresponding points are added to the player's score
    //If the player collects a number square, their counter is reset and a new segment is added to their body
    public int squareScore(Point np) {
        int scoreX = (int)Math.round(numSquareX/10.0) * 10;
        int scoreY = (int)Math.round(numSquareY/10.0) * 10;
        double playerPositionX = (np.getX() + 1) * SegmentSize;
        double playerPositionY = (np.getY()) * SegmentSize + 30;
        Point scoringPosition = new Point(scoreX, scoreY);
        Point playerPosition = new Point((int) playerPositionX, (int)playerPositionY);

        if (playerPosition.equals(scoringPosition)) {
            Point playerOnePos = playerOne.newPosition();
            playerOnePos.x = (playerOnePos.x + 1) * SegmentSize;
            playerOnePos.y = (playerOnePos.y) * SegmentSize + 30;
//
            if (playerOnePos.x == scoringPosition.x && playerOnePos.y == scoringPosition.y) {
                playerOneScore += numSquare;
                playerOne.body.add(playerOne.newPosition());

                playerOneCounter = 20;
            } else {
                playerTwoScore += numSquare;
                playerTwo.body.add(playerTwo.newPosition());
                playerTwoCounter = 20;
            }


            newNumberSquare();
            return numSquare;
        } else {
            return 0;
        }
    }

    //Change player's direction depending on what keys the user presses.
    //For each step, the player's counter is decreased by 1 and if counter reaches 0, a segment of the player is removed and counter is reset
    private class KeyReader extends KeyAdapter {
        public void keyPressed (KeyEvent e) {
            char c = e.getKeyChar( );
            switch (c) {

// player One keys
                case 'q': playerOne.setDirection('Z'); break;
                case 'a': playerOne.setDirection('W'); break;
                case 'd': playerOne.setDirection('E'); break;
                case 'w': playerOne.setDirection('N'); break;
                case 's': playerOne.setDirection('S'); break;
// player Two keys
                case 'p': playerTwo.setDirection('Z'); break;
                case 'j': playerTwo.setDirection('W'); break;
                case 'l': playerTwo.setDirection('E'); break;
                case 'i': playerTwo.setDirection('N'); break;
                case 'k': playerTwo.setDirection('S'); break;

// ignore all other keys
            }  // end switch
            if (c == 's' || c == 'a' || c == 'd' || c == 'w') {
                playerOneCounter -= 1;

                if (playerOneCounter == 0) {
                    playerOneCounter = 20;
                    playerOne.body.remove();

                    if (playerOne.body.size() == 1) {
                        gameOver = true;
                    }
                }
            } else if (c == 'j' || c == 'l' || c == 'i' || c == 'k') {
                playerTwoCounter -= 1;

                if (playerTwoCounter == 0) {
                    playerTwoCounter = 20;
                    playerTwo.body.remove();

                    if (playerTwo.body.size() == 1) {
                        gameOver = true;
                    }
                }
            }
        }  // end keyPressed
    }  // end KeyReader private inner (nested) class
}  // public class CaterpillarGame



