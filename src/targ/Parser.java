package targ;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import targ.instructions.ActivateInstruction;
import targ.instructions.ChargeInstruction;
import targ.instructions.ForwardInstruction;
import targ.instructions.Instruction;
import targ.instructions.LeftInstruction;
import targ.instructions.LinkInstruction;
import targ.instructions.ReverseInstruction;
import targ.instructions.RightInstruction;
import targ.instructions.RunInstruction;
import targ.instructions.SwapInstruction;
import targ.instructions.UnlinkInstruction;

public final class Parser {

  public static Program Parse(ArrayList<Token> tokens) {

    ArrayList<Instruction> instructions = new ArrayList<>();
    HashMap<String, ArrayList<Instruction>> funcMap = new HashMap<>();
    Stack<ArrayList<Instruction>> loopStack = new Stack<>();
    Stack<Integer> loopCounterStack = new Stack<Integer>();

    //go through each token and create some instructions
    int i = 0;
    String functionIndentifier = "";

    while (i  < tokens.size()) {

      Token token = tokens.get(i);

      //skip if end of file
      if (token.type == TokenType.EOF)break;

      //throw error if the token is not a keyword
      if (token.type != TokenType.KEYWORD) {
        throw new RuntimeException("Unexpected token at line " + token.lineNumber + " \'"+token.value+"\'");
      }

      //lets parse the token now
      Command command = Command.valueOf(token.value);

      //check if there is an argument
      boolean hasArgument = false;
      String argument ="";
      if (tokens.get(i + 1).type != TokenType.KEYWORD) {
        hasArgument = true;
        argument = tokens.get(i + 1).value;
        i+=2;
      }else {
        i++;
      }

      //check for control flow commands
      if (command == Command.END) {
        //break out of loop and add queued commands 
        if (loopStack.size() > 0) {
          //get the stack and the number of loops
          var stack = loopStack.pop();
          var repeats = loopCounterStack.pop();

          //where is this stack going?
          //if there is another loop on the stack, add these commands to the currently stacked loop
          //otherwise add them to the proper list
          for (int r = 0; r < repeats; r ++) {
            if (loopStack.size() > 0) {
              RegisterInstructionStack(loopStack.peek(), stack);
            }
            else if (!functionIndentifier.isEmpty()) {
              RegisterInstructionStack(funcMap.get(functionIndentifier), stack);
            }
            else RegisterInstructionStack(instructions, stack); 
          }
        } 
        //break out of the current function
        else if (!functionIndentifier.isEmpty()){
          functionIndentifier = "";
        }
        else throw new RuntimeException("Unmatched END at line " + token.lineNumber);
        continue;
      }
      else if (command == Command.FUNC) {
        //check if we are already in a function as this is not allowed
        if (!functionIndentifier.isEmpty()) throw new RuntimeException("Illegal nesting at line " + token.lineNumber + " \'"+token.value+"\'"); 
        
        //otherwise recognize this as a new function
        functionIndentifier = argument;

        //check to see if there is already a function of this name
        if (funcMap.containsKey(functionIndentifier)) throw new RuntimeException("Func identifieralready used at line " + token.lineNumber + " \'"+token.value+"\'");
        else {
          funcMap.put (functionIndentifier, new ArrayList<Instruction>());
        } 
        continue;
      }
      else if (command == Command.LOOP) {
        //all loops must have an argument
        if (hasArgument == false) throw new RuntimeException("Missing argument at line " + token.lineNumber + " \'"+token.value+"\'"); 
        int repeats = 0;
        try {
          repeats = Integer.parseInt(argument);
        }
        catch (Exception e) {
          throw new RuntimeException("Unexpected token at line " + token.lineNumber + " \'"+token.value+"\'"); 
        }

        //too high of a loop stack 16 is the max
        if (repeats > 16) throw new RuntimeException("Argument out of range at line " + token.lineNumber + " \'"+repeats+"\'"); 


        //add loopStack and counter
        loopStack.push(new ArrayList<Instruction>());
        loopCounterStack.push (repeats);
        continue;
      }

      //add commands to the proper place
      if (loopStack.size() > 0) {
        //add instructions to the loop stack only
        RegisterInstruction (loopStack.peek(), command, token, hasArgument, argument);
      }
      else { //only directly add if not in a loop stack
        if (functionIndentifier.isEmpty())  {
          RegisterInstruction (instructions, command, token, hasArgument, argument);
        }else {
          //find the function map
          var funcMapEntry = funcMap.get(functionIndentifier);
          RegisterInstruction(funcMapEntry, command, token, hasArgument, argument);
        }
      }
    }

    // Final validation: make sure nothing was left open
    if (!functionIndentifier.isEmpty()) {
      throw new RuntimeException("Unexpected EOF: function '" + functionIndentifier + "' was not closed.");
    }
    if (!loopStack.isEmpty()) {
      throw new RuntimeException("Unexpected EOF: loop block was not closed.");
    }   

    return new Program(instructions, funcMap);
  }

  private static void RegisterInstructionStack (ArrayList<Instruction> list, ArrayList<Instruction> instructionStack) {
    list.addAll(instructionStack);
  }

  /**Filters the command type and adds the instructions to the target list */
  private static void RegisterInstruction (ArrayList<Instruction> list, Command command, Token token, boolean hasArgument, String arg) {

    int iarg = 1;
    try {
      iarg = Integer.parseInt(arg);
    }
    catch (Exception e){}

    final int n = iarg;

    //filter commands
    switch (command) {
      default: throw new RuntimeException("Unhandled keyword at line " + token.lineNumber + " \'"+token.value+"\'"); 
      case ACTIVATE:  list.add(new ActivateInstruction(command, token.lineNumber)); break;
      case CHARGE:    list.add(new ChargeInstruction(command, token.lineNumber)); break;
      case END:       break;
      case ERROR :    break; 
      case FORWARD:   repeat(n, () -> list.add(new ForwardInstruction(command, token.lineNumber))); break;
      case FUNC:      break;
      case LEFT:      repeat(n, () -> list.add(new LeftInstruction(command, token.lineNumber))); break;
      case LINK:      list.add(new LinkInstruction(command, token.lineNumber, GetLiteral(arg))); break;
      case LOOP:      break;
      case REVERSE:   repeat(n, () -> list.add(new ReverseInstruction(command, token.lineNumber))); break;
      case RIGHT:     repeat(n, () -> list.add(new RightInstruction(command, token.lineNumber))); break;
      case RUN:       list.add(new RunInstruction(command, token.lineNumber, arg)); break;
      case SWAP:      list.add(new SwapInstruction(command, token.lineNumber, GetLiteral(arg))); break;
      case UNLINK:    list.add(new UnlinkInstruction(command, token.lineNumber, GetLiteral(arg))); break;
    }
  }

  private static Literal GetLiteral(String arg) {
    try {
      return Literal.valueOf(arg.toUpperCase());
    }
    catch (Exception e) {
      return Literal.UNKNOWN;
    }
  }

  /**Adds an instruction to a target n number of times. */
  private static void repeat(int times, Runnable action) {
    for (int i = 0; i < times; i++) action.run();
  }
}
