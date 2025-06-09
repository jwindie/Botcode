package targ.instructions;

import targ.Command;
import targ.Interpretor;

public class ReverseInstruction extends Instruction {

  public ReverseInstruction (Command command, int line) {
    super(command, line);
  }

  @Override
  public void execute(Interpretor interpretor) {
    interpretor.reverse();
  }
}
