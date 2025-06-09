package playground.ui;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.function.Consumer;

import playground.Color;
import playground.Float2;
import playground.PlaygroundApp;

import processing.core.PFont;
import processing.core.PGraphics;
import processing.core.PImage;

/**HTML-like multipurpose UI element.*/
public class Element implements UIConstants {

  //mandatory properties
  protected Float2 position = new Float2();
  protected Float2 globalPosition = new Float2();
  protected Float2 size = new Float2();
  protected Float2 dragOffset = new Float2();
  protected float[] padding = new float[]{0, 0, 0, 0};

  //optional properties
  protected String id;
  protected String text;
  protected PImage image;
  protected Layout layout;
  protected Element parent;

  //display states
  protected boolean hovered;
  protected boolean pressed;
  protected boolean dragged;
  protected boolean disabled = false;
  protected boolean visible = true;

  //behaviour control
  protected boolean drawBounds;
  protected boolean interactable = false;
  protected boolean draggable = false;
  protected boolean ignoreLayout = false;
  protected boolean customDrawingEnabled;

  //collections
  protected ArrayList<Element> children = new ArrayList<Element>();
  protected ArrayList<Consumer<Integer>> clickActions = new ArrayList<Consumer<Integer>>();
  protected ArrayList<Consumer<Element>> dragActions = new ArrayList<Consumer<Element>>();
  protected Hashtable<ConstraintRule, Constraint> constraints = new Hashtable<ConstraintRule, Constraint>();
  protected ArrayDeque<Constraint> instantConstraints = new ArrayDeque<>();
  
  //styles
  protected ElementStyle style = new ElementStyle();
  protected ElementStyle disabledStyle = new ElementStyle().inheritFrom(style);
  protected ElementStyle hoveredStyle = new ElementStyle().inheritFrom(style);
  protected ElementStyle pressedStyle = new ElementStyle().inheritFrom(hoveredStyle);
  
  //#region Constructors
  public Element () {
    ElementStack.AddElement(this);
    onModifySize();
  }

  public Element (String id) {
    this.id = id;
    ElementStack.AddElement(this);
    onModifySize();
  }

  public Element (float x, float y) {
    this (new Float2(x,y));
  }

  public Element (String id, float x, float y) {
    this (id, new Float2(x, y));
  }
    
  public Element (Float2 size) {
    ElementStack.AddElement(this);
    this.size = size;
    onModifySize();
  }

  public Element (String id, Float2 size) {
    this.id = id;
    this.size = size;
    onModifySize();
    ElementStack.AddElement (this);
  }

  //#region Getters
  /**Returns the global x position. */
  public float getGlobalLeft() {
    return parent == null ? position.x : parent.getGlobalLeft() + position.x;
  }
  
  /**Returns the global y position. */
  public float getGlobalTop() {
    return parent == null ? position.y : parent.getGlobalTop() + position.y;
  }

  /**Returns the local y position.*/
  public float getTop() {
    return position.y;
  }

  /**Returns the local x position. */
  public float getLeft() {
    return position.x;
  }

  public float getWidth() {
    return size.x;
  }

  public float getHeight () {
    return size.y;
  }

  /**Returns the left of the parent or 0 if null. */
  public float getContainerLeft() {
    return parent == null ? 0 : parent.getGlobalLeft();
  }

  /**Returns the top of th parent or 0 if null. */
  public float getContainerTop() {
    return parent == null ? 0: parent.getGlobalTop();
  }

  /**Returns the width of the parent or the screen. */
  public float getContainerWidth() {
    return parent == null ? PlaygroundApp.getInstance().width : parent.getWidth();
  }

  /**Returns the width of the parent or the screen. */
  public float getContainerHeight() {
    return parent == null ? PlaygroundApp.getInstance().height : parent.getHeight();
  }

  /**Returns the id of the Element. Returns null if there is no Id assigned. */
  public String getId() {return id;}

