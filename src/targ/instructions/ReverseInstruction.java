package targ.instructions;

import main.App;
import targ.Command;

public class ReverseInstruction extends Instruction {

  public ReverseInstruction (Command command, int line) {
    super(command, line);
  }

  @Override
  public void execute(App interpretor) {
    interpretor.moveReverse();
  }
}
