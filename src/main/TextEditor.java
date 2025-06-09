package main;

import java.util.ArrayList;

import playground.Float2;
import playground.ui.Constraint;
import playground.ui.Element;
import playground.ui.ElementStack;


public class TextEditor extends Window {

  public enum EnumeratedKeyCode {
    UNKNOWN,
    BACKSPACE,
    RETURN,
    UP,
    DOWN,
    TAB,
    LEFT,
    RIGHT,
    CTRL
  }

  final static String regexKeyMatch = "[A-Za-z0-9 !\"#$%&'()*+,\\-./:;<=>?@\\[\\\\\\]^_`{|}~]";
  final static float CURSOR_HEIGHT = .75f;

  App app;
  int lineIndex;
  int columnIndex;
  int fontSize = 28;
  int screenPadding = 10;
  boolean running;
  boolean isMacOs;
  float lineNumShift;
  ArrayList<StringBuilder> lines = new ArrayList<>();
  StringBuilder stringBuilder = new StringBuilder();
  int keysDown;
  String builtString = "";
  float time;
  private int defaultWidth;
  float scrollBarWidth = 10;
  private Float2 textDimensions = new Float2();
  private Float2 viewArea = new Float2();

  //key holding
  private boolean keyIsHeld;
  private boolean repeatKey;
  private float keyHeldTime = 0;
  private float keyHeldDelay = .5f;
  private float keyHeldInterval = .08f;
  private char lastKey;
  private int lastKeyCode;
  private boolean lastKeyWasCoded;

  public TextEditor (int width, int height, App app) {
    this.height = height;
    this.app = app;
    this.g = app.g;

    //default program
    lines.add (new StringBuilder("#Type your program here..."));
    lines.add (new StringBuilder(""));

    rebuildOutputString();   
    lineIndex = lines.size() - 1;
    
    g.textSize(fontSize);
    g.textAlign(LEFT, TOP);
    g.rectMode (CORNER);

    //check if its mac
    isMacOs = (System.getProperty("os.name")).toUpperCase().contains("MAC");

    //create a scroll bar
    float horizontalScrollBallHeight = 10;
    Element scrollAreaX = new Element("scroll-area-vertical", scrollBarWidth, 0);
    Element scrollhandleX = new Element("scroll-handle-vertical", scrollBarWidth,10)
      .isInteractable(true)
      .isDraggable(true)
      .parent(scrollAreaX)
      .applyConstraint(Constraint.boundary())
      .style(s -> s
        .backgroundColor(g.color(199, 191, 187))
      );

    setWidth(defaultWidth = width);
  }

  public float isOverSized() {
    return width - defaultWidth;
  }

  public float getDefaultWidth() {
    return defaultWidth;
  }

  public String getText()  {
    rebuildOutputString();
    return builtString;
  }

  public void draw() {
    //stupid AF and needs to be done on draw
    g.clip(x, y, width, height);
    lineNumShift = 36 + screenPadding;

    //background
    g.rectMode (CORNER);
    g.fill (g.color (247, 237, 233));
    g.noStroke();
    g.rect (x, y, width, height);


    g.fill(70);
    g.textSize(fontSize);
    g.textAlign(LEFT, TOP);
    g.text(builtString, x + screenPadding + lineNumShift, y + screenPadding);

    //line numbers area
    g.fill(0, 40);
    g.rect (x, y, lineNumShift, height); 

    //current line highlight
    g.fill (142, 84, 72, 50);
    g.rect (x,  y + screenPadding + (lineIndex * g.textLeading) -3, width, g.textLeading * CURSOR_HEIGHT + 5);

    //line numbers
    g.fill (0, 80);
    for (int i = 0; i < lines.size(); i ++) {
      g.text (String.format("%02d", i + 1), x + screenPadding, y + screenPadding + (g.textLeading * i));
    }

    //draw the cursor
    float fractional = time - (int)time;
    if (fractional < 0.7f) {
      drawCursor();
    }

    //divider
    int dividerWeightHalf = 2;
    int[] colors = new int[] {
      g.color(142, 84, 72),  // desaturated #8d402f
      g.color(190, 139, 110), // desaturated #be794f
    };
    for (int i = 0; i < colors.length; i ++) {
      g.stroke(colors[i]);
      g.strokeWeight(dividerWeightHalf * 2);
      g.strokeCap(SQUARE);
      g.line (
        x + width - (dividerWeightHalf * (i) * 2) - dividerWeightHalf, y,
        x + width - (dividerWeightHalf * (i) * 2) - dividerWeightHalf, y + height
      );
    }


    g.noClip();
    float scrollBarPosition = 0;

    g.noStroke();
    g.rectMode(CORNER);
    g.fill (199, 191, 187); //rgb(189, 181, 176)
    g.rect (x + width - 18, scrollBarPosition, 10, 80, 5,0,0,5);
  }

