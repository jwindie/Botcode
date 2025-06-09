package main;

import java.util.HashSet;

public class Robot {
  final static boolean dualLink = true;

  public int x = 0;
  public int y = 0;
  public int direction;
  public int color;
  private Field field;

  public HashSet<Robot> linkedRobots = new HashSet<>();

  public Robot (int x, int y, int d, int c, Field field) {
    this.x = x;
    this.y = y;
    this.direction = d;
    this.color = c;
    this.field = field;
  }

  public void moveForward() {
    int _x = x;
    int _y = y;

    switch (direction) {
      case 0 -> _y --; // forward
      case 1 -> _x ++; // right
      case 2 -> _y ++; // down
      case 3 -> _x --; // left
      default -> System.err.println("Invalid direction: " + direction);
    }

    //validate the position
    if (field.validatePosition(_x, _y)) {
      x = _x;
      y = _y;
    }
    else System.out.println("Invalid position");
  }

  public void moveReverse() {
    int _x = x;
    int _y = y;

    switch (direction) {
      case 0 -> _y ++; // forward
      case 1 -> _x --; // right
      case 2 -> _y --; // down
      case 3 -> _x ++; // left
      default -> System.err.println("Invalid direction: " + direction);
    }

    //validate the position
    if (field.validatePosition(_x, _y)) {
      x = _x;
      y = _y;
    }
  }

  public void turnLeft() {
    direction --;
    if (direction < 0) direction += 4;
  }

  public void turnRight() {
    direction ++;
    if (direction > 3) direction -= 4;
  }

  public void link (Robot robot) {
    if (robot == this) return;
    this.linkedRobots.add (robot);
    if (dualLink) robot.linkedRobots.add (this);
  }

  public void unlink (Robot robot) {
    if (robot == this) return;
    this.linkedRobots.remove (robot);
    if (dualLink) robot.linkedRobots.remove (this);
  }

  public void clearLinks() {
    linkedRobots.clear();
  }
}