  /**Returns the name of the element object or the id if it has one. */
  public String getName() {
    if (id != null && !id.isBlank()) return id;
    else return toString();
  }

  public int getDrawOrder() {
    if (parent == null) return ElementStack.getStackIndex(this);
    else return parent.getChildIndex(this);
  }

  public Element getParent() {return parent;}
  
  public int getChildCount() {return children.size();}

  public Element getChild(int index) {
    if (index < 0 || index >= children.size()) return null;
    else return children.get(index);
  }

  /**Returns the index of the element in the children array. */
  public int getChildIndex(Element child) {
    if (children.contains(child)) {
      return children.indexOf(child);
    }
    else return -1;
  }

  public ArrayList<Element> getChildren() {
    return children;
  }

  public ElementStyle getStyle() {
    return style;
  }

  public ElementStyle getHoveredStyle() {
    return hoveredStyle;
  }

  public ElementStyle getPresssedStyle() {
    return pressedStyle;
  }

  public ElementStyle getDisabledStyle () {
    return disabledStyle;
  }

  /**Returns all the styles in a single array. They are ordered: Default, Hovered, Pressed, Disabled */
  public ElementStyle[] getStyles () {
    return new ElementStyle[] {style, hoveredStyle, pressedStyle, disabledStyle};
  }

  public Element element () {
    return this;
  }

  //#region State Queries
  public boolean isHovered() {return hovered;}
  public boolean isPressed() {return pressed;}
  public boolean isDisabled () {return disabled;}
  public boolean isDragged() {return dragged;}
  public boolean isInteractable() {return interactable;}
  public boolean isVisible () {return visible;}
  public boolean isDraggable()  { return draggable;}
  public boolean ignoresLayout() {return ignoreLayout;}
  public boolean hasParent() {return parent != null;}
  public boolean hasPadding() {return padding[0] != 0 || padding[1] != 0 || padding[2] != 0 ||padding[3] != 0;}

  //#region Generic Queries
  public boolean isSibling(Element other) {return parent == other.parent;}
  
  /**Returns true if the supplid point is within the UI element's bounds. */
  public Element isUnderPointer (float x, float y) {
    return isUnderPointer(new Float2(x, y));
  }

  /**Returns true if the supplid point is within the UI element's bounds. */
  public Element isUnderPointer (Float2 point) {
    if (
      point.x > globalPosition.x && 
      point.x < globalPosition.x + size.x && 
      point.y > globalPosition.y && 
      point.y < globalPosition.y + size.y) {
        //currently under pointer
        //iterate reverse to check the children
        //on first find return the element
        for (int i  = children.size() - 1; i >= 0; i --) {
          
          Element child = children.get(i);
          if (child.isDisabled() || !child.isVisible()) continue;
          
          var hovered = child.isUnderPointer(point);
          if (hovered != null) return hovered;
        }
        
        if (this.isInteractable()) return this;
      }
      
      return null;
  }

  //#region Setters
  public ElementStyle setStyle(ElementStyle style) {
    return this.style = style;
  }

  public ElementStyle setHoveredStyle(ElementStyle hoveredStyle) {
    return this.hoveredStyle = hoveredStyle;
  }

  public ElementStyle setPressedStyle(ElementStyle pressedStyle) {
    return this.pressedStyle = pressedStyle;
  }

  public ElementStyle setDisabledStyle(ElementStyle style) {
    return this.disabledStyle = style;
  }

  //#region Chained Setters
  public Element id (String id) {
    ElementStack.RemoveElement(this);
    this.id = id;
    ElementStack.AddElement(this);
    return this;
  }

  /**Sets the local x position. */
  public Element left(float x) {
    this.position.x = x;
    return this;
  }
  
  /**Sets the local y position. */
  public Element top(float y) {
    this.position.y = y;
    return this;
  }

   public Element size (float x, float y) {
    return size(new Float2(x, y));
  }

  public Element size (Float2 size) {
    this.size = size;
    onModifySize();
    return this;
  }
  
  public Element width (float width) {
    this.size.x = width;
    onModifySize();
    return this;
  }
  
