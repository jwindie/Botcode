package main;

import processing.core.PConstants;
import processing.core.PGraphics;

public class Window implements PConstants {
  protected float width;
  protected float height;
  protected float x;
  protected float y;
  protected PGraphics g;

//#region Getters
  public float getWidth() {
    return width;
  }

  public float getHeight() {
    return height;
  }

  public float getX() {
    return x;
  }

  public float getY() {
    return y;
  }

  public PGraphics getGraphics() {
    return g;
  }

  //#region Setters
  public void setWidth(float width) {
    this.width = width;
  }

  public void setHeight(float height) {
    this.height = height;
  }

  public void setSize(float w, float h) {
    this.width = w;
    this.height = h;
  }

  public void setX(float x) {
    this.x = x;
  }

  public void setY(float y) {
    this.y = y;
  }

  public void setPosition(float x, float y) {
    this.x = x;
    this.y = y;
  }

  public void setGraphics(PGraphics graphics) {
    this.g = graphics;
  }

  //#region Methods
  public void draw() {
  }
}