package playground;

import java.util.Objects;

public class Int2 {

  public int x;
  public int y;
  
  public Int2 () {}

  public Int2 (int value) {
    this.x = value;
    this.y = value;
  }

  public Int2 (int x, int y) {
    this.x = x;
    this.y = y;
  }

  public void setAll (int value) {
    this.x = value;
    this.y = value;
  }

  public void set (int x, int y) {
    this.x = x;
    this.y = y;
  }

  public void set (Int2 other) {
    this.x = other.x;
    this.y = other.y;
  }

  public void add (Int2 other) {
    x += other.x;
    y += other.y;
  }

  public void add (int x, int y) {
    this.x += x;
    this.y += y;
  }
  
  public void addAll (int value) {
    x += value;
    y += value;
  }

  public void sub (Int2 other) {
    x -= other.x;
    y -= other.y;
  }
  
  public void sub (int x, int y) {
    this.x -= x;
    this.y -= y;
  }

  public void subAll (int value) {
    x -= value;
    y -= value;
  }

  public void multiply (Int2 other) {
    this.x *= other.x;
    this.y *= other.y;
  }

  public void multiply (int x, int y) {
    this.x *= x;
    this.y *= y;
  }

  public void multiplyAll (int value) {
    this.x *= value;
    this.y *= value;
  }

  public void divide (Int2 other) {  
    if (other.x == 0 || other.y == 0) throw new ArithmeticException("divide by zero");
    this.x /= other.x;
    this.y /= other.y;
  }

  public void divide (int x, int y) {  
    if (x == 0 || y == 0) throw new ArithmeticException("divide by zero");
    this.x /= x;
    this.y /= y;
  }

  public void divideAll (int value) {
    if (value == 0 ) throw new ArithmeticException("divide by zero");
    this.x /= value;
    this.y /= value;
  }

  public Int2 copy () {
    return new Int2 (x,y);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    Int2 int2 = (Int2) obj;
    return int2.x == x && int2.y == y;
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y);
  }

  @Override
  public String toString () {
    return "[" + x + ", " + y + "]";
  }

  //#region Static Methods
  public static Int2 add(Int2 a, Int2 b) {
    return new Int2(a.x + b.x, a.y + b.y);
  }

  public static Int2 sub(Int2 a, Int2 b) {
    return new Int2(a.x - b.x, a.y - b.y);
  }

  public static Int2 multiply(Int2 a, Int2 b) {
    return new Int2(a.x * b.x, a.y * b.y);
  }

  public static Int2 divide(Int2 a, Int2 b) {
    if (b.x == 0 || b.y == 0) throw new ArithmeticException("divide by zero");
    return new Int2(a.x / b.x, a.y / b.y);
  }
}