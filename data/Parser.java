// package botcode;

// import java.util.ArrayList;

// import main.App;
// import targ.instructions.ActivateInstruction;
// import targ.instructions.ChargeInstruction;
// import targ.instructions.EndInstruction;
// import targ.instructions.ForwardInstruction;
// import targ.instructions.Instruction;
// import targ.instructions.LeftInstruction;
// import targ.instructions.LinkInstruction;
// import targ.instructions.ReverseInstruction;
// import targ.instructions.RightInstruction;
// import targ.instructions.SwapInstruction;
// import targ.instructions.UnlinkInstruction;

// public final class Parser {
  
//   public static ArrayList<Instruction> Parse(App target, ArrayList<Token> tokens) {

//     ArrayList<Instruction> instructions = new ArrayList<>();

//     //go through each token and create some instructions
//     int i = 0;
//     while (i  < tokens.size()) {

//       Token token = tokens.get(i);

//       //skip if end of file
//       if (token.type == TokenType.EOF) break;

//       //throw error if the token is not a keyword
//       if (token.type != TokenType.KEYWORD) {
//         throw new RuntimeException("Unexpected token at line " + token.lineNumber + " \'"+token.value+"\'");
//       }

//       //lets parse the token now
//       Command command = Command.valueOf(token.value);
//       int loopsRemaining = 0;
//       int loopStart = -1;

//       //filter flow commands
//       if (command == Command.ERROR) {
//         throw new RuntimeException("Undefined keyword at line " + token.lineNumber + " \'"+token.value+"\'");
//       }
//       else if (command == Command.LOOP) {
//         loopsRemaining = Integer.parseInt(tokens.get(i + 1).value);
//         loopStart = i + 2;
//         i += 1;
//       }
//       else if (command == Command.END) {
//         //break out if we are in a loop
//         if (loopStart >= 0) {
//           loopsRemaining --;
//           if (loopsRemaining > 0) i = loopStart;
//           else loopStart = -1;
//         }
//       }
      
//       //move past the command and then try to collect the argument needed
//       i ++;
//       String arg_String = tokens.get(i).value;
//       int arg_int = Integer.parseInt(tokens.get(i).value);


      
//       //based on the command, filter the instruction to create the actual type of instruction
//       switch (command) {
//         default: throw new RuntimeException("Unhandled keyword at line " + token.lineNumber + " \'"+token.value+"\'"); 
//         case ACTIVATE:  instructions.add(new ActivateInstruction(command, token.lineNumber)); break;
//         case CHARGE:    instructions.add(new ChargeInstruction(command, token.lineNumber)); break;
//         case END:       instructions.add(new EndInstruction(command, token.lineNumber)); break;
//         case ERROR :    continue; 
//         case FORWARD:   instructions.add(new ForwardInstruction(command, token.lineNumber)); break;
//         case FUNC:      continue;
//         case LEFT:      instructions.add(new LeftInstruction(command, token.lineNumber)); break;
//         case LINK:      instructions.add(new LinkInstruction(command, token.lineNumber, Integer.parseInt(arg_String))); break;
//         case LOOP:      continue;
//         case REVERSE:   instructions.add(new ReverseInstruction(command, token.lineNumber)); break;
//         case RIGHT:     instructions.add(new RightInstruction(command, token.lineNumber)); break;
//         case RUN:       continue;
//         case SWAP:      instructions.add(new SwapInstruction(command, token.lineNumber, Integer.parseInt(arg_String))); break;
//         case UNLINK:    instructions.add(new UnlinkInstruction(command, token.lineNumber, Integer.parseInt(arg_String))); break;
//       }
//     }

//     return instructions;
//   }
// }
