package targ.instructions;

import main.App;
import targ.Command;

public class RightInstruction extends Instruction {
  
  public RightInstruction (Command command, int line) {
    super(command, line);
  }

  @Override
  public void execute(App interpretor) {
    interpretor.turnRight();
  }
}
