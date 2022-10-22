# time-inverter
## Description
This is a prototype of a game in which you can change the direction through which you travel through time in. Check out the [latest release](https://github.com/keshuook/time-inverter-prototype/releases/). 
# Alpha-1.2.0
Up until [Alpha-1.1.2](https://github.com/keshuook/time-inverter-prototype#alpha-112) you could only invert the direction through which you travel through time in once. Now you can invert this as many times as you want (its limited to three for now). If you are going to invert your time, arrays `positionsX[n]` and `positionsY[n]` will be created with a size of time. Then the stack `positions` will be copied into them. (This stack stores both the x and the y values).
If you are going to uninvert yourself, the arrays will be of length the sum of half the size of the positions stack and time. These arrays will be added forwards. The code is as follows.
```java
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
```
## History
## Alpha-1.1.2
If the computer can't handle rendering a warning is given.
```java
// Code for rendering
// In main method.
while(true) {
  frameTime = System.currentTimeMillis();
  handleGraphics();
}
```
```java
// In handleGraphics method
try {
  long drawingTime = System.currentTimeMillis() - frameTime;
  Thread.sleep((1000/FPS)-drawingTime);
}
catch (InterruptedException ie) {
  ie.printStackTrace();
}
catch (IllegalArgumentException iae) {
  System.out.println("[Warn] The game is not able to render at "+FPS+" FPS."); 
}
// FPS is an integer set to 30
```
## Alpha-1.1.1
To invert the direction of time, I convert it to an array. In [Prototype 2](https://github.com/keshuook/time-inverter-prototype#prototype-2) I achieved this by using a temp stack to help reverse the original stack. I now realised that I could just directly copy the stack backward into the array like in the following code.
```java
positionsX = new int[time];
positionsY = new int[time];
for(int i = time-1;i >= 0;i--){
  positionsY[i] = positions.pop();
  positionsX[i] = positions.pop();
}
```
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

**The whole code can be found in this repository.**
