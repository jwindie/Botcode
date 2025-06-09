package targ.instructions;

import targ.Command;
import targ.Interpretor;

public class ActivateInstruction extends Instruction {

  public ActivateInstruction (Command command, int line) {
    super(command, line);
  }

  @Override
  public void execute(Interpretor interpretor) {
    interpretor.activate();
  }

}
