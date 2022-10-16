# time-inverter

## Alpha-1.1.0
A collision code was built into the game. In the `moveBy` function of the `Position` class, the x and y variables are only editted if no collisions occur with any thing in the level array.
```java
int[] level = Main.level;
this.x += x;
this.y += y;
for(int i = 0;i < level.length;i+=4){
  if(level[i]+this.x-10 < 0 && level[i]+this.x+10 > 0-level[i+2] && level[i+1]+this.y-10 < 0 && level[i+1]+this.y+10 > 0-level[i+3]) {
    this.x -= x;
    this.y -= y;
  }
}
```
The code for the neat looking timer is as follows:
```java
private static void drawProgress(Graphics g, int c, int total){
  g.setColor(BG_2_COLOR);
  g.fillRect(0, 0, 700, 100);
  g.setColor(Color.CYAN);
  int percentCompleted = (int)(c/(double)(total)*100);
  percentCompleted = percentCompleted > 100 ? 100 : percentCompleted;
  for(int i = 0;i <= percentCompleted;i++) {
    g.fillRect(25+(i*5), 25, 3, 5);
  }
  g.setColor(Color.GRAY);
  for(int i = percentCompleted+1;i <= 100;i++) {
    g.fillRect(25+(i*5), 25, 3, 5);
  }
}
```

## History
## Prototype 1
This is a prototype of a game which inverts time. Using stacks in java from `java.util.Stack` every movement played by the player is recorded and pushed to the stack. If not inverted, the x coordinate followed by the y coordinate is pushed to the stack. On time inversion, a rectangle is drawn from the variables of the stack by popping them (y first and then x).
```java
private static Stack<Integer> positions = new Stack<>();
.....
void main(){
  if(inverted) {
    int tempy = positions.pop();
    fillRectangle(g, -positions.pop()-10, -tempy-10, 20, 20, BG_2_COLOR);
  }else{
    positions.push(p.getX());
    positions.push(p.getY());
  }
}
```
## Prototype 2
Stacks are still used to push the players position however, on inverting the player's time, two arrays positionsX and positionsY of type int[] are created from the stack. A variable `public static int time` keeps track of the number of frames that have gone foward (and backwards). This is there to keep track of the length of the stack.
```java
// Code for keeping track of stack length
if(inverted) {
   time--;
   fillRectangle(g, -positionsX[time]-10, -positionsY[time]-10, 20, 20, FG_COLOR);
}else{
   time++;
   positions.push(p.getX());
   positions.push(p.getY());
}
```
The code which converts the stack into an array has to create a temp stack which makes sure the stack isn't copied in reverse.
```java
// Code for converting the stack positions to an array
if(condition){
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
```
The whole code can be found in this repository.
