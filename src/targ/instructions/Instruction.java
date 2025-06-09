package targ.instructions;

import main.App;
import targ.Command;

public abstract class Instruction {
  protected final Command command;
  protected final int line;

  protected Instruction (Command command, int line) {
    this.command = command;
    this.line = line;
  }

  public abstract void execute(App interpretor);

  @Override
  public String toString() {
    return command.toString();
  }
}
