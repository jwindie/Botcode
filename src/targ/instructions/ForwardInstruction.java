package targ.instructions;

import main.App;
import targ.Command;

public class ForwardInstruction extends Instruction {

  public ForwardInstruction (Command command, int line) {
    super(command, line);
  }

  @Override
  public void execute(App interpretor) {
    interpretor.moveForward();
  }
}
