package playground.ui;

import java.util.function.Consumer;

import playground.Float2;

/**Button element.*/
public class Button extends Element {



  public Button (Float2 size, Consumer<Integer> onClick) {
    super(size);
    this.onClick(onClick);
  }

  public Button () {
    // supe
  }
  
}