  public Element height (float height) {
    this.size.y = height;
    onModifySize();
    return this;
  }

  public Element noImage() {
    image = null;
    return this;
  }

  public Element image(PImage image) {
    this.image = image;
    return this;
  }

  public Element noPadding() {
    padding = new float[] {0, 0, 0, 0};
    return this;
  }

  public Element padding(float value) {
    padding = new float[] {value, value, value, value};
    return this;
  }

  public Element padding (float horizontal, float vertical) {
    padding = new float[] {horizontal, vertical, horizontal, vertical};
    return this;
  }

  public Element padding (float top, float left, float right, float bottom) {
    padding = new float[] {top, left, right, bottom};
    return this;
  }

  public Element text (String text) {
    this.text = text;
    return this;
  }
  
  public Element noText () {
    this.text = null;
    return this;
  }

  public Element parent (Element newParent) {
    return parent (newParent, true);
  }
  
  public Element parent (Element newParent, boolean globalPositionStays) {
    if (parent == newParent) return this;
    else {
      //if there is a change, we will need to recalc this objects entire hierarchy
      if (parent!= null) parent.children.remove (this);

      if (newParent == null) { //null parent
        if (globalPositionStays) {
          position.x += parent.position.x;
          position.y += parent.position.y;
        }

        //when nulling parents add them back to the stack
        // ElementStack.AddElement(this);
      }
      else {
        if (parent != null) { //swap parent
          if (globalPositionStays) {
            position.x -= parent.position.x + newParent.position.x;
            position.y -= parent.position.y + newParent.position.y;
          }
        }
        else { //new parent
          if (globalPositionStays) {
            position.x -= newParent.position.x;
            position.y -= newParent.position.y;
          }
          // ElementStack.RemoveElement(this);
        }
        newParent.children.add (this);        
      }
      parent = newParent;
    }
    return this;
  }

  //#region Chained State Setters
  public Element drawBounds(boolean state) {
    drawBounds = state;
    return this;
  }

  public Element ignoreLayout(boolean state) {
    ignoreLayout = state;
    return this;
  }

  public Element isVisible (boolean state) {
    visible  = state;
    return this;
  }

  public Element isPressed (boolean state) {
    pressed = state;
    return this;
  }
  
  public Element isHovered (boolean state) {
    hovered = state;
    return this;
  }

  public Element isDisabled (boolean state) {
    disabled = state;
    return this;
  }

  public Element isInteractable (boolean state) {
    interactable = state;
    return this;
  }

  public Element isDraggable (boolean state) {
    draggable = state;
    return this;
  }

  public Element enableCustomDrawing (boolean state) {
    customDrawingEnabled = state;
    return this;
  }

  //#region Chained Methods 
  public Element moveTo (float x, float y) {
    return moveTo(new Float2(x,y));
  }
  
  public Element moveTo(Float2 position) {
    this.position.set (position);
    return this;
  }
  
  public Element move (float x ,float y) {
    return move (new Float2(x, y));
  }

  public Element move (Float2 offset) {
    this.position.add(offset);
    return this;
  }
  
  public Element expand (float x, float y) {
    return expand (new Float2(x, y));
  }

  public Element expand (Float2 offset) {
    this.size.add (offset);
    return this;
  }
  
  public Element expandWidth (float offset) {
    this.size.x += offset;
    onModifySize();
    return this;
  }
  
  Element expandHeight (float offset) {
    this.size.y += offset;
    onModifySize();
    return this;
  }

  public Element style(Consumer<ElementStyle> fn) {
  fn.accept(style);
  return this;
  }

  public Element hoveredStyle(Consumer<ElementStyle> fn) {
    fn.accept(hoveredStyle);
    return this;
  }

  public Element pressedStyle(Consumer<ElementStyle> fn) {
    fn.accept(pressedStyle);
    return this;
  }

  public Element disabledStyle(Consumer<ElementStyle> fn) {
    fn.accept(disabledStyle);
    return this;
  }

