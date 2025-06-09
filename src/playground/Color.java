package playground;

import processing.core.PGraphics;

/**Color functions extracted from Processing for use outside the PApplet. */
public final class Color {

  public static final int RED     = rgb(255, 0, 0);
  public static final int GREEN   = rgb(0, 255, 0);
  public static final int BLUE    = rgb(0, 0, 255);
  public static final int CYAN    = rgb(0, 255, 255);
  public static final int MAGENTA = rgb(255, 0, 255);
  public static final int YELLOW  = rgb(255, 255, 0);
  public static final int BLACK   = rgb(0, 0, 0);
  public static final int WHITE   = rgb(255, 255, 255);
  public static final int GRAY    = rgb(128, 128, 128);
  public static final int CLEAR    = rgba(0, 0, 0, 0);

  /**Helper class to allow colors created with VS Code support. */
  public static final int rgb (int r, int g, int b) {
    return color (r, g, b);
  }

  /**Helper class to allow colors created with VS Code support. */
  public static final int rgba (int r, int g, int b, int a) {
    return color (r, g, b, a);
  }

  public static final int gray (int gray) {
    return color (gray);
  }


  public static final int grayAlpha (int gray, int alpha) {
    return color (gray, alpha);
  }

  public static final int color(int gray) {
    if (gray > 255) {
      gray = 255;
    } else if (gray < 0) {
      gray = 0;
    }
    return -16777216 | gray << 16 | gray << 8 | gray;
  }

  public static final int color(int gray, int alpha) {
    if (alpha > 255) {
      alpha = 255;
    } else if (alpha < 0) {
      alpha = 0;
    }

    return gray > 255 ? alpha << 24 | gray & 16777215 : alpha << 24 | gray << 16 | gray << 8 | gray;
  }

  public static final int color(int v1, int v2, int v3) {
    if (v1 > 255) {
      v1 = 255;
    } else if (v1 < 0) {
      v1 = 0;
    }

    if (v2 > 255) {
      v2 = 255;
    } else if (v2 < 0) {
      v2 = 0;
    }

    if (v3 > 255) {
      v3 = 255;
    } else if (v3 < 0) {
      v3 = 0;
    }

    return -16777216 | v1 << 16 | v2 << 8 | v3;
  }

  public static final int color(int v1, int v2, int v3, int alpha) {
    if (alpha > 255) {
      alpha = 255;
    } else if (alpha < 0) {
      alpha = 0;
    }

    if (v1 > 255) {
      v1 = 255;
    } else if (v1 < 0) {
      v1 = 0;
    }

    if (v2 > 255) {
      v2 = 255;
    } else if (v2 < 0) {
      v2 = 0;
    }

    if (v3 > 255) {
      v3 = 255;
    } else if (v3 < 0) {
      v3 = 0;
    }

    return alpha << 24 | v1 << 16 | v2 << 8 | v3;
  }

  public static int lerpColor(int c1, int c2, float amt) {
    return PGraphics.lerpColor(c1, c2, amt, 1);
  }

  public static boolean isFullyTransparent (int c) {
    return c >= 0 && c <= 16777215;
  }
}
