package targ;

import java.util.ArrayList;
import java.util.HashMap;

import targ.instructions.Instruction;

public class Program {
  public final ArrayList<Instruction> instructions;
  public final HashMap<String, ArrayList<Instruction>> funcMap;

  public Program (ArrayList<Instruction> instructions, HashMap<String, ArrayList<Instruction>> funcMap) {
    this.instructions = instructions;
    this.funcMap = funcMap;
  }

  @Override
  public String toString() {

    StringBuilder s = new StringBuilder();
    //print all of the funtions to the screens
    for (var funcListName : funcMap.keySet()) {
      s.append("FUNC " + funcListName +  "\n");

      for (Instruction instruction : funcMap.get(funcListName)) {
        s.append("  "+instruction + "\n");
      }
      s.append("\n");
    }

    //then print the regular instructions
    for (Instruction instruction : instructions) {
      s.append(instruction + "\n");
    }
    
    return s.toString();
  }
}
