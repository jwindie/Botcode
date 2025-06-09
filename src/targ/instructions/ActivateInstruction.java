package targ.instructions;

import main.App;
import targ.Command;

public class ActivateInstruction extends Instruction {

  public ActivateInstruction (Command command, int line) {
    super(command, line);
  }

  @Override
  public void execute(App interpretor) {
    interpretor.activate();
  }

}
