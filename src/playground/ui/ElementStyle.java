package playground.ui;

import playground.Color;
import processing.core.PFont;

public class ElementStyle implements UIConstants  {
  
  ElementStyle inheritedStyle;
  
  private StyleAttribute<Integer> backgroundColor = new StyleAttribute<>();
  private StyleAttribute<Integer> borderColor     = new StyleAttribute<>();
  private StyleAttribute<Float>   borderWidth     = new StyleAttribute<>();
  private StyleAttribute<float[]> cornerRadii     = new StyleAttribute<>();
  private StyleAttribute<Integer> textColor       = new StyleAttribute<>();
  private StyleAttribute<Float>   textSize        = new StyleAttribute<>();
  private StyleAttribute<PFont>   textFont        = new StyleAttribute<>();
  private StyleAttribute<Integer> textAlignH      = new StyleAttribute<>();
  private StyleAttribute<Integer> textAlignV      = new StyleAttribute<>();
  private StyleAttribute<Integer> imageTint       = new StyleAttribute<>();
  
  public int getBackgroundColor () {
    if (backgroundColor.set) return backgroundColor.value;
    else if (inheritedStyle != null) return inheritedStyle.getBackgroundColor();
    else return Color.CLEAR;
  }
  
  public int getBorderColor () {
    if (borderColor.set) return borderColor.value;
    else if (inheritedStyle != null) return inheritedStyle.getBorderColor();
    else return Color.CLEAR;
  }
  
  public float getBorderWidth () {
    if (borderWidth.set) return borderWidth.value;
    else if (inheritedStyle != null) return inheritedStyle.getBorderWidth();
    else return 0;
  }

  public float[] getCornerRadii () {
    if (cornerRadii.set) return cornerRadii.value;
    else if (inheritedStyle != null) return inheritedStyle.getCornerRadii();
    else return new float[0];
  }

  public int getTextColor () {
    if (textColor.set) return textColor.value;
    else if (inheritedStyle != null) return inheritedStyle.getTextColor();
    else return Color.BLACK;
  }
  
  public float getTextSize () {
    if (textSize.set) return textSize.value;
    else if (inheritedStyle != null) return inheritedStyle.getTextSize();
    else return 16;
  }

  public PFont getTextFont() {
    if (textFont.set) return textFont.value;
    else if (inheritedStyle != null) return inheritedStyle.getTextFont();
    else return null;
  }

  public int getTextAlignH () {
    if (textAlignH.set) return textAlignH.value;
    else if (inheritedStyle != null) return inheritedStyle.getTextAlignH();
    else return LEFT;
  }

  public int getTextAlignV () {
    if (textAlignV.set) return textAlignV.value;
    else if (inheritedStyle != null) return inheritedStyle.getTextAlignV();
    else return TOP;
  }
  
  public int getTintColor () {
    if (imageTint.set) return imageTint.value;
    else if (inheritedStyle != null) return inheritedStyle.getTintColor();
    else return Color.WHITE;
  }
  
  public ElementStyle inheritFrom(ElementStyle style) {
    this.inheritedStyle = style;
    return this;
  }
  
  public ElementStyle noBackground() {
    backgroundColor.clear();
    return this;
  }
  
  public ElementStyle backgroundColor(int value) {
    backgroundColor.set(value);
    return this;
  }
  
  public ElementStyle noBorder() {
    borderColor.clear();
    borderWidth.clear();
    return this;
  }
  
  public ElementStyle borderColor(int value) {
    borderColor.set(value);
    return this;
  }
 
  public ElementStyle borderWidth(float value) {
    borderWidth.set(value);
    return this;
  }

  public ElementStyle noCornerRadius() {
    cornerRadii.set(new float[0]);
    return this;
  }

  public ElementStyle cornerRadius(float value) {
    cornerRadii.set(new float[]{value});
    return this;
  }

  public ElementStyle cornerRadius(float a, float b, float c, float d) {
    cornerRadii.set(new float[] {a, b, c ,d});
    return this;
  }

  public ElementStyle cornerRadius(float[] corners) {
    switch (corners.length) {
      case 0 -> cornerRadii.set(new float[] {0, 0, 0 ,0});
      case 1 -> cornerRadii.set(new float[] {corners[0], corners[0], corners[0] ,corners[0]});
      case 4 -> cornerRadii.set(corners.clone());
    }
    return this;
  }

  public ElementStyle clearTextColor () {
    textColor.clear();
    return this;
  }

  public ElementStyle textColor (int value) {
    textColor.set (value);
    return this;
  }

  public ElementStyle clearTextSize () {
    textSize.clear();
    return this;
  }

  public ElementStyle textSize (float value) {
    textSize.set (value);
    return this;
  }

  public ElementStyle cleartextFont () {
    textFont.clear();
    return this;
  }

  public ElementStyle textFont (PFont value) {
    textFont.set (value);
    return this;
  }

  public ElementStyle clearTextAlign () {
    textAlignH.clear();
    textAlignV.clear();
    return this;
  }

  public ElementStyle textAlign (int[] alignment) {
    //ignore setting if any of these are set to inherit
    if (alignment[0] != INHERIT) textAlignH.set (alignment[0]);
    if (alignment[1] != INHERIT) textAlignV.set (alignment[1]);
    return this;
  }
  
  public ElementStyle textAlign (int h, int v) {
    textAlignH.set (h);
    textAlignV.set (v);
    return this;
  }

  public ElementStyle clearTextAlignHorizontal () {
    textAlignH.clear();
    return this;
  }

  public ElementStyle textAlignHorizontal (int alignment) {
    textAlignH.set (alignment);
    return this;
  }

  public ElementStyle clearTextAlignVertical () {
    textAlignV.clear();
    return this;
  }

  public ElementStyle textAlignVertical (int alignment) {
    textAlignV.set (alignment);
    return this;
  }
  
  public ElementStyle clearTint () {
    imageTint.clear();
    return this;
  }
  
  public ElementStyle tint (int value) {
    imageTint.set (value);
    return this;
  }

  public void clearAllAttributes() {
    backgroundColor.clear();
    borderColor.clear();
    borderWidth.clear();
    cornerRadii.clear();
    textColor.clear();
    textSize.clear();
    textFont.clear();
    textAlignH.clear();
    textAlignV.clear();
    imageTint.clear();
  }

  public ElementStyle copy() {
    ElementStyle style = new ElementStyle();
  
    style.inheritedStyle = this.inheritedStyle;
  
    style.backgroundColor = this.backgroundColor.copy();
    style.borderColor     = this.borderColor.copy();
    style.borderWidth     = this.borderWidth.copy();
    style.cornerRadii     = this.cornerRadii.copy();
    style.textColor       = this.textColor.copy();
    style.textSize        = this.textSize.copy();
    style.textFont        = this.textFont.copy();
    style.textAlignH      = this.textAlignH.copy();
    style.textAlignV      = this.textAlignV.copy();
    style.imageTint       = this.imageTint.copy();
  
    return style;
  }
} 