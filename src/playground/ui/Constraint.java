package playground.ui;

import playground.Color;
import playground.Float2;
import processing.core.PGraphics;

public class Constraint implements UIConstants {

  public ConstraintRule rule;  
  public float[] args = new float[0];
  
  public Constraint (ConstraintRule rule) {
    this.rule = rule;
  }
  
  public Constraint (ConstraintRule rule, float...args) {
    this.rule = rule;
    this.args = args;
  }
  
  public void apply(Element e) {

    switch (rule) {
      case WIDTH: applyWidthConstriant(e); break;
      case HEIGHT: applyHeightConstriant(e); break;
      case RIGHT: applyRightConstraint(e); break;
      case BOTTOM: applyBottomConstraint (e); break;
      case CENTER: applyCenterConstraint (e); break;
      case BOUNDS: applyBoundsConstraint (e); break;
    }
  }
  
  public Constraint copy() {
    return new Constraint(rule, args.clone());
  }

  public void applyWidthConstriant(Element e) {
    //check if we are doing min or max
    float containerWidth = e.getContainerWidth();

    if (args.length == 0) { //no args
      e.width(containerWidth);
    }
    else if (args.length == 1) { //percentage width
      e.width(containerWidth * args[0]);
    }
    else if (args.length == 2) { //percentage width, width offset
      e.width((containerWidth * args[0]) + args[1]);
    }
  }

  public void applyHeightConstriant(Element e) {
    //check if we are doing min or max
    float containerHeight = e.getContainerHeight();

    if (args.length == 0) { //no args
      e.height(containerHeight);
    }
    else if (args.length == 1) { //percentage height
      e.height(containerHeight * args[0]);
    }
    else if (args.length == 2) { //percentage height, height offset
      e.height((containerHeight * args[0]) + args[1]);
    }
  }

  public void applyRightConstraint(Element e) {
    //check if we are doing min or max
    float containerWidth = e.getContainerWidth();

    if (args.length == 0) { //no args
      e.left(containerWidth - e.getWidth());
    }
    else if (args.length == 1) { //offset right
      e.left(containerWidth + (args[0] - e.getWidth()));
    }
  }

  public void applyBottomConstraint(Element e) {
    float containerHeight = e.getContainerHeight();
    if (args.length == 0) { //no args
      e.top(containerHeight - e.getHeight());
    }
    else if (args.length == 1) { //offset right
      e.top(containerHeight + (args[0] - e.getHeight()));
    }
  }

  public void applyCenterConstraint(Element e) {
    float containerWidth = e.getContainerWidth();
    float containerHeight = e.getContainerHeight();

    if (args.length == 0) { //no args, assume center both axies
      e.left ((containerWidth - e.getWidth()) / 2);
      e.top ((containerHeight - e.getHeight()) / 2);
    }
    else if (args.length == 2) { //specify the individual axies
      if (args[0] != 0) e.left ((containerWidth - e.getWidth()) / 2);
      if (args[1] != 0) e.top ((containerHeight - e.getHeight()) / 2);    
    }
  }

  public void applyBoundsConstraint (Element e) {
    float containerWidth = e.getContainerWidth();
    float containerHeight = e.getContainerHeight();

    if (args.length == 0) { //bind within the parent or the screen
    
      //object too big for container
      if (e.size.x > containerWidth || e.size.y > containerHeight) { 
        e.moveTo (0,0);
        return;
      }

      Float2 position = e.position.copy();
      
      if (e.getLeft() < 0) position.x = 0;
      if (e.getLeft() + e.getWidth() > containerWidth) position.x = containerWidth - e.getWidth();
      
      if (e.getTop() < 0) position.y = 0;
      if (e.getTop() + e.getHeight() > containerHeight) position.y = containerHeight - e.getHeight();

      e.moveTo(position);
    }
    else if (args.length == 4) { //bind within the parent or the screen with padding
      float leftPadding = args[0];
      float topPadding = args[1];
      float rightPadding = args[2];
      float bottomPadding = args[3];
            
      Float2 position = e.position.copy();

      //object too big for container
      boolean tooBigForContainerX = e.size.x > containerWidth - (leftPadding + rightPadding);
      boolean tooBigForContainerY = e.size.y > containerHeight - (topPadding + bottomPadding);
      
      if (tooBigForContainerX) position.x = leftPadding;
      else {
        if (e.getLeft() < 0 + leftPadding) position.x = 0 + leftPadding;
        if (e.getLeft() + e.getWidth() > containerWidth - rightPadding) position.x = (containerWidth - rightPadding) - e.getWidth();
      }

      if (tooBigForContainerY) position.y = topPadding;
      else {
        if (e.getTop() < 0 + topPadding) position.y = 0 + topPadding;
        if (e.getTop() + e.getHeight() > containerHeight - bottomPadding) position.y = (containerHeight - bottomPadding) - e.getHeight();
      }

      e.moveTo(position);
    }
  }