  /**Useful function for calling modifications on the element chained. */
  public Element modify (Consumer<Element> fn) {
    fn.accept(this);
    return this;
  }
  
  public Element onClick(Consumer<Integer> action) {
    clickActions.add(action);
    return this;
  }

  public Element onDrag(Consumer<Element> action) {
    dragActions.add(action);
    return this;
  }
  
  public Element addChild(Element child) {
    if (child == this || this.children.contains (child)) return this;
    child.parent (this);
    return this;
  }

  public Element addChild(Element child, boolean globalPositionStays) {
    if (child == this || this.children.contains (child)) return this;
    child.parent (this, globalPositionStays);
    return this;
  }
  
  
  public Element addChildren (Element... newChildren) {
    for (Element child : newChildren) {
      if (child == this || this.children.contains (child)) continue;
      child.parent (this);
    }
    return this;
  }

  public Element addChildren (boolean globalPositionStays, Element... newChildren) {
    for (Element child : newChildren) {
      if (child == this || this.children.contains (child)) continue;
      child.parent (this, globalPositionStays);
    }
    return this;
  }
  
  /**Applies a persistent constraint on the element. */
  public Element applyConstraint (Constraint constraint) {
    constraints.put (constraint.rule, constraint);
    return this;
  }

  /**Applies a persistent constraint on the element. */
  public Element applyConstraint (Constraint... newConstraints) {
    for (Constraint constraint : newConstraints){
     this.constraints.put (constraint.rule, constraint);
    }
    return this;
  }

  /*Returns an array of element copies. Can optionally include the original */
  public Element[] duplicate (int copies, boolean includeOriginal) {
    return duplicate(copies, includeOriginal, false);
  }

  /*Returns an array of element copies. Can optionally include the original */
  public Element[] duplicate (int copies, boolean includeOriginal, boolean copyChildren) {
    Element[] copyArray;
    if (includeOriginal) {
      copyArray = new Element[copies + 1];
      copyArray[0] = this;
      for(int i = 1; i < copies + 1; i ++) {
        copyArray[i] = copy(copyChildren);
      }
    }
    else {
      copyArray = new Element[copies];
      for(int i = 0; i < copies; i ++) {
        copyArray[i] = copy(copyChildren);
      }
    }
    return copyArray;
  }

  public Element copy() {
    return copy (false);
  }

  public Element copy (String id) {
    return copy(false).id(id);
  }

  public Element copy (String id, boolean copyChildren) {
    return copy(copyChildren).id(id);
  }
    
  public Element copy (boolean copyChildren) {
    Element e = new Element (size.copy());
    
    e.position = position.copy();
    e.globalPosition = position.copy();
    e.interactable = interactable;
    e.disabled = disabled;
    e.visible = visible;
    e.draggable = draggable;
    e.text = text;
    e.image = image;
    e.ignoreLayout = ignoreLayout;
    e.customDrawingEnabled = customDrawingEnabled;
    e.padding = padding.clone();

    if (parent != null) e.parent (parent, false);
    if (layout != null) e.layout = layout.copy();

    e.style = style.copy();
    e.disabledStyle = disabledStyle.copy().inheritFrom(e.style);
    e.hoveredStyle = hoveredStyle.copy().inheritFrom(e.style);
    e.pressedStyle = pressedStyle.copy().inheritFrom(e.hoveredStyle);  
    
    for (Constraint c : constraints.values()) {
      e.applyConstraint (c.copy());
    }

    //to do this properly, clone the original array (shallow) and then modify the original 
    if (copyChildren) {
      for (Element child : new ArrayList<>(children)) child.copy(copyChildren).parent (e);
    }

    return e;
  }

  //#region Generic Methods
  public void removeConstraint (ConstraintRule rule) {
    constraints.remove (rule);
  }

  public void removeConstraints (ConstraintRule... rules) {
    for (ConstraintRule rule : rules) constraints.remove (rule);
  }

