package targ.instructions;

import main.App;
import targ.Command;

public class LeftInstruction extends Instruction {

  public LeftInstruction (Command command, int line) {
    super(command, line);
  }

  @Override
  public void execute(App interpretor) {
    interpretor.turnLeft();
  }
}