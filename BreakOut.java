import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class BreakOut extends JPanel implements ActionListener, MouseListener{

    private static double Y_SPEED = BreakoutConstants.VELOCITY_Y;
    private static double X_SPEED;

    static final int SCREEN_WIDTH = BreakoutConstants.CANVAS_WIDTH;
    static final int SCREEN_HEIGHT = BreakoutConstants.CANVAS_HEIGHT;

    private BreakOutRect top;
    private BreakOutRect playerPaddle;
    private BreakOutRect rightwall;
    private BreakOutRect leftwall;

    int x ;
    int y ;
    int score = 0;

    static final int UNIT_SIZE = 50;
    static final int DELAY = 10; //120
    char direction = 'R';
    boolean running = false;
    Timer timer;

    private BreakOutBall breakOutBall;
    private BreakOutRect[][] bricks;
    private int currentScore = 0;
    private int currentLives = BreakoutConstants.NTURNS;
    private static final Color[] colors = { Color.red, Color.red, Color.orange,Color.orange, Color.yellow,Color.yellow,
            Color.green,Color.green, Color.cyan , Color.cyan };

    public BreakOut() {
        this.setPreferredSize(new Dimension(BreakoutConstants.CANVAS_WIDTH, BreakoutConstants.CANVAS_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);

        this.addMouseListener(this);

        top = new BreakOutRect(0, 0, BreakoutConstants.CANVAS_WIDTH, BreakoutConstants.CANVAS_HEIGHT / 75);
        playerPaddle = new BreakOutRect(BreakoutConstants.CANVAS_WIDTH * 1 / 2, BreakoutConstants.CANVAS_HEIGHT - BreakoutConstants.PADDLE_Y_OFFSET - 50, BreakoutConstants.PADDLE_WIDTH, BreakoutConstants.PADDLE_HEIGHT);
        rightwall = new BreakOutRect(BreakoutConstants.CANVAS_WIDTH * 99 / 100, 0, BreakoutConstants.CANVAS_WIDTH / 80, BreakoutConstants.CANVAS_HEIGHT);
        leftwall = new BreakOutRect(0, 0, BreakoutConstants.CANVAS_WIDTH / 75, BreakoutConstants.CANVAS_HEIGHT);
        breakOutBall = new BreakOutBall(BreakoutConstants.CANVAS_HEIGHT/2, BreakoutConstants.CANVAS_HEIGHT*2/3, BreakoutConstants.BALL_RADIUS, BreakoutConstants.BALL_RADIUS);
        startGame();
    }

    public void startGame(){

        brickCollection(8);

        running = true;
        timer = new Timer(DELAY,this);
        timer.start();
    }

    public void brickCollection(double xMin) {

        // construct the array (number of bricks 1-100)
        bricks = new BreakOutRect[BreakoutConstants.NBRICK_COLUMNS][BreakoutConstants.NBRICK_ROWS];

        for (int row = 0; row < BreakoutConstants.NBRICK_COLUMNS; row++) {
            for (int col = 0; col < BreakoutConstants.NBRICK_ROWS; col++) {

                int dx= (int) (xMin + col*BreakoutConstants.BRICK_WIDTH + col*BreakoutConstants.BRICK_SEP);
                int dy= BreakoutConstants.BRICK_Y_OFFSET + row*BreakoutConstants.BRICK_HEIGHT + row*BreakoutConstants.BRICK_SEP;

                bricks[row][col] = new BreakOutRect(dx, dy, (int) BreakoutConstants.BRICK_WIDTH,
                        BreakoutConstants.BRICK_HEIGHT);
            }
        }

    }

    public void drawBrick(Graphics g) {

        for (int row = 0; row < BreakoutConstants.NBRICK_COLUMNS; row++) {
            for (int col = 0; col < BreakoutConstants.NBRICK_ROWS; col++) {
                if (bricks[row][col] != null){
                    bricks[row][col].draw(g, colors[ row % colors.length]);
                }
            }
        }

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){

        if(running) {
            g.setColor(Color.white);
            g.setFont( new Font("Ink Free",Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            //g.drawString("Score: "+score, (SCREEN_WIDTH - metrics.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize());

            drawBrick(g);

            breakOutBall.draw(g);

            playerPaddle.draw(g, Color.WHITE);

        }

        else {
            timer.stop();

            if (score >= 50){
                gameWin(g);

            }
            else {
                gameOver(g);

            }
        }

    }

    public void gameOver(Graphics g) {
        //Score

        g.setColor(Color.red);
        g.setFont( new Font("Ink Free",Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());

        //Game Over text
        g.setColor(Color.red);
        g.setFont( new Font("Ink Free",Font.BOLD, 75));

        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
    }

    public void gameWin(Graphics g) {
        //Score

        g.setColor(Color.red);
        g.setFont( new Font("Ink Free",Font.BOLD, 40));
        g.drawString("Congratulation , you won. You have reach 100" , SCREEN_WIDTH/10 , SCREEN_HEIGHT/2 );

    }

    public void move() {

        PointerInfo pi = MouseInfo.getPointerInfo();

        Point p = pi.getLocation();

        playerPaddle.move(p);

        switch(direction) {
            case 'L':
                breakOutBall.moveL();
                break;
            case 'T':
                breakOutBall.moveT();
                break;
            case 'B':
                breakOutBall.moveB();
                break;
            case 'R':
                breakOutBall.moveR();
                break;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(running) {
            move();
            checkBorder();
            checkCollisionPaddle();
            checkCollisionBrick();
        }

        repaint();
    }

    public void checkBorder(){

        if (breakOutBall.y > BreakoutConstants.CANVAS_HEIGHT){
            breakOutBall.setLocation(SCREEN_WIDTH/2 , SCREEN_HEIGHT/2);
        }

        if ( ( breakOutBall.x < 20 ) && (direction == 'B' ) ){ //hit left wall
            direction = 'R' ;
        }

        if ( ( breakOutBall.x < 20) && (direction == 'L' ) ){ //hit left wall
            direction = 'T' ;
        }

        if ( ( breakOutBall.x + breakOutBall.width > SCREEN_WIDTH - breakOutBall.width) && (direction == 'T' ) ){ //hit right wall
            direction = 'L' ;
        }

        if ( ( breakOutBall.x + breakOutBall.width > SCREEN_WIDTH - breakOutBall.width) && (direction == 'R' ) ){ //hit right wall
            direction = 'B' ;
        }

        if ( ( breakOutBall.y < BreakoutConstants.BRICK_Y_OFFSET) && (direction == 'L' ) ){ //hit top wall
            direction = 'B' ;
        }

        if ( ( breakOutBall.y < BreakoutConstants.BRICK_Y_OFFSET ) && (direction == 'T' ) ){ // hit top wall
            direction = 'R' ;
        }

    }

    public void checkCollisionPaddle(){
        if ((breakOutBall.y > playerPaddle.y - 30) && (breakOutBall.y < playerPaddle.y - 10) && (breakOutBall.x + breakOutBall.width/2 < playerPaddle.x + playerPaddle.width + 30) && (breakOutBall.x + breakOutBall.width/2 > playerPaddle.x - 30)){
            if (direction == 'R' ) {
                direction = 'T';
            }
            else if (direction == 'B' ){
                direction = 'L' ;
            }
        }
    }

    public boolean hitBrickupdown() {

        boolean hit = false;

        for (int row = 0; row < BreakoutConstants.NBRICK_COLUMNS; row++) {
            for (int col = 0; col < BreakoutConstants.NBRICK_ROWS; col++) {
                if ((bricks[row][col] != null) && (breakOutBall.overlaps(bricks[row][col]))) {
                    bricks[row][col] = null;
                    hit = true;
                    break;
                }
            }
        }
        return hit;

    }

    public void checkCollisionBrick(){
        if (hitBrickupdown()){
            //System.out.println("true");
            if (direction == 'L'){
                //System.out.println("L");
                direction = 'B';
            }
            else if (direction == 'T'){
                //System.out.println("T");
                direction = 'R';
            }
            else if (direction == 'B'){
                //System.out.println("B");
                direction = 'L';
            }
            else if (direction == 'R'){
                //System.out.println("R");
                direction = 'T';
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        timer.start();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        timer.stop();
    }

}