package targ.instructions;

import main.App;
import targ.Command;
import targ.Literal;

public class LinkInstruction extends Instruction {

  final Literal target;
  
  public LinkInstruction (Command command, int line, Literal target) {
    super(command, line);
    this.target = target;
  }

  @Override
  public void execute(App interpretor) {
    interpretor.link(target);
  }

  @Override
  public String toString() {
    return command.toString() + " " + target.toString().toLowerCase(); 
  }
}
