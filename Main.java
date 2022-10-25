import javax.swing.JFrame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.Font;

import java.util.Stack;

public class Main {
    static final Color BG_COLOR = new Color(44, 51, 51);
    static final Color FG_COLOR = new Color(231, 246, 242);
    static final Color BG_2_COLOR = new Color(57, 91, 100);
    static final Color FG_2_COLOR = new Color(165, 201, 202);
    static final Font FONT = new Font("Arial", Font.CENTER_BASELINE, 25);
    static final int MAX_INV = 1;
    
    private static JFrame frame;
    private static Graphics g;
    private static Position p = new Position();
    private static Coin[] coins = new Coin[10];
    public static int[] level = 
        {-350, -150, 30, 400,
            -350, -180, 700, 30,
            -350, 240, 700, 30,
            315, -150, 30, 400};
    private static Stack<Integer> positions = new Stack<>();
    private static boolean inverted = false;
    private static int time = 300;
    private static int FPS = 30;
    private static int numberOfInversions = 0;
    private static int[][] positionsX = new int[MAX_INV][];
    private static int[][] positionsY = new int[MAX_INV][];
    private static long frameTime = 0;
    private static int score = 0;

    public static void main(String args[]) {
        p.reset();
        frame = new JFrame();

        frame.setSize(700, 550);
        frame.setLocation(200, 50);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setBackground(BG_COLOR);
        frame.setVisible(true);

        for(int i = 0;i < coins.length-1;i++) {
            coins[i] = new Coin(i*25, 0);
        }
        coins[9] = new Coin(225, -5);
        coins[9].big = true;

        frame.addKeyListener(new GameKeyListener(p));
        frame.requestFocus();

        g = frame.getGraphics();
        g.setFont(FONT);

        while(true) {
            frameTime = System.currentTimeMillis();
            handleGraphics();
        }
    }

    private static void handleGraphics() {
        g.clearRect(0, 100, 700, 450);

        for(int i = 0;i < level.length;i+=4) {
            fillRectangle(g, level[i], level[i+1], level[i+2], level[i+3], FG_2_COLOR);
        }

        fillRectangle(g, p.getX(), p.getY(), 20, 20, FG_COLOR);

        positions.push(p.getX());
        positions.push(p.getY());

        if(inverted) time--;
        else time++;

        for(int i = 0;i < numberOfInversions;i++){
            try {
                fillRectangle(g, positionsX[i][time], positionsY[i][time], 20, 20, FG_COLOR);
            }catch(ArrayIndexOutOfBoundsException e){}
        }

        handleCoins();
        handleScore();
        drawProgress(time, 600);
        
        try {
            long drawingTime = System.currentTimeMillis() - frameTime;
            frame.setTitle("Time Inverter | " + (inverted ? "Remaining" : "Elapsed") + "Time: "+time/FPS+"s");
            Thread.sleep((1000/FPS)-drawingTime);
        }
        catch (InterruptedException ie) {
            ie.printStackTrace();
        }
        catch (IllegalArgumentException iae) {
            System.out.println("[Warn] The game is not able to render at "+FPS+" FPS."); 
        }
    }

    private static void invertTime() {
        inverted = inverted ? false : true;
        int i;

        if(inverted) {
            positionsX[numberOfInversions] = new int[time];
            positionsY[numberOfInversions] = new int[time];
            for(i = time-1;!positions.isEmpty();i--){
                positionsY[numberOfInversions][i] = positions.pop();
                positionsX[numberOfInversions][i] = positions.pop();
            }
            for(int j = i;j >= 0;j--){
                positionsY[numberOfInversions][j] = 2000;
                positionsX[numberOfInversions][j] = 2000;
            }
        }else{
            int size = time + positions.size()/2;
            positionsX[numberOfInversions] = new int[size];
            positionsY[numberOfInversions] = new int[size];
            for(i = time;!positions.isEmpty();i++){
                positionsY[numberOfInversions][i] = positions.pop();
                positionsX[numberOfInversions][i] = positions.pop();
            }
            for(int j = i;j < size;j++){
                positionsY[numberOfInversions][j] = 2000;
                positionsX[numberOfInversions][j] = 2000;
            }
        }
        numberOfInversions++;
    }

