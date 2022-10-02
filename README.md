# time-inverter-prototype
This is a prototype of a game which inverts time. Using stacks in java from `java.util.Stack` every movement played by the player is recorded and pushed to the stack. The x coordinate followed by the why coordinate is pushed to it if the timing is not inverted. On time inversion, a rectangle is drawn from the variables of the stack by popping them (y first and then x).
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
The whole code can be found in this repository.
