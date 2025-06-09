package playground;

import java.util.Objects;

public class Float2 {

  public float x;
  public float y;
  
  public Float2 () {}

  public Float2 (float value) {
    this.x = value;
    this.y = value;
  }

  public Float2 (float x, float y) {
    this.x = x;
    this.y = y;
  }

  public void setAll (float value) {
    this.x = value;
    this.y = value;
  }

  public void set (float x, float y) {
    this.x = x;
    this.y = y;
  }

  public void set (Float2 other) {
    this.x = other.x;
    this.y = other.y;
  }

  public void add (Float2 other) {
    x += other.x;
    y += other.y;
  }

  public void add (float x, float y) {
    this.x += x;
    this.y += y;
  }
  
  public void addAll (float value) {
    x += value;
    y += value;
  }

  public void sub (Float2 other) {
    x -= other.x;
    y -= other.y;
  }
  
  public void sub (float x, float y) {
    this.x -= x;
    this.y -= y;
  }

  public void subAll (float value) {
    x -= value;
    y -= value;
  }

  public void multiply (Float2 other) {
    this.x *= other.x;
    this.y *= other.y;
  }

  public void multiply (float x, float y) {
    this.x *= x;
    this.y *= y;
  }

  public void multiplyAll (float value) {
    this.x *= value;
    this.y *= value;
  }

  public void divide (Float2 other) {  
    if (other.x == 0 || other.y == 0) throw new ArithmeticException("divide by zero");
    this.x /= other.x;
    this.y /= other.y;
  }

  public void divide (float x, float y) {  
    if (x == 0 || y == 0) throw new ArithmeticException("divide by zero");
    this.x /= x;
    this.y /= y;
  }

  public void divideAll (float value) {
    if (value == 0 ) throw new ArithmeticException("divide by zero");
    this.x /= value;
    this.y /= value;
  }

  public Float2 copy () {
    return new Float2 (x,y);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    Float2 float2 = (Float2) obj;
    return Float.compare(float2.x, x) == 0 && Float.compare(float2.y, y) == 0;
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
  public static Float2 add(Float2 a, Float2 b) {
    return new Float2(a.x + b.x, a.y + b.y);
  }

  public static Float2 sub(Float2 a, Float2 b) {
    return new Float2(a.x - b.x, a.y - b.y);
  }

  public static Float2 multiply(Float2 a, Float2 b) {
    return new Float2(a.x * b.x, a.y * b.y);
  }

  public static Float2 divide(Float2 a, Float2 b) {
    if (b.x == 0 || b.y == 0) throw new ArithmeticException("divide by zero");
    return new Float2(a.x / b.x, a.y / b.y);
  }
}