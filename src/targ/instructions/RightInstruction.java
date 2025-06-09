package targ.instructions;

import targ.Command;
import targ.Interpretor;

public class RightInstruction extends Instruction {
  
  public RightInstruction (Command command, int line) {
    super(command, line);
  }

  @Override
  public void execute(Interpretor interpretor) {
    interpretor.right();
  }
}
