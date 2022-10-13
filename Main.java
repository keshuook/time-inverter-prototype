import javax.swing.JFrame;

import java.awt.Color;
import java.awt.Canvas;
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
    private static Canvas canvas;
    private static Position p = new Position();
    private static int[] level = 
    {-260, 90, 520, 10};
    private static Stack<Integer> positions = new Stack<>();
    private static boolean inverted = false;
    public static int time = 0;
    public static int[] positionsX = new int[0];
    public static int[] positionsY = new int[0];

    public static void main(String args[]) {
        frame = new JFrame();

        canvas = new Canvas();
        canvas.setSize(700, 550);
        canvas.setBackground(BG_COLOR);

        frame.setLocation(200, 50);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        frame.add(canvas);
        frame.pack();
        frame.setVisible(true);

        canvas.addKeyListener(new GameKeyListener(p));
        frame.requestFocus();
        canvas.requestFocusInWindow();

        while(true) {
            try {
                Thread.sleep(1000/60);
                frame.setTitle("Time Inverter | " + (inverted ? "Remaining" : "Elapsed") + "Time: "+time/60+"s");
            }
            catch (java.lang.InterruptedException ie) {
                ie.printStackTrace();
            }
            handleGraphics();
        }
    }

    private static void handleGraphics() {
        int x = 0;
        int y = 0;
        Graphics g = canvas.getGraphics();

        g.clearRect(0, 0, 700, 550);
        if(p.getX() <= 190 && p.getX() >= 110 && p.getY() >= 40 && p.getY() <= 70){
            inverted = inverted ? false : true;

            positionsX = new int[time];
            positionsY = new int[time];
            Stack<Integer> temp = new Stack<Integer>();
            for(int i = 0;i < time*2;i++){
                temp.push(positions.pop());
            }
            for(int i = 0;i < time;i++){
                positionsX[i] = temp.pop();
                positionsY[i] = temp.pop();
            }
            p.reset();
        }
        fillRectangle(g, -200, -80, 100, 50, BG_2_COLOR);
        for(int i = 0;i < level.length;i+=4) {
            fillRectangle(g, level[i], level[i+1], level[i+2], level[i+3], FG_2_COLOR);
        }
        g.setColor(FG_COLOR);
        g.fillRect(340, 265, 20, 20);
        if(inverted) {
            time--;
            fillRectangle(g, -positionsX[time]-10, -positionsY[time]-10, 20, 20, FG_COLOR);
        }else{
            time++;
            positions.push(p.getX());
            positions.push(p.getY());
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
        this.x += x;
        this.y += y;
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    public void reset(){
        this.x = 0;
        this.y = 0;
    }
}
