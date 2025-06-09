package targ.instructions;

import main.App;
import targ.Command;

public class RunInstruction extends Instruction {

  final String pointer;

  public RunInstruction(Command command, int line, String pointer) {
    super(command, line);
    this.pointer = pointer;
  }

  @Override
  public void execute(App interpretor) {
    interpretor.runFunc(pointer);
  }

  @Override
  public String toString() {
    return command.toString() + " " + pointer; 
  }
}
