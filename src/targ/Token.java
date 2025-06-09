package targ;

public class Token {
  public final TokenType type;
  public final String value;
  public final int lineNumber;

  public Token(TokenType type, String value, int lineNumber) {
    this.type = type;
    this.value = value;
    this.lineNumber = lineNumber;
  }

  @Override
  public String toString() {
    if (type == TokenType.EOF) return String.format("%02d",lineNumber) +" ["+type.toString().toUpperCase()+"]";
    else return String.format("%02d",lineNumber) + " ["+type.toString().toUpperCase()+", "+value+"]";
  }
}