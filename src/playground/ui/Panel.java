package playground.ui;

import playground.Color;
import playground.Float2;
import processing.core.PFont;
import processing.core.PImage;

//**Preconfigured UI element with a solid color and corner rounding*/
public class Panel extends Element {

  public Panel (String id, float width, float height) {
    this (id, new Float2 (width, height), Color.WHITE);
  }

  public Panel (String id, Float2 size) {
    this (id, size, Color.WHITE);
  }

  public Panel (String id, float width, float height, int color) {
    this (id, new Float2 (width, height), color);
  }

  public Panel (String id, Float2 size, int color) {
    this (id, size, color, 0);
  }

  public Panel (String id, Float2 size, int color, float padding) {
    this (id, size, color, padding, 0);
  }
  
  public Panel (String id, float width, float height, int color, float padding) {
    this (id, new Float2 (width, height), color, padding, 0);
  }

  public Panel (String id, Float2 size, int color, float padding, float cornerRadius) {
    this (id, size, color, padding, cornerRadius, cornerRadius, cornerRadius, cornerRadius);
  }

  public Panel (String id, float width, float height, int color,  float padding, float cornerRadius) {
    this (id, new Float2(width, height), color, padding, cornerRadius, cornerRadius, cornerRadius, cornerRadius);
  }

  public Panel (String id, Float2 size, int color,  float padding, float c1, float c2, float c3, float c4 ) {
    this (id, size, color, padding, new float[] {c1,c2,c3,c4});
  }

  public Panel (String id, float width, float height, int color, float padding, float c1, float c2, float c3, float c4 ) {
    this (id, new Float2(width, height), color, padding, new float[] {c1,c2,c3,c4});
  }

  /**Internal "<i>true</i>"" contstructor. */
  protected Panel (String id, Float2 size, int color, float padding, float[] cornerRadii) {
    super (id, size);
    style (s -> s.backgroundColor(color).cornerRadius(cornerRadii));
  }
  
  //#region Builder Methods
  public Panel border(ElementStyle style, float weight) {
    style.borderWidth(weight);
    return this;
  }

  public Panel border (ElementStyle style, float weight, int color) {
  style.borderWidth(weight).borderColor(color);
    return this;
  }  


  public Panel cornerRadius (ElementStyle style, float radius) {
    style.cornerRadius(radius);
    return this;
  }

  public Panel cornerRadius (ElementStyle style, float a, float b, float c, float d) {
    style.cornerRadius(a, b, c, d);
    return this;
  }

  public Panel cornerRadius (ElementStyle style, float[] corners) {
    style.cornerRadius(corners);
    return this;
  }

  //**Sets the text and style properties. Ignores text assignment if the text is null. */
  public Panel label (String text) {
    if (text != null) this.text = text;
    return this;
  }

  //**Sets the text and style properties. Ignores text assignment if the text is null. */
  public Panel label (String text, int color) {
    if (text != null) this.text = text;
    style.textColor(color);
    return this;
  }
 
  //**Sets the text and style properties. Ignores text assignment if the text is null. */
  public Panel label (String text, int color, float size) {
    if (text != null) this.text = text;
    style.textColor(color).textSize(size);
    return this;
  }

  //**Sets the text and style properties. Ignores text assignment if the text is null. */
  public Panel label (String text, int color, float size, int[] alignment) {
    if (text != null) this.text = text;
    style.textColor(color).textSize(size).textAlign(alignment);
    return this;
  }

  //**Sets the text and style properties. Ignores text assignment if the text is null. */
  public Panel label (ElementStyle style, String text, int color, float size, int[] alignment, PFont font) {
    if (text != null) this.text = text;
    style.textColor(color).textSize(size).textAlign(alignment).textFont(font);
    return this;
  }

  //**Sets the image and style properties. Ignores image assignment if the image is null. */
  public Panel image (PImage image) {
    if (image != null) this.image = image;
    return this;
  }

  //**Sets the image and style properties. Ignores image assignment if the image is null. */
  public Panel image (ElementStyle style, PImage image, int tint) {
    if (image != null) this.image = image;
    style.tint(tint);
    return this;
  }
}