// package main;

// import java.nio.file.Files;
// import java.nio.file.Path;
// import java.util.ArrayList;
// import java.util.Stack;

// import processing.core.PApplet;
// import targ.Literal;
// import targ.Parser;
// import targ.Program;
// import targ.Token;
// import targ.Tokenizer;
// import targ.editor.TextEditor;
// import targ.instructions.Instruction;

// public class App extends PApplet {

//   float gridSpacing; 
//   Program parsedProgram;
//   float timer = 0;
//   float executeInterval = .33f;
//   Stack<ArrayList<Instruction>> callStack = new Stack<>();
//   ArrayList<Integer> callStackIndex = new ArrayList<>();
//   int instructionIndex;
//   boolean running;
//   boolean executeImmediate = false;
//   boolean showTextEditor = true;
//   
//   TextEditor editor;
//   Field field;
//   float time;

//   public App() {
// 		super();
// 	}

// 	public static void main(String[] args) {
// 		App.main("main.App");
//   }

//   public boolean isRunning() {
//     return running;
//   }

//   @Override
//   public void settings() {
//     size (1080,640, P2D);
//     pixelDensity(displayDensity());
//     smooth (8);
//   }

// 	@Override
// 	public void setup() {
//     editor = new TextEditor(width - height, height, this);
//     field = new Field(height, g);
//     field.setGridDimensions (15);
//     field.createRobots();

//     textFont (createFont("fonts/pypx.ttf", 100));

//     reloadProgram();
//   }

//   void reloadProgram() {

//     String programFile = "program.targ";

//     try {
//       program = Files.readString(Path.of(sketchPath("data/" + programFile)));
//       System.out.println("\nProcessing Program: " + programFile);
//       System.out.print("\nRAW READ");
//       System.out.println("=".repeat(50));
//       System.out.println("\n"+program); // Debug print

//       //tokenize the program
//       ArrayList<Token> tokens = Tokenizer.tokenize(program);
//       System.out.print("\nTOKENIZED");
//       System.out.println("=".repeat(50));
//       System.out.println();
//       for (var token : tokens) {
//         System.out.println(token);
//       }
//       //parse the program
//       System.out.print("\nPARSED");
//       System.out.println("=".repeat(50));
//       System.out.println();
//       parsedProgram = Parser.Parse(tokens);
//       System.out.println(parsedProgram);

//       System.out.println("=".repeat(50));
//       System.out.println();

//     } catch (Exception e) {
//       e.printStackTrace();
//       exit();
//     }
//   }

//   void update() {
//     time += 1/frameRate;

//     if (running) {
//       timer += 1/frameRate;
//       if (executeImmediate || timer >= executeInterval) {
//         if (! executeImmediate) timer -= executeInterval;
//         executeImmediate = false;
//         executeProgramNext();
//       } 
//     }
//   }

//   @Override
//   public void draw() {
//     update();
//     field.draw(width - height, 0);
//     editor.draw (time); 

//     //draw buttons area
//     int buttonAreaWidth = running ? 150 : 30;
//     rectMode (CORNER);
//     noStroke();
//     fill(112, 65, 56);  // desaturated rgb(112, 65, 56)
//     rect (editor.getWidth(), 0, buttonAreaWidth, 30, 0, 0, 8, 0);

//       if (running) {
//         strokeWeight(4);
//         noFill();
//         stroke (60, 255, 150);
//         rectMode(CORNER);
//         rect (0, 0, width, height);
//       }
//   }

//   @Override 
//   public void keyPressed() {
//     System.out.println(frameCount);
//     if (keyCode == TAB) {
//       showTextEditor = !showTextEditor;
//     }

//     // if (showTextEditor) {
//       editor.keyPressed(key, keyCode, key == CODED);
  
//       if (keyCode == ENTER) {
//         if (!running) {
//           // resetRobots();
//           startProgram();
//         }
//         else endProgram();
//       }
//       else if (key == 'r') {
//         if (running) endProgram();
//         reloadProgram();
//       }
//     }
//   // }

//   /**Takes a look at the next instruction in the program and does it */
//   public void executeProgramNext() {
//     //check if we are in a function call, if so, dothose instructions instead
//     if (callStack.size() > 0) {
//       int stackIndex = callStackIndex.getLast();

//       //check if we are at the end of the stack, if not execute
//       if (stackIndex < callStack.peek().size()){
//         callStack.peek().get(stackIndex).execute(this);
//         System.out.println(callStack.peek().get(stackIndex));
//         callStackIndex.set(callStackIndex.size() - 1, stackIndex + 1);
//       }
//       else endFunc();
//     }
//     //get the program instruction at line index
//     else if (instructionIndex < parsedProgram.instructions.size()){
//       parsedProgram.instructions.get(instructionIndex).execute(this);
//       System.out.println(parsedProgram.instructions.get(instructionIndex));
//       instructionIndex ++;
//     }
//     else endProgram();
//   }

//   public void startProgram() {
//     instructionIndex = 0;
//     running = true;
//   }

//   public void endProgram() {
//     running = false;
//     callStack.clear();
//     callStackIndex.clear();
//     timer = 0;
//   }

//   public void endFunc(){
//     //simple pop the stack and remove last for the index
//     callStack.pop();
//     callStackIndex.removeLast();
//     executeImmediate = true;
//   }

//   public void runFunc(String pointer){
//     //add to the stack
//     callStack.push(parsedProgram.funcMap.get(pointer));
//     callStackIndex.addLast(0);
//     executeImmediate = true;
//   }

//   public void moveForward() {
//     field.moveForward();
//   }

//   public void moveReverse() {
//     field.moveReverse();
//   }

//   public void turnLeft() {
//     field.turnLeft();
//   }

//   public void turnRight() {
//     field.turnRight();
//   }

//   public void activate() {
//   }  
  
//   public void charge() {
//   }

//   public void swap(Literal target) {
//     field.swap(target);
//     executeImmediate = true;
//   }

//   public void link(Literal target) {
//     field.link(target);
//     executeImmediate = true;
//   }
  
//   public void unlink(Literal target) {
//     field.unlink(target);
//     executeImmediate = true;
//   }
// }
  