    private static void handleCoins() {
        for(int i = 0;i < coins.length;i++) {
            if(coins[i].isCollidingWith(p)) {
                if(coins[i].big) {
                    invertTime();
                    coins[i].setCollectedForever();
                }
                coins[i].collected(true);
                if(!coins[i].big) {
                    coins[i].setCollectedAt(time, inverted);
                    score+= 20;
                }
            }
            int[] collected = coins[i].getCollectedAt();
            if(time < collected[0] && time > collected[1]) {
                coins[i].collected(false);
                coins[i].render(g);
            }
        }
    }

    private static void handleScore(){
        g.setColor(BG_2_COLOR);
        g.fillRect(0, 0, 700, 100);
        g.setColor(FG_2_COLOR);
        g.drawString(score+"", 600, 60);
    }

    private static void fillRectangle(Graphics g, int x, int y, int width, int height, Color c){
        g.setColor(c);
        g.fillRect(350+x, 275+y, width, height);
    }

    private static void drawProgress(int c, int total) {
        g.setColor(Color.GREEN);
        int percentCompleted = (int)(c/(double)(total)*100);

        percentCompleted = percentCompleted > 100 ? 100 : percentCompleted;
        for(int i = 0;i <= percentCompleted;i++) {
            g.fillRect(25+(i*5), 60, 3, 5);
        }
        g.setColor(Color.GRAY);
        for(int i = percentCompleted+1;i <= 100;i++) {
            g.fillRect(25+(i*5), 60, 3, 5);
        }
    }
}
class GameKeyListener implements KeyListener {
    private Position p;

    public GameKeyListener(Position p){
        this.p = p;    
    }

    public void keyPressed (KeyEvent e) {
        switch(e.getKeyCode()){
            case 37:
            p.moveBy(-5, 0);
            break;
            case 38:
            p.moveBy(0, -5);
            break;
            case 39:
            p.moveBy(5, 0);
            break;
            case 40:
            p.moveBy(0, 5);
            break;
        }
    }

    public void keyReleased (KeyEvent e) {

    }

    public void keyTyped (KeyEvent e) {

    }
}
class Position {
    private int x = 0;
    private int y = 0;
    public void moveBy(int x,int y){
        int[] level = Main.level;
        this.x += x;
        this.y += y;
        for(int i = 0;i < level.length;i+=4){
            if(this.x >= level[i]-15 && this.x <= level[i]+level[i+2]-5 && this.y >= level[i+1]-15 && this.y <= level[i+1]+level[i+3]-5) {
                this.x -= x;
                this.y -= y;
            }
        }
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    public void reset(){
        this.x = -320;
        this.y = 100;
    }
}
class Coin {
    private int x,y;
    private int[] collectedAt = {2000, -1};
    private boolean collected = false;
    public boolean big = false;
    public Coin(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void render(Graphics g) {
        if(collected) return;
        if(big){
            g.setColor(Color.CYAN);
            g.fillOval(this.x+350, this.y+275, 20, 20);
        }else{
            g.setColor(Color.YELLOW);
            g.fillOval(this.x+350, this.y+275, 10, 10);
        }
    }

    public boolean isCollidingWith(Position p) {
        if(collected) return false;
        int max = this.big ? 15 : 5;
        return p.getX() >= this.x-15 && p.getX() <= this.x+max && p.getY() >= this.y-15 && p.getY() <= this.y+max;
    }

    public void collected(boolean isCollected) {
        this.collected = isCollected;
    }

    public boolean collected(){
        return this.collected;
    }

    public void setCollectedAt(int time, boolean inverted) {
        this.collectedAt[inverted? 1 : 0] = time;
    }

    public void setCollectedForever(){
        this.collectedAt[0] = -1;
        this.collectedAt[1] = 20000;
    }

    public int[] getCollectedAt() {
        return this.collectedAt;
    }
}