  void drawCursor() {
    g.fill (60, 100, 255, 130); //transparent
    // if (columnIndex == maxLineLenght) g.fill (255, 100, 60, 130); //transparent
    // g.fill (152, 167, 244); //opaque
    g.noStroke();
    g.rectMode (CORNERS);
    
    float cursorPosX = g.textWidth(lines.get(lineIndex).substring(0,columnIndex)) + x + screenPadding + lineNumShift;
    float cursorPosY =  (g.textLeading * lineIndex) + y + screenPadding;
    
    g.rect (cursorPosX, cursorPosY-3, cursorPosX + 14, cursorPosY + (g.textLeading * CURSOR_HEIGHT) + 2);

  }

  public void update (float deltaTime) {
    time += deltaTime;

    if (keyIsHeld) {
      keyHeldTime += deltaTime;
      if (repeatKey && keyHeldTime > keyHeldInterval) {
        keyHeldTime -= keyHeldInterval;
        keyPressed(lastKey, lastKeyCode, lastKeyWasCoded);
      }
      else if (keyHeldTime > keyHeldDelay) {
        repeatKey = true;
        keyHeldTime -= keyHeldDelay;
      }
    }
  }

  public void lineUp(){
    if(lineIndex > 0) {
      StringBuilder line = lines.get(lineIndex);

      //are we at the end of the line?
      if (columnIndex == line.length()) {
        lineIndex --;
        line = lines.get(lineIndex);
        columnIndex = line.length();
      }
      else {
        lineIndex --;
        line = lines.get(lineIndex);
        //check if too far in the line
        if (columnIndex > line.length()) columnIndex = line.length();
      }
    }
  }
  
  public void lineDown(){
   if (lineIndex < lines.size() - 1) {
    StringBuilder line = lines.get(lineIndex);
      //are we at the end of the line?
      if (columnIndex == line.length()) {
        lineIndex ++;
        line = lines.get(lineIndex);
        columnIndex = line.length();
      }
      else {
        lineIndex ++;
        line = lines.get(lineIndex);
        //check if too far in the line
        if (columnIndex > line.length()) columnIndex = line.length();
      }
    }
  }
  
  public void cursorLeft(){
    time = 0;
    //are we at the beginning of the line?
    if (columnIndex == 0) {

      //are there lines above?
      if (lineIndex > 0) {
        lineIndex --;
        columnIndex = lines.get(lineIndex).length();
      }
    }
    else columnIndex --;
  }
 
  public void cursorRight(){
    time = 0;
    //are we at the end of the line?
    if (columnIndex == lines.get(lineIndex).length()) {

      //are there more lines?
      if (lineIndex < lines.size() - 1) {
        lineIndex ++;
        columnIndex = 0;
      }
    }
    else {
      columnIndex ++;
    }
  }

  public void notifyIsRunning(boolean state) {
    this.running = state;
  }
  
  public void rebuildOutputString() {
    //update the stringBuildera
    stringBuilder.setLength(0);
    for (StringBuilder line : lines) {
      stringBuilder.append (line.toString() + "\n");
    }
    builtString = stringBuilder.toString();

    app.writeProgramToFile (builtString);

    //here check if there needs to be a scroll area
    checkContentDimensionsForScroll();


    // textDimensions.set(g.textWidth(builtString), g.textLeading * builtString.lines().count());
    // var scrollX = ElementStack.getElementById("scroll-area-x");
    // if (scrollX != null) {
    //   float of = width - (screenPadding + lineNumShift + 20);
    //   scrollX.isVisible(textDimensions.x > of);
    //   updateScrollXHandleSize(scrollX, of);
    // }
  }
  
