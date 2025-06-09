package targ.instructions;

import main.App;
import targ.Command;

public class ChargeInstruction extends Instruction {

  public ChargeInstruction (Command command, int line) {
    super(command, line);
  }

  @Override
  public void execute(App interpretor) {
    interpretor.charge();
  }
}
