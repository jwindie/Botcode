package targ.instructions;

import targ.Command;
import targ.Interpretor;

public class RunInstruction extends Instruction {

  final String pointer;

  public RunInstruction(Command command, int line, String pointer) {
    super(command, line);
    this.pointer = pointer;
  }

  @Override
  public void execute(Interpretor interpretor) {
    interpretor.runFunc(pointer);
  }

  @Override
  public String toString() {
    return command.toString() + " " + pointer; 
  }
}
