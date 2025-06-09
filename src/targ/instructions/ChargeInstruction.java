package targ.instructions;

import targ.Command;
import targ.Interpretor;

public class ChargeInstruction extends Instruction {

  public ChargeInstruction (Command command, int line) {
    super(command, line);
  }

  @Override
  public void execute(Interpretor interpretor) {
    interpretor.charge();
  }
}
