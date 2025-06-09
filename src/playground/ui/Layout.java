package playground.ui;

import playground.Float2;

public class Layout implements UIConstants {
  
  public LayoutRule rule;  
  public float[] args = new float[0];
  
  public Layout (LayoutRule rule) {
    this.rule = rule;
  }
  
  public Layout (LayoutRule rule, float...args) {
    this.rule = rule;
    this.args = args;
  }
  
  public void apply(Element e) {

    switch (rule) {
      case HORIZONTAL: applyHorizontalLayout(e); break;
      case VERTICAL: applyVerticalLayout(e); break;
    }
  }
  
  public Layout copy() {
    return new Layout(rule, args.clone());
  }

  public void applyHorizontalLayout (Element element) {
    int direction = (int) args[0];
    int verticalAlignment = (int) args[1];
    float start = args[2];
    float spacing = args[3];
    //are we aligning to the left or right?
    if (direction == LEFT) {
      float x = start;

      for (Element child : element.getChildren()) {
        if (child.ignoresLayout()) continue;

        float y = 0;
        if (verticalAlignment == TOP) y = 0;
        if (verticalAlignment == CENTER) y = (element.getHeight()/2) - (child.getHeight()/2);
        if (verticalAlignment == BOTTOM) y = element.getHeight() - child.getHeight();

        child.moveTo(new Float2 (x, y));
        x += child.getWidth() + spacing;
      }
    }
    else if (direction == RIGHT) {
      float x = element.getWidth() - start;

      //go through the children in reverse order
      for (int i = element.getChildCount()-1; i >= 0; i --) {
        Element child = element.getChild(i);
        if (child.ignoresLayout()) continue;
        
        //check vertical align
        float y = 0;
        if (verticalAlignment == TOP) y = 0;
        if (verticalAlignment == CENTER) y = (element.getHeight()/2) - (child.getHeight()/2);
        if (verticalAlignment == BOTTOM) y = element.getHeight() - child.getHeight();

        //place the element
        child.moveTo(new Float2 (x - child.getWidth(), y));
        x -= (child.getWidth() + spacing);
      }
    }
  }

  public void applyVerticalLayout (Element element) {
    int direction = (int) args[0];
    int horizontalAlignment = (int) args[1];
    float start = args[2];
    float spacing = args[3];
    //are we aligning to the left or right?
    if (direction == DOWN) {
      float y = start;

      for (Element child : element.getChildren()) {
        if (child.ignoresLayout()) continue;
        float x = 0;
        if (horizontalAlignment == LEFT) x = 0;
        if (horizontalAlignment == CENTER) x = (element.getWidth()/2) - (child.getWidth()/2);
        if (horizontalAlignment == RIGHT) x = element.getWidth() - child.getWidth();

        child.moveTo(new Float2 (x, y));
        y += child.getHeight() + spacing;
      }
    }
    else if (direction == UP) {
      float y = element.getHeight() - start;

      //go through the children in reverse order
      for (int i = element.getChildCount()-1; i >= 0; i --) {
        Element child = element.getChild(i);
        if (child.ignoresLayout()) continue;
        
        //check vertical align
        float x = 0;
        if (horizontalAlignment == LEFT) x = 0;
        if (horizontalAlignment == CENTER) x = (element.getWidth()/2) - (child.getWidth()/2);
        if (horizontalAlignment == RIGHT) x = element.getWidth() - child.getWidth();

        //place the element
        child.moveTo(new Float2 (x, y - child.getHeight()));
        y -= (child.getHeight() + spacing);
      }
    }
  }

  public String getName() {
    switch (rule) {
      case HORIZONTAL: return "HORIZONTAL";
      case VERTICAL: return "VERTICAL";
      default: return "";
    }
  }

  //#region Factory Methods
  public static Layout horizontalLeft (int verticalAlignment) {
    return new Layout(LayoutRule.HORIZONTAL, LEFT, verticalAlignment, 0, 0);
  }

  public static Layout horizontalLeftWithSpacing (int verticalAlignment, float spacing) {
    return new Layout(LayoutRule.HORIZONTAL, LEFT, verticalAlignment, 0, spacing);
  }

  public static Layout horizontalLeftWithOffsetAndSpacing (int verticalAlignment, float offset, float spacing) {
    return new Layout(LayoutRule.HORIZONTAL, LEFT, verticalAlignment, offset, spacing);
  }

  public static Layout horizontalRight (int verticalAlignment) {
    return new Layout(LayoutRule.HORIZONTAL, RIGHT, verticalAlignment, 0, 0);
  }

  public static Layout horizontalRightWithSpacing (int verticalAlignment, float spacing) {
    return new Layout(LayoutRule.HORIZONTAL, RIGHT, verticalAlignment, 0, spacing);
  }

  public static Layout horizontalRightWithOffsetAndSpacing (int verticalAlignment, float offset, float spacing) {
    return new Layout(LayoutRule.HORIZONTAL, RIGHT, verticalAlignment, offset, spacing);
  }
  
  public static Layout verticalDown (int horizontalAlignment) {
    return new Layout(LayoutRule.VERTICAL, DOWN, horizontalAlignment, 0, 0);
  }

  public static Layout verticalDownWithSpacing (int horizontalAlignment, float spacing) {
    return new Layout(LayoutRule.VERTICAL, DOWN, horizontalAlignment, 0, spacing);
  }

  public static Layout verticalDownWithOffsetAndSpacing (int horizontalAlignment, float offset, float spacing) {
    return new Layout(LayoutRule.VERTICAL, DOWN, horizontalAlignment, offset, spacing);
  }

  public static Layout verticalUp (int horizontalAlignment) {
    return new Layout(LayoutRule.VERTICAL, UP, horizontalAlignment, 0, 0);
  }

  public static Layout verticalUpWithSpacing (int horizontalAlignment, float spacing) {
    return new Layout(LayoutRule.VERTICAL, UP, horizontalAlignment, 0, spacing);
  }

  public static Layout verticalUpWithOffsetAndSpacing (int horizontalAlignment, float offset, float spacing) {
    return new Layout(LayoutRule.VERTICAL, UP, horizontalAlignment, offset, spacing);
  }
}