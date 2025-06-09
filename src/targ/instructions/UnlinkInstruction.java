package targ.instructions;

import main.App;
import targ.Command;
import targ.Literal;

public class UnlinkInstruction extends Instruction {

  final Literal target;
  public UnlinkInstruction (Command command, int line, Literal target) {
    super(command, line);
    this.target = target;
  }

  @Override
  public void execute(App interpretor) {
    interpretor.unlink(target);
  }

  @Override
  public String toString() {
    return command.toString() + " " + target.toString().toLowerCase(); 
  }
}
