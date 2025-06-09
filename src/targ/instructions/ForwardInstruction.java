package targ.instructions;

import targ.Command;
import targ.Interpretor;

public class ForwardInstruction extends Instruction {

  public ForwardInstruction (Command command, int line) {
    super(command, line);
  }

  @Override
  public void execute(Interpretor interpretor) {
    interpretor.forward();
  }
}
