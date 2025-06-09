package playground;

import processing.core.PApplet;

public class PlaygroundApp extends PApplet {

  private static PlaygroundApp instance;

  public static PlaygroundApp getInstance() {
    return instance;
  }

  public static Int2 getSreenSize() {
    return new Int2(instance.width, instance.height);
  }

  public PlaygroundApp() {
    super();
    instance = this;
  }
}