  public void keyPressed(char key, int keyCode, boolean keyIsCoded) {
    lastKey = key;
    lastKeyCode = keyCode;
    lastKeyWasCoded = keyIsCoded; 

    EnumeratedKeyCode eKeyCode = EnumeratedKeyCode.UNKNOWN;
    
    if (key == BACKSPACE || (isMacOs && key == DELETE)) eKeyCode = EnumeratedKeyCode.BACKSPACE;
    else if (key == ENTER || key == RETURN) eKeyCode = EnumeratedKeyCode.RETURN;
    else if (keyCode == UP) eKeyCode = EnumeratedKeyCode.UP;
    else if (keyCode == DOWN) eKeyCode = EnumeratedKeyCode.DOWN;
    else if (keyCode == LEFT) eKeyCode = EnumeratedKeyCode.LEFT;
    else if (keyCode == RIGHT) eKeyCode = EnumeratedKeyCode.RIGHT;
    else if (keyCode == TAB) eKeyCode = EnumeratedKeyCode.TAB;
    else if (keyCode == CONTROL) eKeyCode = EnumeratedKeyCode.CTRL;

    if (eKeyCode != EnumeratedKeyCode.UNKNOWN) {
      System.out.println(eKeyCode);
      handleCodedKey(eKeyCode);
      keyIsHeld = true;
    }
    else if(String.valueOf(key).matches(regexKeyMatch)) {
      // System.out.println(key);
      typeChar(key);
      keyIsHeld = true;
    }
    else {
      //unsupported keys
      //play sound or soemthing
    }
  }

  public void keyReleased() {
    repeatKey = false;
    keyIsHeld = false;
    keyHeldTime = 0;
  }

  private void typeChar(char c) {
    StringBuilder line = lines.get(lineIndex);

    // if (line.length() == maxLineLenght) return;

    line.insert (columnIndex, c);
    columnIndex ++;
    rebuildOutputString();
  }

  private void typeString(String c) {
    StringBuilder line = lines.get(lineIndex);
    line.insert (columnIndex, c);
    columnIndex += c.length();
    rebuildOutputString();
  }

  private void deleteChar() {
    //are we at the beginning of the line?
    if (columnIndex  == 0)  {
      
      //are the lines above?
      if (lineIndex > 0)  {

        //are we carrying text with us?
        if (lines.get(lineIndex).length() > 0) {
          //copy the text in the line
          String carry = lines.get(lineIndex).toString();
         
          //delete the line
          lines.remove(lineIndex);
          //jump up a line
          lineIndex --;
          columnIndex = lines.get(lineIndex).length();
          //append the line
          lines.get(lineIndex).append(carry);
          rebuildOutputString();
          
        }
        else {
          lines.removeLast();
          lineIndex --;
          columnIndex = lines.get(lineIndex).length();
        }
      }
    }
    else {
      StringBuilder line = lines.get(lineIndex);
      line.deleteCharAt(columnIndex-1);
      columnIndex--;
      rebuildOutputString();
    }
  }

  private void lineReturn() {
    //if at the end of the line jump to the next line
    if (columnIndex == lines.get(lineIndex).length()) {

      //at the end of file
      if(lineIndex == lines.size() -1) {
        lines.add(new StringBuilder());
        lineIndex ++;
        columnIndex = 0;
      }
      //there is a line below
      else {
        lines.add(lineIndex + 1, new StringBuilder());
        lineIndex ++;
        columnIndex = 0;
      }
    }
    //if we are in the mid of a line...
    else {
      var line = lines.get(lineIndex);

      System.out.println("Line return mid line...");
      String newLine = line.substring(columnIndex, line.length());
      System.out.println(newLine);
      line.delete(columnIndex, line.length());

      //insert a new line
      lines.add(lineIndex + 1, new StringBuilder(newLine));

      lineIndex ++;
      columnIndex = 0;
    }
    rebuildOutputString();
  }

  private void handleCodedKey (EnumeratedKeyCode code) {
    switch (code) {
      case BACKSPACE: deleteChar(); break;
      case DOWN: lineDown(); break;
      case LEFT: cursorLeft(); break;
      case RETURN: lineReturn(); break;
      case RIGHT: cursorRight(); break;
      case UP: lineUp(); break;
      case TAB: typeString("  ");
      default: //do nothing
        break;
    }
  }     

  @Override 
  public void setWidth (float width) {
    super.setWidth(width);

    //update the scroll view
    viewArea.set (
      width - (screenPadding + lineNumShift +scrollBarWidth),
      height - screenPadding
    );

    Element scrollBarVertical = ElementStack.getElementById("scroll-area-vertical")
      .height(height)
      .left(width - (scrollBarWidth + 8));
  }

  private void checkContentDimensionsForScroll() {

    // float yOverflow = textDimensions.y - (height - screenPadding);
    // if (yOverflow > 0) {  //there is overflow
    //   float percentOverflow = (height - screenPadding) / textDimensions.y;
    // }
    // else {  //make the scroll bar the whole entire area
    //   Element scrollHandleVertical = ElementStack.getElementById("scroll-handle-vertical");
    //   scrollHandleVertical.height(scrollHandleVertical.getContainerHeight());
    // }
  }
}