   public void removeAllConstraints () {
    constraints.clear();
  }
  
  public void applyLayout (Layout layout) {
    this.layout = layout;
  }

  public void clearAllStyles() {
    style.clearAllAttributes();
    hoveredStyle.clearAllAttributes();
    pressedStyle.clearAllAttributes();
    disabledStyle.clearAllAttributes();
  }

  /**Use when you have manually changed the styles to allow them to inherit the deualt way.
   * <p>Pressed -> Hovered -> Default</p>
   * <p>Disabled -> Default</p>
   */
  public void updateStyleInheritanceTree() {
    disabledStyle.inheritFrom(style);
    hoveredStyle.inheritFrom(style);
    pressedStyle.inheritFrom(hoveredStyle);
  }

  /**Applies a one-time constraint on the element cleared at the end of the frame. */
  public void constrainOnce(Constraint constraint) {
    instantConstraints.add(constraint);
  }

  public void click(int button) {
    for (Consumer<Integer> action : clickActions) action.accept(button);
  }

  public void beginDrag (float x, float y) {
    beginDrag(new Float2(x, y));
  }

  public void beginDrag(Float2 mouse) {
    dragged = true;
    dragOffset.set(Float2.sub(position, mouse));
  }

  public void drag (float x, float y) {
    drag(new Float2(x, y));
  }

  public void drag (Float2 dragPosition) {
    Float2 finalPosition = Float2.add (dragPosition, dragOffset);
    moveTo(finalPosition);
  }

  public void endDrag() {
    dragged = false;
  }

  /**Draws this element first in the stack. 
   * If it is parented, it will draw as the first of the children. */
  public void drawFirst() {
    if (parent == null) {
      ElementStack.orderFirst(this);
    }
    else {
      parent.children.remove(this);
      parent.children.addFirst(this);
    }
  }

  /**Draws this element last in the stack. 
   * If it is parented, it will draw as the last of the children. */
  public void drawLast() {
    if (parent == null) {
      ElementStack.orderLast(this);
    }
    else {
      parent.children.remove(this);
      parent.children.addLast(this);
    }
  }
  public void drawBefore(Element other) {
    //check to see if they are both siblings, otherwie ignore
    if (!isSibling(other)) return;

    if (parent == null) ElementStack.orderBefore(this, other);
    else {
      int drawOrder = parent.getChildIndex(other) - 1;
      if (drawOrder <= 0) drawOrder = 0;
      
      parent.children.remove(this);
      parent.children.add(drawOrder, this);
    }
  }

  public void drawAfter(Element other) {
    //check to see if they are both siblings, otherwie ignore
    if (!isSibling(other)) return;

    if (parent == null) ElementStack.orderAfter(this, other);
    else {
      int drawOrder = parent.getChildIndex(other) + 1;
      if (drawOrder >= parent.children.size()) drawOrder = parent.children.size() - 1;
      
      parent.children.remove(this);
      parent.children.add(drawOrder, this);
    }
  }
  
  //#region Draw Methods
  public void draw (PGraphics g) {
    draw (g, false);
  }
    
  public void draw(PGraphics g, boolean showDebug) {
    if (visible) {
      
      //apply all the constraints
      while (!instantConstraints.isEmpty()) {
        instantConstraints.poll().apply(this);
      }

      for (Constraint constraint : constraints.values())  {
        constraint.apply (this);
        if ((hovered || dragged) && (drawBounds || showDebug)) constraint.drawDebug (g, this);
      }

      if (dragged) for (Consumer<Element> action : dragActions) action.accept(this);

      //calculate the draw x and y
      globalPosition.set (getGlobalLeft(), getGlobalTop());
      
      ElementStyle currentStyle;
      
      if (disabled) currentStyle = disabledStyle;
      else if (pressed) currentStyle = pressedStyle;
      else if (hovered) currentStyle = hoveredStyle;
      else currentStyle = style;
      
      if (customDrawingEnabled) {
        drawCustom (g, currentStyle);
      }
      else {
        drawBackground(g, currentStyle);
        if (image != null) drawImage(g, currentStyle);
        if (text != null) drawText(g, currentStyle);   
      }
              
      if (drawBounds || showDebug) drawBounds(g);
      
      if (layout != null) layout.apply(this);
      for (Element child : children) child.draw(g, showDebug);

      if (hovered && (drawBounds || showDebug)) drawBounds(g);
    }
  }

