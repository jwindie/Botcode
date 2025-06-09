package targ.instructions;

import targ.Command;
import targ.Interpretor;
import targ.Literal;

public class SwapInstruction extends Instruction {

  final Literal target;
  public SwapInstruction (Command command, int line, Literal target) {
    super(command, line);
    this.target = target;
  }

  @Override
  public void execute(Interpretor interpretor) {
    interpretor.swap(target);
  }

  @Override
  public String toString() {
    return command.toString() + " " + target.toString().toLowerCase(); 
  }
}
