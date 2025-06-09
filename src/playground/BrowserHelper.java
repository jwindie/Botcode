package playground;

import java.awt.Desktop;
import java.net.URI;

public final class BrowserHelper {

  /**tries to open a URL on the user's default browser. */
  public static void openURL(String url) {
    try {
      if (!Desktop.isDesktopSupported()) {
        System.err.println("Desktop API is not supported on this system.");
        return;
      }
      Desktop.getDesktop().browse(new URI(url));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}