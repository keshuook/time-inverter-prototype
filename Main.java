import javax.swing.JFrame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

import java.util.Stack;

public class Main {
    static final Color BG_COLOR = new Color(44, 51, 51);
    static final Color FG_COLOR = new Color(231, 246, 242);
    static final Color BG_2_COLOR = new Color(57, 91, 100);
    static final Color FG_2_COLOR = new Color(165, 201, 202);
    private static JFrame frame;
    private static Graphics g;
    private static Position p = new Position();
    public static int[] level = 
        {-250, -300, 30, 600,
            250, -300, 30, 600,
            -250, -300, 500, 30,
            -250, 300, 530, 30};
    private static Stack<Integer> positions = new Stack<>();
    private static boolean inverted = false;
    private static int time = 0;
    private static int FPS = 30;
    private static int numberOfInversions = 0;
    private static int[][] positionsX = new int[3][];
    private static int[][] positionsY = new int[3][];
    private static long frameTime = 0;

    public static void main(String args[]) {
        p.reset();
        frame = new JFrame();

        frame.setSize(700, 550);
        frame.setLocation(200, 50);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setBackground(BG_COLOR);
        frame.setVisible(true);

        frame.addKeyListener(new GameKeyListener(p));
        frame.requestFocus();
        while(true) {
            frameTime = System.currentTimeMillis();
            handleGraphics();
        }
    }

    private static void handleGraphics() {
        int x = 0;
        int y = 0;
        g = frame.getGraphics();
        g.clearRect(0, 0, 700, 550);
        if(p.getX() <= 200 && p.getX() >= 100 && p.getY() >= 30 && p.getY() <= 80){
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
            p.reset();
        }
        fillRectangle(g, -220, -80, 100, 50, BG_2_COLOR);
        fillRectangle(g, 150, -80, 100, 50, BG_2_COLOR);
        fillRectangle(g, -200, -80, 100, 50, BG_2_COLOR);
        for(int i = 0;i < level.length;i+=4) {
            fillRectangle(g, level[i], level[i+1], level[i+2], level[i+3], FG_2_COLOR);
        }
        g.setColor(FG_COLOR);
        g.fillRect(340, 265, 20, 20);

        positions.push(p.getX());
        positions.push(p.getY());

        if(inverted) time--;
        else time++;

        for(int i = 0;i < numberOfInversions;i++){
            try {
                fillRectangle(g, -positionsX[i][time]-10, -positionsY[i][time]-10, 20, 20, FG_COLOR);
            }catch(ArrayIndexOutOfBoundsException e){}
        }

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

    private static void drawRectangle(Graphics g, int x, int y, int width, int height, Color c){
        g.setColor(c);
        g.drawRect(350+x+p.getX(), 275+y+p.getY(), width, height);
    }

    private static void fillRectangle(Graphics g, int x, int y, int width, int height, Color c){
        g.setColor(c);
        g.fillRect(350+x+p.getX(), 275+y+p.getY(), width, height);
    }

    private static void staticFillRectangle(Graphics g, int x, int y, int width, int height, Color c){
        g.setColor(c);
        g.fillRect(x, y, width, height);
    }

    private static void drawProgress(int c, int total) {
        g.setColor(BG_2_COLOR);
        g.fillRect(0, 0, 700, 100);
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
            p.moveBy(5, 0);
            break;
            case 38:
            p.moveBy(0, 5);
            break;
            case 39:
            p.moveBy(-5, 0);
            break;
            case 40:
            p.moveBy(0, -5);
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
            if(level[i]+this.x-10 < 0 && level[i]+this.x+10 > 0-level[i+2] && level[i+1]+this.y-10 < 0 && level[i+1]+this.y+10 > 0-level[i+3]) {
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
        this.x = -200;
        this.y = 60;
    }
}
