package targ;
import java.util.ArrayList;
import java.util.Set;

public final class Tokenizer {

  public static ArrayList<Token> tokenize(String program) {
    //create list of tokens
    ArrayList<Token> tokens = new ArrayList<>();

    //first split the program into lines
    String[] lines = program.split("\n");
    int lineNumber = 1;

    //go through each line of the file and populate the list with the tokens split
    for (String line : lines) {

      String trimmed = line.trim();

      //ignore comments and empty lines
      if (trimmed.isEmpty() || trimmed.startsWith("#"))  {
        lineNumber ++;
        continue;
      }

      //split the line into parts
      // the "\\s+" is Regex and will split on one or more whitespace characters
      //the \\s is whitespace and the + means one or more of the preceeding thing
      String[] parts = trimmed.split ("\\s+");
      for (String part : parts) {

        //logic on the parts
        if (isKeyword (part)) {
          tokens.add (new Token (TokenType.KEYWORD, part, lineNumber));
        }
        else if (isNumeric(part)) {
          tokens.add (new Token(TokenType.NUMBER, part, lineNumber));
        }
        else tokens.add (new Token(TokenType.IDENTIFIER, part, lineNumber));
      }

      lineNumber ++;
    }

    //add the final EOF token
    tokens.add (new Token(TokenType.EOF, "", lineNumber));

    return tokens;
  }

  /**Helper method to check if string is a keyword. */
  static boolean isKeyword(String s) {
    return Set.of("FORWARD", "REVERSE", "LEFT", "RIGHT", "ACTIVATE", 
      "CHARGE", "SWAP", "LINK", "UNLINK", "WHITE", "RED", "BLUE", 
      "GREEN", "ORANGE", "FUNC", "RUN", "END", "GOTO", "LABEL", "LOOP")
      .contains(s);
  }

  /**HElper method to check if string is a number */
  static boolean isNumeric(String s) {
    return s.matches("-?\\d+");
  }
}