  void drawBounds(PGraphics g) {
    g.stroke (hovered ? 255 : g.color (0,0, 255));
    g.strokeWeight (1);
    g.noFill();
    g.rect (globalPosition.x, globalPosition.y, size.x, size.y);

    if (hasPadding()) {
      g.stroke (Color.rgb(200, 200, 0));
      g.rect (globalPosition.x + padding[0], globalPosition.y + padding[1], size.x - (padding[0] + padding[2]), size.y - (padding[1] + padding[3]));
    }
  }
  
  void drawBackground(PGraphics g, ElementStyle currentStyle) {
    int fill = currentStyle.getBackgroundColor();
    float weight = currentStyle.getBorderWidth();
    if (Color.isFullyTransparent (fill) && weight <= 0) return;

    //draw the base shape if needed
    float[] cornerRadii = currentStyle.getCornerRadii();

    if (weight > 0){
      g.stroke(currentStyle.getBorderColor());
      g.strokeWeight (weight);
      g.noFill();
    }
    else g.noStroke();

    if (!Color.isFullyTransparent (fill)) g.fill(currentStyle.getBackgroundColor());
    else g.noFill();
      
    drawBackgroundRect(g, globalPosition.x, globalPosition.y, size.x, size.y, cornerRadii);
  }

  void drawBackgroundRect (PGraphics g, float x, float y, float w, float h, float[] corners) {
    switch (corners.length) {
      case 0: g.rect (x, y, w, h); break;
      case 1: g.rect (x, y, w, h, corners[0]); break;
      case 4: g.rect (x, y, w, h, corners[0], corners[1], corners[2], corners[3]); break;
    }
  }

  void drawText (PGraphics g, ElementStyle currentStyle) {
    PFont font = currentStyle.getTextFont();
    float fontSize = currentStyle.getTextSize();

    if (fontSize > 0) {
      if (font != null) g.textFont (font, currentStyle.getTextSize());
      else g.textSize (currentStyle.getTextSize());
      g.textAlign (currentStyle.getTextAlignH(), currentStyle.getTextAlignV());
      g.fill(currentStyle.getTextColor());
      g.text (text, globalPosition.x + padding[0], globalPosition.y + padding[1], size.x - (padding[0] + padding[2]), size.y - (padding[1] + padding[3]));
    }
  }
  
  void drawImage(PGraphics g, ElementStyle currentStyle) {
    g.tint (currentStyle.getTintColor());
    g.image (image, globalPosition.x + padding[0], globalPosition.y + padding[1], size.x - (padding[0] + padding[2]), size.y - (padding[1] + padding[3]));

  }

  protected void drawCustom(PGraphics g, ElementStyle currentStyle) {}

  //#region ON methods
  void onModifySize() {
    if (size.x < 0) size.x = 0;
    if (size.y < 0) size.y = 0;
  }

  //#region Info Methods
  public String getInfo() {
    return 
        "id: " + (id != null && !id.isBlank() ? id : "none") + "\n"
      + "postion: " + position.toString() + "\n"
      + "size: " + size.toString() + "\n"
      + "parent: " + (parent == null ? "none" : parent.getName()) + "\n"
      + "children: " + children.size() + "\n"
      + "constraints: " + getCosntraintInfo()
      + (layout == null ? "" : "layout: " + layout.getName())
    ;
  }

  public String getCosntraintInfo () {
    if (constraints.size() == 0) return "none";

    String info = "";
    for (Constraint c : constraints.values()) {
      info += c.getInfo()  + " ";
    }
    info += "\n";

    return info;
  }
}