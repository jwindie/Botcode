package main;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GL2ES2;

import playground.Float2;
import playground.PlaygroundApp;
import playground.ui.Constraint;
import playground.ui.Element;
import playground.ui.ElementStack;
import playground.ui.Layout;
import processing.opengl.PGraphicsOpenGL;

public class App extends PlaygroundApp {

  private Field field;
  private TextEditor editor;


  public App() {
		super();
	}

	public static void main(String[] args) {
		App.main("main.App");
  }

  @Override
  public void settings() {
    size (1080,640);
    pixelDensity(displayDensity());
    // smooth (8);
    noSmooth();
  }

	@Override
	public void setup() {
    surface.setTitle("Botcode Editor");
    editor = new TextEditor(width - height, height, this);
    field = new Field(height, g);
    field.setX(editor.getWidth());
    field.setGridDimensions (15);
    field.createRobots();

    createUI();

    // reloadProgram();
  }

  void createUI() {
    ElementStack.setGraphics (g);
    textFont (createFont("fonts/ModernDOS8x8.ttf", 8, false));
    // textFont(loadFont("fonts/ModernDOS8x8-140.vlw"));
    ElementStack.setInspectorTextColor(color (255,0,0));
    ElementStack.setInspectorTextSize (32);
    // ElementStack.showDebug(true);
    // ElementStack.showElementInspector(true);

    Element buttonDock = new Element("button-dock", 90, 30)
      .moveTo(field.getX(), 0)
      .style (s -> s
        .backgroundColor(color (112, 65, 56))
        .cornerRadius(0,0,6,0)
      );

    Element buttonDockCorner = buttonDock.copy("button-dock-corner")
      .size(0,0)
      .top(buttonDock.getHeight())
      .style(s -> s.cornerRadius(0,0,-15, 0));

    Element playButton = new Element("play-button", 30,30)
      .parent(buttonDock)
      .image(loadImage("icons/icons8-play-52.png"))
      .padding(4)
      .isInteractable(true)
      .style (s -> s.tint (color (40)))
      .hoveredStyle(s -> s.tint(color (255, 200)))
      .pressedStyle(s -> s.tint(color (255, 100)))
      .onClick((button) -> {
        System.out.println("Clicked play Button");
        modifyTextEditorWidth(editor.getDefaultWidth());
        ElementStack.getElementById("editor-drag-handle").left (0);
      });
    
    Element pauseButton = playButton.copy("pause-button")
      .image(loadImage("icons/icons8-pause-52.png"))
      .onClick((button) -> System.out.println("Clicked pause Button"));


    Element stopButton = playButton.copy("stop-button")
      .image(loadImage("icons/icons8-stop-52.png"))
      .onClick((button) -> System.out.println("Clicked stop Button"));

    buttonDock.applyLayout(Layout.horizontalLeft(CENTER));

    Element dragWindowDragArea = new Element(500, height)
      .left(editor.width - 8);

    Element dragWindowSize = new Element("editor-drag-handle", 8, height)
      .left (editor.width - 8)
      .isInteractable(true)
      .isDraggable(true)
      .applyConstraint(Constraint.boundary())
      .parent(dragWindowDragArea, true)
      .onDrag((e) -> modifyTextEditorWidth(e.getGlobalLeft() + 8));
  }

  void modifyTextEditorWidth(float width) {
    //move all the elements that needit
    editor.setWidth(width);
    ElementStack.getElementById("button-dock").left(width);
    ElementStack.getElementById("button-dock-corner").left(width);
  } 

  void update() {
    float deltaTime = 1f/frameRate;
    editor.update(deltaTime);
    ElementStack.update(new Float2(mouseX, mouseY));

    // if (running) {
    //   timer += 1/frameRate;
    //   if (executeImmediate || timer >= executeInterval) {
    //     if (! executeImmediate) timer -= executeInterval;
    //     executeImmediate = false;
    //     executeProgramNext();
    //   } 
    // }
  }

  @Override
  public void keyPressed() {
    // keyIsHeld = true;
    editor.keyPressed(key, keyCode, key == CODED);
    if (key == ESC) key = 0;
  }

  @Override 
  public void keyReleased() {
    editor.keyReleased();
  }

  @Override 
  public void mousePressed() {
    ElementStack.mousePressed(mouseButton);
  }

  @Override 
  public void mouseReleased() {
    ElementStack.mouseReleased();
  }

  @Override
  public void draw() {
    update();

    background(255, 0, 255);

    field.draw(width - height, 0);

    float fieldDarken = map (editor.isOverSized(), 0, 400, 0, 230);
    fieldDarken = constrain(fieldDarken, 0, 230);
    System.out.println(fieldDarken);
    if (fieldDarken > 0) {
      fill (36, 31, 29, fieldDarken);
      rectMode (CORNER);
      noStroke();
      rect (field.getX(), field.getY(), field.getWidth(), field.getHeight());
      System.out.println(field.getWidth());
    }

    editor.draw (); 

    ElementStack.draw();
  }

  @Override
  public void mouseDragged() {
    ElementStack.mouseDragged(new Float2(mouseX, mouseY));
  }
}