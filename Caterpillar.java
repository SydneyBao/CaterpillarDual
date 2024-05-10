/***
 * This program displays a caterpillar and controls its movement by setting the proper direction and adding/removing body segments
 * until the program detects the snake is in the proper position.
 */

import java.util.*;
import  java.awt.*;
import  java.util.Enumeration;

class Caterpillar {
    private Color color; // Tricky! Notice how position is accessed
    private Point position; // by different methods in this class...
    private char direction = 'E';
    public Queue<Point> body = new LinkedList<>();
    private Queue<Character> commands = new LinkedList<>() ;

    //constructor
    public Caterpillar (Color c, Point sp) {
        color = c;

        for (int i = 0; i < 10; i++) { // each caterpillar is
            position = new Point(sp.x + i, sp.y); // 10 pieces (circles)
            body.add(position);
        }
    }

    //connects user keyboard input with direction of movement for snake
    public void setDirection (char d) {
        commands.add(new  Character(d));
    }

    //moves the snake by adding and removing segments
    public void move (CaterpillarGame game) {
        // first see if we should change direction
        if (commands.size() > 0) {
            Character c = (Character) commands.peek(); // just peek
            commands.remove();
            direction = c.charValue();    // Character wrapper to char

            if (direction == 'Z') return;
        }

        // then find new position
        Point np = newPosition();

        if (game.canMove(np)) {
            // erase one segment, add another
            body.remove();
            body.add(np);
            position = np;
        }
    }

    //changes x and y values depending on the direction the snake must move
    public Point newPosition() {
        int x =  position.x;
        int y =  position.y;

        if (direction == 'E') x++;
        else if (direction == 'W') x--;
        else if (direction == 'N') y--;
        else if (direction == 'S') y++;
        Point point = new Point(x, y);
        return point;
    }

    //checks if snake is in proper position by looping through all body segments
    public boolean inPosition(Point np) {
        Enumeration e = Collections.enumeration(body);

        while (e.hasMoreElements()) {
            Point location = (Point) e.nextElement();
            if (np.equals(location)) {
                return true;
            }
        }
        return false;
    }

    //loops through all body segments of snake and draws them in the proper position
    public void paint (Graphics g) {
        g.setColor(color);
        Enumeration e = Collections.enumeration(body);

        e.asIterator().forEachRemaining(entry -> {
            while (e.hasMoreElements( )) {
                Point p = (Point) e.nextElement();
                g.fillOval(5 + CaterpillarGame.SegmentSize * p.x, 15 + CaterpillarGame.SegmentSize * p.y, CaterpillarGame.SegmentSize, CaterpillarGame.SegmentSize);
                body.remove(e);
            }
        });
    }
}

