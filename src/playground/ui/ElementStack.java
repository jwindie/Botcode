package playground.ui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;

import playground.Float2;
import playground.Int2;
import processing.core.PConstants;
import processing.core.PGraphics;

public final class ElementStack implements PConstants {
  static Hashtable<String, Element> idElements = new Hashtable<>();
  static HashSet<Element> elementHashSet = new HashSet<>();
  static ArrayList<Element> elementArrayList = new ArrayList<>();

  static PGraphics g;
  static boolean showDebug = false;
  static boolean showElementInspector = false;
  static Int2 lastScreenSize = new Int2();
  static Float2 mousePosition = new Float2();
  static Element hoveredElement, pressedElement, draggedElement;
  static int inspectorTextColor, inspectorTextSize;

  public static void setGraphics(PGraphics g) {
    ElementStack.g = g;
  }

  public static void setInspectorTextColor (int color) {
    inspectorTextColor = color;
  }

  public static void setInspectorTextSize (int size) {
    inspectorTextSize = size;
  }

  public static void showDebug(boolean state) {
    showDebug = state;
  }

  public static void showElementInspector(boolean state) {
    showElementInspector = state;
  }

  public static Element getElementById(String id) {
    return idElements.get(id);
  }

  public static int size() {
    return elementHashSet.size();
  }

  public static Element get (int index) {
    return elementArrayList.get (index);
  }

  public static void AddElement(Element e) {
    if (!elementHashSet.contains(e)) {
      elementHashSet.add(e);
      elementArrayList.add (e);
      if (e.getId() != null && !e.getId().isBlank()) idElements.put(e.getId(), e);
    }
  }

  public static void RemoveElement(Element e) {
    if (elementHashSet.contains(e)) {
      elementHashSet.remove(e);
      elementArrayList.remove(e);
      if (e.getId() != null && !e.getId().isBlank()) idElements.remove(e.getId());
    }
  }

  public static ArrayList<Element> getElements() {
    return elementArrayList;
  }

  /**Returns the index of the element in the stack. */
  public static int getStackIndex(Element e) {
    if (elementHashSet.contains(e)) {
      return elementArrayList.indexOf(e);
    }
    else return -1;
  }

  /**Orders the element first here as long as it doesnt have a parent */
  public static void orderFirst(Element e) {
    //only operate on the element if it is in the stack
    if (elementHashSet.contains(e)) {
      elementArrayList.remove (e);
      elementArrayList.addFirst(e);
    }
  }

  /**Orders the element last here as long as it doesnt have a parent */
  public static void orderLast(Element e) {
    //only operate on the element if it is in the stack
    if (elementHashSet.contains(e)) {
      elementArrayList.remove (e);
      elementArrayList.addLast(e);
    }
  }

  public static void orderBefore (Element e, Element other) {
    //check that both are in the stack
    if (elementHashSet.contains(e) && elementHashSet.contains(other)) {

      int drawOrder = getStackIndex(other) - 1;
      if (drawOrder <= 0) drawOrder = 0;
      
      elementArrayList.remove(e);
      elementArrayList.add(drawOrder, e);
    }
  }

  public static void orderAfter (Element e, Element other) {
    //check that both are in the stack
    if (elementHashSet.contains(e) && elementHashSet.contains(other)) {

      int drawOrder = getStackIndex(other) + 1;
      if (drawOrder >= elementArrayList.size()) drawOrder = elementArrayList.size() - 1;
      
      elementArrayList.remove(e);
      elementArrayList.add(drawOrder, e);
    }
  }

  public static void update (Float2 mouse) {
    if (draggedElement == null) handle();
    mousePosition.set (mouse);
  }

  public static void draw() {
    //get the current rect and ellipseModes
    var oldRect = g.rectMode;
    var oldEllipse = g.ellipseMode;

    //swap them for this drawing contenxt
    g.rectMode (CORNER);
    g.ellipseMode (CORNER);
    
    for (Element e : ElementStack.getElements()) {
      if (e.getParent() == null) e.draw(g, showDebug);
    }

    if (showElementInspector) {
      g.fill(0, 100);
      g.noStroke();
      g.textAlign(LEFT, TOP);
      g.textSize (inspectorTextSize);
      g.fill (inspectorTextColor);
      g.text (
        "hoveredElement : " + (hoveredElement == null ? "none" : hoveredElement.getName()) + "\n" +
        "pressedElement : " + (pressedElement == null ? "none" : pressedElement.getName()) + "\n" + 
        "draggedElement : " + (draggedElement == null ? "none" : draggedElement.getName()) + "\n" + 
        (hoveredElement == null ? "" : hoveredElement.getInfo()),
        20, 20
      );
    }

    //restore the shape modes
    g.rectMode (oldRect);
    g.ellipseMode (oldEllipse);
  }

  public static void mouseReleased() {
    // lastMousePosition.x = lastMousePosition.y = -1;

    if (pressedElement != null) {
      pressedElement.isPressed(false);
      pressedElement = null;
    }

    if (draggedElement != null) {
      draggedElement.endDrag();
      draggedElement = null;
    }
  }

  public static void mousePressed(int mouseButton) {
    if (hoveredElement != null) {
      pressedElement = hoveredElement;
      pressedElement.isPressed(true);

      //which button was pressed
      int button = -1;
      if (mouseButton == LEFT) button = 0;
      else if (mouseButton == RIGHT) button = 1;
      else if (mouseButton == CENTER) button = 2;

      pressedElement.click(button);
    }
  }

  public static void mouseDragged(Float2 mousePosition) {
    //do we have a pressed element?
    if (draggedElement == null) {
      boolean check =  
        pressedElement != null && 
        pressedElement.isDraggable();
      
      if (check){
        draggedElement = pressedElement;
        draggedElement.beginDrag(mousePosition);
      }
    }

    if (draggedElement!= null) {
      //drag it
      draggedElement.drag(mousePosition);
    }
  }

  private static void handle() {
    Element lastHoveredElement = null;

    if (hoveredElement != null) lastHoveredElement = hoveredElement;
    hoveredElement = null;

    // for (Element e : ElementStack.getElements()) {
    //go through element stack in reverse draw order
    for (int i = ElementStack.size() - 1; i >= 0; i --) {
      Element e = ElementStack.get(i);

      if (e.isDisabled() || !e.isInteractable()) continue;
      if (e.isUnderPointer (mousePosition)) {
        (hoveredElement = e).isHovered(true);
        break;
      }
    }

    //is the new hovered element the same as the last one?
    if (hoveredElement != lastHoveredElement) {
      if (lastHoveredElement != null)  {
        lastHoveredElement.isHovered(false);
      }
      if (hoveredElement != null) hoveredElement.isHovered(true);
    }

    //if the hovered and pressed arent the same unpress unless dragging
    if (pressedElement != null && pressedElement.isHovered() == false && draggedElement == null) {
      pressedElement.isPressed(false);
      pressedElement = null;
    }
  }
}