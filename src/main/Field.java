package main;

import processing.core.PGraphics;
import targ.Literal;

public class Field extends Window {

  int gridDimensions;
  float gridSpacing;
  boolean isBounded = true;

  //robots
  Robot[] robots;
  Robot mainRobot;
  int robotColors[];
  int[][] occupiedPositions;

  public Field (int width, PGraphics g) {
    this.width = width;
    this.height = width;
    this.g = g;
  }

  public void draw (float x, float y) {
    //background
    g.fill(g.color (45, 42, 40));
    g.noStroke();
    g.rectMode(CORNER);
    g.rect (x, y, height, height);
    
    //grid
    drawGrid(x, y, 10);

    //robots
    for (int i = 0; i < robots.length; i ++) {
      drawRobot(robots[i], x, y, robots[i] == mainRobot);
    }
  }

  public void setGridDimensions(int gridDimensions) {
    this.gridDimensions = gridDimensions;
    this.gridSpacing = (float) height / gridDimensions;
  }

   public void createRobots() {
    robotColors = new int[]{
      g.color(240, 230, 210),   // Warm parchment white (slightly cleaner)
      g.color(215, 125, 40),    // Retro burnt orange
      g.color(190, 60, 70),     // Deep vintage red
      g.color(80, 125, 145),    // Desaturated denim blue
      g.color(110, 145, 85),    // Olive-moss green
      g.color(135, 100, 145),   // Vintage plum purple (slightly richer)
      g.color(120, 85, 60),   // Rich retro brown
    };

    robots = new Robot[robotColors.length];
    occupiedPositions = new int[robots.length][2];

    for (int i = 0; i < robots.length; i ++) {
      robots[i] = new Robot(0, 0, 0, robotColors[i], this);
    }
    
    //position and orient them
    resetRobots();
    
  }

  private void drawGrid(float xOffset, float yOffset, int gridThickness) {
    float halfGridSpacing = gridSpacing / 2f;
    float halfGridThickness = gridThickness / 2f;
    float corner = (gridSpacing- halfGridThickness) * .15f;
    float smallCorner = corner / 2;
    float[] corners = new float[4];

    g.noStroke();
    g.fill(10, 8, 6, 25);
    g.rectMode(CENTER);

    for (int x = 0; x < gridDimensions; x ++) {
      for (int y = 0; y < gridDimensions; y ++) {

        // set corners
        corners[0] = corners[1] = corners[2] = corners[3] = corner;

        if (x == 0) corners[0] = corners[3] = smallCorner;
        if (x == gridDimensions -1) corners[1]  = corners[2]= smallCorner;
        if (y == 0) corners[0] = corners[1] = smallCorner;
        if (y == gridDimensions -1) corners[2] = corners[3] = smallCorner;

        //draw a rounded rect at the position
        g.rect (
          xOffset + (gridSpacing * x) + halfGridSpacing,
          yOffset + (gridSpacing * y) + halfGridSpacing,
          gridSpacing - halfGridThickness, gridSpacing - halfGridThickness, 
          corners[0], corners[1], corners[2], corners[3]
        );
      }
    }
  }

  private void drawRobot(Robot robot, float x, float y, boolean isMain) {
    g.strokeCap(ROUND);
    g.rectMode(CENTER);
    g.fill(robot.color);

    g.pushMatrix();

    g.translate(
      x + (robot.x * gridSpacing) + (gridSpacing / 2f),
      y + (robot.y * gridSpacing) + (gridSpacing / 2f)
    );
    g.rotate (App.radians(robot.direction * 90));

    g.noStroke ();
    g.rect (0, 0, gridSpacing * .9f, gridSpacing * .9f, gridSpacing*.1f);

    g.stroke(20);
    g.strokeWeight (6);
    float spread = isMain ? gridSpacing * .3f : gridSpacing * .05f;
    g.line (-spread, -gridSpacing/4f, spread, -gridSpacing/4f);

    g.strokeWeight (2);
    for (Robot linked : robot.linkedRobots) {
      if (linked == mainRobot) {
      g.fill(linked.color);
      g.rect (0, gridSpacing * .1f, gridSpacing/5,  gridSpacing/5, gridSpacing * .05f);
      }
    }

    g.popMatrix();
  }

  private void resetRobots() {
    
    int startPositionX = (gridDimensions / 2) -3;
    int startPositionY = gridDimensions - 1;

    for (int i = 0; i < robots.length; i ++) {
      occupiedPositions[i] = new int[] {
        robots[i].x = startPositionX + i,
        robots[i].y = startPositionY,
      };
      robots[i].direction = 0;
      robots[i].clearLinks();
    }

    mainRobot = robots[0];
  }

  public void moveForward() {
    mainRobot.moveForward();
    for(Robot robot : mainRobot.linkedRobots) {
      robot.moveForward();
    }
  }

  public void moveReverse() {
    mainRobot.moveReverse();
    for(Robot robot : mainRobot.linkedRobots) {
      robot.moveReverse();
    }
  }

  public void turnLeft() {
    mainRobot.turnLeft();
    for(Robot robot : mainRobot.linkedRobots) {
      robot.turnLeft();
    }
  }

  public void turnRight() {
    mainRobot.turnRight();
    for(Robot robot : mainRobot.linkedRobots) {
      robot.turnRight();
    }
  }

  public void activate() {
  }  
  
  public void charge() {
  }

  public void swap(Literal target) {
    mainRobot = getRobotFromLiteral(target);
  }

  public void link(Literal target) {
    if (target == Literal.ALL) {
      for(Robot robot : robots) mainRobot.link(robot);
    } 
    else mainRobot.link(getRobotFromLiteral(target));
  }
  
  public void unlink(Literal target) {
    if (target == Literal.ALL) {
      for(Robot robot : robots) mainRobot.unlink(robot);
    }
    else mainRobot.unlink(getRobotFromLiteral(target));
  }

  public Robot getRobotFromLiteral (Literal literal) {
    switch (literal) {
      case WHITE:   return robots[0];
      case ORANGE:  return robots[1];
      case RED:     return robots[2];
      case BLUE:    return robots[3];
      case PURPLE:  return robots[5];
      case BROWN:   return robots[6];
      // case GREEN:   return robots[4];
      default: throw new RuntimeException("Unhandled literal");
    }
  }

  public boolean validatePosition(int x, int y) {
    for (int i = 0; i < robots.length; i ++) {
      if (occupiedPositions[i][0] == x) return false;
      if (occupiedPositions[i][1] == y) return false;
    }

    //also check to make sure the point is within the fields boundaries
    if (isBounded) {
      if (x < 0 || y < 0) return false;
      if (x >= gridDimensions || y >= gridDimensions) return false;
    }

    return true;
  }
}