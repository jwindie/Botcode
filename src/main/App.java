package main;

import java.util.ArrayList;

import playground.Float2;
import playground.PlaygroundApp;
import playground.ui.Constraint;
import playground.ui.Element;
import playground.ui.ElementStack;
import playground.ui.Layout;
import processing.core.PFont;
import targ.Parser;
import targ.Program;
import targ.Token;
import targ.Tokenizer;

public class App extends PlaygroundApp {

  private Field field;
  private TextEditor editor;
  private PFont codeFont;
  private PFont debugFont;

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
    frameRate(60);


    editor = new TextEditor(width - height, height, this);
    field = new Field(height, g);
    field.setX(editor.getWidth());
    field.setGridDimensions (15);
    field.createRobots();

    codeFont = createFont("fonts/ModernDOS8x8.ttf", 8, false);
    debugFont = createFont("fonts/CourierPrime-Regular.ttf", 16, true);
    textFont (codeFont);

    ElementStack.setGraphics (g);
    ElementStack.setInspectorTextColor(color (0));
    ElementStack.setInspectorTextSize (20);
    // ElementStack.showDebug(true);
    // ElementStack.showElementInspector(true);
    createUI();

    //test fiile
    FileHandler.writeFile("test.txt", "This is a test file!");
  }

  void createUI() {

    Element buttonDock = new Element("button-dock", 120, 30)
      .moveTo(field.getX(), 0)
      .style (s -> s
        .backgroundColor(color (112, 65, 56))
        .cornerRadius(0,0,10,0)
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
        onClickedPlayButton();
      });
    
    Element pauseButton = playButton.copy("pause-button")
      .image(loadImage("icons/icons8-pause-52.png"))
      .onClick((button) -> System.out.println("Clicked pause Button"));


    Element stopButton = playButton.copy("stop-button")
      .image(loadImage("icons/icons8-stop-52.png"))
      .onClick((button) -> System.out.println("Clicked stop Button"));

    Element settingsButton = playButton.copy("settings-button")
      .image(loadImage("icons/icons8-settings-50.png"))
      .onClick((button) -> toggleSettingsModal());

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

    Element modalBlock = new Element("modal-block")
      .isVisible(false)
      .isInteractable(true)
      .applyConstraint(Constraint.maxHeight())
      .applyConstraint(Constraint.maxWidth())
      .style(s -> s.backgroundColor(color(39, 29, 21, 180))); //rgb(39, 29, 21)

    Element settingWindow = new Element("settings-modal", width * .7f, height *.7f)
      .parent(modalBlock)
      .isVisible(false)
      .isInteractable(true)
      .isDraggable(true)
      .applyConstraint(Constraint.boundaryWithPadding(10))
      .style(s -> s
        .backgroundColor(color (255))
        .cornerRadius(10)
    );
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
    field.update(deltaTime);
    ElementStack.update(new Float2(mouseX, mouseY));

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
    if (fieldDarken > 0) {
      fill (36, 31, 29, fieldDarken);
      rectMode (CORNER);
      noStroke();
      rect (field.getX(), field.getY(), field.getWidth(), field.getHeight());
    }

    textFont(codeFont);
    editor.draw (); 

    textFont(debugFont);
    ElementStack.draw();
    
    textFont(codeFont);
  }

  @Override
  public void mouseDragged() {
    ElementStack.mouseDragged(new Float2(mouseX, mouseY));
  }

  public void writeProgramToFile(String contents) {
    FileHandler.writeFile("program.targ", contents);
  }

  private void onClickedPlayButton() {
    runProgram(loadProgram(editor.getText()));
  }

  private void toggleSettingsModal() {
    var element = ElementStack.getElementById("settings-modal");
    element.isVisible(!element.isVisible());
    ElementStack.getElementById("modal-block").isVisible(element.isVisible());
    if (element.isVisible()) {
      element.constrainOnce(Constraint.center());
    }
  }

  private Program loadProgram(String programRaw) {
    // String programFile = path;

    try {
      // String programRaw = Files.readString(Path.of(sketchPath("data/" + programFile)));
      // System.out.println("\nProcessing Program: " + programFile);
      System.out.print("\nRAW READ");
      System.out.println("=".repeat(50));
      System.out.println("\n"+programRaw); // Debug print

      //tokenize the program
      ArrayList<Token> tokens = Tokenizer.tokenize(programRaw);
      System.out.print("\nTOKENIZED");
      System.out.println("=".repeat(50));
      System.out.println();
      for (var token : tokens) {
        System.out.println(token);
      }

      //parse the program
      System.out.print("\nPARSED");
      System.out.println("=".repeat(50));
      System.out.println();
      Program program = Parser.Parse(tokens);
      System.out.println(program);

      System.out.println("=".repeat(50));
      System.out.println();

      return program;

    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("FUCK");
    }
  }

  private void runProgram(Program program) {
    field.runProgram (program);
  }
}