  public void drawDebug (PGraphics g, Element e) {
    if (rule == ConstraintRule.BOUNDS) {
      g.stroke (Color.rgb(200, 0, 0));
      g.strokeWeight (1);
      g.noFill();

      if (args.length == 0) {
        g.rect (e.getContainerLeft(), e.getContainerTop(), e.getContainerWidth(), e.getContainerHeight());
      }
      else if (args.length == 4)  {
        g.rect(
          e.parent.getLeft() + args[0],
          e.parent.getTop() + args[1],
          e.parent.getWidth() - (args[0] + args[2]),
          e.parent.getHeight() - (args[1] + args[3])
        );
      }
    }
  }

  public String getName() {
    return rule.toString();
  }

  public String getInfo() {
    return getName();
  }

  //#region Helper methods
  public static Constraint maxWidth () {
    return new Constraint(ConstraintRule.WIDTH);
  }

  public static Constraint percentageWidth (float percent) {
    if (percent < 0) percent = 0;
    if (percent > 1) percent = 1;
    
    return new Constraint(ConstraintRule.WIDTH, percent);
  }

  public static Constraint percentageWidthWithOffset (float percent, float offset) {
    if (percent < 0) percent = 0;
    if (percent > 1) percent = 1;

    return new Constraint(ConstraintRule.WIDTH, percent, offset);  
  }

  public static Constraint maxHeight () {
    return new Constraint(ConstraintRule.HEIGHT);
  }

  public static Constraint percentageHeight (float percent) {
    if (percent < 0) percent = 0;
    if (percent > 1) percent = 1;
    
    return new Constraint(ConstraintRule.HEIGHT, percent);
  }

  public static Constraint percentageHeightWithOffset (float percent, float offset) {
    if (percent < 0) percent = 0;
    if (percent > 1) percent = 1;

    return new Constraint(ConstraintRule.HEIGHT, percent, offset);  
  }

  public static Constraint maxRight ()  {
    return new Constraint(ConstraintRule.RIGHT);
  }

  public static Constraint maxRightWithOffset (float offset)  {
    return new Constraint(ConstraintRule.RIGHT, offset);
  }

  public static Constraint maxBottom ()  {
    return new Constraint(ConstraintRule.BOTTOM);
  }

  public static Constraint maxBottomWithOffset (float offset)  {
    return new Constraint(ConstraintRule.BOTTOM, offset);
  }

  public static Constraint center() {
    return new Constraint(ConstraintRule.CENTER);
  }

  public static Constraint centerHorizontal () {
    return new Constraint(ConstraintRule.CENTER, 1, 0);
  }

  public static Constraint centerVertical () {
    return new Constraint(ConstraintRule.CENTER, 0, 1);
  }

  public static Constraint boundary() {
    return new Constraint(ConstraintRule.BOUNDS);
  }

  public static Constraint boundaryWithPadding(float padding) {
    return new Constraint(ConstraintRule.BOUNDS, padding, padding, padding, padding);
  }
  
  public static Constraint boundaryWithPadding(float horizontalPadding, float verticalPadding) {
    return new Constraint(ConstraintRule.BOUNDS, horizontalPadding, verticalPadding, horizontalPadding, verticalPadding);
  }

  public static Constraint boundaryWithPadding(float leftPadding, float topPadding, float widthPadding, float heightPadding) {
    return new Constraint(ConstraintRule.BOUNDS, leftPadding, topPadding, widthPadding, heightPadding);
  }
}