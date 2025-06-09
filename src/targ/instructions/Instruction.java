package targ.instructions;

import targ.Command;
import targ.Interpretor;

public abstract class Instruction {
  protected final Command command;
  protected final int line;

  protected Instruction (Command command, int line) {
    this.command = command;
    this.line = line;
  }

  public abstract void execute(Interpretor interpretor);

  @Override
  public String toString() {
    return command.toString();
  }
}
