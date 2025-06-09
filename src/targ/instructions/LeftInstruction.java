package targ.instructions;

import targ.Command;
import targ.Interpretor;

public class LeftInstruction extends Instruction {

  public LeftInstruction (Command command, int line) {
    super(command, line);
  }

  @Override
  public void execute(Interpretor interpretor) {
    interpretor.left();
  }
}